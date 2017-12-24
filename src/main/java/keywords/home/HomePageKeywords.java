package keywords.home;

import common.CommonSystemProperties;
import keywords.BaseUiKeywords;
import pages.bizibox.top.home.HomePage;
import selenium.pagefactory.framework.browser.web.WebBrowser;


public class HomePageKeywords extends BaseUiKeywords {

    public HomePageKeywords() {
        super();
    }

    public HomePageKeywords(WebBrowser browserForKeywords) {
        super(browserForKeywords);
    }

    public void attemptLogin() {
        HomePage homePage = loadTopLevelPage(HomePage.class);
        homePage.attemptLogin(CommonSystemProperties.BIZIBOX_USERNAME, CommonSystemProperties.BIZIBOX_PASSWORD);
    }

}
