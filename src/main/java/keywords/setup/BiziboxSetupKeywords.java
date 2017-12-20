package keywords.setup;

import common.CommonSystemProperties;
import selenium.pagefactory.framework.browser.web.WebBrowser;
import selenium.pagefactory.framework.exception.BiziboxWebDriverException;
import util.BrowserSetupUtil;

public class BiziboxSetupKeywords {

    public void initBrowserAndTestState() throws BiziboxWebDriverException {
        WebBrowser browser = BrowserSetupUtil.createBrowser(CommonSystemProperties.BIZIBOX_SERVER_URL);
    }
}
