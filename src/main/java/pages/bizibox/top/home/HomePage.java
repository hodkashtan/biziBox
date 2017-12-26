package pages.bizibox.top.home;

import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.testng.Assert;
import org.testng.log4testng.Logger;
import pages.bizibox.top.BiziboxPage;
import pages.bizibox.top.mainaccountants.MainAccountantsPage;
import selenium.pagefactory.framework.config.TimeoutType;
import selenium.pagefactory.framework.pages.WebPagePath;
import uris.bizibox.BiziboxPaths;

@WebPagePath(path = BiziboxPaths.HOME_PAGE_PATH)
public class HomePage extends BiziboxPage {
    private static final Logger LOGGER = Logger.getLogger(HomePage.class);

    public static final String LOGIN_FORM_CLASS = "formLogin";
    public static final String USERNAME_ID = "mat-input-0";
    public static final String PASSWORD_ID = "mat-input-1";
    public static final String SUBMIT_XPATH = "/html/body/app-root/div/ng-component/form/button[1]";
    public static final String RESET_PASSWORD_XPATH = "/html/body/app-root/div/ng-component/form/button[2]";
    public static final String WRONG_PASSWORD_XPATH = "/html/body/app-root/div/ng-component/form/div";

    @FindBy(id = USERNAME_ID)
    private WebElement usernameInput;

    @FindBy(id = PASSWORD_ID)
    private WebElement passwordInput;

    @FindBy(xpath = SUBMIT_XPATH)
    private WebElement submitButton;

    @FindBy(xpath = RESET_PASSWORD_XPATH)
    private WebElement resetButton;

    @FindBy(xpath = WRONG_PASSWORD_XPATH)
    private WebElement wrongPasswordLabel;

    @Override
    public By getPageIdentifier() {
        return By.className(LOGIN_FORM_CLASS);
    }

    public void login(String username, String password) {
        LOGGER.info("Starting to login to Bizibox as '" + username + "':'" + password + "'......");
        getActions().waitForPageToBeStable(TimeoutType.TWENTY_SECONDS);
        enterUsername(username);
        enterPassword(password);
        MainAccountantsPage mainAccountantsPage = clickLogin();
        getActions().waitForPageToBeStable(TimeoutType.TWENTY_SECONDS);
        Assert.assertTrue(seleniumActions.findVisibleElement(mainAccountantsPage.getPageIdentifier()).isDisplayed(),
                "Failed to login to Bizibox as " + username + "':'" + password + "'");
        LOGGER.info("SUCCESS - Logged in with the Bizibox login page as '" + username + "' : '" + password + "'");
    }

    public void reset() {
        LOGGER.info("Enter dummy credentials and reset fields");
        getActions().waitForPageToBeStable(TimeoutType.TWENTY_SECONDS);
        enterUsername("dummy");
        enterPassword("dummy");
        verifyUsernameNotEmpty();
        verifyPasswordNotEmpty();
        getActions().waitForPageToBeStable(TimeoutType.DEFAULT);
        clickReset();

        LOGGER.info("SUCCESS - Reset button works");
    }

    public void enterUsername(String username) {
        seleniumActions.inputText(usernameInput, username);
    }

    public void enterPassword(String password) {
        seleniumActions.inputText(passwordInput, password);
    }

//    public void verifyUsernamePresent() {
//        WebElement username = seleniumActions.getElement(By.id(USERNAME_ID));
//        Assert.assertNull(username, "Failed to verify username is present in page");
//    }

    public MainAccountantsPage clickLogin() {
        int attempts = 0;
        while (attempts < 5) {
            if (verifyPasswordNotEmpty() && verifyUsernameNotEmpty()) {
                return seleniumActions.clickAndLoadTopLevelPage(submitButton, MainAccountantsPage.class, TimeoutType.LONG);
            } else {
                attempts++;
            }
        }

        LOGGER.error("Failed to click on login button after " + attempts + " attempts");
        Assert.fail("Failed to click on login button " + attempts + " attempts");
        return null;
    }

    public void clickInvalidLogin() {
        seleniumActions.click(submitButton, TimeoutType.LONG);
        getActions().waitForPageToBeStable(TimeoutType.DEFAULT);

        //is wrong credentials
        if (seleniumActions.findVisibleElement(By.className(LOGIN_FORM_CLASS)).isDisplayed() &&
                !seleniumActions.findVisibleElement(By.xpath(WRONG_PASSWORD_XPATH)).isDisplayed()) {
            Assert.fail();
        }
    }



    public void clickReset() {
        seleniumActions.click(resetButton, TimeoutType.DEFAULT);
        getActions().waitForPageToBeStable(TimeoutType.DEFAULT);

        Assert.assertTrue(!verifyUsernameNotEmpty(),"Failed to reset entered username");
        Assert.assertTrue(!verifyPasswordNotEmpty(),"Failed to reset entered password");
    }

    public boolean verifyUsernameNotEmpty() {
        return StringUtils.isNotEmpty(seleniumActions.findVisibleElement(By.id(USERNAME_ID)).getAttribute("value"));
    }

    public boolean verifyPasswordNotEmpty() {
        return StringUtils.isNotEmpty(seleniumActions.findVisibleElement(By.id(PASSWORD_ID)).getAttribute("value"));
    }
}
