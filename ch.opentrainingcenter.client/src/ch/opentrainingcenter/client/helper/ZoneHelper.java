package ch.opentrainingcenter.client.helper;

import java.awt.Color;

import org.apache.log4j.Logger;

import ch.opentrainingcenter.client.Activator;
import ch.opentrainingcenter.client.PreferenceConstants;
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
        final Color c = ColorFromPreferenceHelper.getColor(zone.getName() + "_color", 80);//  //$NON-NLS-1$
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
