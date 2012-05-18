package ch.opentrainingcenter.transfer.impl;

import java.util.Date;

import ch.opentrainingcenter.transfer.ITraining;
import ch.opentrainingcenter.transfer.IWeather;

public class Training implements ITraining {

    private int id;
    private Date date;
    private double dauerInSekunden;
    private double laengeInMeter;
    private int avgHeartBeat;
    private int maxHeartRate;
    private double maxSpeed;
    private String note;
    private IWeather weather;

    public Training() {
        // maybe for hibernate
    }

    public Training(final Date dateOfStart, final double timeInSeconds, final double distance, final int avgHeartRate,
            final int maxHeartBeat, final double maximumSpeed, final String note) {
        date = dateOfStart;
        dauerInSekunden = timeInSeconds;
        laengeInMeter = distance;
        avgHeartBeat = avgHeartRate;
        maxHeartRate = maxHeartBeat;
        maxSpeed = maximumSpeed;
        this.note = note;
    }

    @Override
    public void setId(final int id) {
        this.id = id;
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public Date getDateOfStart() {
        return date;
    }

    @Override
    public void setDateOfStart(final Date dateOfStart) {
        this.date = dateOfStart;
    }

    @Override
    public double getDauerInSekunden() {
        return dauerInSekunden;
    }

    @Override
    public void setDauerInSekunden(final double dauerInSekunden) {
        this.dauerInSekunden = dauerInSekunden;
    }

    @Override
    public double getLaengeInMeter() {
        return laengeInMeter;
    }

    @Override
    public void setLaengeInMeter(final double laengeInMeter) {
        this.laengeInMeter = laengeInMeter;
    }

    @Override
    public int getAverageHeartBeat() {
        return avgHeartBeat;
    }

    @Override
    public void setAverageHeartBeat(final int avgHeartBeat) {
        this.avgHeartBeat = avgHeartBeat;
    }

    @Override
    public int getMaxHeartBeat() {
        return maxHeartRate;
    }

    @Override
    public void setMaxHeartBeat(final int maxHeartBeat) {
        this.maxHeartRate = maxHeartBeat;
    }

    @Override
    public double getMaxSpeed() {
        return maxSpeed;
    }

    @Override
    public void setMaxSpeed(final double maxSpeed) {
        this.maxSpeed = maxSpeed;
    }

    @Override
    public String toString() {
        return "Training [id=" + id + ", date=" + date + ", dauerInSekunden=" + dauerInSekunden + ", laengeInMeter=" + laengeInMeter + ", avgHeartBeat=" //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$
                + avgHeartBeat + ", maxHeartRate=" + maxHeartRate + ", maxSpeed=" + maxSpeed + "]"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
    }

    @Override
    public String getNote() {
        return note;
    }

    @Override
    public void setNote(final String note) {
        this.note = note;
    }

    @Override
    public IWeather getWeather() {
        return this.weather;
    }

    @Override
    public void setWeather(final IWeather weather) {
        this.weather = weather;
    }
}
