package selenium.pagefactory.framework.browser.web;

import org.openqa.selenium.Dimension;
import org.openqa.selenium.Platform;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.logging.LogEntries;
import org.openqa.selenium.logging.LogType;
import org.openqa.selenium.logging.LoggingPreferences;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.testng.log4testng.Logger;
import selenium.pagefactory.framework.browser.Browser;
import selenium.pagefactory.framework.config.TimeoutsConfig;
import selenium.pagefactory.framework.exception.BiziboxWebDriverException;
import selenium.pagefactory.framework.pages.BaseTopLevelPage;
import selenium.pagefactory.framework.pages.TopLevelPage;
import selenium.pagefactory.framework.webservice.EndpointBuilder;

import javax.annotation.Nullable;
import java.net.URI;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;

/**
 * Base Browser class.
 * Contains a lot of the configuration that is common across browsers.
 * Subclasses must implement getBrowserType, getDesiredCapabilities, isRemote, and getActions
 */
public abstract class WebBrowser extends Browser<WebDriver> {
    private static final Logger LOGGER = Logger.getLogger(WebBrowser.class);

    private final Optional<String> webDriverPath;
    private final Optional<String> browserBinaryPath;
    private final Optional<String> browserVersion;
    private final Optional<String> browserLocale;
    private final Optional<Integer> startWindowWidth;
    private final Optional<Integer> startWindowHeight;
    private final Optional<Level> browserLogLevel;
    private final Optional<String> browserLogFile;
    private final Optional<Platform> platform;
    private Optional<Map<String, String>> extraCapabilites;

    public WebBrowser(String baseTestUrl,
                      TimeoutsConfig timeouts,
                      Optional<String> webDriverPath,
                      Optional<String> browserBinaryPath,
                      Optional<String> browserVersion,
                      Optional<String> browserLocale,
                      Optional<Integer> startWindowWidth,
                      Optional<Integer> startWindowHeight,
                      Optional<Platform> platform,
                      Optional<Map<String, String>> extras) {

        this(baseTestUrl, timeouts, webDriverPath, browserBinaryPath, browserVersion, browserLocale,
                startWindowWidth, startWindowHeight,
                Optional.empty(), Optional.empty(), platform, extras);

    }

    public WebBrowser(String baseTestUrl,
                      TimeoutsConfig timeouts,
                      Optional<String> webDriverPath,
                      Optional<String> browserBinaryPath,
                      Optional<String> browserVersion,
                      Optional<String> browserLocale,
                      Optional<Integer> startWindowWidth,
                      Optional<Integer> startWindowHeight,
                      Optional<Level> browserLogLevel,
                      Optional<String> browserLogFile,
                      Optional<Platform> platform,
                      Optional<Map<String, String>> extras) {
        super(baseTestUrl, timeouts);
        this.webDriverPath = webDriverPath;
        this.browserBinaryPath = browserBinaryPath;
        this.browserVersion = browserVersion;
        this.browserLocale = browserLocale;
        this.startWindowWidth = startWindowWidth;
        this.startWindowHeight = startWindowHeight;
        this.browserLogLevel = browserLogLevel;
        this.browserLogFile = browserLogFile;
        this.platform = platform;
        this.extraCapabilites = extras;
    }

    /**
     * Initialize the browser. This creates a web driver instance, which opens the Browser to a blank page.
     * Resize the window to the configured values.
     *
     * @throws BiziboxWebDriverException
     */
    public void initializeBrowser() throws BiziboxWebDriverException {
        this.webDriver = createWebDriver();
        if (startWindowWidth.isPresent() && startWindowHeight.isPresent()) {
            this.webDriver.manage().window().setSize(new Dimension(startWindowWidth.get(), startWindowHeight.get()));
        }
        // Safari web driver doesn't support setting timeouts.
        if (getBrowserType() != WebBrowserType.SAFARI) {
            this.webDriver.manage().timeouts().pageLoadTimeout(getPageTimeoutSeconds(), TimeUnit.SECONDS);
            this.webDriver.manage().timeouts().implicitlyWait(getImplicitWaitTimeoutMillis(), TimeUnit.MILLISECONDS);
        }
        LOGGER.info("SUCCESS - Created WebBrowser of type " + getBrowserType() + ": " + webDriver);
    }

    public abstract WebBrowserType getBrowserType();

    public abstract DesiredCapabilities getDesiredCapabilities();

    public Optional<Map<String, String>> getExtraDesiredCapabilities(){
        return this.extraCapabilites;
    }

    public LoggingPreferences getLoggingPreferences() {
        Level level = getLogLevel();
        LoggingPreferences loggingPreferences = new LoggingPreferences();
        loggingPreferences.enable(LogType.BROWSER, level);
        loggingPreferences.enable(LogType.CLIENT, level);
        loggingPreferences.enable(LogType.DRIVER, level);
        loggingPreferences.enable(LogType.SERVER, level);
        return loggingPreferences;
    }

    public Optional<Integer> getStartWindowWidth() {
        return startWindowWidth;
    }

    public Optional<Integer> getStartWindowHeight() {
        return startWindowHeight;
    }

    public Optional<String> getWebDriverPath() {
        return webDriverPath;
    }

    public Optional<String> getBrowserBinaryPath() {
        return browserBinaryPath;
    }

    public Optional<String> getBrowserVersion() {
        return browserVersion;
    }

    public Optional<String> getBrowserLocale() {
        return browserLocale;
    }

    public Optional<Level> getBrowserLogLevel() {
        return browserLogLevel;
    }

    public Level getLogLevel() {
        return browserLogLevel.isPresent() ? browserLogLevel.get() : Level.WARNING;
    }

    public Optional<String> getBrowserLogFile() {
        return browserLogFile;
    }

    public Optional<Platform> getPlatform() {
        return platform;
    }

    public TimeoutsConfig getTimeouts() {
        return timeouts;
    }

    /**
     * Opens a new page in the Browser by URL. An absolute URL or the path can be provided.
     * If a path is provided, then the baseTestUrl provided when creating the browser will be used as the
     * base of the URL.
     *
     * Invalidates the cached page and loads a fresh new page.
     *
     * @param href - the href from a link, which may be a relative path from baseTestUrl or may be absolute
     * @return - a generic {@link selenium.pagefactory.framework.pages.BaseTopLevelPage}
     * page object. To open a page with more specific functionality, you must extend
     * {@link selenium.pagefactory.framework.pages.BaseTopLevelPage} and then
     * call {@link #openPageByURL(String, Class)}.
     */
    public TopLevelPage openPageByURL(String href) {
        return openPageByURL(href, BaseTopLevelPage.class);
    }

    /**
     * Opens a new page in the Browser by URL. An absolute URL or the path can be provided.
     *
     * Invalidates the cached page and loads a fresh new page.
     *
     * @param uri - the href from a link, which may be a relative path from baseTestUrl or may be absolute
     * @param pageClass - the {@link selenium.pagefactory.framework.pages.TopLevelPage} class to load.
     */
    public <T extends TopLevelPage> T openPageByURL(URI uri, Class<T> pageClass) {
        URI absoluteURI;
        if (uri.isAbsolute()) {
            absoluteURI = uri;
        } else {
            String fullURIStr = EndpointBuilder.uri(baseTestUrl, "/", uri.toString());
            absoluteURI = URI.create(fullURIStr);
        }
        LOGGER.info("Opening web page by URL " + absoluteURI);
        runLeavePageHook();
        invalidateCachedPage();
        T page = PAGE_UTILS.loadPageFromURL(absoluteURI, pageClass, getWebDriver(), getActions());
        setCachedPage(page);
        return page;
    }

    /**
     * Opens a new page in the Browser by URL. An absolute URL or the path can be provided.
     *
     * Invalidates the cached page and loads a fresh new page.
     *
     * @param href - the href from a link, which may be a relative path from baseTestUrl or may be absolute
     * @param pageClass - the {@link selenium.pagefactory.framework.pages.TopLevelPage} class to load.
     */
    public <T extends TopLevelPage> T openPageByURL(String href, Class<T> pageClass) {
        URI uri = URI.create(href);
        return openPageByURL(uri, pageClass);
    }

    /**
     * Refresh the current page, without giving back a newly initialized Page object.
     */
    public void refreshPage() {
        runLeavePageHook();
        webDriver.navigate().refresh();
        if (optionalCachedPage.isPresent()) {
            TopLevelPage cachedPage = optionalCachedPage.get().getCachedPage();
            cachedPage.refreshElements();
        }
    }

    /**
     * @param pageClass - the class of the expected Page after refreshing.
     */
    public <T extends TopLevelPage> T refreshPage(Class<T> pageClass) {
        runLeavePageHook();
        invalidateCachedPage();
        webDriver.navigate().refresh();
        T page = loadTopLevelPage(pageClass);
        setCachedPage(page);
        return page;
    }

    public void cleanSession() {
        webDriver.manage().deleteAllCookies();
    }

    @Nullable
    public abstract LogEntries getBrowserLogEntries();

    /**
     * Helper to set properties of the DesiredCapabilities that are common across all browsers.
     * @param desiredCapabilities
     */
    protected void setCommonWebBrowserCapabilities(DesiredCapabilities desiredCapabilities) {
        // If a required version is present, then set this as a desired capability. Only affects Remote browsers.
        Optional<String> browserVersion = getBrowserVersion();
        if (browserVersion.isPresent() && !browserVersion.get().isEmpty()) {
            desiredCapabilities.setCapability(CapabilityType.VERSION, browserVersion.get());
        }

        // Set logging preferences.
        LoggingPreferences loggingPreferences = getLoggingPreferences();
        desiredCapabilities.setCapability(CapabilityType.LOGGING_PREFS, loggingPreferences);

        // If a platform is specified, set this desired capability. Only affects Remote browsers.
        Optional<Platform> platform = getPlatform();
        if (platform.isPresent()) {
            desiredCapabilities.setPlatform(platform.get());
        }
        setExtrasCapabilities(desiredCapabilities, getExtraDesiredCapabilities());
    }

    protected void setExtrasCapabilities(DesiredCapabilities currentCapabilities, Optional<Map<String, String>> extrasCapabilities){
        if(extrasCapabilities.isPresent()){
            for(Map.Entry<String, String> entry: extrasCapabilities.get().entrySet()){
                currentCapabilities.setCapability(entry.getKey(), entry.getValue());
                LOGGER.info("Adding Extra Capability: " + entry);
            }
        }
    }
}
