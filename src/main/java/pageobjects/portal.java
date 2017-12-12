package pageobjects;

import org.openqa.selenium.support.CacheLookup;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class portal {
    private WebDriver driver;
    private int timeout = 15;

    @FindBy(id = "oHomeMainItemBtn3")
    @CacheLookup
    private WebElement home_button;

    @FindBy(css = "a[href='tel:03-5610382']")
    @CacheLookup
    private WebElement tel;

    @FindBy(css = "a[href='/contact']")
    @CacheLookup
    private WebElement contact;

    @FindBy(css = "a[href='/how-it-works/overview']")
    @CacheLookup
    private WebElement overview;

    @FindBy(css = "a[href='https://bizibox.biz/how-it-works/mobile']")
    @CacheLookup
    private WebElement mobile;

    @FindBy(css = "a[href='http://bizibox.biz/how-it-works/Questions-and-Answers']")
    @CacheLookup
    private WebElement qna;

    @FindBy(css = "a[href='https://bizibox.biz/about/aboutus']")
    @CacheLookup
    private WebElement aboutus;

    @FindBy(css = "a[href='https://bizibox.biz/how-it-works/professionalinfo']")
    @CacheLookup
    private WebElement professionalInfo;

    @FindBy(css = "a[href='https://bizibox.biz/about/publications']")
    @CacheLookup
    private WebElement publicAtions;

    @FindBy(css = "a[href='/']")
    @CacheLookup
    private WebElement bizibox;

    @FindBy(css = "a[href='/articles']")
    @CacheLookup
    private WebElement bizitip;

    @FindBy(css = "a[href='https://entry.co.il']")
    @CacheLookup
    private WebElement entry;

    @FindBy(css = "a[href='http://www.riko.co.il']")
    @CacheLookup
    private WebElement nadavRikover;

    private final String pageLoadedText = "";

    private final String pageUrl = "/";

    @FindBy(css = "a[href='mailto:service@bizibox.biz']")
    @CacheLookup
    private WebElement servicebiziboxBiz;

    public portal() {
    }

    public portal(WebDriver driver) {
        this();
        this.driver = driver;
    }

    public portal(WebDriver driver, int timeout) {
        this(driver);
        this.timeout = timeout;
    }

    /**
     * Click on Bizibox Link.
     *
     * @return the portal class instance.
     */
    public portal clickBiziboxLink() {
        bizibox.click();
        return this;
    }

    /**
     * Click on Bizitip Link.
     *
     * @return the portal class instance.
     */
    public portal clickBizitipLink() {
        bizitip.click();
        return this;
    }

    /**
     * Click on Entry Link.
     *
     * @return the portal class instance.
     */
    public portal clickEntryLink() {
        entry.click();
        return this;
    }

    /**
     * Click on 035610382 Link.
     *
     * @return the portal class instance.
     */
    public portal clickLinkTel() {
        tel.click();
        return this;
    }

    /**
     * Click on  Link.
     *
     * @return the portal class instance.
     */
    public portal clickLinkMobile() {
        mobile.click();
        return this;
    }

    /**
     * Click on  Link.
     *
     * @return the portal class instance.
     */
    public portal clickLinkQnA() {
        qna.click();
        return this;
    }

    /**
     * Click on  Link.
     *
     * @return the portal class instance.
     */
    public portal clickLinkAboutUs() {
        aboutus.click();
        return this;
    }

    /**
     * Click on  Link.
     *
     * @return the portal class instance.
     */
    public portal clickLinkProfessionalInfo() {
        professionalInfo.click();
        return this;
    }

    /**
     * Click on  Link.
     *
     * @return the portal class instance.
     */
    public portal clickLinkPublicAtions() {
        publicAtions.click();
        return this;
    }

    /**
     * Click on Nadav Rikover Link.
     *
     * @return the portal class instance.
     */
    public portal clickNadavRikoverLink() {
        nadavRikover.click();
        return this;
    }

    /**
     * Click on Servicebizibox.biz Link.
     *
     * @return the portal class instance.
     */
    public portal clickServicebiziboxBizLink() {
        servicebiziboxBiz.click();
        return this;
    }

    /**
     * Verify that the page loaded completely.
     *
     * @return the portal class instance.
     */
    public portal verifyPageLoaded() {
        (new WebDriverWait(driver, timeout)).until(new ExpectedCondition<Boolean>() {
            public Boolean apply(WebDriver d) {
                return d.getPageSource().contains(pageLoadedText);
            }
        });
        return this;
    }

    /**
     * Verify that current page URL matches the expected URL.
     *
     * @return the portal class instance.
     */
    public portal verifyPageUrl() {
        (new WebDriverWait(driver, timeout)).until(new ExpectedCondition<Boolean>() {
            public Boolean apply(WebDriver d) {
                return d.getCurrentUrl().contains(pageUrl);
            }
        });
        return this;
    }
}
