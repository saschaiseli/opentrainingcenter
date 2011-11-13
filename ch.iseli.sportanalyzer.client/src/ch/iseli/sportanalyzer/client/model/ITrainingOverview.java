package ch.iseli.sportanalyzer.client.model;

public interface ITrainingOverview {
    /**
     * @return das start datum des trainings
     */
    public String getDatum();

    /**
     * @return die Dauer des laufes. z.B 2:52:42
     */
    public String getDauer();

    /**
     * @return die lauf laenge in kilometer (ohne angabe der Einheit)
     */
    public String getLaengeInKilometer();

    /**
     * @return länge des laufes in meter
     */
    public double getLaengeInMeter();

    /**
     * @return durchschnittliche herzfrequenz
     */
    public String getAverageHeartBeat();

    /**
     * @return maximale herzfrequenz
     */
    public String getMaxHeartBeat();

    /**
     * @return durschnittliche geschwindigkeit (pace --> min/km)
     */
    public String getPace();

    /**
     * @return maximale pace (pace --> min/km).
     */
    public String getMaxSpeed();
}
