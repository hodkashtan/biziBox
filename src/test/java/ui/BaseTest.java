package ui;

import common.CommonSystemProperties;
import keywords.navigation.NavigateByURLKeywords;
import keywords.setup.BiziboxSetupKeywords;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import org.testng.log4testng.Logger;
import selenium.pagefactory.framework.exception.BiziboxWebDriverException;

public class BaseTest {

    private static Logger LOGGER = Logger.getLogger(BaseTest.class);

    private static final NavigateByURLKeywords navigateByURLKeywords = new NavigateByURLKeywords();

    @BeforeClass(groups = { "configure" })
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
    public void test1() {
        LOGGER.info("This is test1");
//        navigateByURLKeywords.goToLoginPageByUrl();
//        HomePageKeywords homePageKeywords = new HomePageKeywords();
//        homePageKeywords.attemptLogin();
        navigateByURLKeywords.loginApp(CommonSystemProperties.BIZIBOX_USERNAME, CommonSystemProperties.BIZIBOX_PASSWORD);


    }

    @AfterClass
    public void tearDown() {
    }
}
