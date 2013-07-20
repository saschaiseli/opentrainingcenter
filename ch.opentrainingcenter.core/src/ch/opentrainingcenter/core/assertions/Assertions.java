package ch.opentrainingcenter.core.assertions;

import org.apache.log4j.Logger;

public class Assertions {

    private static final Logger LOG = Logger.getLogger(Assertions.class);

    public static void notNull(final Object object) {
        notNull(object, "Objekt " + object + " darf nicht null sein!!"); //$NON-NLS-1$ //$NON-NLS-2$
    }

    public static void notNull(final Object object, final String message) {
        if (object == null) {
            // ja log and throw pattern :-)
            LOG.error(message);
            throw new IllegalArgumentException(message);
        }
    }

    /**
     * Wenn expression true ist, wird eine IllegalArgumentException mit der
     * Meldung geworfen!
     */
    public static void isValid(final boolean expression, final String message) {
        if (expression) {
            // ja log and throw pattern :-)
            LOG.error(message);
            throw new IllegalArgumentException(message);
        }
    }
}
