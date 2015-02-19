package ch.opentrainingcenter.core.charts.internal;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TreeMap;

import ch.opentrainingcenter.core.charts.IStatistikCreator;
import ch.opentrainingcenter.transfer.ITraining;

public class StatistikCreator implements IStatistikCreator {

    private static final int LAST_KW_IN_YEAR = 52;

    @Override
    public Map<Integer, List<ITraining>> getTrainingsProJahr(final List<ITraining> allTrainings) {
        final Map<Integer, List<ITraining>> trainingsPer = new HashMap<Integer, List<ITraining>>();
        for (final ITraining training : allTrainings) {
            final Calendar cal = Calendar.getInstance(Locale.GERMAN);
            cal.setTimeInMillis(training.getDatum());
            final int year = cal.get(Calendar.YEAR);
            List<ITraining> perYear = trainingsPer.get(year);
            if (perYear == null) {
                perYear = new ArrayList<ITraining>();
            }
            perYear.add(training);
            trainingsPer.put(year, perYear);
        }
        return trainingsPer;
    }

    @Override
    public Map<Integer, Map<Integer, List<ITraining>>> getTrainingsProMonat(final List<ITraining> allTrainings) {
        final Map<Integer, Map<Integer, List<ITraining>>> trainingsPer = new HashMap<Integer, Map<Integer, List<ITraining>>>();
        final int outer = Calendar.YEAR;
        final int inner = Calendar.MONTH;
        calculate(allTrainings, trainingsPer, outer, inner);
        return trainingsPer;
    }

    @Override
    public Map<Integer, Map<Integer, List<ITraining>>> getTrainingsProWoche(final List<ITraining> allTrainings) {
        final Map<Integer, Map<Integer, List<ITraining>>> trainingsPer = new HashMap<Integer, Map<Integer, List<ITraining>>>();
        final int outer = Calendar.YEAR;
        final int inner = Calendar.WEEK_OF_YEAR;
        calculate(allTrainings, trainingsPer, outer, inner);
        return trainingsPer;
    }

    private void calculate(final List<ITraining> allTrainings, final Map<Integer, Map<Integer, List<ITraining>>> trainingsPer, final int outer, final int inner) {
        for (final ITraining training : allTrainings) {
            final Calendar cal = Calendar.getInstance(Locale.GERMAN);
            final Date datum = new Date(training.getDatum());
            cal.setTime(datum);
            final int year;
            // da monat mit 0 beginnt muss noch eins addiert werden.
            final int week;
            if (inner == Calendar.MONTH) {
                week = cal.get(inner) + 1;
            } else {
                week = cal.get(inner);
            }

            if (isLaeufeProKW(outer, inner) && isKWFehler(datum)) {
                year = cal.get(outer) - 1;
            } else {
                year = cal.get(outer);
            }
            Map<Integer, List<ITraining>> yearMap = trainingsPer.get(year);
            if (yearMap == null) {
                yearMap = new TreeMap<Integer, List<ITraining>>();
            }
            List<ITraining> weekMap = yearMap.get(week);
            if (weekMap == null) {
                weekMap = new ArrayList<ITraining>();
            }
            weekMap.add(training);
            yearMap.put(week, weekMap);
            trainingsPer.put(year, yearMap);
        }
    }

    private boolean isLaeufeProKW(final int outer, final int inner) {
        return outer == Calendar.YEAR && inner == Calendar.WEEK_OF_YEAR;
    }

    private boolean isKWFehler(final Date datum) {
        final Calendar cal = Calendar.getInstance();
        cal.setTime(datum);
        return cal.get(Calendar.WEEK_OF_YEAR) == LAST_KW_IN_YEAR && cal.get(Calendar.MONTH) == 0;
    }

    @Override
    public List<ITraining> getTrainingsProTag(final List<ITraining> allTrainings) {
        return allTrainings;
    }
}
