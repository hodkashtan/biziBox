package selenium.pagefactory.framework.browser.mobile;

import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.AndroidKeyCode;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.touch.TouchActions;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.testng.log4testng.Logger;
import selenium.pagefactory.framework.actions.AndroidSeleniumActions;
import selenium.pagefactory.framework.config.TimeoutsConfig;
import selenium.pagefactory.framework.exception.BiziboxWebDriverException;

import java.io.IOException;
import java.net.URL;
import java.time.Duration;

public class AndroidMobileBrowser extends MobileBrowser {

    protected boolean touchMode;
    private String appPackage;
    private String appActivity;
    private static final Logger LOGGER = Logger.getLogger(AndroidMobileBrowser.class);

    public AndroidMobileBrowser(String baseTestUrl,
                                String browserName,
                                String platform,
                                String platformName,
                                String platformVersion,
                                String deviceName,
                                String newCommandTimeout,
                                String automationName,
                                String version,
                                String autoLaunch,
                                String app,
                                String appPackage,
                                String appActivity,
                                TimeoutsConfig timeouts,
                                boolean touchMode,
                                boolean fullReset) throws BiziboxWebDriverException {
        super(baseTestUrl, timeouts, browserName, platform, platformName, platformVersion, deviceName,
                newCommandTimeout, automationName, version, autoLaunch, app, fullReset);
        this.touchMode = touchMode;
        this.appPackage = appPackage;
        this.appActivity = appActivity;
    }

    @Override
    public DesiredCapabilities getDesiredCapabilities() {
        DesiredCapabilities desiredCapabilities = new DesiredCapabilities();
        desiredCapabilities.setCapability(CapabilityType.BROWSER_NAME, browserName);
        desiredCapabilities.setCapability("platform", platform);
        desiredCapabilities.setCapability("platformName", platformName);
        desiredCapabilities.setCapability("platformVersion", platformVersion);
        desiredCapabilities.setCapability("deviceName", deviceName);
        desiredCapabilities.setCapability("newCommandTimeout", newCommandTimeout);
        desiredCapabilities.setCapability("automationName", automationName);
        desiredCapabilities.setCapability("version", version);
        desiredCapabilities.setCapability("autoLaunch", autoLaunch);
        desiredCapabilities.setCapability("app", app);
        desiredCapabilities.setCapability("appPackage", appPackage);
        desiredCapabilities.setCapability("appWaitActivity", appActivity);
        desiredCapabilities.setCapability("fullReset", fullReset);
        desiredCapabilities.setCapability(CapabilityType.ForSeleniumServer.ENSURING_CLEAN_SESSION, true);
        return desiredCapabilities;
    }

    protected AndroidDriver createWebDriver() throws BiziboxWebDriverException {
        try {
            printCapabilities(getDesiredCapabilities());
            return new SwipeableWebDriver(new URL(getBaseTestUrl()), getDesiredCapabilities());
        } catch (IOException e) {
            throw new BiziboxWebDriverException("Error starting appium driver service", e);
        }
    }

    @Override
    public AndroidSeleniumActions getActions() {
        return new AndroidSeleniumActions(this);
    }

    public String getAppPackage() {
        return appPackage;
    }

    public String getAppActivity() {
        return appActivity;
    }

    /**
     *
     * @return true if Android is API 17 or down, and as a result uses touch mode
     */
    public boolean isTouchMode() {
        return touchMode;
    }

    /**
     *
     * @param touchMode - true if Android is API 17 or lower
     */
    public void setTouchMode(boolean touchMode) {
        this.touchMode = touchMode;
    }

    /**
     * Swipe from the top to bottom for a second
     */
    @Override
    public void dragDown() {
        int midScreen = getScreenWidth() / 2;
        touchAction.longPress(midScreen, 450, Duration.ofMillis(1500)).moveTo(midScreen, getScreenHeight() - 250).release().perform();
    }

    /**
     * Swipe from the down to up for a second
     */
    @Override
    public void dragUp() {
        int midScreen = webDriver.manage().window().getSize().getWidth() / 2;
        touchAction.longPress(midScreen, getScreenHeight() - 250).moveTo(midScreen, 250).release().perform();
    }

    /**
     * Swipe from the top to bottom for a second
     *
     * @param yStart - coordinate to start swiping
     * @param yEnd - coordinate to stop swiping
     */
    @Override
    public void drag(int yStart, int yEnd) {
        int midScreen = getScreenWidth() / 2;
        touchAction.longPress(midScreen, yStart).moveTo(midScreen, yEnd).release().perform();
    }

    /**
     * Swipe from the top to bottom for a second
     *
     * @param yStart - coordinate to start swiping
     * @param yEnd - coordinate to stop swiping
     */

    @Override
    public void drag(int yStart, int yEnd, int duration) {
        int midScreen = getScreenWidth() / 2;
        touchAction.longPress(midScreen, yStart, Duration.ofMillis(duration)).moveTo(midScreen, yEnd).release().perform();
    }

    @Override
    public void tap(WebElement webElement) {
        if (touchMode) {
            TouchActions action = new TouchActions(webDriver);
            try {
                action.down(webElement.getLocation().getX(), webElement.getLocation().getY()).clickAndHold()
                        .release(webElement).perform();
            } catch (NullPointerException e) {

            }
        } else {
            touchAction.tap(webElement);
        }
    }

    @Override
    public void tap(int xLocation, int yLocation) {
        if (touchMode) {
            TouchActions action = new TouchActions(webDriver);
            try {
                action.down(xLocation, yLocation).clickAndHold().perform();
            } catch (NullPointerException e) {
                LOGGER.error("Failed To Tap due to NullPointerException", e);
            }
        } else {
            touchAction.tap(xLocation, yLocation);
        }
    }

    public void clickHomePage() {
        ((AndroidDriver)webDriver).pressKeyCode(AndroidKeyCode.HOME);
    }

    public void clickBack() {
        ((AndroidDriver)webDriver).pressKeyCode(AndroidKeyCode.BACK);
    }

    @Override
    public void scrollToTop() {
        LOGGER.error("Method ScrollToTop is not yet implemented");
    }
}