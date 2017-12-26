package keywords.navigation;

import keywords.BaseUiKeywords;
import org.testng.log4testng.Logger;
import selenium.pagefactory.framework.browser.web.WebBrowser;

public class NavigateByURLKeywords extends BaseUiKeywords {
    private static final Logger LOGGER = Logger.getLogger(NavigateByURLKeywords.class);

    public NavigateByURLKeywords() {
        super();
    }

    public NavigateByURLKeywords(WebBrowser webBrowser) {
        super(webBrowser);
    }

    /**
     * Get the current URL to use later
     */
    public String getCurrentURL() {
        return getBrowser().getWebDriver().getCurrentUrl();
    }

    /**
     * Go to any URL
     */
    public void goToURL(String url) {
        getBrowser().openPageByURL(url);
    }
}