package ch.opentrainingcenter.core.exceptions;

/**
 * Exception welche geworfen wird, wenn ein GPS File nicht konvertiert werden
 * kann.
 */
public class ConvertException extends Exception {

    private static final long serialVersionUID = 1L;

    public ConvertException() {
        super();
    }

    public ConvertException(final String message, final Throwable cause, final boolean enableSuppression, final boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    public ConvertException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public ConvertException(final String message) {
        super(message);
    }

    public ConvertException(final Throwable cause) {
        super(cause);
    }

}
