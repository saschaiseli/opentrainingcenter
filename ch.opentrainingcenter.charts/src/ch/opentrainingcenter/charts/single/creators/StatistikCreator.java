package ch.opentrainingcenter.charts.single.creators;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TreeMap;

import org.apache.log4j.Logger;

import ch.opentrainingcenter.charts.bar.IStatistikCreator;
import ch.opentrainingcenter.model.training.ISimpleTraining;

public class StatistikCreator implements IStatistikCreator {

    private static final int LAST_KW_IN_YEAR = 52;
    private static final Logger LOGGER = Logger.getLogger(StatistikCreator.class);

    @Override
    public Map<Integer, List<ISimpleTraining>> getTrainingsProJahr(final List<ISimpleTraining> allTrainings) {
        final Map<Integer, List<ISimpleTraining>> trainingsPer = new HashMap<Integer, List<ISimpleTraining>>();
        for (final ISimpleTraining training : allTrainings) {
            final Calendar cal = Calendar.getInstance(Locale.GERMAN);
            cal.setTime(training.getDatum());
            final int year = cal.get(Calendar.YEAR);
            LOGGER.debug("Lauf aus dem Jahr " + year); //$NON-NLS-1$
            List<ISimpleTraining> perYear = trainingsPer.get(year);
            if (perYear == null) {
                perYear = new ArrayList<ISimpleTraining>();
            }
            perYear.add(training);
            trainingsPer.put(year, perYear);
        }
        return trainingsPer;
    }

    @Override
    public Map<Integer, Map<Integer, List<ISimpleTraining>>> getTrainingsProMonat(final List<ISimpleTraining> allTrainings) {
        final Map<Integer, Map<Integer, List<ISimpleTraining>>> trainingsPer = new HashMap<Integer, Map<Integer, List<ISimpleTraining>>>();
        final int outer = Calendar.YEAR;
        final int inner = Calendar.MONTH;
        calculate(allTrainings, trainingsPer, outer, inner);
        return trainingsPer;
    }

    @Override
    public Map<Integer, Map<Integer, List<ISimpleTraining>>> getTrainingsProWoche(final List<ISimpleTraining> allTrainings) {
        final Map<Integer, Map<Integer, List<ISimpleTraining>>> trainingsPer = new HashMap<Integer, Map<Integer, List<ISimpleTraining>>>();
        final int outer = Calendar.YEAR;
        final int inner = Calendar.WEEK_OF_YEAR;
        calculate(allTrainings, trainingsPer, outer, inner);
        return trainingsPer;
    }

    private void calculate(final List<ISimpleTraining> allTrainings, final Map<Integer, Map<Integer, List<ISimpleTraining>>> trainingsPer, final int outer,
            final int inner) {
        for (final ISimpleTraining training : allTrainings) {
            final Calendar cal = Calendar.getInstance(Locale.GERMAN);
            final Date datum = training.getDatum();
            cal.setTime(datum);
            final int year;
            // da monat mit 0 beginnt muss noch eins addiert werden.
            final int week = inner == Calendar.MONTH ? cal.get(inner) + 1 : cal.get(inner);
            if (isLaeufeProKW(outer, inner) && isKWFehler(datum)) {
                year = cal.get(outer) - 1;
            } else {
                year = cal.get(outer);
            }
            LOGGER.debug("Lauf aus der inner " + week + " vom Jahr: " + year); //$NON-NLS-1$//$NON-NLS-2$
            Map<Integer, List<ISimpleTraining>> yearMap = trainingsPer.get(year);
            if (yearMap == null) {
                yearMap = new TreeMap<Integer, List<ISimpleTraining>>();
            }
            List<ISimpleTraining> weekMap = yearMap.get(week);
            if (weekMap == null) {
                weekMap = new ArrayList<ISimpleTraining>();
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
    public List<ISimpleTraining> getTrainingsProTag(final List<ISimpleTraining> allTrainings) {
        return allTrainings;
    }
}
