package selenium.pagefactory.framework.actions;

import selenium.pagefactory.framework.browser.mobile.IOSMobileBrowser;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebElement;

import java.util.HashMap;

/**
 * Selenium Actions for iOS Applications
 *
 * Currently, only scrollTo option is implemented differently for iOS
 */
public class IOSSeleniumActions extends BaseSeleniumActions<IOSMobileBrowser> {

    public IOSSeleniumActions(IOSMobileBrowser browser) {
        super(browser);
    }

    @Override
    public void scrollIntoView(WebElement el) {
        boolean elementInView = el.isDisplayed();
        while (!elementInView) {
            getBrowser().dragUp();
            elementInView = el.isDisplayed();
        }
        HashMap<String, String> scrollObject = new HashMap<String, String>();
        String widId = ((RemoteWebElement) el).getId();
        scrollObject.put("element", widId);
        getBrowser().getWebDriver().executeScript("mobile: scrollTo", scrollObject);
    }
}
