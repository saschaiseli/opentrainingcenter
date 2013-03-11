package ch.opentrainingcenter.transfer;

import java.util.Date;

public interface ITraining {

    void setId(int id);

    int getId();

    Date getDateOfStart();

    void setDateOfStart(Date dateOfStart);

    double getLaengeInMeter();

    void setLaengeInMeter(double laengeInMeter);

    int getAverageHeartBeat();

    void setAverageHeartBeat(int avgHeartBeat);

    int getMaxHeartBeat();

    void setMaxHeartBeat(int maxHeartBeat);

    double getMaxSpeed();

    void setMaxSpeed(double maxSpeed);

    double getDauerInSekunden();

    void setDauerInSekunden(double dauerInSekunden);

    /**
     * @return eine Notiz zu dem Record.
     */
    String getNote();

    /**
     * setzt eine notiz zu einem Lauf.
     */
    void setNote(String note);

    IWeather getWeather();

    void setWeather(IWeather weather);

    IRoute getRoute();

    void setRoute(IRoute route);
}
