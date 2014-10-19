package ch.opentrainingcenter.transfer;

import java.util.Date;
import java.util.List;

public interface ITraining {

    void setId(int id);

    int getId();

    /**
     * Startzeitpunkt in Millisekunden
     */
    long getDatum();

    /**
     * Startzeitpunkt in Millisekunden
     */
    void setDatum(long dateOfStart);

    /**
     * Zeitpunkt wann der Record importiert wurde.
     */
    Date getDateOfImport();

    /**
     * Setzt den Zeitpunkt wann der Record importiert wurde.
     */
    void setDateOfImport(Date dateOfImport);

    String getFileName();

    void setFileName(String fileName);

    double getLaengeInMeter();

    void setLaengeInMeter(double laengeInMeter);

    /**
     * @return Durchschnittlicher Puls. 0 wenn nichts gemessen wurde.
     */
    int getAverageHeartBeat();

    void setAverageHeartBeat(int avgHeartBeat);

    /**
     * @return Maximalen Puls. 0 wenn nichts gemessen wurde.
     */
    int getMaxHeartBeat();

    void setMaxHeartBeat(int maxHeartBeat);

    /**
     * @return maximale geschwindigkeit in m/s
     */
    double getMaxSpeed();

    /**
     * @param maxSpeed
     *            maximale geschwindigkeit in m/s
     */
    void setMaxSpeed(double maxSpeed);

    /**
     * @return die Dauer des Laufes in Sekunden.
     */
    double getDauer();

    void setDauer(double dauerInSekunden);

    String getNote();

    void setNote(String note);

    IWeather getWeather();

    void setWeather(IWeather weather);

    IShoe getShoe();

    void setShoe(final IShoe shoe);

    IRoute getRoute();

    void setRoute(IRoute route);

    IAthlete getAthlete();

    void setAthlete(IAthlete athlete);

    List<ITrackPointProperty> getTrackPoints();

    void setTrackPoints(final List<ITrackPointProperty> trackPoints);

    List<ILapInfo> getLapInfos();

    void setLapInfos(final List<ILapInfo> lapInfos);

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

    Sport getSport();

    void setSport(Sport sport);

    TrainingType getTrainingType();

    void setTrainingType(TrainingType trainingType);

    /**
     * Anzahl Fehler in Prozent 0...100
     * 
     * @return Prozentwert von fehlenden Koordinaten.
     */
    Integer getGeoQuality();

    /**
     * Anzahl Fehler in Prozent 0...100
     * 
     * @param fehlerInProzent
     *            0-100
     */
    void setGeoQuality(Integer fehlerInProzent);

    Integer getTrainingEffect();

    void setTrainingEffect(Integer trainingEffect);
}
