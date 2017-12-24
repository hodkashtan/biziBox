package selenium.pagefactory.framework.browser.mobile;

import io.appium.java_client.ios.IOSDriver;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import selenium.pagefactory.framework.actions.IOSSeleniumActions;
import selenium.pagefactory.framework.config.TimeoutsConfig;
import selenium.pagefactory.framework.exception.BiziboxWebDriverException;

import java.io.IOException;
import java.net.URL;
import java.time.Duration;
import java.util.HashMap;


/**
 * Known bug of Apple from Xcode 5 and iOS 7.1 Simulator - swipe is not working on simulator.
 * As a workaround, using scrollTo in JavaScript.
 * As in real devices regular swipe works but not scrollTo, using the regular command as well
 * TODO: verify if relevant for selenium 3.8.1 and higher iOS
 */

public class IOSMobileBrowser extends MobileBrowser {

    public IOSMobileBrowser(String baseTestUrl,
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
                            boolean fullReset,
                            TimeoutsConfig timeouts) throws BiziboxWebDriverException {
        super(baseTestUrl, timeouts, browserName, platform, platformName, platformVersion, deviceName,
                newCommandTimeout, automationName, version, autoLaunch, app, fullReset);
    }

    @Override
    public DesiredCapabilities getCapabilities() {
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
        desiredCapabilities.setCapability("fullReset", fullReset);
        desiredCapabilities.setCapability("rotatable", "true");
        return desiredCapabilities;
    }

    protected IOSDriver createWebDriver() throws BiziboxWebDriverException {
        try {
            printCapabilities(getCapabilities());
            return new IOSDriver(new URL(getBaseTestUrl()), getCapabilities());
        } catch (IOException e) {
            throw new BiziboxWebDriverException("Error starting appium driver service", e);
        }
    }

    @Override
    public IOSSeleniumActions getActions() {
        return new IOSSeleniumActions(this);
    }
    /**
     * Swipe from the right to left for a second
     */
    public void swipeLeft() {
        super.swipeLeft();
        HashMap<String, String> scrollObject = new HashMap<String, String>();
        scrollObject.put("direction", "left");
        webDriver.executeScript("mobile: scroll", scrollObject);
    }

    /**
     * Swipe from the left to right for a second
     */
    public void swipeRight() {
        super.swipeRight();
        HashMap<String, String> scrollObject = new HashMap<String, String>();
        scrollObject.put("direction", "right");
        webDriver.executeScript("mobile: scroll", scrollObject);
    }

    /**
     * Swipe from the top to buttom for a second
     */
    public void dragDown() {
        int midScreen = getScreenWidth() / 2;
        touchAction.longPress(midScreen, 140, Duration.ofMillis(1500)).moveTo(midScreen, getScreenHeight() - 140).release().perform();
    }

    /**
     * Swipe from the down to up for a second
     */
    public void dragUp() {
        int midScreen = getScreenWidth() / 2;
        touchAction.longPress(midScreen, getScreenHeight() - 140, Duration.ofMillis(1500)).moveTo(midScreen, 140).release().perform();
    }

    /**
     * Will function only with real device
     * @param startX - 0 is the left side of the smart-phone
     * @param endX - coordinate to stop swipe
     * @param startY - 0 is the upper side of the smart-phone
     * @param endY - coordinate to stop swipe
     * @param duration - in milliseconds
     */
    public void swipe(int startX, int endX, int startY, int endY, int duration) {
        touchAction.press(startX, startY).moveTo(endX, endY).release().perform();
    }

    /**
     *  Uses iOS functionality of automatic scroll to top when clicking status bar
     */
    public void scrollToTop() {
        getWebDriver().findElementByClassName("UIAStatusBar").click();
    }

    public void openNotifications() {
        int midScreenWidth = getScreenWidth() / 2;
        touchAction.press(midScreenWidth, 0).moveTo(midScreenWidth, getScreenHeight()).release().perform();
        webDriver.quit();
    }
}
