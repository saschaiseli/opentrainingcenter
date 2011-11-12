package ch.iseli.sportanalyzer.client.helper;

import java.text.DecimalFormat;

public class DistanceHelper {
    public static String roundDistanceFromMeterToKm(double distanceInMeter) {
        double distanceInKilometer = distanceInMeter / 1000;
        DecimalFormat format = new DecimalFormat("0.000");
        return format.format(distanceInKilometer) + "km";
    }
}
