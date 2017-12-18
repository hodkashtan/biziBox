package ui;

import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import org.testng.log4testng.Logger;

public class PortalTest {

    private static Logger LOGGER = Logger.getLogger(PortalTest.class);

    @BeforeClass
    public void setUp() {
        // code that will be invoked when this test is instantiated
    }

    @Test(groups = { "configure" })
    public void test1() {
        LOGGER.debug("This is ui test1");
    }

    @Test(groups = { "configure" })
    public void test2() {
        LOGGER.info("This is ui test2");
    }

    @AfterClass
    public void tearDown() {

    }

}
