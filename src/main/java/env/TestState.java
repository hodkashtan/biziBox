package env;

import org.testng.log4testng.Logger;
import selenium.pagefactory.framework.browser.web.WebBrowser;
import util.KeepAliveThread;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class TestState {

    private static final Logger LOG = Logger.getLogger(TestState.class);
    private static final DateFormat DATE_FORMAT = new SimpleDateFormat("MM-dd-yyyy_hh_mm_ss");
    // -------- State per test case -------
    private WebBrowser browser;
    private List<WebBrowser> extraBrowserList;
    private final Date startDate;
    private KeepAliveThread keepAliveThread = null;

    public TestState(WebBrowser browser) {
        this.browser = browser;
        this.startDate = new Date();
        this.extraBrowserList = new ArrayList<>();
        extraBrowserList.add(browser);
    }

    public WebBrowser getBrowser() {
        return browser;
    }

    public int addNewBrowser(WebBrowser webBrowser) {
        extraBrowserList.add(webBrowser);
        return extraBrowserList.indexOf(webBrowser);
    }

    public void setCurrentBrowser(int webBrowserIndex) {
        browser = extraBrowserList.get(webBrowserIndex);
    }

    public String getStartDateAsString() {
        return DATE_FORMAT.format(startDate);
    }

    public void startKeepAliveThread() {
        if (keepAliveThread != null) {
            return;
        }
        if (browser == null) {
            return;
        }
        keepAliveThread = new KeepAliveThread(browser);
        keepAliveThread.start();
    }

    public void shutDownKeepAliveThread() {
        if (keepAliveThread == null) {
            return;
        }
        try {
            keepAliveThread.stopMe();
            keepAliveThread.join();
        } catch (Exception e) {
            LOG.warn("Error shutting down Keep-Alive thread: ", e);
            // do nothing - non-critical error.
        }
    }

    public void quitCurrentWebBrowser() {
        if (browser == null) {
            LOG.info("Browser is null when calling TestState.quitCurrentWebBrowser(). Nothing to do.");
            return;
        }

        try {
            for (WebBrowser b:extraBrowserList) {
                b.cleanSession();
            }
        } catch (Exception e1) {
            LOG.warn("Test case finished, but then failed cleaning the browser session", e1);
        } finally {
            try {
                for (WebBrowser b:extraBrowserList) {
                    b.quit();
                }
                extraBrowserList.clear();
                browser = null;
            } catch (Exception e) {
                LOG.warn("Test case finished, but then the Browser failed to quit:", e);
            }
        }
    }

}
