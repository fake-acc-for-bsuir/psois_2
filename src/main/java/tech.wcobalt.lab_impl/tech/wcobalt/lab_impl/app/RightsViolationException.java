package tech.wcobalt.lab_impl.app;

public class RightsViolationException extends Exception {
    public RightsViolationException() {
        super();
    }

    public RightsViolationException(String message) {
        super(message);
    }

    public RightsViolationException(String message, Throwable cause) {
        super(message, cause);
    }

    public RightsViolationException(Throwable cause) {
        super(cause);
    }

    protected RightsViolationException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
