package selenium.pagefactory.framework.actions;

import selenium.pagefactory.framework.browser.web.ChromeBrowser;

/**
 * Selenium Actions for Chrome Browser.
 *
 * Currently, this is the same as BaseSeleniumActions, as we don't have any need to implement anything differently
 * for Chrome.
 */
public class ChromeSeleniumActions extends BaseSeleniumActions<ChromeBrowser> {
    public ChromeSeleniumActions(ChromeBrowser browser) {
        super(browser);
    }
}
