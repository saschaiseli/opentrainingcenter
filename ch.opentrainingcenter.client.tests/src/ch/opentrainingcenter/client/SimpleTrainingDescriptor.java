package ch.opentrainingcenter.client;

import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import ch.opentrainingcenter.client.model.ISimpleTraining;
import ch.opentrainingcenter.client.model.RunType;
import ch.opentrainingcenter.client.model.impl.SimpleTraining;

public class SimpleTrainingDescriptor {

    private int year;
    private int monat;
    private int day;
    private double distanzInMeter = 0;
    private double dauerInSekunden = 0;
    private int avgHeartRate = 0;
    private int maxHeartRate = 0;
    private double speed = 0;
    private RunType type = RunType.NONE;
    private Date date;

    private SimpleTrainingDescriptor(final int year, final int monat, final int day) {
        this.year = year;
        this.monat = monat;
        this.day = day;
    }

    public static SimpleTrainingDescriptor createSimpleTraining(final int year, final int monat, final int day) {
        return new SimpleTrainingDescriptor(year, monat, day);
    }

    public SimpleTrainingDescriptor setJahr(final int year) {
        this.year = year;
        return this;
    }

    public SimpleTrainingDescriptor setMonat(final int monat) {
        this.monat = monat;
        return this;
    }

    public SimpleTrainingDescriptor setTag(final int day) {
        this.day = day;
        return this;
    }

    public SimpleTrainingDescriptor setDate(final Date date) {
        this.date = date;
        return this;
    }

    public SimpleTrainingDescriptor setDistanz(final int distanzInMeter) {
        this.distanzInMeter = distanzInMeter;
        return this;
    }

    public SimpleTrainingDescriptor setDauerInSekunden(final int dauerInSekunden) {
        this.dauerInSekunden = dauerInSekunden;
        return this;
    }

    public SimpleTrainingDescriptor setAvgHeartRate(final int avgHeartRate) {
        this.avgHeartRate = avgHeartRate;
        return this;
    }

    public SimpleTrainingDescriptor setMaxHeartRate(final int maxHeartRate) {
        this.maxHeartRate = maxHeartRate;
        return this;
    }

    public SimpleTrainingDescriptor setSpeed(final double speed) {
        this.speed = speed;
        return this;
    }

    public SimpleTrainingDescriptor setRunType(final RunType type) {
        this.type = type;
        return this;
    }

    public ISimpleTraining build() {
        final Calendar cal = Calendar.getInstance(Locale.GERMAN);
        final Date datum;
        if (date == null) {
            cal.set(Calendar.DAY_OF_MONTH, day);
            cal.set(Calendar.MONTH, monat - 1);
            cal.set(Calendar.YEAR, year);
            datum = cal.getTime();
        } else {
            datum = date;
        }
        return new SimpleTraining(distanzInMeter, dauerInSekunden, datum, avgHeartRate, maxHeartRate, speed, type);
    }
}
