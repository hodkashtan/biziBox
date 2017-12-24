package keywords.navigation;

import keywords.BaseUiKeywords;
import org.testng.log4testng.Logger;
import pages.bizibox.top.home.HomePage;
import selenium.pagefactory.framework.browser.web.WebBrowser;
import selenium.pagefactory.framework.config.TimeoutType;
import uris.bizibox.BiziboxURIs;

import java.net.URI;

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

    //----------- Go to pages by URL -----------

    public void goToLoginPageByUrl() {
        WebBrowser browser = getBrowser();
        URI loginPageURI = BiziboxURIs.HOME_PAGE.getFullURI(getBaseTestURL());
        browser.openPageByURL(loginPageURI, HomePage.class);
    }

    public void loginApp(String username, String password) {
        getActions().waitForPageToBeStable(TimeoutType.DEFAULT);
        LOGGER.info("Starting to login to Jive as '" + username + "' / '" + password + "'......");
        goToLoginPageByUrl();
        HomePage loginPage = loadTopLevelPage(HomePage.class);
        getActions().waitForPageToBeStable(TimeoutType.TWENTY_SECONDS);
        loginPage.enterUsername(username);
        loginPage.enterPassword(password);
        loginPage.clickLogin();
        getActions().waitForPageToBeStable(TimeoutType.TWENTY_SECONDS);
        LOGGER.info("SUCCESS - Logged in with the Jive login page as '" + username + "' / '" + password + "'");
    }
}
