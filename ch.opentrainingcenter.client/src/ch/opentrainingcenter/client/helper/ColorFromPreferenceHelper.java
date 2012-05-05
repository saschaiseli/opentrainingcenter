package ch.opentrainingcenter.client.helper;

import java.awt.Color;
import java.awt.Paint;

import org.apache.log4j.Logger;
import org.eclipse.jface.preference.IPreferenceStore;

import ch.opentrainingcenter.client.PreferenceConstants;

public final class ColorFromPreferenceHelper {

    private static final Logger LOGGER = Logger.getLogger(ColorFromPreferenceHelper.class);

    private ColorFromPreferenceHelper() {

    }

    /**
     * Gibt Farbe aus den Preferences zurück. Wird der Key nicht gefunden kommt
     * schwarz zurück!
     * 
     * @param store
     *            TODO
     * @param preferenceKey
     *            Key der auf die Preferences zugreift.
     *            {@link PreferenceConstants}.
     * @param alpha
     *            alpha wert 0...255 Transparency (0 transparent, 255 maximale
     *            Farbe)
     * 
     * @return {@link Paint}
     */
    public static Color getColor(final IPreferenceStore store, final String key, final int alpha) {
        final String cBelow = store.getString(key);
        if (cBelow != null) {
            final String[] splitBelow = cBelow.split(","); //$NON-NLS-1$
            return new Color(Integer.valueOf(splitBelow[0]), Integer.valueOf(splitBelow[1]), Integer.valueOf(splitBelow[2]), alpha);
        } else {
            LOGGER.error("Farbe nicht gefunden: " + key); //$NON-NLS-1$
            return Color.black;
        }
    }

}
