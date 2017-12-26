package keywords;

import common.CommonSystemProperties;
import env.Suite;
import selenium.pagefactory.framework.actions.SeleniumActions;
import selenium.pagefactory.framework.browser.web.WebBrowser;
import selenium.pagefactory.framework.pages.SubPage;
import selenium.pagefactory.framework.pages.TopLevelPage;

public abstract class BaseUiKeywords {
    public BaseUiKeywords() {
        this(null);
    }

    public BaseUiKeywords(WebBrowser browserForKeywords) {
        this.browserForKeywords = browserForKeywords;
    }

    /**
     * Get the current browser for running tests.
     * If the browser was specifically set for this Keywords instance, then use that one.
     *
     * Otherwise, return the singleton browser used in a single-threaded environment by default.
     * @return - a WebBrowser instance.
     */
    public WebBrowser getBrowser() {
        if (browserForKeywords != null) {
            return browserForKeywords;
        }

        return Suite.getInstance()
                .getCurrentTestState()
                .getBrowser();
    }

    // Browser to use for Keywords class, mainly for multi-threaded, multi-browser tests
    private final WebBrowser browserForKeywords;

    public String getBaseTestURL() {
        if (browserForKeywords != null) {
            return browserForKeywords.getBaseTestUrl();
        }

        if (Suite.getInstance().getCurrentTestState() != null) {
            return Suite.getInstance()
                    .getCurrentTestState()
                    .getBrowser()
                    .getBaseTestUrl();
        }

        return CommonSystemProperties.BIZIBOX_SERVER_URL;
    }

    public SeleniumActions getActions() {
        return getBrowser().getActions();
    }

    public <T extends TopLevelPage> T loadTopLevelPage(Class<T> pageClass) {
        WebBrowser browser = getBrowser();
        return browser.loadTopLevelPage(pageClass);
    }

    public <T extends SubPage> T loadSubPage(Class<T> pageClass) {
        WebBrowser browser = getBrowser();
        return browser.loadSubPage(pageClass);
    }
}
