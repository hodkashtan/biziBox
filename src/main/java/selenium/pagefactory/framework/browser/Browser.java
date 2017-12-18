package selenium.pagefactory.framework.browser;

import com.google.common.base.Preconditions;
import org.testng.log4testng.Logger;
import selenium.pagefactory.framework.actions.SeleniumActions;
import selenium.pagefactory.framework.browser.web.WebBrowserType;
import selenium.pagefactory.framework.config.TimeoutsConfig;
import selenium.pagefactory.framework.exception.BiziboxWebDriverException;
import selenium.pagefactory.framework.pages.PageUtils;
import selenium.pagefactory.framework.pages.SubPage;
import selenium.pagefactory.framework.pages.TopLevelPage;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.util.Objects;
import java.util.Optional;

public abstract class Browser<D extends WebDriver> {
    private static final Logger LOGGER = Logger.getLogger(Browser.class);
    protected D webDriver;
    protected String baseTestUrl;
    protected TimeoutsConfig timeouts;
    protected Optional<CachedPage> optionalCachedPage = Optional.empty();
    protected static final PageUtils PAGE_UTILS = new PageUtils();

    protected Browser(String baseTestUrl, TimeoutsConfig timeoutsConfig) {
        this.baseTestUrl = Preconditions.checkNotNull(baseTestUrl);
        this.timeouts = timeoutsConfig;
    }

    public abstract WebBrowserType getBrowserType();

    public abstract SeleniumActions getActions();

    public abstract DesiredCapabilities getDesiredCapabilities();

    public abstract void initializeBrowser() throws BiziboxWebDriverException;

    public String getBaseTestUrl() {
        return baseTestUrl;
    }

    public void setBaseTestUrl(String baseTestUrl) {
        this.baseTestUrl = baseTestUrl;
    }

    public TimeoutsConfig getTimeouts() {
        return timeouts;
    }

    protected abstract D createWebDriver() throws BiziboxWebDriverException;

    public D getWebDriver() {
        return webDriver;
    }

    public long getPageTimeoutSeconds() {
        return timeouts.getPageLoadTimeoutSeconds();
    }

    public long getImplicitWaitTimeoutMillis() {
        return timeouts.getImplicitWaitTimeoutMillis();
    }

    public Optional<CachedPage> getOptionalCachedPage() {
        return optionalCachedPage;
    }

    /**
     * Invalidate cached page, and return a fresh TopLevelPage with newly initialized WebElements.
     *
     * This method does not do a Browser Refresh of the page.
     *
     * It does:
     * Invalidate the cache.
     * Initialize the current page again by loading webelements and running page load hooks
     *
     * @param pageClass - the class of the current Page
     * @return the new instance of a TopLevelPage
     */
    public <T extends TopLevelPage> T reloadTopLevelPage(Class<T> pageClass) {
        invalidateCachedPage();
        return loadTopLevelPage(pageClass);
    }

    /**
     * Load a sub page. No caching is used for {@link selenium.pagefactory.framework.pages.SubPage}'s.
     *
     * @param pageClass - the class of the SubPage that is currently present on the DOM in the browser to load.
     */
    public <T extends SubPage> T loadSubPage(Class<T> pageClass) {
        return PAGE_UTILS.loadCurrentPage(pageClass, webDriver, getActions());
    }

    /**
     * If the current page is still valid, and the URL hasn't changed, and the
     * class given as input is assignable from the cached page,
     * THEN return the cached page and avoid re-initializing web elements and running page hooks.
     * Otherwise, invalidate the cache and load as normal.
     *
     * @param pageClass - the class of the current Page
     */
    public <T extends TopLevelPage> T loadTopLevelPage(Class<T> pageClass) {
        if (shouldUseCachedPage(pageClass)) {
            LOGGER.info("CACHE HIT: Fetching page of type " + pageClass.getSimpleName() + " from the Page Cache");
            // This cast is safe, because we check in shouldUseCachedPage
            return (T) optionalCachedPage.get().getCachedPage();
        }
        LOGGER.info("Loading page of type " + pageClass.getSimpleName());
        // If the page wasn't valid, then invalidate the cache.
        runLeavePageHook();
        invalidateCachedPage();

        // First load the page without the page load hook so that we can store the failing page in the cache
        T page = PAGE_UTILS.loadCurrentPageWithoutPageLoadHook(pageClass, webDriver, getActions());
        setCachedPage(page);

        // Next, run page load hook and sub-page load hooks
        page.pageLoadHook();
        PAGE_UTILS.runPageLoadHooksForSubPages(page, getActions());

        return page;
    }


    /**
     * Save a screenshot in PNG format to given file name.
     *
     * @param filename
     * @return - a File representing the saved screenshot.
     */
    public File saveScreenshotToFile(String filename) {
        TakesScreenshot screenshotDriver;
        screenshotDriver = ((TakesScreenshot) getWebDriver());
        File scrFile = screenshotDriver.getScreenshotAs(OutputType.FILE);

        // Now you can do whatever you need to do with it, for example copy somewhere
        File outFile = new File(filename);
        try {
            FileUtils.copyFile(scrFile, outFile);
        } catch (IOException e) {
            LOGGER.error("Error saving screenshot!", e);
        }
        return outFile;
    }

    public void invalidateCachedPage() {
        optionalCachedPage = Optional.empty();
    }

    //--------------Private helpers------------
    protected void setCachedPage(TopLevelPage p) {
        if (getBrowserType()!=WebBrowserType.MOBILE) {
            final String url = webDriver.getCurrentUrl();
            CachedPage cachedPage = new CachedPage(url, p);
            optionalCachedPage = Optional.of(cachedPage);
            LOGGER.debug("Set cached page of type " + p.getClass().getSimpleName() + " with URL " + url);
        }
    }

    private <T extends TopLevelPage> boolean shouldUseCachedPage(Class<T> pageClass) {
        if (!optionalCachedPage.isPresent()) {
            return false;
        }
        CachedPage cachedPage = optionalCachedPage.get();

        // The cached page must be an instance of the required page class.
        if (!pageClass.isInstance(cachedPage.getCachedPage())) {
            return false;
        }

        try {
            URI currentURI = URI.create(webDriver.getCurrentUrl());
            URI cachedURI = URI.create(cachedPage.getUrl());

            // Hosts must be equal
            if (!Objects.equals(currentURI.getHost(), cachedURI.getHost())) {
                return false;
            }

            // Paths must be equal
            if (!Objects.equals(currentURI.getPath(), cachedURI.getPath())) {
                return false;
            }

        } catch (Exception e) {
            LOGGER.debug("Error constructing URIs from the current webdriver URL", e);
            return false;
        }

        return true;
    }

    public void runLeavePageHook() {
        if (optionalCachedPage.isPresent()) {
            optionalCachedPage.get().getCachedPage().leavePageHook();
        }
    }

    /**
     * Refresh the current page, without giving back a newly initialized Page object.
     */
    public abstract void refreshPage();

    /**
     * @param pageClass - the class of the expected Page after refreshing.
     */
    public abstract <T extends TopLevelPage> T refreshPage(Class<T> pageClass);

    public void quit() {
        LOGGER.info("Quitting WebDriver: " + webDriver);
        webDriver.quit();
        LOGGER.info("SUCCESS - quit WebDriver: " + webDriver);
    }
}