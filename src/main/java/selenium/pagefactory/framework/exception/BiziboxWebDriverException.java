package selenium.pagefactory.framework.exception;

public class BiziboxWebDriverException extends Exception {
    public BiziboxWebDriverException(String msg) {
        super(msg);
    }

    public BiziboxWebDriverException(String msg, Exception e) {
        super(msg, e);
    }
}
