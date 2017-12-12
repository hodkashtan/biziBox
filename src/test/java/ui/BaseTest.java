package ui;

import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

public class BaseTest {

    @BeforeClass
    public void setUp() {
        // code that will be invoked when this test is instantiated
    }

    @Test(groups = { "configure" })
    public void test1() {
        System.out.println("This is test1");
    }

    @Test(groups = { "configure" })
    public void test2() {
        System.out.println("This is test2");
    }

    @AfterClass
    public void tearDown() {

    }
}
