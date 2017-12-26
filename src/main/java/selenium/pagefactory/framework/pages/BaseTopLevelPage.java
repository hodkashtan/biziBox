package selenium.pagefactory.framework.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.support.PageFactory;
import org.testng.log4testng.Logger;
import selenium.pagefactory.framework.actions.SeleniumActions;
import selenium.pagefactory.framework.browser.web.WebBrowser;
import selenium.pagefactory.framework.config.TimeoutType;
import selenium.pagefactory.framework.exception.InvalidPageUrlException;

import javax.annotation.Nonnull;
import java.net.URI;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Base abstract class for a TopLevelPage. Implements the default pageLoadHook that waits for the page identifier to be present.
 * Subclasses should call super.pageLoadHook() if they want to wait on the page identifier.
 */
public class BaseTopLevelPage<S extends SeleniumActions> implements TopLevelPage {
    @SuppressWarnings("unused")
    private static Logger LOGGER = Logger.getLogger(BaseTopLevelPage.class);

    private static final PageUtils PAGE_UTILS = new PageUtils();

    private long pageLoadTime;

    protected S seleniumActions;

    public final S getActions() {
        return seleniumActions;
    }

    public final void setActions(SeleniumActions actions) {
        this.seleniumActions = (S) actions;
    }

    @Nonnull
    @Override
    public String getWebPagePath() {
        Optional<String> optionalPathFromAnnotation = PAGE_UTILS.getWebPagePathForClass(getClass());
        if (optionalPathFromAnnotation.isPresent()) {
            return optionalPathFromAnnotation.get();
        }
        return "/";
    }

    @Override
    public void pageLoadHook() {
        if (pageLoadTime == 0) {
            pageLoadTime = System.currentTimeMillis();
        }

        // First do the default load hook, which verifies an element is present
        PAGE_UTILS.defaultPageLoadHook(this, seleniumActions, getPageReadyTimeout());

        // Next, verify that the current URL matches the value annotated with @WebPagePath
        verifyCurrentURL();
    }

    @Override
    public TimeoutType getPageReadyTimeout() {
        return TimeoutType.PAGE_READY_TIMEOUT;
    }

    /**
     * Verify that the current URL the browser is pointing to matches the path given by the @WebPagePath annotation.
     *
     * For example, if the current URL is "http://example.com/foo/bar" and we specify @WebPagePath(path = "/bar") then this would be a match, because we only
     * require the URL to end with the given path. (Since we can't know the root context of the server).
     *
     * If the current URL is "http:/example.com/foo/1234" and we specify @WebPagePath(isRegex = true, path = "/foo/\\d+"), then this would match as a regex.
     */
    public void verifyCurrentURL() {
        WebPagePath webPagePath = getClass().getAnnotation(WebPagePath.class);

        // If the @WebPagePath annotation isn't present, or browser isn't a WebBrowser, then return.
        if (webPagePath == null || !(seleniumActions.getBrowser() instanceof WebBrowser)) {
            return;
        }

        String expectedPath = webPagePath.path();
        boolean regex = webPagePath.isRegex();

        String currentURL = seleniumActions.getCurrentURL();

        // Not sure when a WebDriver returns null for current URL, but just don't validate in this case
        if (currentURL == null) {
            return;
        }

        URI currentURI = URI.create(currentURL);
        String currentPath = currentURI.getPath();

        if (currentURI.getFragment() != null && !currentURI.getFragment().isEmpty()) {
            currentPath += "#" + currentURI.getFragment();
        }

        // Remove trailing slashes
        if (currentPath.endsWith("/")) {
            currentPath = currentPath.substring(0, currentPath.length() - 1);
        }
        if (expectedPath.endsWith("/")) {
            expectedPath = expectedPath.substring(0, expectedPath.length() - 1);
        }

        if (regex) {
            Pattern pattern = Pattern.compile(expectedPath);
            Matcher m = pattern.matcher(currentPath);
            if (!m.find() || m.regionEnd() != currentPath.length()) {
                throw new InvalidPageUrlException(String.format("The current path of the web browser is %s, but expected the path to end with an expression " +
                                                                    "matching the regex '%s'",
                                                                currentPath, expectedPath));
            }

            LOGGER.info("SUCCESS - the current path '" + currentPath + "' matches the regex '" + expectedPath + "'");
        } else {
            // The current path should end with the expected path --- we don't know what the Root context of the server is.
            if (!currentPath.endsWith(expectedPath)) {
                throw new InvalidPageUrlException(String.format("The current path of the web browser is %s, but expected the path to end with '%s'",
                                                                currentPath, expectedPath));
            }

            LOGGER.info("SUCCESS - the current path '" + currentPath + "' matches the required path '" + expectedPath + "'");
        }
    }


    @Override
    public By getPageIdentifier() {
        return null;
    }

    public final void initSubPages() {
        PAGE_UTILS.initSubPages(this, seleniumActions);
    }

    @Override
    public final void refreshElements() {
        PageFactory.initElements(getActions().getBrowser().getWebDriver(), this);
        initSubPages();
        pageLoadHook();
    }

    @Override
    public void refreshPage() {
        getActions().getBrowser().refreshPage();
        refreshElements();
    }

    @Override
    public void leavePageHook() {

    }

    @Override
    public long getPageLoadTime() {
        return pageLoadTime;
    }
}
