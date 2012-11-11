package ch.opentrainingcenter.core.helper;

import java.awt.Color;

import org.apache.log4j.Logger;
import org.eclipse.jface.preference.IPreferenceStore;

import ch.opentrainingcenter.core.PreferenceConstants;
import ch.opentrainingcenter.transfer.IAthlete;

public final class ZoneHelper {

    private static final Logger LOGGER = Logger.getLogger(ZoneHelper.class);
    private final IPreferenceStore store;

    public enum Zone {
        RECOM(PreferenceConstants.RECOM), //
        GA1(PreferenceConstants.GA1), //
        GA12(PreferenceConstants.GA12), //
        GA2(PreferenceConstants.GA2), //
        WSA(PreferenceConstants.WSA);

        private final String name;

        private Zone(final String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }
    }

    public ZoneHelper(final IPreferenceStore store) {
        this.store = store;
    }

    /**
     * @param zone
     *            die Zone
     * @return die farbe aus der preferences page
     */
    public Color getZonenFarbe(final Zone zone) {
        return ColorFromPreferenceHelper.getColor(store, zone.getName() + "_color", 80);//  //$NON-NLS-1$
    }

    public double getZonenWert(final IAthlete athlete, final Zone zone) {
        final int zonenWert = store.getInt(zone.getName());
        if (zonenWert <= 0) {
            LOGGER.error("zonenwert ist null: " + zone.getName() + " Athlete " + athlete.toString()); //$NON-NLS-1$//$NON-NLS-2$
            return 0;
        }
        return athlete.getMaxHeartRate().doubleValue() * (zonenWert / 100.0);
    }
}
