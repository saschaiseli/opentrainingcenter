package ch.iseli.sportanalyzer.client.model.impl;

import java.util.Date;

import ch.iseli.sportanalyzer.client.helper.DistanceHelper;
import ch.iseli.sportanalyzer.client.helper.TimeHelper;
import ch.iseli.sportanalyzer.client.model.ISimpleTraining;

public class SimpleTraining implements ISimpleTraining {
    private final double distanzInMeter;
    private final double dauerInSekunden;
    private final Date datum;
    private final int avgHeartRate;
    private final int maxHeartRate;
    private final String readableZeit;
    private final String laengeInKilometer;
    private final String pace;
    private final String speed;

    public SimpleTraining(final double distanzInMeter, final double dauerInSekunden, final Date datum, final int avgHeartRate, final int maxHeartRate,
            final double speed) {
        this.distanzInMeter = distanzInMeter;
        this.avgHeartRate = avgHeartRate;
        this.maxHeartRate = maxHeartRate;
        laengeInKilometer = DistanceHelper.roundDistanceFromMeterToKm(distanzInMeter);
        this.dauerInSekunden = dauerInSekunden;
        readableZeit = TimeHelper.convertSecondsToHumanReadableZeit(dauerInSekunden);
        this.datum = datum;
        pace = DistanceHelper.calculatePace(distanzInMeter, dauerInSekunden);
        this.speed = DistanceHelper.calculatePace(speed);
    }

    /*
     * (non-Javadoc)
     * 
     * @see ch.iseli.sportanalyzer.client.model.ISimpleTraining#getDistanzInMeter()
     */
    @Override
    public double getDistanzInMeter() {
        return distanzInMeter;
    }

    /*
     * (non-Javadoc)
     * 
     * @see ch.iseli.sportanalyzer.client.model.ISimpleTraining#getDauerInSekunden()
     */
    @Override
    public double getDauerInSekunden() {
        return dauerInSekunden;
    }

    /*
     * (non-Javadoc)
     * 
     * @see ch.iseli.sportanalyzer.client.model.ISimpleTraining#getDatum()
     */
    @Override
    public Date getDatum() {
        return datum;
    }

    /*
     * (non-Javadoc)
     * 
     * @see ch.iseli.sportanalyzer.client.model.ISimpleTraining#getAvgHeartRate()
     */
    @Override
    public int getAvgHeartRate() {
        return avgHeartRate;
    }

    @Override
    public String getZeit() {
        return readableZeit;
    }

    @Override
    public String getFormattedDate() {
        return TimeHelper.convertDateToString(datum, false);
    }

    @Override
    public String getLaengeInKilometer() {
        return laengeInKilometer;
    }

    @Override
    public String getMaxHeartBeat() {
        if (maxHeartRate > 0) {
            return String.valueOf(maxHeartRate);
        } else {
            return "-"; //$NON-NLS-1$
        }
    }

    @Override
    public String getPace() {
        return pace;
    }

    @Override
    public String getMaxSpeed() {
        return speed;
    }
}
