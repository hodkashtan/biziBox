package pages.bizibox.top.home;

import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.testng.Assert;
import org.testng.log4testng.Logger;
import pages.bizibox.top.BiziboxPage;
import selenium.pagefactory.framework.config.TimeoutType;
import selenium.pagefactory.framework.pages.WebPagePath;
import uris.bizibox.BiziboxPaths;

@WebPagePath(path = BiziboxPaths.HOME_PAGE_PATH)
public class HomePage extends BiziboxPage {
    private static final Logger LOGGER = Logger.getLogger(HomePage.class);

    public static final String LOGIN_FORM_XPATH = "/html/body/div[1]/div/section/form";
    public static final String USERNAME_XPATH = "/html/body/div[1]/div/section/form/div[1]/div/input[2]";
    public static final String PASSWORD_XPATH = "/html/body/div[1]/div/section/form/div[2]/div/input[2]";
    public static final String SUBMIT_XPATH = "/html/body/div[1]/div/section/form/div[5]/input";

    @FindBy(xpath = USERNAME_XPATH)
    private WebElement usernameInput;

    @FindBy(xpath = PASSWORD_XPATH)
    private WebElement passwordInput;

    @FindBy(xpath = SUBMIT_XPATH)
    private WebElement submitButton;

    @Override
    public By getPageIdentifier() {
        return By.xpath(LOGIN_FORM_XPATH);
    }

    public void verifyUsernamePresent() {
        WebElement username = a.getElement(By.xpath(USERNAME_XPATH));
        Assert.assertNull(username, "Failed to verify username is present in page");
    }

    public BiziboxPage clickLogin() {
        int attemps = 0;
        while (attemps < 5) {
            if (verifyPasswordNotEmpty() && verifyUsernameNotEmpty()) {
                return a.clickAndLoadTopLevelPage(submitButton, BiziboxPage.class, TimeoutType.LONG);
            } else {
                attemps++;
            }
        }

        LOGGER.error("Failed to click on login button");
        return null;
    }

    public void attemptLogin(String username, String password) {
        enterUsername(username);
        enterPassword(password);
        a.click(submitButton, TimeoutType.DEFAULT);
    }

    public void enterUsername(String username) {
        a.inputText(usernameInput, username);
    }

    public void enterPassword(String password) {
        a.inputText(passwordInput, password);
    }

    public boolean verifyUsernameNotEmpty() {
        return StringUtils.isNotEmpty(a.findVisibleElement(By.xpath(USERNAME_XPATH)).getAttribute("value"));
    }

    public boolean verifyPasswordNotEmpty() {
        return StringUtils.isNotEmpty(a.findVisibleElement(By.xpath(PASSWORD_XPATH)).getAttribute("value"));
    }
}
