package pages.bizibox.top;

import org.openqa.selenium.By;
import org.testng.log4testng.Logger;
import selenium.pagefactory.framework.pages.BaseTopLevelPage;
import selenium.pagefactory.framework.pages.WebPagePath;
import uris.bizibox.BiziboxPaths;

@WebPagePath(isRegex = true, path = BiziboxPaths.HOME_PAGE_PATH)
public class BiziboxPage extends BaseTopLevelPage {
    private static final Logger LOGGER = Logger.getLogger(BiziboxPage.class);

    public static final String LOGIN_XPATH = "/html/head/title";

    private boolean pageLoaded = false;

    public By getPageIdentifier() {
        return By.xpath(LOGIN_XPATH);
    }

    @Override
    public void pageLoadHook() {
        super.pageLoadHook();
        if (!pageLoaded) {
            pageLoaded = true;
            getActions().waitForWebPageReadyStateToBeComplete(); // Wait for document.readyState to be "complete"
        }
    }
}
