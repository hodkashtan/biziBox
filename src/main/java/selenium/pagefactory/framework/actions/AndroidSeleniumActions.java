package selenium.pagefactory.framework.actions;

import selenium.pagefactory.framework.browser.mobile.AndroidMobileBrowser;

/**
 * Selenium Actions for Android Applications
 *
 * Currently, this is the same as BaseSeleniumActions, as we don't have any need to implement anything differently
 * for Android.
 */
public class AndroidSeleniumActions extends BaseSeleniumActions<AndroidMobileBrowser> {

    public AndroidSeleniumActions(AndroidMobileBrowser browser) {
        super(browser);
    }
}
