package ch.opentrainingcenter.transfer;

import java.util.Date;
import java.util.List;

public interface ITraining {

    void setId(int id);

    int getId();

    long getDatum();

    void setDatum(long dateOfStart);

    Date getDateOfImport();

    void setDateOfImport(Date dateOfImport);

    String getFileName();

    void setFileName(String fileName);

    double getLaengeInMeter();

    void setLaengeInMeter(double laengeInMeter);

    int getAverageHeartBeat();

    void setAverageHeartBeat(int avgHeartBeat);

    int getMaxHeartBeat();

    void setMaxHeartBeat(int maxHeartBeat);

    double getMaxSpeed();

    void setMaxSpeed(double maxSpeed);

    double getDauer();

    void setDauer(double dauerInSekunden);

    String getNote();

    void setNote(String note);

    IWeather getWeather();

    void setWeather(IWeather weather);

    IRoute getRoute();

    void setRoute(IRoute route);

    IAthlete getAthlete();

    void setAthlete(IAthlete athlete);

    List<ITrackPointProperty> getTrackPoints();

    void setTrackPoints(final List<ITrackPointProperty> trackPoints);

    ITrainingType getTrainingType();

    void setTrainingType(ITrainingType type);

    /**
     * Inkrementierte Meter die der Lauf nach oben geht.
     */
    Integer getUpMeter();

    /**
     * Inkrementierte Meter die der Lauf nach oben geht.
     */
    void setUpMeter(Integer upMeter);

    /**
     * Inkrementierte Meter die der Lauf nach unten geht.
     */
    Integer getDownMeter();

    /**
     * Inkrementierte Meter die der Lauf nach unten geht.
     */
    void setDownMeter(Integer downMeter);
}
