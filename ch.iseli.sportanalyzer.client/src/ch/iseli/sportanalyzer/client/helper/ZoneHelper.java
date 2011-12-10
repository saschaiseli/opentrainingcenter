package ch.iseli.sportanalyzer.client.helper;

import java.awt.Color;

import org.apache.log4j.Logger;

import ch.iseli.sportanalyzer.client.Activator;
import ch.iseli.sportanalyzer.client.PreferenceConstants;
import ch.opentrainingcenter.transfer.IAthlete;

public class ZoneHelper {

    private static final Logger logger = Logger.getLogger(ZoneHelper.class);

    public enum Zone {

        AEROBE(PreferenceConstants.AEROBE), SCHWELLE(PreferenceConstants.SCHWELLENZONE), ANAEROBE(PreferenceConstants.ANAEROBE);

        private final String name;

        private Zone(final String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }
    }

    private ZoneHelper() {
    }

    /**
     * @param zone
     *            die Zone
     * @return die farbe aus der preferences page
     */
    public static Color getZonenFarbe(final Zone zone) {
        final String color = Activator.getDefault().getPreferenceStore().getString(zone.getName() + "_color"); //$NON-NLS-1$
        final String[] split = color.split(","); //$NON-NLS-1$
        final Color c = new Color(Integer.valueOf(split[0]), Integer.valueOf(split[1]), Integer.valueOf(split[2]), 80);
        return c;
    }

    public static double getZonenWert(final IAthlete athlete, final Zone zone) {
        final int zonenWert = Activator.getDefault().getPreferenceStore().getInt(zone.getName());
        if (zonenWert <= 0) {
            logger.error("zonenwert ist null: " + zone.getName() + " Athlete " + athlete.toString()); //$NON-NLS-1$//$NON-NLS-2$
            return 0;
        }
        return athlete.getMaxHeartRate().doubleValue() * (zonenWert / 100.0);
    }
}
