package util;

import com.google.common.base.Throwables;
import org.testng.log4testng.Logger;
import selenium.pagefactory.framework.browser.Browser;

public class KeepAliveThread extends Thread{
    private static final Logger logger = Logger.getLogger(KeepAliveThread.class);
    public static final long PAUSE_MILLIS = 30000;
    public static final long MAX_TEST_CASE_TIME_MILLIS = 10 * 60 * 1000; // Max of 10 minutes

    private Browser browser;
    private boolean finished = false;
    private final long startTime;

    public KeepAliveThread(Browser browser) {
        super();
        this.browser = browser;
        this.startTime = System.currentTimeMillis();
    }

    @Override
    public void run() {
        logger.info("Started keep-alive selenium thread for browser " +  browser.getBrowserType().toString());
        boolean doContinue = true;
        while (doContinue) {
            doContinue = pingSeleniumServer();
        }
        logger.info("Keep-alive thread terminating...");
    }

    /**
     * Do something minimal to contact the selenium server every 15 seconds.
     * If the browser is null, this means to terminate the thread.
     */
    public boolean pingSeleniumServer() {
        try {
            Thread.sleep(PAUSE_MILLIS);
        } catch (InterruptedException e) {
            logger.trace("KeepAliveThread thread interrupted at end of test case. Exiting thread.");
            return false; // Don't continue if something interrupts the thread. Probably interrupted by RobotState at the end of a test case.
        }

        synchronized (this) {
            if (finished || browser == null) {
                notifyAll();
                return false;
            }
            long currentTime = System.currentTimeMillis();
            if (currentTime - startTime > MAX_TEST_CASE_TIME_MILLIS) {
                logger.warn("Ending Keep-Alive thread because it was running for longer than the 10 minute maximum test case time.");
                notifyAll();
                return false;
            }
            try {
                browser.getWebDriver().getTitle();
            } catch (Exception e) {
                logger.info("Exception in KeepAliveThread when pinging the selenium server. Exiting thread.");
                logger.debug(Throwables.getStackTraceAsString(e));
                notifyAll();
                return false; // Don't continue if there's an error contacting the selenium server.
            }
            notifyAll();
        }
        return true;
    }

    /**
     * Set a flag indicating the thread should terminate and join the main thread.
     */
    public synchronized void stopMe() {
        finished = true;
        notifyAll();
    }
}
