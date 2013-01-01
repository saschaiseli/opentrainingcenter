package ch.opentrainingcenter.core.assertions;

public class Assertions {

    public static void notNull(final Object object) {
        notNull(object, "Objekt " + object + " darf nicht null sein!!"); //$NON-NLS-1$ //$NON-NLS-2$
    }

    public static void notNull(final Object object, final String message) {
        if (object == null) {
            throw new IllegalArgumentException(message);
        }
    }
}
