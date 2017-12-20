package selenium.pagefactory.framework.browser.web;

/**
 * Represents the type of Browser being created.
 */
public enum WebBrowserType {
    IE, CHROME, FIREFOX, SAFARI, MOBILE, EDGE;

    public static WebBrowserType forName(String name) {
        for (WebBrowserType type: WebBrowserType.values()) {
            if (type.toString().equalsIgnoreCase(name)) {
                return type;
            }
        }
        throw new IllegalArgumentException("WebBrowserType must be 'IE', 'CHROME', 'FIREFOX', 'SAFARI', 'EDGE' or 'MOBILE'");
    }

    public boolean isMobile(){
        return MOBILE.equals(this);
    }
}
