package selenium.pagefactory.framework.browser.mobile;

import io.appium.java_client.TouchAction;
import io.appium.java_client.android.AndroidDriver;
import org.testng.log4testng.Logger;
import selenium.pagefactory.framework.browser.Browser;
import selenium.pagefactory.framework.browser.web.WebBrowserType;
import selenium.pagefactory.framework.config.TimeoutsConfig;
import selenium.pagefactory.framework.exception.BiziboxWebDriverException;
import selenium.pagefactory.framework.pages.BaseTopLevelPage;
import selenium.pagefactory.framework.pages.TopLevelPage;
import io.appium.java_client.AppiumDriver;
import org.openqa.selenium.ScreenOrientation;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.time.Duration;
import java.util.Map;
import java.util.concurrent.TimeUnit;


/**
 * Mobile Browser - Extends Selenium's Appium Driver functionality
 * Working on Android and iOS
 * Supports pages
 */
public abstract class MobileBrowser extends Browser<AppiumDriver> {
    private static Logger LOGGER = Logger.getLogger(MobileBrowser.class);

    protected TouchAction touchAction;
    protected String browserName;
    protected String platform;
    protected String platformName;
    protected String platformVersion;
    protected String deviceName;
    protected String newCommandTimeout;
    protected String automationName;
    protected String version;
    protected String autoLaunch;
    protected String app;
    protected boolean fullReset;

    protected MobileBrowser(String baseTestUrl,
                            TimeoutsConfig timeoutsConfig, String browserName,
                            String platform,
                            String platformName,
                            String platformVersion,
                            String deviceName,
                            String newCommandTimeout,
                            String automationName,
                            String version,
                            String autoLaunch,
                            String app,
                            boolean fullReset) throws BiziboxWebDriverException {
        super(baseTestUrl, timeoutsConfig);
        this.browserName = browserName;
        this.platform = platform;
        this.platformName = platformName;
        this.platformVersion = platformVersion;
        this.deviceName = deviceName;
        this.newCommandTimeout = newCommandTimeout;
        this.automationName = automationName;
        this.version = version;
        this.autoLaunch = autoLaunch;
        this.app = app;
        this.fullReset = fullReset;
    }

    public void initializeBrowser() throws BiziboxWebDriverException {
        this.webDriver = createWebDriver();
        this.webDriver.manage().timeouts().implicitlyWait(getImplicitWaitTimeoutMillis(), TimeUnit.MILLISECONDS);
        touchAction = new TouchAction(webDriver);
    }

    public int getScreenWidth() {
        return this.webDriver.manage().window().getSize().getWidth();
    }

    public int getScreenHeight() {
        return this.webDriver.manage().window().getSize().getHeight();
    }

    protected abstract AppiumDriver createWebDriver() throws BiziboxWebDriverException;

    protected void printCapabilities(DesiredCapabilities desiredCapabilities) {
        LOGGER.info("Loading capabilities..");
        for (Map.Entry<String, ?> desiredCapability : desiredCapabilities.asMap().entrySet()) {
            LOGGER.info(desiredCapability.getKey() + "  -  " + desiredCapability.getValue());
        }
    }

    /**
     * Refresh the current page, without giving back a newly initialized Page object.
     */
    @Override
    public void refreshPage() {
        runLeavePageHook();
        BaseTopLevelPage currentPage = PAGE_UTILS.loadCurrentPage(BaseTopLevelPage.class, webDriver, this.getActions());
        currentPage.refreshPage();
        if (optionalCachedPage.isPresent()) {
            TopLevelPage cachedPage = optionalCachedPage.get().getCachedPage();
            cachedPage.refreshElements();
        }
    }

    /**
     *
     * @param pageClass - the class of the expected Page after refreshing.
     * @param <T> - class that extends TopLevelPage class
     * @return - a page of the requested class
     */
    @Override
    public <T extends TopLevelPage> T refreshPage(Class<T> pageClass) {
        runLeavePageHook();
        invalidateCachedPage();
        T page = loadTopLevelPage(pageClass);
        page.refreshPage();
        page = loadTopLevelPage(pageClass);
        setCachedPage(page);
        return page;
    }

    @Override
    public WebBrowserType getBrowserType() {
        return WebBrowserType.MOBILE;
    }

    public String getPlatformName() {
        return platformName;
    }

    public String getPlatform() {
        return platform;
    }

    public String getPlatformVersion() {
        return platformVersion;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public String getApp() {
        return app;
    }

    //**********~~~~~~~~~~~~~ Mobile Actions ~~~~~~~~~~~~~~~*************

    public void rotateLandscape() {
        webDriver.rotate(ScreenOrientation.LANDSCAPE);
    }

    public void rotatePortrait() {
        webDriver.rotate(ScreenOrientation.PORTRAIT);
    }

    /**
     * Swipe from the right to left for a second
     */
    public void swipeLeft() {
        touchAction.longPress(getScreenWidth() - 5, getScreenHeight() / 2).moveTo(5, getScreenHeight() / 2).release().perform();
    }

    /**
     * Swipe from the left to right for a second
     */
    public void swipeRight() {
        touchAction.longPress(5, getScreenHeight() / 2).moveTo(getScreenWidth() - 5, getScreenHeight() / 2).release().perform();
    }

    /**
     * Swipe from the top to bottom for a second
     */
    public void dragDown() {
        int midScreen = getScreenWidth() / 2;
        touchAction.longPress(midScreen, 250).moveTo(midScreen, getScreenHeight() - 250).release().perform();
    }

    /**
     * Swipe from the down to up for a second
     */
    public void dragUp() {
        int midScreen = webDriver.manage().window().getSize().getWidth() / 2;
        touchAction.longPress(midScreen, getScreenHeight() - 250).moveTo(midScreen, 250).release().perform();
    }

    /**
     * Swipe from the top to bottom for 2.5 seconds
     * @param yStart - 0 is the upper side of the smart-phone
     * @param yEnd - the end coordinate of the drag function
     */
    public void drag(int yStart, int yEnd) {
        int midScreen = getScreenWidth() / 2;
        touchAction.longPress(midScreen, yStart).moveTo(midScreen, yEnd).release().perform();
    }

    public void drag(int yStart, int yEnd, int duration) {
        int midScreen = getScreenWidth() / 2;
        touchAction.longPress(midScreen, yStart, Duration.ofMillis(duration)).moveTo(midScreen, yEnd).release().perform();
    }

    /**
     *
     * @param startX - 0 is the left side of the smart-phone
     * @param endX - end coordinate of the right/left movement
     * @param startY - 0 is the upper side of the smart-phone
     * @param endY - end coordinate of the up/down movement
     * @param duration - in milliseconds
     */
    public void swipe(int startX, int endX, int startY, int endY, int duration) {
        touchAction.press(startX, startY).moveTo(endX, endY).release().perform();
    }

    public void putApplicationToBackground(int duration) {
        webDriver.runAppInBackground(Duration.ofSeconds(duration));
    }

    public void lockMobile(int duration) {
    }

    public void tap(WebElement webElement) {
        touchAction.tap(webElement);
    }
    public void tap(int xLocation, int yLocation) {
        touchAction.tap(xLocation, yLocation);
    }

    public void initApp() {
        webDriver.closeApp();
        webDriver.launchApp();
    }

    public abstract void scrollToTop();
}
