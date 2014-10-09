package ch.opentrainingcenter.model.chart;

import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import ch.opentrainingcenter.transfer.HeartRate;
import ch.opentrainingcenter.transfer.ITraining;
import ch.opentrainingcenter.transfer.RunData;
import ch.opentrainingcenter.transfer.TrainingType;
import ch.opentrainingcenter.transfer.factory.CommonTransferFactory;

public final class TrainingDescriptor {

    private int year;
    private int monat;
    private int day;
    private double distanzInMeter = 0;
    private double dauerInSekunden = 0;
    private int avgHeartRate = 0;
    private int maxHeartRate = 0;
    private double speed = 0;
    private TrainingType type = TrainingType.NONE;
    private Date date;

    private TrainingDescriptor(final int year, final int monat, final int day) {
        this.year = year;
        this.monat = monat;
        this.day = day;
    }

    public static TrainingDescriptor createSimpleTraining(final int year, final int monat, final int day) {
        return new TrainingDescriptor(year, monat, day);
    }

    public TrainingDescriptor setJahr(final int year) {
        this.year = year;
        return this;
    }

    public TrainingDescriptor setMonat(final int monat) {
        this.monat = monat;
        return this;
    }

    public TrainingDescriptor setTag(final int day) {
        this.day = day;
        return this;
    }

    public TrainingDescriptor setDate(final Date date) {
        this.date = date;
        return this;
    }

    public TrainingDescriptor setDistanz(final int distanzInMeter) {
        this.distanzInMeter = distanzInMeter;
        return this;
    }

    public TrainingDescriptor setDauerInSekunden(final int dauerInSekunden) {
        this.dauerInSekunden = dauerInSekunden;
        return this;
    }

    public TrainingDescriptor setAvgHeartRate(final int avgHeartRate) {
        this.avgHeartRate = avgHeartRate;
        return this;
    }

    public TrainingDescriptor setMaxHeartRate(final int maxHeartRate) {
        this.maxHeartRate = maxHeartRate;
        return this;
    }

    public TrainingDescriptor setSpeed(final double speed) {
        this.speed = speed;
        return this;
    }

    public TrainingDescriptor setRunType(final TrainingType type) {
        this.type = type;
        return this;
    }

    public ITraining build() {
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
        final RunData runData = new RunData(datum.getTime(), dauerInSekunden, distanzInMeter, speed);
        final HeartRate heart = new HeartRate(avgHeartRate, maxHeartRate);
        final ITraining training = CommonTransferFactory.createTraining(runData, heart);
        training.setTrainingType(type);
        return training;
    }
}
