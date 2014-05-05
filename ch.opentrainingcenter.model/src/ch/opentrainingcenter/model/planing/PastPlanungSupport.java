package ch.opentrainingcenter.model.planing;

import ch.opentrainingcenter.i18n.Messages;

public final class PastPlanungSupport {
    private PastPlanungSupport() {

    }

    public static String getKmProWoche(final IPastPlanung woche) {
        final int kmProWoche = woche.getPlanung().getKmProWoche();
        return isGroesserNull(kmProWoche);
    }

    public static String getLangerLauf(final IPastPlanung element) {
        final int langerLauf = element.getPlanung().getLangerLauf();
        return isGroesserNull(langerLauf);
    }

    public static String getIntervall(final IPastPlanung element) {
        final int kmProWoche = element.getPlanung().getKmProWoche();
        final String result;
        if (kmProWoche > 0) {
            if (element.getPlanung().isInterval()) {
                result = Messages.Common_YES;
            } else {
                result = Messages.Common_NO;
            }
        } else {
            result = String.valueOf(""); //$NON-NLS-1$
        }
        return result;
    }

    private static String isGroesserNull(final int langerLauf) {
        String result;
        if (langerLauf > 0) {
            result = String.valueOf(langerLauf);
        } else {
            result = String.valueOf(""); //$NON-NLS-1$
        }
        return result;
    }

}
