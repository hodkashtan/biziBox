package ui;

import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import org.testng.log4testng.Logger;

public class BaseTest {

    Logger logger = Logger.getLogger(BaseTest.class);

    @BeforeClass
    public void setUp() {
        // code that will be invoked when this test is instantiated
    }

    @Test(groups = { "configure" })
    public void test1() {
        logger.info("This is test1");
    }

    @Test(groups = { "configure" })
    public void test2() {
        logger.warn("This is test2");
    }

    @AfterClass
    public void tearDown() {

    }
}
