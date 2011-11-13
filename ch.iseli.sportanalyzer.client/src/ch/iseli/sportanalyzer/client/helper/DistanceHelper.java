package ch.iseli.sportanalyzer.client.helper;

import java.text.DecimalFormat;

public class DistanceHelper {
    /**
     * z.B. aus 10123.234535 --> 10.123
     * 
     * @param distanceInMeter
     * @return distanz in kilometer, gerundet auf einen meter
     */
    public static String roundDistanceFromMeterToKm(double distanceInMeter) {
        double distanceInKilometer = distanceInMeter / 1000;
        DecimalFormat format = new DecimalFormat("0.000");
        return format.format(distanceInKilometer);
    }

    /**
     * z.B. aus 10123.234535 --> 10.123km
     * 
     * @param distanceInMeter
     * @return distanz in kilometer, gerundet auf einen meter und mit einheit
     */
    public static String roundDistanceFromMeterToKmMitEinheit(double distanceInMeter) {
        return roundDistanceFromMeterToKm(distanceInMeter) + "km";
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
    public static String calculatePace(double distanceInMeter, double timeInSeconds) {
        double km = distanceInMeter / 1000;
        double min = timeInSeconds / 60;
        double d = min / km;
        double floor = Math.floor(d);
        double sek = d - floor;
        double sekunden = 60 / (1 / sek);
        String s;
        if ((int) sekunden < 10) {
            s = "0" + (int) sekunden;
        } else {
            s = String.valueOf((int) sekunden);
        }
        return String.valueOf((int) floor) + ":" + s;
    }

    /**
     * Berechnet aus m/s die pace
     * 
     * @param maximumSpeed
     * @return
     */
    public static String calculatePace(double speedMperSecond) {
        double secPerKm = 1000 / speedMperSecond;
        return calculatePace(1000, secPerKm);
    }
}
