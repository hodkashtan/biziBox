package selenium.pagefactory.framework.browser.web;

import org.testng.log4testng.Logger;
import selenium.pagefactory.framework.actions.SafariSeleniumActions;
import selenium.pagefactory.framework.config.TimeoutsConfig;
import org.openqa.selenium.Platform;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.logging.LogEntries;
import org.openqa.selenium.logging.LogType;
import org.openqa.selenium.logging.LoggingPreferences;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.safari.SafariDriver;
import org.openqa.selenium.safari.SafariOptions;

import javax.annotation.Nullable;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.logging.Level;

public class SafariBrowser extends WebBrowser {
    public SafariBrowser(String baseTestUrl,
                         TimeoutsConfig timeouts,
                         Optional<String> driverPath,
                         Optional<String> browserBinaryPath,
                         Optional<String> browserVersion,
                         Optional<String> browserLocale,
                         Optional<Integer> startWindowWidth,
                         Optional<Integer> startWindowHeight,
                         Optional<Level> browserLogLevel,
                         Optional<String> browserLogFile,
                         Optional<Platform> platform,
                         Optional<Map<String, String>> extra
                         ) {

        super(baseTestUrl, timeouts, driverPath, browserBinaryPath, browserVersion, browserLocale,
                startWindowWidth, startWindowHeight, browserLogLevel, browserLogFile, platform, extra);
    }

    private static final Logger LOGGER = Logger.getLogger(SafariBrowser.class);


    @Override
    public WebBrowserType getBrowserType() {
        return WebBrowserType.SAFARI;
    }

    @Override
    public LoggingPreferences getLoggingPreferences() {
        Level level = getLogLevel();
        LoggingPreferences loggingPreferences = new LoggingPreferences();
        loggingPreferences.enable(LogType.BROWSER, level);
        loggingPreferences.enable(LogType.DRIVER, level);
        return loggingPreferences;
    }

    @Override
    public SafariOptions getDesiredCapabilities() {
        SafariOptions capabilities = new SafariOptions();

        setCommonWebBrowserCapabilities(capabilities);

        capabilities.setCapability(CapabilityType.ForSeleniumServer.ENSURING_CLEAN_SESSION, true);

        capabilities.useCleanSession(true);

        // Selenium seems to be broken if we specify SafariOptions for a RemoteWebDriver.
        // java.lang.ClassCastException: org.json.JSONObject cannot be cast to java.lang.String
        // at org.openqa.selenium.remote.BeanToJsonConverter.convertObject(BeanToJsonConverter.java:202)

        // desiredCapabilities.setCapability(SafariOptions.CAPABILITY, safariOptions);

        return capabilities;
    }

    @Override
    public SafariSeleniumActions getActions() {
        return new SafariSeleniumActions(this);
    }

    @Override
    protected WebDriver createWebDriver() {
        return new SafariDriver(getDesiredCapabilities());
    }

    @Nullable
    public LogEntries getBrowserLogEntries() {
        if (webDriver == null) {
            LOGGER.info("WebDriver was null in ChromeBrowser#getBrowserLogEntries! Returning null.");
            return null;
        }
        LOGGER.debug("Getting available log types...");
        Set<String> availableLogTypes = webDriver.manage().logs().getAvailableLogTypes();
        LOGGER.debug("Found log types: " + availableLogTypes);
        if (availableLogTypes == null || !availableLogTypes.contains(LogType.BROWSER)) {
            return null;
        }
        LogEntries logs = webDriver.manage().logs().get(LogType.BROWSER);
        LOGGER.info("Success - obtained Browser logs for a local ChromeBrowser!");
        return logs;
    }
}
