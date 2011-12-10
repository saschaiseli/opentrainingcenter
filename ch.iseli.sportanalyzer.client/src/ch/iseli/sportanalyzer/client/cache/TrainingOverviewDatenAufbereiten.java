package ch.iseli.sportanalyzer.client.cache;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.apache.log4j.Logger;

import ch.iseli.sportanalyzer.client.model.SimpleTraining;

public class TrainingOverviewDatenAufbereiten {

    private static final Logger logger = Logger.getLogger(TrainingOverviewDatenAufbereiten.class);

    private final List<SimpleTraining> all;
    private final List<SimpleTraining> trainingsPerWeek = new ArrayList<SimpleTraining>();
    private final List<SimpleTraining> trainingsPerMonth = new ArrayList<SimpleTraining>();
    private final List<SimpleTraining> trainingsPerYear = new ArrayList<SimpleTraining>();

    private final TrainingCenterDataCache cache;

    public TrainingOverviewDatenAufbereiten() {
        super();
        cache = TrainingCenterDataCache.getInstance();
        all = cache.getAllSimpleTrainings();
        cache.addListener(new IRecordListener() {

            @Override
            public void recordChanged(final Collection<TrainingCenterRecord> entry) {
                loadDataFromCache();
            }

            @Override
            public void deleteRecord(final Collection<TrainingCenterRecord> entry) {
                loadDataFromCache();
            }

        });
        loadDataFromCache();
    }

    private void loadDataFromCache() {
        logger.debug("update daten vom cache"); //$NON-NLS-1$
        all.clear();
        all.addAll(cache.getAllSimpleTrainings());
        trainingsPerWeek.clear();
        trainingsPerWeek.addAll(createMonatsUndWochenMap(Calendar.YEAR, Calendar.WEEK_OF_YEAR));
        //
        trainingsPerMonth.clear();
        trainingsPerMonth.addAll(createMonatsUndWochenMap(Calendar.YEAR, Calendar.MONTH));

        trainingsPerYear.clear();
        trainingsPerYear.addAll(createYear());
    }

    private List<SimpleTraining> createYear() {
        final Map<Integer, List<SimpleTraining>> trainingsPer = new HashMap<Integer, List<SimpleTraining>>();
        for (final SimpleTraining training : all) {
            final Calendar cal = Calendar.getInstance();
            cal.setTime(training.getDatum());
            final int year = cal.get(Calendar.YEAR);
            logger.debug("Lauf aus dem Jahr " + year); //$NON-NLS-1$
            List<SimpleTraining> perYear = trainingsPer.get(year);
            if (perYear == null) {
                perYear = new ArrayList<SimpleTraining>();
            }
            perYear.add(training);
            trainingsPer.put(year, perYear);
        }
        final Map<Integer, Map<Integer, List<SimpleTraining>>> all = new HashMap<Integer, Map<Integer, List<SimpleTraining>>>();
        all.put(1, trainingsPer);
        return createSum(all);
    }

    private List<SimpleTraining> createMonatsUndWochenMap(final int outer, final int inner) {
        final Map<Integer, Map<Integer, List<SimpleTraining>>> trainingsPer = new HashMap<Integer, Map<Integer, List<SimpleTraining>>>();
        for (final SimpleTraining training : all) {
            final Calendar cal = Calendar.getInstance();
            cal.setTime(training.getDatum());
            final int year = cal.get(outer);
            // da monat mit 0 beginnt muss noch eins addiert werden.
            final int week = inner == Calendar.MONTH ? cal.get(inner) + 1 : cal.get(inner);
            logger.debug("Lauf aus der inner " + week + " vom Jahr: " + year); //$NON-NLS-1$//$NON-NLS-2$
            Map<Integer, List<SimpleTraining>> yearMap = trainingsPer.get(year);
            if (yearMap == null) {
                yearMap = new TreeMap<Integer, List<SimpleTraining>>();
            }
            List<SimpleTraining> weekMap = yearMap.get(week);
            if (weekMap == null) {
                weekMap = new ArrayList<SimpleTraining>();
            }
            weekMap.add(training);
            yearMap.put(week, weekMap);
            trainingsPer.put(year, yearMap);
        }

        return createSum(trainingsPer);
    }

    private List<SimpleTraining> createSum(final Map<Integer, Map<Integer, List<SimpleTraining>>> trainingsPer) {
        final List<SimpleTraining> result = new ArrayList<SimpleTraining>();
        for (final Map.Entry<Integer, Map<Integer, List<SimpleTraining>>> perYear : trainingsPer.entrySet()) {
            for (final Map.Entry<Integer, List<SimpleTraining>> perInner : perYear.getValue().entrySet()) {
                double distance = 0;
                double seconds = 0;
                int heartRate = 0;
                int countHeartIsZero = 0;
                Date date = null;
                for (final SimpleTraining training : perInner.getValue()) {
                    distance += training.getDistanzInMeter();
                    seconds += training.getDauerInSekunden();
                    final int avgHeartRate = training.getAvgHeartRate();
                    if (avgHeartRate <= 0) {
                        countHeartIsZero++;
                    }
                    heartRate += Integer.valueOf(avgHeartRate);
                    date = training.getDatum();
                }
                int avgHeartRate;
                if (heartRate > 0) {
                    avgHeartRate = heartRate / (perInner.getValue().size() - countHeartIsZero);
                } else {
                    avgHeartRate = 0;
                }
                final SimpleTraining e = new SimpleTraining(distance, seconds, date, avgHeartRate);
                if (e.getAvgHeartRate() > 0) {
                    result.add(e);
                }
            }
        }
        return result;
    }

    public List<SimpleTraining> getTrainingsPerDay() {
        return Collections.unmodifiableList(all);
    }

    public List<SimpleTraining> getTrainingsPerMonth() {
        return Collections.unmodifiableList(trainingsPerMonth);
    }

    public List<SimpleTraining> getTrainingsPerWeek() {
        return Collections.unmodifiableList(trainingsPerWeek);
    }

    public List<SimpleTraining> getTrainingsPerYear() {
        return Collections.unmodifiableList(trainingsPerYear);
    }

}
