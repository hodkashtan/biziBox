package keywords.home;

import keywords.BaseUiKeywords;
import org.testng.log4testng.Logger;
import pages.bizibox.top.home.HomePage;
import selenium.pagefactory.framework.browser.web.WebBrowser;
import uris.bizibox.BiziboxURIs;

import java.net.URI;


public class HomePageKeywords extends BaseUiKeywords {

    private static final Logger LOGGER = Logger.getLogger(HomePageKeywords.class);

    public HomePageKeywords() {
        super();
    }

    public HomePageKeywords(WebBrowser browserForKeywords) {
        super(browserForKeywords);
    }

    public void goToLoginPageByUrl() {
        WebBrowser browser = getBrowser();
        URI loginPageURI = BiziboxURIs.HOME_PAGE.getFullURI(getBaseTestURL());
        browser.openPageByURL(loginPageURI, HomePage.class);
    }

    public void loginApp(String username, String password) {
        goToLoginPageByUrl();
        HomePage loginPage = loadTopLevelPage(HomePage.class);
        loginPage.login(username, password);
    }

    public void reset() {
        goToLoginPageByUrl();
        HomePage loginPage = loadTopLevelPage(HomePage.class);
        loginPage.reset();
    }


}