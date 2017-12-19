package pages.bizibox.top;

import org.openqa.selenium.By;
import org.testng.log4testng.Logger;
import selenium.pagefactory.framework.config.TimeoutType;
import selenium.pagefactory.framework.pages.BaseTopLevelPage;

public class BiziboxPage extends BaseTopLevelPage {
    private static final Logger LOGGER = Logger.getLogger(BiziboxPage.class);
    private boolean pageLoaded = false;

    public static final By TIPS_CLOSE_LINK = By.xpath("*[@id=\"rtl\"]/body/div[1]/div[2]/div[2]/div[2]/img[2]");

    @Override
    public void pageLoadHook() {
        super.pageLoadHook();
        if (!pageLoaded) {
            pageLoaded = true;
            getActions().waitForWebPageReadyStateToBeComplete(); // Wait for document.readyState to be "complete"
        }
        if (a.isVisible(TIPS_CLOSE_LINK)) {
            a.clickAndVerifyNotVisible(TIPS_CLOSE_LINK, TIPS_CLOSE_LINK, TimeoutType.DEFAULT);
        }
    }
}
