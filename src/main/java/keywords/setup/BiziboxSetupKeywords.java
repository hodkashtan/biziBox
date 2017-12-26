package keywords.setup;

import com.codeborne.selenide.Configuration;
import common.CommonSystemProperties;
import env.Suite;
import env.TestState;
import keywords.BaseUiKeywords;
import org.openqa.selenium.Capabilities;
import org.testng.log4testng.Logger;
import selenium.pagefactory.framework.browser.BrowserUtil;
import selenium.pagefactory.framework.browser.web.RemoteBrowser;
import selenium.pagefactory.framework.browser.web.WebBrowser;
import selenium.pagefactory.framework.exception.BiziboxWebDriverException;
import util.BrowserSetupUtil;

import java.util.Optional;
import java.util.concurrent.TimeUnit;

public class BiziboxSetupKeywords extends BaseUiKeywords {

    private static final Logger LOGGER = Logger.getLogger(BiziboxSetupKeywords.class);

    /**
     * Suite Setup
     */
    public void initSuite() {

        LOGGER.info("Running 'Before Bizibox Suite => initSuite()'");
        LOGGER.info("MAVEN_OPTS: \"" + System.getenv("MAVEN_OPTS") + "\"");
        LOGGER.info("Max heap size: " + Runtime.getRuntime().maxMemory());
        LOGGER.info("Total memory: " + Runtime.getRuntime().totalMemory());

        // Configure Selenide timeouts.
        configureSelenide();

        if (CommonSystemProperties.DISABLE_SUITE_SETUP) {
            LOGGER.warn("Skipping suite setup's initSuite(), because tests.disableSuiteSetup = true");
            return;
        }

        LOGGER.info("Success - finished 'Before Suite'");
    }

    private void configureSelenide() {
        LOGGER.info("Selenide timeout before configuration: " +  Configuration.timeout);
        Configuration.timeout = TimeUnit.SECONDS.toMillis(60);
        LOGGER.info("Selenide timeout set to 60 seconds");

        LOGGER.info("Selenide pollingInterval before configuration: " + Configuration.pollingInterval);
        Configuration.pollingInterval = 500;
        LOGGER.info("Selenide pollingInterval set to 500ms (standard Selenium polling interval, reduce stress on Selenium Grid)");
    }

    public void initBrowserAndTestState() throws BiziboxWebDriverException {
        Suite suite = Suite.getInstance();

        WebBrowser browser = BrowserSetupUtil.createBrowser(CommonSystemProperties.BIZIBOX_SERVER_URL);

        TestState testState = new TestState(browser);

        suite.setCurrentTestState(testState);

        if(!CommonSystemProperties.IS_DOCKERIZED_ENV) {
            logSeleniumNode(browser);
        }

        logAllCapabilities(browser);
    }

    private void logSeleniumNode(WebBrowser browser) {
        // If we're not running in the Selenium Grid remotely, then return.
        if (!(browser instanceof RemoteBrowser)) {
            return;
        }
        // If we are using a remote browser, then
        RemoteBrowser remoteBrowser = (RemoteBrowser) browser;
        Optional<String> seleniumNodeUrl = BrowserUtil.getSeleniumNodeUrl(remoteBrowser);
        if (seleniumNodeUrl.isPresent()) {
            LOGGER.info("Selenium Node: " + seleniumNodeUrl.get());
        }
    }

    private void logAllCapabilities(WebBrowser browser) {
        Capabilities capabilities = browser.getCapabilities();
        LOGGER.info("All Browser Capabilities: "+ capabilities.toString());
    }

    public int createNewBrowser() throws BiziboxWebDriverException {
        WebBrowser newBrowser = BrowserSetupUtil.createBrowser(CommonSystemProperties.BIZIBOX_SERVER_URL);
        return Suite.getInstance().getCurrentTestState().addNewBrowser(newBrowser);
    }

    public void setCurrentBrowser(int browserIndex) {
        Suite.getInstance().getCurrentTestState().setCurrentBrowser(browserIndex);
    }
}
