package selenium.pagefactory.framework.browser.web;

import org.openqa.selenium.Platform;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxBinary;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.logging.LogEntries;
import org.openqa.selenium.logging.LogType;
import org.openqa.selenium.logging.LoggingPreferences;
import org.openqa.selenium.remote.CapabilityType;
import org.testng.log4testng.Logger;
import selenium.pagefactory.framework.actions.FirefoxSeleniumActions;
import selenium.pagefactory.framework.config.TimeoutsConfig;

import javax.annotation.Nullable;
import java.io.File;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

public class FirefoxBrowser extends WebBrowser {

    public FirefoxBrowser(String baseTestUrl,
                          TimeoutsConfig timeoutsConfig,
                          Optional<String> webDriverPath,
                          Optional<String> browserBinaryPath,
                          Optional<String> browserVersion,
                          Optional<String> browserLocale,
                          Optional<Integer> startWindowWidth,
                          Optional<Integer> startWindowHeight,
                          Optional<Platform> platform,
                          Optional<Map<String, String>> extra) {

        super(baseTestUrl, timeoutsConfig, webDriverPath, browserBinaryPath, browserVersion, browserLocale, startWindowWidth, startWindowHeight, platform, extra);
    }

    private static final Logger LOGGER = Logger.getLogger(FirefoxBrowser.class);


    @Override
    public WebBrowserType getBrowserType() {
        return WebBrowserType.FIREFOX;
    }

    @Override
    public FirefoxOptions getCapabilities() {
        FirefoxOptions capabilities = new FirefoxOptions();

        setCommonWebBrowserCapabilities(capabilities);

        capabilities.setCapability(CapabilityType.ForSeleniumServer.ENSURING_CLEAN_SESSION, true);
        capabilities.setCapability("marionette", true);

        FirefoxProfile profile = new FirefoxProfile();

//        profile.setEnableNativeEvents(true);
        capabilities.setCapability(FirefoxDriver.PROFILE, profile);

        // If the browserBinaryPath is present, and it points to a real file, then set this as the Firefox Binary
        Optional<String> browserBinaryPath = getBrowserBinaryPath();
        if (browserBinaryPath.isPresent() && !browserBinaryPath.get().isEmpty()) {
            final String browserBinaryPathStr = browserBinaryPath.get();
            File file = new File(browserBinaryPathStr);
            if (file.exists()) {
                capabilities.setCapability(FirefoxDriver.BINARY, new FirefoxBinary(file));
            }
        }

        // Set logging preferences.
        LoggingPreferences loggingPreferences = getLoggingPreferences();
        capabilities.setCapability(CapabilityType.LOGGING_PREFS, loggingPreferences);

        return capabilities;
    }

    @Override
    protected WebDriver createWebDriver() {
        FirefoxOptions firefoxOptions = new FirefoxOptions();
//        firefoxOptions.setCapability("marionette", true);
        return new FirefoxDriver(firefoxOptions);
    }

    @Override
    public FirefoxSeleniumActions getActions() {
        return new FirefoxSeleniumActions(this);
    }

    @Nullable
    public LogEntries getBrowserLogEntries() {
        if (webDriver == null) {
            LOGGER.info("WebDriver was null in FirefoxBrowser#getBrowserLogEntries! Returning null.");
            return null;
        }
        LOGGER.debug("Getting available log types...");
        Set<String> availableLogTypes = webDriver.manage().logs().getAvailableLogTypes();
        LOGGER.debug("Found log types: " + availableLogTypes);
        if (availableLogTypes == null || !availableLogTypes.contains(LogType.BROWSER)) {
            return null;
        }
        LogEntries logs = webDriver.manage().logs().get(LogType.BROWSER);
        LOGGER.info("Success - obtained Browser logs for a local FirefoxBrowser!");
        return logs;
    }
}
