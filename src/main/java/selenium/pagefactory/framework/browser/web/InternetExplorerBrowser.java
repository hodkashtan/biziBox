package selenium.pagefactory.framework.browser.web;

import org.openqa.selenium.Platform;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.ie.InternetExplorerDriverLogLevel;
import org.openqa.selenium.ie.InternetExplorerOptions;
import org.openqa.selenium.logging.LogEntries;
import org.openqa.selenium.logging.LoggingPreferences;
import org.openqa.selenium.remote.CapabilityType;
import selenium.pagefactory.framework.actions.InternetExplorerActions;
import selenium.pagefactory.framework.config.TimeoutsConfig;

import javax.annotation.Nullable;
import java.util.Map;
import java.util.Optional;
import java.util.logging.Level;

public class InternetExplorerBrowser extends WebBrowser {
    public InternetExplorerBrowser(String baseTestUrl,
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
                                   Optional<Map<String, String>> extra) {
        super(baseTestUrl, timeouts, driverPath, browserBinaryPath, browserVersion, browserLocale, startWindowWidth, startWindowHeight, browserLogLevel, browserLogFile, platform, extra);
    }

    @Override
    public WebBrowserType getBrowserType() {
        return WebBrowserType.IE;
    }

    @Override
    public InternetExplorerOptions getCapabilities() {
        InternetExplorerOptions capabilities = new InternetExplorerOptions();

        setCommonWebBrowserCapabilities(capabilities);

        capabilities.setCapability(CapabilityType.ForSeleniumServer.ENSURING_CLEAN_SESSION, true);
        capabilities.setCapability(InternetExplorerDriver.NATIVE_EVENTS, false);
        capabilities.setCapability(InternetExplorerDriver.IGNORE_ZOOM_SETTING, true);
        capabilities.setCapability(InternetExplorerDriver.IE_ENSURE_CLEAN_SESSION, true);
        capabilities.setCapability(InternetExplorerDriver.ENABLE_PERSISTENT_HOVERING, true);


        Level logLevel = getLogLevel();
        capabilities.setCapability(InternetExplorerDriver.LOG_LEVEL, convertJavaLogLevelToIeLogLevel(logLevel.toString()));

        Optional<String> browserLogFile = getBrowserLogFile();
        if (browserLogFile.isPresent() && !browserLogFile.get().isEmpty()) {
            capabilities.setCapability(InternetExplorerDriver.LOG_FILE, browserLogFile.get());
        }

        // Set logging preferences.
        LoggingPreferences loggingPreferences = getLoggingPreferences();
        capabilities.setCapability(CapabilityType.LOGGING_PREFS, loggingPreferences);

        return capabilities;
    }

    private static String convertJavaLogLevelToIeLogLevel(String javaLogLevel) {
        if ("WARN".equals(javaLogLevel)) {
            javaLogLevel = "WARNING";
        }
        Level javaLevel = Level.parse(javaLogLevel);
        if (Level.ALL.equals(javaLevel)) {
            return InternetExplorerDriverLogLevel.TRACE.toString();
        } else if (Level.CONFIG.equals(javaLevel)) {
            return InternetExplorerDriverLogLevel.TRACE.toString();
        } else if (Level.FINE.equals(javaLevel)) {
            return InternetExplorerDriverLogLevel.DEBUG.toString();
        } else if (Level.FINER.equals(javaLevel)) {
            return InternetExplorerDriverLogLevel.TRACE.toString();
        } else if (Level.FINEST.equals(javaLevel)) {
            return InternetExplorerDriverLogLevel.TRACE.toString();
        } else if (Level.INFO.equals(javaLevel)) {
            return InternetExplorerDriverLogLevel.INFO.toString();
        } else if (Level.OFF.equals(javaLevel)) {
            return InternetExplorerDriverLogLevel.FATAL.toString();
        } else if (Level.SEVERE.equals(javaLevel)) {
            return InternetExplorerDriverLogLevel.ERROR.toString();
        } else if (Level.WARNING.equals(javaLevel)) {
            return InternetExplorerDriverLogLevel.WARN.toString();
        }
        return InternetExplorerDriverLogLevel.INFO.toString();
    }

    @Override
    public InternetExplorerActions getActions() {
        return new InternetExplorerActions(this);
    }

    @Override
    protected WebDriver createWebDriver() {
        return new InternetExplorerDriver(getCapabilities());
    }

    @Nullable
    public LogEntries getBrowserLogEntries() {
        return null; // Can't get Console logs for Internet Explorer, at least not remotely.
    }
}
