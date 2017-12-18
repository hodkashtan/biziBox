package selenium.pagefactory.framework.actions;

import org.testng.log4testng.Logger;
import selenium.pagefactory.framework.browser.web.InternetExplorerBrowser;
import selenium.pagefactory.framework.config.TimeoutType;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriverException;

/**
 * SeleniumActions class for InternetExplorer.
 */
public class InternetExplorerActions extends BaseSeleniumActions<InternetExplorerBrowser> {
    private final static Logger LOGGER = Logger.getLogger(InternetExplorerActions.class);

    public InternetExplorerActions(InternetExplorerBrowser browser) {
        super(browser);
    }

    //Workaround for http://code.google.com/p/selenium/issues/detail?id=7524, just for IE
    //TODO: check if relevant in 3.8.1
    @Override
    public void verifyElementInvisible(By locator, TimeoutType timeout) {
        try {
            super.verifyElementInvisible(locator, timeout);
        } catch (WebDriverException e) {
            LOGGER.debug("WebDriverException in InternetExplorerActions#verifyElementInvisible: " + e.getMessage(), e);
            // The issue happens when the element is removed from the DOM, so just try again and it should work
            super.verifyElementInvisible(locator, timeout);
        }
    }

}
