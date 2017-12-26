package ui;

import common.CommonSystemProperties;
import keywords.home.HomePageKeywords;
import keywords.setup.BiziboxSetupKeywords;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeGroups;
import org.testng.annotations.Test;
import org.testng.log4testng.Logger;
import selenium.pagefactory.framework.exception.BiziboxWebDriverException;

public class BaseTest {

    private static Logger LOGGER = Logger.getLogger(BaseTest.class);

    @BeforeGroups(groups = { "configure" })
    public void setUp() {
        // code that will be invoked when this test is instantiated
        try {
            BiziboxSetupKeywords biziboxSetupKeywords = new BiziboxSetupKeywords();
            biziboxSetupKeywords.initSuite();
            biziboxSetupKeywords.initBrowserAndTestState();
        } catch (BiziboxWebDriverException e) {
            LOGGER.error("Failed to init suite", e);
        }
    }

    @Test(groups = { "configure" })
    public void resetCredentials() {
        HomePageKeywords homePageKeywords = new HomePageKeywords();
        homePageKeywords.reset();
    }

//    @Test(groups = { "configure" })
//    public void loginWrongCredentials() {
//        LOGGER.info("Go to login page, enter wrong credentials and login");
//        HomePageKeywords homePageKeywords = new HomePageKeywords();
//        homePageKeywords.loginApp(CommonSystemProperties.BIZIBOX_USERNAME + "_invalid", CommonSystemProperties.BIZIBOX_PASSWORD);
//    }

    @Test(groups = { "configure" })
    public void login() {
        LOGGER.info("Go to login page, enter credentials and login");
        HomePageKeywords homePageKeywords = new HomePageKeywords();
        homePageKeywords.loginApp(CommonSystemProperties.BIZIBOX_USERNAME, CommonSystemProperties.BIZIBOX_PASSWORD);
    }


    @AfterClass
    public void tearDown() {

    }
}
