package ch.opentrainingcenter.client.model;

import ch.opentrainingcenter.client.model.impl.GoldMedalModel.Intervall;

public interface IGoldMedalModel {

    String getSchnellstePace();

    void setSchnellstePace(final String schnellstePace);

    String getSchnellstePace(final Intervall intervall);

    void setSchnellstePace(final Intervall intervall, final String schnellstePace);

    String getLongestDistance();

    void setLongestDistance(final double longestDistance);

    String getLongestRun();

    void setLongestRun(final String longestRun);

    String getHighestPulse();

    void setHighestPulse(final int highestPulse);

    String getHighestAveragePulse();

    void setHighestAveragePulse(final int highestAveragePulse);

}