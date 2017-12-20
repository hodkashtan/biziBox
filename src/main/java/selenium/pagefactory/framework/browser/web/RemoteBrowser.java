package selenium.pagefactory.framework.browser.web;

import org.openqa.selenium.Capabilities;
import org.testng.log4testng.Logger;
import selenium.pagefactory.framework.actions.SeleniumActions;
import selenium.pagefactory.framework.exception.BiziboxWebDriverException;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.logging.LogEntries;
import org.openqa.selenium.logging.LogType;
import org.openqa.selenium.remote.Augmenter;
import org.openqa.selenium.remote.LocalFileDetector;
import org.openqa.selenium.remote.RemoteWebDriver;

import javax.annotation.Nullable;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Set;
import java.util.logging.Level;

/**
 * <p>Represents a RemoteBrowser, i.e. running a Browser on a Selenium Node controlled by a Selenium Hub.
 * To create an instance, pass in the "delegate" browser and the URL to the Selenium Hub.
 * Example Selenium Hub URL: http://hub.my.company.com:4444/wd/hub</p>
 *
 * See <a href="http://code.google.com/p/selenium/wiki/Grid2">http://code.google.com/p/selenium/wiki/Grid2</a>
 */
public class RemoteBrowser extends WebBrowser {
    protected WebBrowser delegate;
    protected String seleniumHubURL;
    private static final Logger LOGGER = Logger.getLogger(RemoteBrowser.class);


    public RemoteBrowser(WebBrowser delegate, String seleniumHubURL) {
        super(delegate.getBaseTestUrl(),
                delegate.getTimeouts(),
                delegate.getWebDriverPath(),
                delegate.getBrowserBinaryPath(),
                delegate.getBrowserVersion(),
                delegate.getBrowserLocale(),
                delegate.getStartWindowWidth(),
                delegate.getStartWindowHeight(), delegate.getBrowserLogLevel(), delegate.getBrowserLogFile(), delegate.getPlatform(), delegate.getExtraDesiredCapabilities());
        this.delegate = delegate;
        this.seleniumHubURL = seleniumHubURL;
    }

    @Override
    public WebBrowserType getBrowserType() {
        return delegate.getBrowserType();
    }

    @Override
    public Capabilities getDesiredCapabilities() {
        return delegate.getDesiredCapabilities();
    }

    @Override
    protected WebDriver createWebDriver() throws BiziboxWebDriverException {
        try {
            RemoteWebDriver driver = new RemoteWebDriver(new URL(seleniumHubURL), delegate.getDesiredCapabilities());
            Level level = getLogLevel();
            driver.setLogLevel(level);
            driver.setFileDetector(new LocalFileDetector()); // Allow to upload local files to remote webdriver
            // https://code.google.com/p/selenium/source/browse/java/client/src/org/openqa/selenium/remote/LocalFileDetector.java
            return driver;
        } catch (MalformedURLException e) {
            throw new BiziboxWebDriverException("Invalid Selenium Hub URL given: " + seleniumHubURL, e);
        }
    }

    @Override
    public SeleniumActions getActions() {
        SeleniumActions actions = delegate.getActions();
        actions.setBrowser(this);  //We are running remotely, so the Actions should use the RemoteBrowser and RemoteWebDriver
        return actions;
    }

    /**
     * Get the Browser logs (console logs) from the Remote Browser.
     * Added more logging to debug a 5 minute gap in time we saw in a recent failed test run.
     * The issue is probably unrelated to this, but it can't hurt to log more data so we can rule it out.
     *
     * @return - a {@link org.openqa.selenium.logging.LogEntries} with all the log entries since last time this was called.
     */
    @Nullable
    public LogEntries getBrowserLogEntries() {
        if (delegate.getBrowserType() == WebBrowserType.IE) {
            LOGGER.info("IE does not support getting Browser Logs remotely. Returning null from getBrowserLogEntries");
            return null;
        }
        try {
            if (webDriver == null) {
                LOGGER.info("The web driver was null in getBrowserLogEntries. Returning null.");
                return null;
            }
            LOGGER.debug("Getting the available log types from remote Selenium node...");
            Set<String> availableLogTypes = webDriver.manage().logs().getAvailableLogTypes();

            LOGGER.debug("Found available log types: " + String.valueOf(availableLogTypes));

            if (availableLogTypes == null || !availableLogTypes.contains(LogType.BROWSER)) {
                LOGGER.info(LogType.BROWSER + " log type not allowed. Returning null.");
                return null;
            }
            LOGGER.debug("Fetching logs from remote server...");

            LogEntries logs = webDriver.manage().logs().get(LogType.BROWSER);

            LOGGER.info("Success getting remote logs!");

            return logs;
        } catch (Exception e) {
            // If some error occurs making the HTTP request to get logs, just return null.
            LOGGER.info("Error retrieving remote logs: " + e.getMessage());
            return null;
        }
    }

    /**
     * Save a screenshot in PNG format to given file name.
     *
     * @param filename
     * @return - a File representing the saved screenshot.
     */
    @Override
    public File saveScreenshotToFile(String filename) {
        TakesScreenshot screenshotDriver;
        screenshotDriver = (TakesScreenshot) new Augmenter().augment(getWebDriver());
        File scrFile = screenshotDriver.getScreenshotAs(OutputType.FILE);
        // Now you can do whatever you need to do with it, for example copy somewhere
        File outFile = new File(filename);
        try {
            FileUtils.copyFile(scrFile, outFile);
        } catch (IOException e) {
            LOGGER.error("Error saving screenshot!", e);
        }
        return outFile;
    }
}
