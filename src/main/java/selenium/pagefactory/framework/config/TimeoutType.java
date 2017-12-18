package selenium.pagefactory.framework.config;

public enum TimeoutType {
    /** Default timeout. The timeout used is context-sensitive based on the method in SeleniumActions you are calling.
     *  This is what you want to use most of the time. **/
    DEFAULT,
    /** Timeout to wait for an element to be click-able when attempting to click. **/
    CLICK_TIMEOUT,
    /** Timeout to wait for an element to be present on the DOM or to be visible. **/
    WEB_ELEMENT_PRESENCE_TIMEOUT,
    /** Timeout waiting for the page to refresh, or for an element to become stale or removed from the DOM. **/
    PAGE_REFRESH_TIMEOUT,
    /** Timeout waiting for a new page to load, used to set the internal Selenium page load timeout **/
    PAGE_LOAD_TIMEOUT,
    /** Timeout waiting for elements to be present on a page after page load **/
    PAGE_READY_TIMEOUT,
    /** Timeout waiting on anything that requires refreshing the page many times, e.g. something like an Activity Stream.
     *  Used by SeleniumActions#findElementContainingTextWithRefresh and other methods that refresh the page until something is present. **/
    POLLING_WITH_REFRESH_TIMEOUT,
    /** Arbitrary short timeout configured by the client. **/
    SHORT,
    /** Arbitrary medium timeout configured by the client. **/
    MEDIUM,
    /** Arbitrary long timeout configured by the client. **/
    LONG,
    /** Fixed timeouts not affected by configuration.
     *  Usage is discouraged; but this is useful when you need an exact timeout that can't be changed by configuration. **/
    ONE_SECOND, TWO_SECONDS, FIVE_SECONDS, TEN_SECONDS, TWENTY_SECONDS, SIXTY_SECONDS, NINETY_SECONDS,
    TWO_MINUTES, THREE_MINUTES, FIVE_MINUTES, TEN_MINUTES, THIRTY_MINUTES, SIXTY_MINUTES, NINETY_MINUTES, TWO_HOURS
}
