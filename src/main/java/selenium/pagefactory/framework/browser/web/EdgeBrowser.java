package selenium.pagefactory.framework.browser.web;

import org.openqa.selenium.Platform;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.logging.LogEntries;
import org.openqa.selenium.logging.LoggingPreferences;
import org.openqa.selenium.remote.CapabilityType;
import selenium.pagefactory.framework.actions.EdgeActions;
import selenium.pagefactory.framework.config.TimeoutsConfig;

import javax.annotation.Nullable;
import java.util.Map;
import java.util.Optional;
import java.util.logging.Level;

public class EdgeBrowser extends WebBrowser {
    public EdgeBrowser(String baseTestUrl,
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
    public EdgeOptions getCapabilities() {
        EdgeOptions capabilities = new EdgeOptions();

        setCommonWebBrowserCapabilities(capabilities);

        Optional<String> browserLogFile = getBrowserLogFile();
        if (browserLogFile.isPresent() && !browserLogFile.get().isEmpty()) {
            capabilities.setCapability(InternetExplorerDriver.LOG_FILE, browserLogFile.get());
        }

        // Set logging preferences.
        LoggingPreferences loggingPreferences = getLoggingPreferences();
        capabilities.setCapability(CapabilityType.LOGGING_PREFS, loggingPreferences);

        return capabilities;
    }

    @Override
    public EdgeActions getActions() {
        return new EdgeActions(this);
    }

    @Override
    protected WebDriver createWebDriver() {
        return new EdgeDriver(getCapabilities());
    }

    @Nullable
    public LogEntries getBrowserLogEntries() {
        return null; // Can't get Console logs for Edge, at least not remotely.
    }
}
