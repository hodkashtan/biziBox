package selenium.pagefactory.framework.browser;

import com.google.common.base.MoreObjects;
import com.google.common.base.Preconditions;
import org.testng.log4testng.Logger;
import selenium.pagefactory.framework.browser.web.*;
import selenium.pagefactory.framework.config.TimeoutsConfig;
import selenium.pagefactory.framework.exception.BiziboxWebDriverException;
import org.openqa.selenium.Platform;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.logging.Level;

/**
 * <p>Builder class for creating a {@link selenium.pagefactory.framework.browser.web.RemoteBrowser}.
 * A RemoteBrowser is a browser running in a Selenium Grid, that works
 * by connecting to a Selenium Hub. See https://code.google.com/p/selenium/wiki/Grid2</p>
 *
 * <p>In other words, a RemoteBrowser is a wrapper around a {@link org.openqa.selenium.remote.RemoteWebDriver}
 * that simplifies configuration and unifies options across all Browsers.</p>
 *
 * <p>You can call {@link #getBuilder(WebBrowserType, String, String)} to get a builder, or you can equivalently call
 * {@link #getChromeBuilder(String, String)}, {@link #getFirefoxBuilder(String, String)}, or {@link #getInternetExplorerBuilder(String, String)}.
 *
 * Calling RemoteBrowserBuilder.getBuilder(BrowserType.CHROME, ...)
 * is equivalent to calling RemoteBrowserBuilder.getChromeBuilder(...).</p>
 */
public class RemoteBrowserBuilder {
    private static final Logger LOGGER = Logger.getLogger(RemoteBrowserBuilder.class);

    private final WebBrowserType browserType;
    private final String baseTestUrl;
    private final String seleniumHubURL;

    private TimeoutsConfig timeoutsConfig;

    private Optional<String> browserVersion = Optional.empty();
    private Optional<String> browserLocale = Optional.empty();
    private Optional<Integer> startWindowWidth = Optional.empty();
    private Optional<Integer> startWindowHeight = Optional.empty();
    private Optional<Level> browserLogLevel = Optional.empty();
    private Optional<String> browserLogFile = Optional.empty();
    private Optional<Platform> platform = Optional.empty();
    private Optional<List<String>> options = Optional.empty();
    private Optional<Map<String, String>> extrasCapabilities = Optional.empty();

    private RemoteBrowserBuilder(WebBrowserType browserType,
                                 String baseTestUrl,
                                 String seleniumHubURL) {
        this.browserType = Preconditions.checkNotNull(browserType, "You must provide a non-null BrowserType!");
        this.baseTestUrl = Preconditions.checkNotNull(baseTestUrl, "You must provide a non-null baseTestUrl!");
        this.seleniumHubURL = Preconditions.checkNotNull(seleniumHubURL, "You must provide a non-null seleniumHubURL");
        this.timeoutsConfig = TimeoutsConfig.defaultTimeoutsConfig();
    }

    //------------Getters in case the client wants to inspect the config they have so far-----------
    public WebBrowserType getBrowserType() {
        return browserType;
    }

    public String getBaseTestUrl() {
        return baseTestUrl;
    }

    public String getSeleniumHubURL() {
        return seleniumHubURL;
    }

    public TimeoutsConfig getTimeoutsConfig() {
        return timeoutsConfig;
    }

    public Optional<String> getBrowserVersion() {
        return browserVersion;
    }

    public Optional<String> getBrowserLocale() {
        return browserLocale;
    }

    public Optional<Integer> getStartWindowWidth() {
        return startWindowWidth;
    }

    public Optional<Integer> getStartWindowHeight() {
        return startWindowHeight;
    }

    public Optional<Level> getBrowserLogLevel() {
        return browserLogLevel;
    }

    public Optional<String> getBrowserLogFile() {
        return browserLogFile;
    }

    public Optional<Platform> getPlatform() {
        return platform;
    }

    public Optional<List<String>> getOptions() {
        return options;
    }


    /**
     * Get a RemoteBrowserBuilder used to construct a RemoteBrowser instance that helps you to run Selenium tests
     * against a remote Browser running in a Selenium Grid.
     *
     * @param browserType - CHROME, FIREFOX, or IE
     * @param baseTestUrl - base URL of the webapp you are testing, e.g. http://my.site.com/base
     * @param seleniumHubURL - URL with port to the Selenium HUB, e.g. http://selenium.my.company.com:4444/wd/hub
     */
    public static RemoteBrowserBuilder getBuilder(WebBrowserType browserType,
                                              String baseTestUrl,
                                              String seleniumHubURL) {
        return new RemoteBrowserBuilder(browserType, baseTestUrl, seleniumHubURL);
    }

    /**
     * Get a RemoteBrowserBuilder used to construct a RemoteBrowser instance that helps you to run Selenium tests
     * against a remote Browser running in a Selenium Grid. For CHROME browser.
     *
     * @param baseTestUrl - base URL of the webapp you are testing, e.g. http://my.site.com/base
     * @param seleniumHubURL - URL with port to the Selenium HUB, e.g. http://selenium.my.company.com:4444/wd/hub
     */
    public static RemoteBrowserBuilder getChromeBuilder(String baseTestUrl, String seleniumHubURL) {
        return new RemoteBrowserBuilder(WebBrowserType.CHROME, baseTestUrl, seleniumHubURL);
    }

    /**
     * Get a RemoteBrowserBuilder used to construct a RemoteBrowser instance that helps you to run Selenium tests
     * against a remote Browser running in a Selenium Grid. For FIREFOX browser.
     *
     * @param baseTestUrl - base URL of the webapp you are testing, e.g. http://my.site.com/base
     * @param seleniumHubURL - URL with port to the Selenium HUB, e.g. http://selenium.my.company.com:4444/wd/hub
     */
    public static RemoteBrowserBuilder getFirefoxBuilder(String baseTestUrl, String seleniumHubURL) {
        return new RemoteBrowserBuilder(WebBrowserType.FIREFOX, baseTestUrl, seleniumHubURL);
    }

    /**
     * Get a RemoteBrowserBuilder used to construct a RemoteBrowser instance that helps you to run Selenium tests
     * against a remote Browser running in a Selenium Grid. For IE browser.
     *
     * @param baseTestUrl - base URL of the webapp you are testing, e.g. http://my.site.com/base
     * @param seleniumHubURL - URL with port to the Selenium HUB, e.g. http://selenium.my.company.com:4444/wd/hub
     */
    public static RemoteBrowserBuilder getInternetExplorerBuilder(String baseTestUrl, String seleniumHubURL) {
        return new RemoteBrowserBuilder(WebBrowserType.IE, baseTestUrl, seleniumHubURL);
    }

    /**
     * Get a RemoteBrowserBuilder used to construct a RemoteBrowser instance that helps you to run Selenium tests
     * against a remote Browser running in a Selenium Grid. For Edge browser.
     *
     * @param baseTestUrl - base URL of the webapp you are testing, e.g. http://my.site.com/base
     * @param seleniumHubURL - URL with port to the Selenium HUB, e.g. http://selenium.my.company.com:4444/wd/hub
     */
    public static RemoteBrowserBuilder getEdgeBuilder(String baseTestUrl, String seleniumHubURL) {
        return new RemoteBrowserBuilder(WebBrowserType.EDGE, baseTestUrl, seleniumHubURL);
    }

    /**
     * Get a RemoteBrowserBuilder used to construct a RemoteBrowser instance that helps you to run Selenium tests
     * against a remote Browser running in a Selenium Grid. For Safari browser.
     *
     * @param baseTestUrl - base URL of the webapp you are testing, e.g. http://my.site.com/base
     * @param seleniumHubURL - URL with port to the Selenium HUB, e.g. http://selenium.my.company.com:4444/wd/hub
     */
    public static RemoteBrowserBuilder getSafariBuilder(String baseTestUrl, String seleniumHubURL) {
        return new RemoteBrowserBuilder(WebBrowserType.SAFARI, baseTestUrl, seleniumHubURL);
    }

    /**
     * Creates the RemoteBrowser instance, which includes creating the actual Browser process via the underlying WebDriver.
     *
     * @return - a {@link selenium.pagefactory.framework.browser.web.RemoteBrowser},
     * @throws BiziboxWebDriverException when something goes wrong with creating a new WebDriver.
     */
    public RemoteBrowser build() throws BiziboxWebDriverException {
        LOGGER.info("Building Remote Browser with the following config: \n" + toString());
        WebBrowser browser;
        switch (browserType) {
            case FIREFOX:
                browser = new FirefoxBrowser(baseTestUrl, timeoutsConfig, Optional.empty(), Optional.empty(), browserVersion, browserLocale, startWindowWidth, startWindowHeight, platform, extrasCapabilities);
                break;
            case CHROME:
                browser = new ChromeBrowser(baseTestUrl, timeoutsConfig, Optional.empty(), Optional.empty(), browserVersion, browserLocale, startWindowWidth, startWindowHeight,
                        browserLogLevel, browserLogFile, platform, options, extrasCapabilities);
                break;
            case IE:
                browser = new InternetExplorerBrowser(baseTestUrl, timeoutsConfig, Optional.empty(), Optional.empty(), browserVersion, browserLocale, startWindowWidth, startWindowHeight,
                        browserLogLevel, browserLogFile, platform, extrasCapabilities);
                break;
            case EDGE:
                browser = new EdgeBrowser(baseTestUrl, timeoutsConfig, Optional.empty(), Optional.empty(), browserVersion, browserLocale, startWindowWidth, startWindowHeight,
                        browserLogLevel, browserLogFile, platform, extrasCapabilities);
                break;
            case SAFARI:
                browser = new SafariBrowser(baseTestUrl, timeoutsConfig, Optional.empty(), Optional.empty(), browserVersion, browserLocale, startWindowWidth, startWindowHeight,
                                            browserLogLevel, browserLogFile, Optional.empty(), extrasCapabilities);
                break;

            default:
                throw new IllegalArgumentException("Only FIREFOX, CHROME, IE, EDGE, and SAFARI are currently supported!");
        }
        RemoteBrowser remoteBrowser = new RemoteBrowser(browser, seleniumHubURL);
        remoteBrowser.initializeBrowser();
        return remoteBrowser;
    }

    public RemoteBrowserBuilder withTimeoutsConfig(TimeoutsConfig timeoutsConfig) {
        this.timeoutsConfig = timeoutsConfig == null ? TimeoutsConfig.defaultTimeoutsConfig() : timeoutsConfig;
        return this;
    }

    public RemoteBrowserBuilder withBrowserVersion(String browserVersion) {
        this.browserVersion = Optional.ofNullable(browserVersion);
        return this;
    }

    public RemoteBrowserBuilder withBrowserLocale(String browserLocale) {
        this.browserLocale = Optional.ofNullable(browserLocale);
        return this;
    }

    public RemoteBrowserBuilder withStartWindowWidth(Integer startWindowWidth) {
        this.startWindowWidth = Optional.ofNullable(startWindowWidth);
        return this;
    }

    public RemoteBrowserBuilder withStartWindowHeight(Integer startWindowHeight) {
        this.startWindowHeight = Optional.ofNullable(startWindowHeight);
        return this;
    }

    public RemoteBrowserBuilder withBrowserLogLevel(Level browserLogLevel) {
        this.browserLogLevel = Optional.ofNullable(browserLogLevel);
        return this;
    }

    public RemoteBrowserBuilder withBrowserLogFile(String browserLogFile) {
        this.browserLogFile = Optional.ofNullable(browserLogFile);
        return this;
    }

    public RemoteBrowserBuilder withPlatform(Platform platform) {
        this.platform = Optional.ofNullable(platform);
        return this;
    }

    public RemoteBrowserBuilder withOptions(List<String> options) {
        this.options = Optional.ofNullable(options);
        return this;
    }

    public RemoteBrowserBuilder withExtraCapabilities(Map<String, String> extras) {
        this.extrasCapabilities = Optional.ofNullable(extras);
        return this;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("browserType", browserType)
                .add("baseTestUrl", baseTestUrl)
                .add("seleniumHubURL", seleniumHubURL)
                .add("browserVersion", browserVersion)
                .add("browserLocale", browserLocale)
                .add("startWindowWidth", startWindowWidth)
                .add("startWindowHeight", startWindowHeight)
                .add("browserLogLevel", browserLogLevel)
                .add("browserLogFile", browserLogFile)
                .add("platform", platform)
                .add("options", options)
                .toString();
    }

}
