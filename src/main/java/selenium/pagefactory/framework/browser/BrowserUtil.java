package selenium.pagefactory.framework.browser;

import com.google.common.base.Throwables;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicHttpEntityEnclosingRequest;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.CommandExecutor;
import org.openqa.selenium.remote.HttpCommandExecutor;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.remote.SessionId;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.log4testng.Logger;
import selenium.pagefactory.framework.browser.web.RemoteBrowser;

import java.io.InputStream;
import java.net.URI;
import java.net.URL;
import java.util.Optional;

public class BrowserUtil {
    private static final Logger LOGGER = Logger.getLogger(BrowserUtil.class);
    private static final ObjectMapper objectMapper = new ObjectMapper();
    public static final int DEFAULT_TIMEOUT_SECONDS = 30;

    /**
     * Helper to wait until the length of an HTML page (as a String) is stable for 1 second.
     * Useful to wait for javascript actions that modify the DOM of the page to complete.
     *
     * @param browser - this will probably only be useful for a {@link selenium.pagefactory.framework.browser.web.WebBrowser}
     */
    public static void waitForPageHtmlToBeStable(Browser browser, int timeoutSeconds) {
        final long START = System.currentTimeMillis();
        Wait<WebDriver> wait = new WebDriverWait(browser.webDriver, timeoutSeconds);
        wait.until((WebDriver webDriver) ->
                (((JavascriptExecutor)webDriver).executeScript("return document.readyState").equals("complete")));
        long END = System.currentTimeMillis();
        LOGGER.info("Success - waited for the page HTML to be stable! Took " + (END - START) + " ms");
    }

    public static void waitForPageHtmlToBeStable(Browser browser) {
        waitForPageHtmlToBeStable(browser, DEFAULT_TIMEOUT_SECONDS);
    }

    /**
     * Helper to determine the Selenium Node we're running on via the Selenium API
     * @param browser - a remote browser that is running in a Selenium Grid
     * @return - the URL for the Node that the test is running on.
     */
    public static Optional<String> getSeleniumNodeUrl(RemoteBrowser browser) {
        RemoteWebDriver remoteWebDriver = (RemoteWebDriver) browser.getWebDriver();
        SessionId sessionId = remoteWebDriver.getSessionId();
        CommandExecutor commandExecutor = remoteWebDriver.getCommandExecutor();
        if (commandExecutor instanceof HttpCommandExecutor) {
            HttpCommandExecutor httpCommandExecutor = (HttpCommandExecutor) commandExecutor;
            URL remoteServer = httpCommandExecutor.getAddressOfRemoteServer();
            return getSeleniumNodeUrl(remoteServer, sessionId.toString());
        }
        return Optional.empty();
    }

    // Helper for above method.
    private static Optional<String> getSeleniumNodeUrl(URL remoteServer, String sessionId) {
        try {
            URI gridApiURI = new URI(remoteServer.getProtocol(), null, remoteServer.getHost(), remoteServer.getPort(),
                                     "/grid/api/testsession", "session=" + sessionId, null);
            CloseableHttpClient client = HttpClientBuilder.create().build();
            BasicHttpEntityEnclosingRequest request = new BasicHttpEntityEnclosingRequest("POST", gridApiURI.toString());
            HttpHost host = new HttpHost(remoteServer.getHost(), remoteServer.getPort());
            HttpResponse response = client.execute(host, request);
            InputStream inputStream = response.getEntity().getContent();
            JsonNode obj = objectMapper.readTree(inputStream);
            String nodeHost = obj.get("proxyId").asText();
            return Optional.ofNullable(nodeHost);
        } catch (Exception e) {
            LOGGER.warn("Error determining Selenium Node URL: " + e.getMessage());
            LOGGER.debug(Throwables.getStackTraceAsString(e));
            return Optional.empty();
        }
    }
}
