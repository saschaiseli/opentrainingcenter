package ch.iseli.sportanalyzer.client.helper;

import java.text.DecimalFormat;

public class DistanceHelper {
    /**
     * z.B. aus 10123.234535 --> 10.123
     * 
     * @param distanceInMeter
     * @return distanz in kilometer, gerundet auf einen meter
     */
    public static String roundDistanceFromMeterToKm(final double distanceInMeter) {
        final double distanceInKilometer = distanceInMeter / 1000;
        final DecimalFormat format = new DecimalFormat("0.000"); //$NON-NLS-1$
        return format.format(distanceInKilometer);
    }

    /**
     * z.B. aus 10123.234535 --> 10.123km
     * 
     * @param distanceInMeter
     * @return distanz in kilometer, gerundet auf einen meter und mit einheit
     */
    public static String roundDistanceFromMeterToKmMitEinheit(final double distanceInMeter) {
        return roundDistanceFromMeterToKm(distanceInMeter) + "km"; //$NON-NLS-1$
    }

    /**
     * Berechnet die Pace (min/km)
     * 
     * <pre>
     * z.b 1km in 5 min 15 sekunden 
     * --> muss 5:15 ergeben
     * </pre>
     * 
     * @param distanceInMeter
     * @param timeInSeconds
     * @return min/km im format MM:SS
     */
    public static String calculatePace(final double distanceInMeter, final double timeInSeconds) {
        final double km = distanceInMeter / 1000;
        final double min = timeInSeconds / 60;
        final double d = min / km;
        final double floor = Math.floor(d);
        final double sek = d - floor;
        final double sekunden = 60 / (1 / sek);
        String s;
        if ((int) sekunden < 10) {
            s = "0" + (int) sekunden; //$NON-NLS-1$
        } else {
            s = String.valueOf((int) sekunden);
        }
        return String.valueOf((int) floor) + ":" + s; //$NON-NLS-1$
    }

    /**
     * Berechnet aus m/s die pace
     * 
     * @param maximumSpeed
     * @return
     */
    public static String calculatePace(final double speedMperSecond) {
        final double secPerKm = 1000 / speedMperSecond;
        return calculatePace(1000, secPerKm);
    }
}
