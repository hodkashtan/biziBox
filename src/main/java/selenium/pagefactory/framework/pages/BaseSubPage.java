package selenium.pagefactory.framework.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.support.PageFactory;
import org.testng.log4testng.Logger;
import selenium.pagefactory.framework.actions.SeleniumActions;
import selenium.pagefactory.framework.config.TimeoutType;

import javax.annotation.Nullable;

/**
 * Base class for a SubPage. Implements the default pageLoadHook that waits for the page identifier to be present.
 * Subclasses should call super.pageLoadHook() if they want to wait on the page identifier.
 */
public class BaseSubPage<S extends SeleniumActions> implements SubPage {
    @SuppressWarnings("unused")
    private static Logger LOGGER = Logger.getLogger(BaseSubPage.class);
    private static final PageUtils PAGE_UTILS = new PageUtils();
    protected S a;
    protected Page parent = null;

    public final void setParent(Page parent) {
        this.parent = parent;
    }

    public final Page getParent() {
        return this.parent;
    }

    public final boolean hasParent() {
        return this.parent != null;
    }

    public final S getActions() {
        return a;
    }

    public final void setActions(SeleniumActions actions) {
        this.a = (S) actions;
    }

    public void pageLoadHook() {
        PAGE_UTILS.defaultPageLoadHook(this, a, getPageReadyTimeout());
    }

    public TimeoutType getPageReadyTimeout() {
        return TimeoutType.PAGE_READY_TIMEOUT;
    }

    @Nullable
    public By getPageIdentifier() {
        return null;
    }

    @Nullable
    public By getPageContainer() {
        return null;
    }

    public final void initSubPages() {
        PAGE_UTILS.initSubPages(this, a);
    }

    @Override
    public final void refreshElements() {
        PageFactory.initElements(getActions().getBrowser().getWebDriver(), this);
        initSubPages();
        pageLoadHook();
    }

    @Override
    public final void refreshPage() {
        getActions().getBrowser().refreshPage();
        refreshElements();
    }

}
