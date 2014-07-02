package ch.opentrainingcenter.core.helper;

import java.awt.Color;

import org.apache.log4j.Logger;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.swt.graphics.Device;
import org.eclipse.swt.widgets.Display;

public final class ColorFromPreferenceHelper {

    private static final String FARBE_NICHT_GEFUNDEN = "Farbe nicht gefunden: %s";
    private static final String FARBE_NICHT_KONVERTIERBAR = "Farbe: '%s'  kann nicht konvertiert werden";
    private static final Logger LOGGER = Logger.getLogger(ColorFromPreferenceHelper.class);

    private ColorFromPreferenceHelper() {

    }

    /**
     * Gibt Farbe aus den Preferences zurück. Wird der Key nicht gefunden kommt
     * schwarz zurück!
     * 
     * @param store
     *            PreferenceStore
     * @param preferenceKey
     *            Key der auf die Preferences zugreift.
     * @param alpha
     *            alpha wert 0...255 Transparency (0 transparent, 255 maximale
     *            Farbe)
     * 
     */
    public static Color getColor(final IPreferenceStore store, final String key, final int alpha) {
        final String cBelow = store.getString(key);
        if (cBelow != null) {
            final String[] splitBelow = cBelow.split(","); //$NON-NLS-1$
            if (splitBelow.length != 3) {
                throw new IllegalArgumentException(String.format(FARBE_NICHT_KONVERTIERBAR, cBelow));
            }
            return new Color(Integer.valueOf(splitBelow[0]), Integer.valueOf(splitBelow[1]), Integer.valueOf(splitBelow[2]), alpha);
        } else {
            LOGGER.error(String.format(FARBE_NICHT_GEFUNDEN, key));
            return Color.black;
        }
    }

    public static org.eclipse.swt.graphics.Color getSwtColor(final IPreferenceStore store, final String key) {
        final String cBelow = store.getString(key);
        final Device device = Display.getCurrent();
        if (cBelow != null) {
            final String[] splitBelow = cBelow.split(","); //$NON-NLS-1$
            if (splitBelow.length != 3) {
                throw new IllegalArgumentException(String.format(FARBE_NICHT_KONVERTIERBAR, cBelow));
            }
            return new org.eclipse.swt.graphics.Color(device, Integer.valueOf(splitBelow[0]), Integer.valueOf(splitBelow[1]), Integer.valueOf(splitBelow[2]));
        } else {
            LOGGER.error(String.format(FARBE_NICHT_GEFUNDEN, key));
            return new org.eclipse.swt.graphics.Color(device, 255, 255, 255);
        }
    }
}
