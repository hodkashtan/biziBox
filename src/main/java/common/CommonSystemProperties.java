package common;

import static uris.bizibox.URIUtil.removeTrailingSlash;

public class CommonSystemProperties {

    public final static String BIZIBOX_SERVER_URL = System.getProperty("tests.bizibox.server");
    public final static String BROWSER_NAME = System.getProperty("testng.selenium.browser", "CHROME");

    public static final String BROWSER_LOG_LEVEL = System.getProperty("tests.browser.log.level", "ALL");
    // ------- Browser & Selenium Properties ------
    public static final String BROWSER_PATH = System.getProperty("tests.browser.path");
    public static final String BROWSER_VERSION = System.getProperty("tests.browser.version");
    public static final String SELENIUM_HUB_URL = removeTrailingSlash(System.getProperty("tests.selenium.hub.url"));
    public static final int START_WINDOW_HEIGHT = Integer.parseInt(System.getProperty("tests.window.height", "1024"));
    public static final int START_WINDOW_WIDTH = Integer.parseInt(System.getProperty("tests.window.width", "1280"));
    public static final String WEBDRIVER_PATH = System.getProperty("tests.webdriver.path");

}
