package ch.opentrainingcenter.client.helper;

public class SpeedCalculator {

    private static final int KM_IN_METER = 1000;

    private static final int MINUTE_IN_SEKUNDEN = 60;

    /**
     * @param d1
     *            position 1 in meter
     * @param d2
     *            position 2 in meter
     * @param t1
     *            zeit in sekunden
     * @param t2
     *            zeit in sekunden
     * @return geschwindigkeit in meter pro sekunde
     */
    public static double calculateSpeedMpS(final double d1, final double d2, final int t1, final int t2) {
        final double deltaStrecke = d2 - d1;
        final int deltaTime = t2 - t1;
        return deltaStrecke / deltaTime;
    }

    /**
     * berechnet die pace in minuten pro kilometer. die nachkommastellen verstehen sich als sekunden und nicht als dezimale angabe. das heisst 0.3 wäre 0 minuten und 30 sekunden.
     * 
     * @param d1
     *            position 1 in meter
     * @param d2
     *            position 2 in meter
     * @param t1
     *            zeit in sekunden
     * @param t2
     *            zeit in sekunden
     * @return geschwindigkeit in minuten pro km [min/km]
     */
    public static double calculatePace(final double d1, final double d2, final double t1, final double t2) {
        final double deltaStrecke = d2 - d1;
        final double deltaTime = t2 - t1;
        final double factor = KM_IN_METER / deltaStrecke;
        final double timeInSekundenFuerKm = deltaTime * factor;
        final double minuten = Math.floor(timeInSekundenFuerKm / MINUTE_IN_SEKUNDEN);
        final int restlicheSekunden = (int) Math.round(timeInSekundenFuerKm - (minuten * MINUTE_IN_SEKUNDEN));
        final double result = minuten + (restlicheSekunden / 100d);
        if (!Double.isNaN(result)) {
            return result;
        } else {
            return -1;
        }
    }

    /**
     * Konvertiert geschwindigkeit von m/s nach min/km
     * 
     * @param speedMeterPerSecond
     *            v[m/s]
     * @return pace [min/km]
     */
    public static double convertMeterPerSecondToPace(final double speedMeterPerSecond) {
        return 0;
    }
}
