package ch.opentrainingcenter.transfer.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import ch.opentrainingcenter.transfer.IAthlete;
import ch.opentrainingcenter.transfer.ILapInfo;
import ch.opentrainingcenter.transfer.IRoute;
import ch.opentrainingcenter.transfer.ITrackPointProperty;
import ch.opentrainingcenter.transfer.ITraining;
import ch.opentrainingcenter.transfer.ITrainingType;
import ch.opentrainingcenter.transfer.IWeather;

// Generated 04.04.2013 20:38:06 by Hibernate Tools 3.4.0.CR1

/**
 * Training generated by hbm2java
 */
public class Training implements java.io.Serializable, ITraining {

    private static final long serialVersionUID = 1L;
    private int id;

    private long datum;
    private double dauer;
    private double laengeInMeter;
    private int averageHeartBeat;
    private int maxHeartBeat;
    private double maxSpeed;

    private String note;
    private IAthlete athlete;
    private ITrainingType trainingType;
    private IRoute route;
    private IWeather weather;

    private List<ITrackPointProperty> trackPoints = new ArrayList<>();
    private Date dateOfImport;
    private String fileName;
    private Integer upMeter;
    private Integer downMeter;
    private List<ILapInfo> lapInfos = new ArrayList<>();

    public Training() {
    }

    public Training(final long dateOfStart, final double timeInSeconds, final double distance, final int avgHeartRate, final int mxHeartBeat,
            final double maximumSpeed, final String note, final IWeather weather, final IRoute route) {
        datum = dateOfStart;
        dauer = timeInSeconds;
        laengeInMeter = distance;
        averageHeartBeat = avgHeartRate;
        maxHeartBeat = mxHeartBeat;
        maxSpeed = maximumSpeed;
        this.note = note;
        this.weather = weather;
        this.route = route;
    }

    @Override
    public int getId() {
        return this.id;
    }

    @Override
    public void setId(final int id) {
        this.id = id;
    }

    @Override
    public ITrainingType getTrainingType() {
        return this.trainingType;
    }

    @Override
    public void setTrainingType(final ITrainingType trainingType) {
        this.trainingType = trainingType;
    }

    @Override
    public IWeather getWeather() {
        return this.weather;
    }

    @Override
    public void setWeather(final IWeather weather) {
        this.weather = weather;
    }

    @Override
    public IAthlete getAthlete() {
        return this.athlete;
    }

    @Override
    public void setAthlete(final IAthlete athlete) {
        this.athlete = athlete;
    }

    @Override
    public long getDatum() {
        return this.datum;
    }

    @Override
    public void setDatum(final long datum) {
        this.datum = datum;
    }

    @Override
    public double getDauer() {
        return this.dauer;
    }

    @Override
    public void setDauer(final double dauer) {
        this.dauer = dauer;
    }

    @Override
    public double getLaengeInMeter() {
        return this.laengeInMeter;
    }

    @Override
    public void setLaengeInMeter(final double laengeinmeter) {
        this.laengeInMeter = laengeinmeter;
    }

    @Override
    public int getAverageHeartBeat() {
        return this.averageHeartBeat;
    }

    @Override
    public void setAverageHeartBeat(final int averageheartbeat) {
        this.averageHeartBeat = averageheartbeat;
    }

    @Override
    public int getMaxHeartBeat() {
        return this.maxHeartBeat;
    }

    @Override
    public void setMaxHeartBeat(final int maxheartbeat) {
        this.maxHeartBeat = maxheartbeat;
    }

    @Override
    public double getMaxSpeed() {
        return this.maxSpeed;
    }

    @Override
    public void setMaxSpeed(final double maxspeed) {
        this.maxSpeed = maxspeed;
    }

    @Override
    public String getNote() {
        return this.note;
    }

    @Override
    public void setNote(final String note) {
        this.note = note;
    }

    @Override
    public IRoute getRoute() {
        return route;
    }

    @Override
    public void setRoute(final IRoute route) {
        this.route = route;
    }

    @Override
    public List<ITrackPointProperty> getTrackPoints() {
        return trackPoints;
    }

    @Override
    public void setTrackPoints(final List<ITrackPointProperty> trackPoints) {
        this.trackPoints = trackPoints;
    }

    @Override
    public Date getDateOfImport() {
        return dateOfImport;
    }

    @Override
    public void setDateOfImport(final Date dateOfImport) {
        this.dateOfImport = dateOfImport;

    }

    @Override
    public String getFileName() {
        return fileName;
    }

    @Override
    public void setFileName(final String fileName) {
        this.fileName = fileName;
    }

    @SuppressWarnings("nls")
    @Override
    public String toString() {
        return "Training [datum=" + datum + ", dauer=" + dauer + ", laengeInMeter=" + laengeInMeter + ", athlete=" + athlete + ", trainingType=" + trainingType
                + ", route=" + route + ", fileName=" + fileName + "]";
    }

    @Override
    public Integer getUpMeter() {
        return upMeter;
    }

    @Override
    public void setUpMeter(final Integer upMeter) {
        this.upMeter = upMeter;
    }

    @Override
    public Integer getDownMeter() {
        return downMeter;
    }

    @Override
    public void setDownMeter(final Integer downMeter) {
        this.downMeter = downMeter;
    }

    @Override
    public List<ILapInfo> getLapInfos() {
        return Collections.unmodifiableList(lapInfos);
    }

    @Override
    public void setLapInfos(final List<ILapInfo> lapInfos) {
        this.lapInfos = lapInfos;
    }
}
