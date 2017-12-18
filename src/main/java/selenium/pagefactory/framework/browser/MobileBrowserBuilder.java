package selenium.pagefactory.framework.browser;

import com.google.common.base.MoreObjects;
import com.google.common.base.Preconditions;
import org.testng.log4testng.Logger;
import selenium.pagefactory.framework.browser.mobile.AndroidMobileBrowser;
import selenium.pagefactory.framework.browser.mobile.IOSMobileBrowser;
import selenium.pagefactory.framework.browser.mobile.MobileBrowser;
import selenium.pagefactory.framework.browser.mobile.MobilePlatformName;
import selenium.pagefactory.framework.config.TimeoutsConfig;
import selenium.pagefactory.framework.exception.BiziboxWebDriverException;

/**
 * <p>Builder class for creating an App that is running on an emulator or a connected device that connected
 *    to the same host as the test code.
 *    Creates either a {@link selenium.pagefactory.framework.browser.mobile.AndroidMobileBrowser},
 *    {@link selenium.pagefactory.framework.browser.mobile.IOSMobileBrowser}..</p>
 *
 *  <p>A Browser is basically a wrapper for a WebDriver that greatly simplifies configuration,
 *  adds useful utilities, and has methods
 *  for loading {@link selenium.pagefactory.framework.pages.Page}'s.
 *
 *  Pages provide an object-oriented solution to Selenium testing. You can write Page classes that model a web page
 *  in the web app you are testing.</p>
 */
public class MobileBrowserBuilder {
    private static final Logger LOGGER = Logger.getLogger(MobileBrowserBuilder.class);

    private String baseTestUrl;
    private TimeoutsConfig timeoutsConfig;
    private String browserName;
    private MobilePlatformName platformName;
    private String platformVersion;
    private String platform;
    private String deviceName;
    private String app;
    private String appPackage;
    private String appActivity;
    private String newCommandTimeout;
    private String automationName;
    private String version;
    private String autoLaunch;
    private boolean fullReset;
    private boolean touchMode;


    private MobileBrowserBuilder(String baseTestUrl,
                                 MobilePlatformName platformName) {
        this.baseTestUrl = Preconditions.checkNotNull(baseTestUrl, "You must provide a non-null baseTestUrl!");
        this.timeoutsConfig = TimeoutsConfig.defaultTimeoutsConfig();
        this.platformName = Preconditions.checkNotNull(platformName, "You must provide a non-null platformName!");
        this.fullReset = true;

    }

    //------------Getters in case the client wants to inspect the config they have so far-----------
    public String getBaseTestUrl() {
        return baseTestUrl;
    }

    public TimeoutsConfig getTimeoutsConfig() {
        return timeoutsConfig;
    }

    public String getBrowserName() {
        return browserName;
    }

    public MobilePlatformName getPlatformName() {
        return platformName;
    }

    public String getPlatformVersion() {
        return platformVersion;
    }

    public String getPlatform() {
        return platform;
    }

    public String getNewCommandTimeout() {
        return newCommandTimeout;
    }

    public String getAutomationName() {
        return automationName;
    }

    public String getVersion() {
        return version;
    }

    public String getAutoLaunch() {
        return autoLaunch;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public String getApp() {
        return app;
    }

    public String getAppPackage() {
        return appPackage;
    }

    public String getAppActivity() {
        return appActivity;
    }

    public boolean isTouchMode() {
        return touchMode;
    }

    public boolean isFullReset() {
        return fullReset;
    }

    /**
     * Get a MobileBrowserBuilder for Android and base URL for the webapp you are testing against.
     * @param baseTestUrl - base URL for your webapp, e.g. http://my.site.com/base
     */
    public static MobileBrowserBuilder getAndroidBuilder(String baseTestUrl) {
        return new MobileBrowserBuilder(baseTestUrl, MobilePlatformName.ANDROID);
    }

    /**
     * Get a MobileBrowserBuilder for iOS and base URL for the webapp you are testing against.
     * @param baseTestUrl - base URL for your webapp, e.g. http://my.site.com/base
     */
    public static MobileBrowserBuilder getIOSBuilder(String baseTestUrl) {
        return new MobileBrowserBuilder(baseTestUrl, MobilePlatformName.IOS);
    }


    /**
     * Creates the MobileBrowser instance, which includes creating the actual Browser process via the underlying Appium
     * Server
     * @return - a {@link selenium.pagefactory.framework.browser.mobile.AndroidMobileBrowser},
     * {@link selenium.pagefactory.framework.browser.mobile.IOSMobileBrowser}
     * @throws selenium.pagefactory.framework.exception.BiziboxWebDriverException when something goes wrong with creating a new WebDriver
     */
    public MobileBrowser build() throws BiziboxWebDriverException {
        LOGGER.info("Building Mobile Browser with the following config: \n" + toString());
        MobileBrowser browser;
        switch (platformName) {
            case ANDROID:
                browser = new AndroidMobileBrowser(baseTestUrl, browserName, platform, platformName.getPlatformName(),
                        platformVersion, deviceName, newCommandTimeout, automationName, version, autoLaunch,
                        app, appPackage, appActivity, timeoutsConfig, touchMode, fullReset);
                break;
            case IOS:
                browser = new IOSMobileBrowser(baseTestUrl, browserName, platform, platformName.getPlatformName(),
                        platformVersion, deviceName, newCommandTimeout, automationName, version, autoLaunch,
                        app, fullReset, timeoutsConfig);
                break;
            default:
                throw new IllegalArgumentException("Only IOS and Android are currently supported!");
        }
        browser.initializeBrowser();
        return browser;
    }

    public MobileBrowserBuilder withTouchMode(boolean touchMode) {
        this.touchMode = touchMode;
        return this;
    }

    public MobileBrowserBuilder withTimeoutsConfig(TimeoutsConfig timeoutsConfig) {
        this.timeoutsConfig = timeoutsConfig == null ? TimeoutsConfig.defaultTimeoutsConfig() : timeoutsConfig;
        return this;
    }

    public MobileBrowserBuilder withBrowserName(String browserName) {
        this.browserName = browserName;
        return this;
    }

    public MobileBrowserBuilder withPlatformName(MobilePlatformName platformName) {
        this.platformName = platformName;
        return this;
    }

    public MobileBrowserBuilder withPlatformVersion(String platformVersion) {
        this.platformVersion = platformVersion;
        return this;
    }

    public MobileBrowserBuilder withDeviceName(String deviceName) {
        this.deviceName = deviceName;
        return this;
    }

    public MobileBrowserBuilder withApp(String app) {
        this.app = app;
        return this;
    }

    public MobileBrowserBuilder withAppPackage(String appPackage) {
        this.appPackage = appPackage;
        return this;
    }

    public MobileBrowserBuilder withAppActivity(String appActivity) {
        this.appActivity = appActivity;
        return this;
    }

    public MobileBrowserBuilder withNewCommandTimeout(String newCommandTimeout) {
        this.newCommandTimeout = newCommandTimeout;
        return this;
    }

    public MobileBrowserBuilder withAutomationName(String automationName) {
        this.automationName = automationName;
        return this;
    }

    public MobileBrowserBuilder withVersion(String version) {
        this.version = version;
        return this;
    }

    public MobileBrowserBuilder withAutoLaunch(String autoLaunch) {
        this.autoLaunch = autoLaunch;
        return this;
    }

    public MobileBrowserBuilder withPlatform(String platform) {
        this.platform = platform;
        return this;
    }

    public MobileBrowserBuilder withFullReset(boolean fullReset) {
        this.fullReset = fullReset;
        return this;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("baseTestUrl", baseTestUrl)
                .add("browserName", browserName)
                .add("platformName", platformName.getPlatformName())
                .add("platform", platform)
                .add("platformVersion", platformVersion)
                .add("deviceName", deviceName)
                .add("app", app)
                .add("appPackage", appPackage)
                .add("appActivity", appActivity)
                .add("newCommandTimeout", newCommandTimeout)
                .add("automationName", automationName)
                .add("version", version)
                .add("autoLaunch", autoLaunch)
                .toString();
    }
}
