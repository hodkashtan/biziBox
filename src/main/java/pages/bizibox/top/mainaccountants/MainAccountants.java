package pages.bizibox.top.mainaccountants;

import org.openqa.selenium.By;
import pages.bizibox.top.BiziboxPage;

public class MainAccountants extends BiziboxPage {

    private static final String COMPANY_DETAILS_XPATH = "/html/body/div[2]/div/div[3]/div/div[2]/ul/li[1]/span";

    @Override
    public By getPageIdentifier() {
        return By.xpath(COMPANY_DETAILS_XPATH);
    }

}
