package ch.opentrainingcenter.model.training.internal;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.HashMap;
import java.util.Map;

import ch.opentrainingcenter.model.training.IGoldMedalModel;
import ch.opentrainingcenter.model.training.Intervall;

public class GoldMedalModel implements IGoldMedalModel {

    private static final String UNKNOWN = "-"; //$NON-NLS-1$

    private String schnellstePace;
    private final Map<Intervall, String> schnellstePaces = new HashMap<Intervall, String>();
    private double longestDistance;
    private String longestRun;
    private int highestPulse;
    private int highestAveragePulse;
    private int lowestAveragePulse;

    @Override
    public String getSchnellstePace() {
        return schnellstePace;
    }

    @Override
    public void setSchnellstePace(final String schnellstePace) {
        this.schnellstePace = schnellstePace;
    }

    @Override
    public String getSchnellstePace(final Intervall intervall) {
        return schnellstePaces.get(intervall);
    }

    @Override
    public void setSchnellstePace(final Intervall intervall, final String schnellstePace) {
        schnellstePaces.put(intervall, schnellstePace);
    }

    @Override
    public String getLongestDistance() {
        if (longestDistance > 0) {
            return getFormattedString(longestDistance);
        } else {
            return UNKNOWN;
        }
    }

    @Override
    public void setLongestDistance(final double longestDistance) {
        this.longestDistance = longestDistance;
    }

    @Override
    public String getLongestRun() {
        return longestRun;
    }

    @Override
    public void setLongestRun(final String longestRun) {
        this.longestRun = longestRun;
    }

    @Override
    public String getHighestPulse() {
        return highestPulse > 0 ? String.valueOf(highestPulse) : UNKNOWN;
    }

    @Override
    public void setHighestPulse(final int highestPulse) {
        this.highestPulse = highestPulse;
    }

    @Override
    public String getHighestAveragePulse() {
        return highestAveragePulse > 0 ? String.valueOf(highestAveragePulse) : UNKNOWN;
    }

    @Override
    public void setHighestAveragePulse(final int highestAveragePulse) {
        this.highestAveragePulse = highestAveragePulse;
    }

    @Override
    public String getLowestAveragePulse() {
        return lowestAveragePulse > 0 ? String.valueOf(lowestAveragePulse) : UNKNOWN;
    }

    @Override
    public void setLowestAveragePulse(final int lowestAveragePulse) {
        this.lowestAveragePulse = lowestAveragePulse;
    }

    private String getFormattedString(double value) {
        final DecimalFormat format = new DecimalFormat("0.000"); //$NON-NLS-1$
        final DecimalFormatSymbols dfs = new DecimalFormatSymbols();
        dfs.setDecimalSeparator('.');
        format.setDecimalFormatSymbols(dfs);
        return format.format(value);
    }
}
