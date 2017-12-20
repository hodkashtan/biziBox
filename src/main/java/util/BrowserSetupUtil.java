package util;

import common.CommonSystemProperties;
import org.apache.commons.lang3.StringUtils;
import org.testng.log4testng.Logger;
import selenium.pagefactory.framework.browser.BrowserUtil;
import selenium.pagefactory.framework.browser.LocalBrowserBuilder;
import selenium.pagefactory.framework.browser.RemoteBrowserBuilder;
import selenium.pagefactory.framework.browser.web.RemoteBrowser;
import selenium.pagefactory.framework.browser.web.WebBrowser;
import selenium.pagefactory.framework.browser.web.WebBrowserType;
import selenium.pagefactory.framework.config.TimeoutsConfig;
import selenium.pagefactory.framework.exception.BiziboxWebDriverException;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class BrowserSetupUtil {
    private static final Logger LOGGER = Logger.getLogger(BrowserSetupUtil.class);

    public static WebBrowser createBrowser(String baseTestURL) throws BiziboxWebDriverException {
        WebBrowserType webBrowserType = WebBrowserType.forName(CommonSystemProperties.BROWSER_NAME);

        String seleniumHubURL = CommonSystemProperties.SELENIUM_HUB_URL;
        if(StringUtils.isNotEmpty(seleniumHubURL))
            return createRemoteBrowser(webBrowserType, baseTestURL, seleniumHubURL, getBrowserStackCapabilities());

        return createLocalBrowser(webBrowserType, baseTestURL);
    }

    public static WebBrowser createRemoteBrowser(WebBrowserType webBrowserType, String baseTestURL, String seleniumHubURL, Map<String, String> extras) throws BiziboxWebDriverException {
        // Override default timeouts
        TimeoutsConfig timeoutsConfig = TimeoutsConfig.builder()
                .build();

        return RemoteBrowserBuilder.getBuilder(webBrowserType, baseTestURL, seleniumHubURL)
                .withTimeoutsConfig(timeoutsConfig)
                .withBrowserVersion(CommonSystemProperties.BROWSER_VERSION)
                .withStartWindowHeight(CommonSystemProperties.START_WINDOW_HEIGHT)
                .withStartWindowWidth(CommonSystemProperties.START_WINDOW_WIDTH)
                .withBrowserLogLevel(java.util.logging.Level.parse(CommonSystemProperties.BROWSER_LOG_LEVEL))
                .withExtraCapabilities(extras)
                .build();
    }

    public static WebBrowser createLocalBrowser(WebBrowserType webBrowserType, String baseTestURL) throws BiziboxWebDriverException {

        // Override default timeouts
        TimeoutsConfig timeoutsConfig = TimeoutsConfig.builder()
                .build();

        return LocalBrowserBuilder.getBuilder(webBrowserType, baseTestURL)
                .withTimeoutsConfig(timeoutsConfig)
                .withBrowserBinaryPath(CommonSystemProperties.BROWSER_PATH)
                .withWebDriverPath(CommonSystemProperties.WEBDRIVER_PATH)
                .withStartWindowHeight(CommonSystemProperties.START_WINDOW_HEIGHT)
                .withStartWindowWidth(CommonSystemProperties.START_WINDOW_WIDTH)
                .withBrowserLogLevel(java.util.logging.Level.parse(CommonSystemProperties.BROWSER_LOG_LEVEL))
                .build();
    }



    public static void logSeleniumNode(WebBrowser browser) {
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

    private static Map<String, String> getBrowserStackCapabilities(){
        Map<String, String> moreCapabilities = new HashMap<String, String>();

        /////////////////////////////////////////////////////////////////////////////////////////////////////////////
        //Self-signed certificates capability
        /////////////////////////////////////////////////////////////////////////////////////////////////////////////
        String browserstackSslCerts = System.getProperty("tests.browserstack.ssl.certs");
        if(!com.google.common.base.Strings.isNullOrEmpty(browserstackSslCerts)){
            moreCapabilities.put("acceptSslCerts", browserstackSslCerts);
        }

        /////////////////////////////////////////////////////////////////////////////////////////////////////////////
        //Selenium capabilities
        /////////////////////////////////////////////////////////////////////////////////////////////////////////////
        String browserstackPlatform = System.getProperty("tests.browserstack.platform");
        String browserstackBrowserName = System.getProperty("tests.browserstack.browser.name");
        String browserstackVersion = System.getProperty("tests.browserstack.version");

        if(!com.google.common.base.Strings.isNullOrEmpty(browserstackPlatform)){
            moreCapabilities.put("platform", browserstackPlatform);
        }
        if(!com.google.common.base.Strings.isNullOrEmpty(browserstackBrowserName)){
            moreCapabilities.put("browserName", browserstackBrowserName);
        }
        if(!com.google.common.base.Strings.isNullOrEmpty(browserstackVersion)){
            moreCapabilities.put("version", browserstackVersion);
        }

        /////////////////////////////////////////////////////////////////////////////////////////////////////////////
        //Browserstack-specific capabilities
        /////////////////////////////////////////////////////////////////////////////////////////////////////////////
        String browserstackOs = System.getProperty("tests.browserstack.os");
        String browserstackOsVersion = System.getProperty("tests.browserstack.os.version");
        String browserstackBrowser = System.getProperty("tests.browserstack.browser");
        String browserstackBrowserVersion = System.getProperty("tests.browserstack.browser.version");

        if(!com.google.common.base.Strings.isNullOrEmpty(browserstackOs)){
            moreCapabilities.put("os", browserstackOs);
        }
        if(!com.google.common.base.Strings.isNullOrEmpty(browserstackOsVersion)){
            moreCapabilities.put("os_version", browserstackOsVersion);
        }
        if(!com.google.common.base.Strings.isNullOrEmpty(browserstackBrowser)){
            moreCapabilities.put("browser", browserstackBrowser);
        }
        if(!com.google.common.base.Strings.isNullOrEmpty(browserstackBrowserVersion)){
            moreCapabilities.put("browser_version", browserstackBrowserVersion);
        }

        /////////////////////////////////////////////////////////////////////////////////////////////////////////////
        //Test configuration capabilities
        /////////////////////////////////////////////////////////////////////////////////////////////////////////////
        String browserstackProject = System.getProperty("tests.browserstack.project");
        String browserstackBuild = System.getProperty("tests.browserstack.build");
        String browserstackName = System.getProperty("tests.browserstack.name");
        String browserstackLocal = System.getProperty("tests.browserstack.local");
        String browserstackDebug = System.getProperty("tests.browserstack.debug");
        String browserstackVideo = System.getProperty("tests.browserstack.video");
        String browserstackTimezone = System.getProperty("tests.browserstack.timezone");
        String browserstackResolution = System.getProperty("tests.browserstack.resolution");
        String browserstackSeleniumVersion = System.getProperty("tests.browserstack.selenium.version","3.8.1");

        if(!com.google.common.base.Strings.isNullOrEmpty(browserstackProject)){
            moreCapabilities.put("project", browserstackProject);
        }
        if(!com.google.common.base.Strings.isNullOrEmpty(browserstackBuild)){
            moreCapabilities.put("build", browserstackBuild);
        }
        if(!com.google.common.base.Strings.isNullOrEmpty(browserstackName)){
            moreCapabilities.put("name", browserstackName);
        }
        if(!com.google.common.base.Strings.isNullOrEmpty(browserstackLocal)){
            moreCapabilities.put("browserstack.local", browserstackLocal);
        }
        if(!com.google.common.base.Strings.isNullOrEmpty(browserstackDebug)){
            moreCapabilities.put("browserstack.debug", browserstackDebug);
        }
        if(!com.google.common.base.Strings.isNullOrEmpty(browserstackVideo)){
            moreCapabilities.put("browserstack.video", browserstackVideo);
        }
        if(!com.google.common.base.Strings.isNullOrEmpty(browserstackTimezone)){
            moreCapabilities.put("browserstack.timezone", browserstackTimezone);
        }
        if(!com.google.common.base.Strings.isNullOrEmpty(browserstackResolution)){
            moreCapabilities.put("resolution", browserstackResolution);
        }
        if(!com.google.common.base.Strings.isNullOrEmpty(browserstackSeleniumVersion)){
            moreCapabilities.put("browserstack.selenium_version", browserstackSeleniumVersion);
        }

        /////////////////////////////////////////////////////////////////////////////////////////////////////////////
        //Mobile capabilities
        /////////////////////////////////////////////////////////////////////////////////////////////////////////////
        String browserstackMobileDevice = System.getProperty("tests.browserstack.mobile.device");
        String browserstackMobileOs = System.getProperty("tests.browserstack.mobile.os");
        String browserstackMobileBrowser = System.getProperty("tests.browserstack.mobile.browser");
        String browserstackMobileDeviceOrientation = System.getProperty("tests.browserstack.mobile.device.orientation");

        if(!com.google.common.base.Strings.isNullOrEmpty(browserstackMobileDevice)){
            moreCapabilities.put("device", browserstackMobileDevice);
        }
        if(!com.google.common.base.Strings.isNullOrEmpty(browserstackMobileOs)){
            moreCapabilities.put("os", browserstackMobileOs);
        }
        if(!com.google.common.base.Strings.isNullOrEmpty(browserstackMobileBrowser)){
            moreCapabilities.put("browser", browserstackMobileBrowser);
        }
        if(!com.google.common.base.Strings.isNullOrEmpty(browserstackMobileDeviceOrientation)){
            moreCapabilities.put("deviceOrientation", browserstackMobileDeviceOrientation);
        }

        /////////////////////////////////////////////////////////////////////////////////////////////////////////////
        //IE/Edge capabilities
        /////////////////////////////////////////////////////////////////////////////////////////////////////////////
        String browserstackIeNoFlash = System.getProperty("tests.browserstack.ie.noflash");
        String browserstackIeCompatibility = System.getProperty("tests.browserstack.ie.compatibility");
        String browserstackIeDriver = System.getProperty("tests.browserstack.ie.driver");
        String browserstackIeEnablePopUps = System.getProperty("tests.browserstack.ie.enable.popups");
        String browserstackEdgeEnablePopUps = System.getProperty("tests.browserstack.edge.enable.popups");

        if(!com.google.common.base.Strings.isNullOrEmpty(browserstackIeNoFlash)){
            moreCapabilities.put("browserstack.ie.noFlash", browserstackIeNoFlash);
        }
        if(!com.google.common.base.Strings.isNullOrEmpty(browserstackIeCompatibility)){
            moreCapabilities.put("browserstack.ie.compatibility", browserstackIeCompatibility);
        }
        if(!com.google.common.base.Strings.isNullOrEmpty(browserstackIeDriver)){
            moreCapabilities.put("browserstack.ie.driver", browserstackIeDriver);
        }
        if(!com.google.common.base.Strings.isNullOrEmpty(browserstackIeEnablePopUps)){
            moreCapabilities.put("browserstack.ie.enablePopups", browserstackIeEnablePopUps);
        }
        if(!com.google.common.base.Strings.isNullOrEmpty(browserstackEdgeEnablePopUps)){
            moreCapabilities.put("browserstack.edge.enablePopups", browserstackEdgeEnablePopUps);
        }

        /////////////////////////////////////////////////////////////////////////////////////////////////////////////
        //Safari capabilities
        /////////////////////////////////////////////////////////////////////////////////////////////////////////////
        String browserstackSafariEnablePopUps = System.getProperty("tests.browserstack.safari.enable.popups");
        String browserstackSafariAllowCookies = System.getProperty("tests.browserstack.safari.allow.cookies");
        String browserstackSafariDriver = System.getProperty("tests.browserstack.safari.driver");

        if(!com.google.common.base.Strings.isNullOrEmpty(browserstackSafariEnablePopUps)){
            moreCapabilities.put("browserstack.safari.enablePopups", browserstackSafariEnablePopUps);
        }
        if(!com.google.common.base.Strings.isNullOrEmpty(browserstackSafariAllowCookies)){
            moreCapabilities.put("browserstack.safari.allowAllCookies", browserstackSafariAllowCookies);
        }
        if(!com.google.common.base.Strings.isNullOrEmpty(browserstackSafariDriver)){
            moreCapabilities.put("browserstack.safari.driver", browserstackSafariDriver);
        }

        /////////////////////////////////////////////////////////////////////////////////////////////////////////////
        //Firefox and Gecko driver capabilities
        /////////////////////////////////////////////////////////////////////////////////////////////////////////////
        String browserstackGeckoDriver = System.getProperty("tests.browserstack.geckodriver");

        if(!com.google.common.base.Strings.isNullOrEmpty(browserstackGeckoDriver)){
            moreCapabilities.put("browserstack.geckodriver", browserstackGeckoDriver);
        }

        return moreCapabilities;
    }
}
