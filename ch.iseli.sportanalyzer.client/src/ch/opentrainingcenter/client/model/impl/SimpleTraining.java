package ch.opentrainingcenter.client.model.impl;

import java.util.Date;

import ch.opentrainingcenter.client.helper.DistanceHelper;
import ch.opentrainingcenter.client.helper.TimeHelper;
import ch.opentrainingcenter.client.model.ISimpleTraining;
import ch.opentrainingcenter.client.model.RunType;

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
    private final RunType type;

    public SimpleTraining(final double distanzInMeter, final double dauerInSekunden, final Date datum, final int avgHeartRate, final int maxHeartRate,
            final double speed, final RunType type) {
        this.distanzInMeter = distanzInMeter;
        this.avgHeartRate = avgHeartRate;
        this.maxHeartRate = maxHeartRate;
        this.type = type;
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
     * @see ch.opentrainingcenter.client.model.ISimpleTraining#getDistanzInMeter()
     */
    @Override
    public double getDistanzInMeter() {
        return distanzInMeter;
    }

    /*
     * (non-Javadoc)
     * 
     * @see ch.opentrainingcenter.client.model.ISimpleTraining#getDauerInSekunden()
     */
    @Override
    public double getDauerInSekunden() {
        return dauerInSekunden;
    }

    /*
     * (non-Javadoc)
     * 
     * @see ch.opentrainingcenter.client.model.ISimpleTraining#getDatum()
     */
    @Override
    public Date getDatum() {
        return datum;
    }

    /*
     * (non-Javadoc)
     * 
     * @see ch.opentrainingcenter.client.model.ISimpleTraining#getAvgHeartRate()
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

    @Override
    public RunType getType() {
        return type;
    }

    @Override
    public String toString() {
        return "SimpleTraining [distanzInMeter=" + distanzInMeter + ", dauerInSekunden=" + dauerInSekunden + ", datum=" + datum + "]"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
    }

}
