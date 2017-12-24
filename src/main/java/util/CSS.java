package util;

import com.google.common.base.Joiner;

public class CSS {
    private static final Joiner SPACE_JOINER = Joiner.on(' ');

    public static String join(String... cssLocators) {
        return SPACE_JOINER.join(cssLocators);
    }

    public static String addClass(String css, String cssClass) {
        return css + "." + cssClass;
    }
}
