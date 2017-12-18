package selenium.pagefactory.framework.browser;

import com.google.common.base.MoreObjects;
import com.google.common.base.Preconditions;
import org.testng.log4testng.Logger;
import selenium.pagefactory.framework.browser.web.*;
import selenium.pagefactory.framework.config.TimeoutsConfig;
import selenium.pagefactory.framework.exception.BiziboxWebDriverException;

import java.util.List;
import java.util.Optional;
import java.util.logging.Level;

/**
 * <p>Builder class for creating a Browser that is running on the same host as the test code.
 *    Creates either a {@link selenium.pagefactory.framework.browser.web.ChromeBrowser},
 *    {@link selenium.pagefactory.framework.browser.web.FirefoxBrowser}, or
 *    {@link selenium.pagefactory.framework.browser.web.InternetExplorerBrowser}.</p>
 *
 *  <p>You can call the more general {@link #getBuilder(WebBrowserType, String)}, or the more specific methods
 *  {@link #getChromeBuilder(String)}, {@link #getFirefoxBuilder(String)}, and {@link #getInternetExplorerBuilder(String)}.
 *
 *  Then call the methods to add parameters, such as {@link #withBrowserBinaryPath(String)}, and finally call
 *  {@link #build()} to create the Browser instance.
 *  </p>
 *
 *  <p>A Browser is basically a wrapper for a WebDriver that greatly simplifies configuration,
 *  adds useful utilities, and has methods
 *  for loading {@link selenium.pagefactory.framework.pages.Page}'s.
 *
 *  Pages provide an object-oriented solution to Selenium testing. You can write Page classes that model a web page
 *  in the web app you are testing.</p>
 */
public class LocalBrowserBuilder {
    private static final Logger LOGGER = Logger.getLogger(LocalBrowserBuilder.class);

    private final WebBrowserType browserType;
    private final String baseTestUrl;

    private TimeoutsConfig timeoutsConfig;

    private Optional<String> webDriverPath = Optional.empty();
    private Optional<String> browserBinaryPath = Optional.empty();
    private Optional<String> browserLocale = Optional.empty();
    private Optional<Integer> startWindowWidth = Optional.empty();
    private Optional<Integer> startWindowHeight = Optional.empty();
    private Optional<Level> browserLogLevel = Optional.empty();
    private Optional<String> browserLogFile = Optional.empty();
    private Optional<List<String>> options = Optional.empty();

    private LocalBrowserBuilder(WebBrowserType browserType, String baseTestUrl) {
        this.browserType = Preconditions.checkNotNull(browserType, "You must provide a non-null browserType!");
        this.baseTestUrl = Preconditions.checkNotNull(baseTestUrl, "You must provide a non-null baseTestUrl!");
        this.timeoutsConfig = TimeoutsConfig.defaultTimeoutsConfig();
    }

    //------------Getters in case the client wants to inspect the config they have so far-----------
    public WebBrowserType getBrowserType() {
        return browserType;
    }

    public String getBaseTestUrl() {
        return baseTestUrl;
    }

    public TimeoutsConfig getTimeoutsConfig() {
        return timeoutsConfig;
    }

    public Optional<String> getWebDriverPath() {
        return webDriverPath;
    }

    public Optional<String> getBrowserBinaryPath() {
        return browserBinaryPath;
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

    public Optional<List<String>> getOptions() {
        return options;
    }

    /**
     * Get a LocalBrowserBuilder for the given browser and base URL for the webapp you are testing against.
     * @param browserType - type of Browser, either CHROME, FIREFOX, or IE
     * @param baseTestUrl - base URL for your webapp, e.g. http://my.site.com/base
     */
    public static LocalBrowserBuilder getBuilder(WebBrowserType browserType, String baseTestUrl) {
        return new LocalBrowserBuilder(browserType, baseTestUrl);
    }

    /**
     * Get a LocalBrowserBuilder for CHROME and base URL for the webapp you are testing against.
     * @param baseTestUrl - base URL for your webapp, e.g. http://my.site.com/base
     */
    public static LocalBrowserBuilder getChromeBuilder(String baseTestUrl) {
        return new LocalBrowserBuilder(WebBrowserType.CHROME, baseTestUrl);
    }

    /**
     * Get a LocalBrowserBuilder for FIREFOX and base URL for the webapp you are testing against.
     * @param baseTestUrl - base URL for your webapp, e.g. http://my.site.com/base
     */
    public static LocalBrowserBuilder getFirefoxBuilder(String baseTestUrl) {
        return new LocalBrowserBuilder(WebBrowserType.FIREFOX, baseTestUrl);
    }

    /**
     * Get a LocalBrowserBuilder for IE and base URL for the webapp you are testing against.
     * @param baseTestUrl - base URL for your webapp, e.g. http://my.site.com/base
     */
    public static LocalBrowserBuilder getInternetExplorerBuilder(String baseTestUrl) {
        return new LocalBrowserBuilder(WebBrowserType.IE, baseTestUrl);
    }

    /**
     * Creates the Browser instance, which includes creating the actual Browser process via the underlying WebDriver.
     * @return - a {@link selenium.pagefactory.framework.browser.web.FirefoxBrowser},
     * {@link selenium.pagefactory.framework.browser.web.ChromeBrowser},
     * or {@link selenium.pagefactory.framework.browser.web.InternetExplorerBrowser}
     * @throws BiziboxWebDriverException when something goes wrong with creating a new WebDriver instance.
     */
    public WebBrowser build() throws BiziboxWebDriverException {
        LOGGER.info("Building Local Browser with the following config: \n" + toString());
        WebBrowser browser;
        switch (browserType) {
            case FIREFOX:
                browser = new FirefoxBrowser(baseTestUrl, timeoutsConfig, webDriverPath, browserBinaryPath, Optional.empty(), browserLocale, startWindowWidth, startWindowHeight, Optional.empty(), Optional.empty());
                break;
            case CHROME:
                browser = new ChromeBrowser(baseTestUrl, timeoutsConfig, webDriverPath, browserBinaryPath, Optional.empty(), browserLocale, startWindowWidth, startWindowHeight,
                        browserLogLevel, browserLogFile, Optional.empty(), options, Optional.empty());
                break;
            case IE:
                browser = new InternetExplorerBrowser(baseTestUrl, timeoutsConfig, webDriverPath, browserBinaryPath, Optional.empty(), browserLocale, startWindowWidth, startWindowHeight,
                        browserLogLevel, browserLogFile, Optional.empty(), Optional.empty());
                break;
            case SAFARI:
                browser = new SafariBrowser(baseTestUrl, timeoutsConfig, webDriverPath, browserBinaryPath, Optional.empty(), browserLocale, startWindowWidth, startWindowHeight,
                                            browserLogLevel, browserLogFile, Optional.empty(), Optional.empty());
                break;
            default:
                throw new IllegalArgumentException("Only Firefox, Chrome, and IE are currently supported!");
        }
        browser.initializeBrowser();
        return browser;
    }

    public LocalBrowserBuilder withTimeoutsConfig(TimeoutsConfig timeoutsConfig) {
        this.timeoutsConfig = timeoutsConfig == null ? TimeoutsConfig.defaultTimeoutsConfig() : timeoutsConfig;
        return this;
    }

    public LocalBrowserBuilder withWebDriverPath(String pathToWebDriver) {
        this.webDriverPath = Optional.ofNullable(pathToWebDriver);
        return this;
    }

    public LocalBrowserBuilder withBrowserBinaryPath(String pathToBrowserBinary) {
        this.browserBinaryPath = Optional.ofNullable(pathToBrowserBinary);
        return this;
    }

    public LocalBrowserBuilder withBrowserLocale(String browserLocale) {
        this.browserLocale = Optional.ofNullable(browserLocale);
        return this;
    }

    public LocalBrowserBuilder withStartWindowWidth(Integer startWindowWidth) {
        this.startWindowWidth = Optional.ofNullable(startWindowWidth);
        return this;
    }

    public LocalBrowserBuilder withStartWindowHeight(Integer startWindowHeight) {
        this.startWindowHeight = Optional.ofNullable(startWindowHeight);
        return this;
    }

    public LocalBrowserBuilder withBrowserLogLevel(Level browserLogLevel) {
        this.browserLogLevel = Optional.ofNullable(browserLogLevel);
        return this;
    }

    public LocalBrowserBuilder withBrowserLogFile(String browserLogFile) {
        this.browserLogFile = Optional.ofNullable(browserLogFile);
        return this;
    }

    public LocalBrowserBuilder withOptions(List<String> options) {
        this.options = Optional.ofNullable(options);
        return this;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("browserType", browserType)
                .add("baseTestUrl", baseTestUrl)
                .add("webDriverPath", webDriverPath)
                .add("browserBinaryPath", browserBinaryPath)
                .add("browserLocale", browserLocale)
                .add("startWindowWidth", startWindowWidth)
                .add("startWindowHeight", startWindowHeight)
                .add("browserLogLevel", browserLogLevel)
                .add("browserLogFile", browserLogFile)
                .add("options", options)
                .toString();
    }
}
