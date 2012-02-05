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
    public static double calculateSpeedMpS(double d1, double d2, int t1, int t2) {
        double deltaStrecke = d2 - d1;
        int deltaTime = t2 - t1;
        return deltaStrecke / deltaTime;
    }

    /**
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
    public static double calculatePace(double d1, double d2, double t1, double t2) {
        double deltaStrecke = d2 - d1;
        double deltaTime = t2 - t1;
        double factor = KM_IN_METER / deltaStrecke;
        double timeInSekundenFuerKm = deltaTime * factor;
        double minuten = Math.floor(timeInSekundenFuerKm / MINUTE_IN_SEKUNDEN);
        int restlicheSekunden = (int) Math.round(timeInSekundenFuerKm - (minuten * MINUTE_IN_SEKUNDEN));
        return minuten + (restlicheSekunden / 100d);
    }

    /**
     * Konvertiert geschwindigkeit von m/s nach min/km
     * 
     * @param speedMeterPerSecond
     *            v[m/s]
     * @return pace [min/km]
     */
    public static double convertMeterPerSecondToPace(double speedMeterPerSecond) {
        return 0;
    }
}
