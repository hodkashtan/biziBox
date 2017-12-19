package pages.bizibox.sub;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import selenium.pagefactory.framework.pages.BaseSubPage;

public class GlobalNav extends BaseSubPage {

    // ------ Permanent Web Elements ------
    private static final String HOME_LINK_ID = "rtl";
    private static final String HOME_LINK_XPATH = "//*[@id=\"rtl\"]/body/div[1]/div[2]/div[2]/div[2]/img[2]";

    @FindBy(id = HOME_LINK_XPATH)
    private WebElement homeLink;

    //TODO: find global sub page css selector
    @Override
    public By getPageIdentifier() {
        return By.id(HOME_LINK_ID);
    }
}
