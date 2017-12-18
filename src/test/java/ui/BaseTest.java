package ui;

import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import org.testng.log4testng.Logger;

public class BaseTest {

    private static Logger LOGGER = Logger.getLogger(BaseTest.class);

    @BeforeClass(groups = { "configure" })
    public void setUp() {
        // code that will be invoked when this test is instantiated
//        WebBrowser browser = ;
//        browser.loadTopLevelPage(BizibozPortal.class);
    }

    @Test(groups = { "configure" })
    public void test1() {
        LOGGER.info("This is test1");
    }

    @Test(groups = { "configure" })
    public void test2() {
        LOGGER.warn("This is test2");
    }

    @AfterClass
    public void tearDown() {

    }
}
