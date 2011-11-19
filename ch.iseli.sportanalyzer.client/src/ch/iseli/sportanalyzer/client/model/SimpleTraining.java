package ch.iseli.sportanalyzer.client.model;

import java.util.Date;

public class SimpleTraining {
    private final double distanzInMeter;
    private final double dauerInSekunden;
    private final Date datum;
    private final int avgHeartRate;

    public SimpleTraining(double distanzInMeter, double dauerInSekunden, Date datum, int avgHeartRate) {
        super();
        this.distanzInMeter = distanzInMeter;
        this.dauerInSekunden = dauerInSekunden;
        this.datum = datum;
        this.avgHeartRate = avgHeartRate;
    }

    public double getDistanzInMeter() {
        return distanzInMeter;
    }

    public double getDauerInSekunden() {
        return dauerInSekunden;
    }

    public Date getDatum() {
        return datum;
    }

    public int getAvgHeartRate() {
        return avgHeartRate;
    }

}
