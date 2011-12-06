package ch.iseli.sportanalyzer.client.model;

import ch.iseli.sportanalyzer.client.helper.ZoneHelper;

public interface ITrainingOverview {
    /**
     * @return das start datum des trainings
     */
    String getDatum();

    /**
     * @return die Dauer des laufes. z.B 2:52:42
     */
    String getDauer();

    /**
     * @return die lauf laenge in kilometer (ohne angabe der Einheit)
     */
    String getLaengeInKilometer();

    /**
     * @return länge des laufes in meter
     */
    double getLaengeInMeter();

    /**
     * @return durchschnittliche herzfrequenz
     */
    String getAverageHeartBeat();

    /**
     * @return maximale herzfrequenz
     */
    String getMaxHeartBeat();

    /**
     * @return durschnittliche geschwindigkeit (pace --> min/km)
     */
    String getPace();

    /**
     * @return maximale pace (pace --> min/km).
     */
    String getMaxSpeed();

    SimpleTraining getSimpleTraining();

    ZoneHelper.Zone getZone();
}
