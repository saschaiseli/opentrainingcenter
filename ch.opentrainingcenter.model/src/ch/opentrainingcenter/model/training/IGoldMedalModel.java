package ch.opentrainingcenter.model.training;

import ch.opentrainingcenter.core.data.Pair;

public interface IGoldMedalModel {

    Pair<Long, String> getEmpty();

    Pair<Long, String> getSchnellstePace();

    void setSchnellstePace(final Pair<Long, String> schnellstePace);

    Pair<Long, String> getSchnellstePace(final Intervall intervall);

    void setSchnellstePace(final Intervall intervall, final Pair<Long, String> schnellstePace);

    Pair<Long, String> getLongestDistance();

    void setLongestDistance(final Pair<Long, String> longestDistance);

    Pair<Long, String> getLongestRun();

    void setLongestRun(final Pair<Long, String> longestRun);

    Pair<Long, String> getHighestPulse();

    void setHighestPulse(final Pair<Long, String> highestPulse);

    Pair<Long, String> getHighestAveragePulse();

    void setHighestAveragePulse(final Pair<Long, String> highestAveragePulse);

    Pair<Long, String> getLowestAveragePulse();

    void setLowestAveragePulse(final Pair<Long, String> lowestAveragePulse);

}