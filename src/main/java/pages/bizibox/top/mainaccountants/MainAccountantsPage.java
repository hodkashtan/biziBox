package pages.bizibox.top.mainaccountants;

import org.openqa.selenium.By;
import pages.bizibox.top.BiziboxPage;
import selenium.pagefactory.framework.pages.WebPagePath;
import uris.bizibox.BiziboxPaths;

@WebPagePath(path = BiziboxPaths.MAIN_ACCOUNTANT_PREFIX_PATH + BiziboxPaths.MANAGER_ACC_PREFIX_PATH + BiziboxPaths.ACCOUNTANT_PAGE_PATH)
public class MainAccountantsPage extends BiziboxPage {

    private static final String COMPANY_DETAILS_XPATH = "/html/body/div[2]/div/div[3]/div/div[2]/ul/li[1]/span";
    private static final String USER_XPATH = "/html/body/div[2]/div/div[1]/header/div/div/div[3]/strong";

    @Override
    public By getPageIdentifier() {
        return By.xpath(COMPANY_DETAILS_XPATH);
    }

}
