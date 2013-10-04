package ch.opentrainingcenter.model.planing;

public final class PastPlanungSupport {
    private PastPlanungSupport() {

    }

    public static String getKmProWoche(final IPastPlanung woche) {
        final String result;
        final int kmProWoche = woche.getPlanung().getKmProWoche();
        result = isGroesserNull(kmProWoche);
        return result;
    }

    public static String getLangerLauf(final IPastPlanung element) {
        String result;
        final int langerLauf = element.getPlanung().getLangerLauf();
        result = isGroesserNull(langerLauf);
        return result;
    }

    public static String getIntervall(final IPastPlanung element) {
        final int kmProWoche = element.getPlanung().getKmProWoche();
        final String result;
        if (kmProWoche > 0) {
            result = String.valueOf(element.getPlanung().isInterval());
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
