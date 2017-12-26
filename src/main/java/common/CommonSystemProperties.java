package common;

import static uris.bizibox.URIUtil.removeTrailingSlash;

public class CommonSystemProperties {

    // ------- Bizibox properties -----
    public final static String BIZIBOX_SERVER_URL = System.getProperty("tests.bizibox.server", "http://172.25.101.21:8080");
    public final static String BIZIBOX_USERNAME = System.getProperty("tests.bizibox.user", "autotest");
    public final static String BIZIBOX_PASSWORD = System.getProperty("tests.bizibox.password", "1234");

    // ------- Browser & Selenium Properties ------
    public static final String BROWSER_LOG_LEVEL = System.getProperty("tests.browser.log.level", "ALL");
    public final static String BROWSER_NAME = System.getProperty("testng.selenium.browser", "CHROME");
    public static final String BROWSER_PATH = System.getProperty("tests.browser.path");
    public static final String BROWSER_VERSION = System.getProperty("tests.browser.version");
    public static final String SELENIUM_HUB_URL = removeTrailingSlash(System.getProperty("tests.selenium.hub.url"));
    public static final int START_WINDOW_HEIGHT = Integer.parseInt(System.getProperty("tests.window.height", "1024"));
    public static final int START_WINDOW_WIDTH = Integer.parseInt(System.getProperty("tests.window.width", "1280"));
    public static final String WEB_DRIVER_PATH = System.getProperty("tests.webdriver.path");

    // ------- Configuration properties ------
    public static final boolean DISABLE_SUITE_SETUP = Boolean.valueOf(System.getProperty("tests.disableSuiteSetup"));
    public static final boolean IS_DOCKERIZED_ENV = Boolean.valueOf(System.getProperty("tests.docker.isdockerized", "false"));

}
