package ch.iseli.sportanalyzer.client.model;

import java.util.Date;

public class SimpleTraining {
    double distanzInMeter;
    double dauerInSekunden;
    Date datum;

    public SimpleTraining(double distanzInMeter, double dauerInSekunden, Date datum) {
        super();
        this.distanzInMeter = distanzInMeter;
        this.dauerInSekunden = dauerInSekunden;
        this.datum = datum;
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

}
