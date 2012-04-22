package ch.opentrainingcenter.client.helper;

import java.awt.Color;
import java.awt.Paint;

import org.apache.log4j.Logger;
import org.eclipse.jface.preference.IPreferenceStore;

import ch.opentrainingcenter.client.Activator;
import ch.opentrainingcenter.client.PreferenceConstants;

public class ColorFromPreferenceHelper {

    private static final Logger logger = Logger.getLogger(ColorFromPreferenceHelper.class);

    /**
     * Gibt Farbe aus den Preferences zurück. Wird der Key nicht gefunden kommt schwarz zurück!
     * 
     * @param preferenceKey
     *            Key der auf die Preferences zugreift. {@link PreferenceConstants}.
     * @param alpha
     *            alpha wert 0...255 Transparency (0 transparent, 255 maximale Farbe)
     * @return {@link Paint}
     */
    public static Color getColor(final String preferenceKey, final int alpha) {
        final IPreferenceStore preferenceStore = Activator.getDefault().getPreferenceStore();
        final String cBelow = preferenceStore.getString(preferenceKey);
        if (cBelow != null) {
            final String[] splitBelow = cBelow.split(","); //$NON-NLS-1$
            return new Color(Integer.valueOf(splitBelow[0]), Integer.valueOf(splitBelow[1]), Integer.valueOf(splitBelow[2]), alpha);
        } else {
            logger.error("Farbe nicht gefunden: " + preferenceKey); //$NON-NLS-1$
            return Color.black;
        }
    }
}
