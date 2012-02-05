package ch.opentrainingcenter.client.cache;

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

import ch.opentrainingcenter.client.model.ISimpleTraining;
import ch.opentrainingcenter.client.model.ModelFactory;
import ch.opentrainingcenter.tcx.ActivityT;

public class TrainingOverviewDatenAufbereiten {

    private static final Logger logger = Logger.getLogger(TrainingOverviewDatenAufbereiten.class);

    private final List<ISimpleTraining> all;
    private final List<ISimpleTraining> trainingsPerWeek = new ArrayList<ISimpleTraining>();
    private final List<ISimpleTraining> trainingsPerMonth = new ArrayList<ISimpleTraining>();
    private final List<ISimpleTraining> trainingsPerYear = new ArrayList<ISimpleTraining>();

    private final TrainingCenterDataCache cache;

    public TrainingOverviewDatenAufbereiten() {
        super();
        cache = TrainingCenterDataCache.getInstance();
        all = cache.getAllSimpleTrainings();
        cache.addListener(new IRecordListener() {

            @Override
            public void recordChanged(final Collection<ActivityT> entry) {
                loadDataFromCache();
            }

            @Override
            public void deleteRecord(final Collection<ActivityT> entry) {
                loadDataFromCache();
            }

        });
        loadDataFromCache();
    }

    private void loadDataFromCache() {
        logger.debug("update daten vom cache"); //$NON-NLS-1$
        // all.clear();
        // all.addAll(cache.getAllSimpleTrainings());
        trainingsPerWeek.clear();
        trainingsPerWeek.addAll(createMonatsUndWochenMap(Calendar.YEAR, Calendar.WEEK_OF_YEAR));
        //
        trainingsPerMonth.clear();
        trainingsPerMonth.addAll(createMonatsUndWochenMap(Calendar.YEAR, Calendar.MONTH));

        trainingsPerYear.clear();
        trainingsPerYear.addAll(createYear());
    }

    private List<ISimpleTraining> createYear() {
        final Map<Integer, List<ISimpleTraining>> trainingsPer = new HashMap<Integer, List<ISimpleTraining>>();
        for (final ISimpleTraining training : all) {
            final Calendar cal = Calendar.getInstance();
            cal.setTime(training.getDatum());
            final int year = cal.get(Calendar.YEAR);
            logger.debug("Lauf aus dem Jahr " + year); //$NON-NLS-1$
            List<ISimpleTraining> perYear = trainingsPer.get(year);
            if (perYear == null) {
                perYear = new ArrayList<ISimpleTraining>();
            }
            perYear.add(training);
            trainingsPer.put(year, perYear);
        }
        final Map<Integer, Map<Integer, List<ISimpleTraining>>> all = new HashMap<Integer, Map<Integer, List<ISimpleTraining>>>();
        all.put(1, trainingsPer);
        return createSum(all);
    }

    private List<ISimpleTraining> createMonatsUndWochenMap(final int outer, final int inner) {
        final Map<Integer, Map<Integer, List<ISimpleTraining>>> trainingsPer = new HashMap<Integer, Map<Integer, List<ISimpleTraining>>>();
        for (final ISimpleTraining training : all) {
            final Calendar cal = Calendar.getInstance();
            cal.setTime(training.getDatum());
            final int year = cal.get(outer);
            // da monat mit 0 beginnt muss noch eins addiert werden.
            final int week = inner == Calendar.MONTH ? cal.get(inner) + 1 : cal.get(inner);
            logger.debug("Lauf aus der inner " + week + " vom Jahr: " + year); //$NON-NLS-1$//$NON-NLS-2$
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

        return createSum(trainingsPer);
    }

    private List<ISimpleTraining> createSum(final Map<Integer, Map<Integer, List<ISimpleTraining>>> trainingsPer) {
        final List<ISimpleTraining> result = new ArrayList<ISimpleTraining>();
        for (final Map.Entry<Integer, Map<Integer, List<ISimpleTraining>>> perYear : trainingsPer.entrySet()) {
            for (final Map.Entry<Integer, List<ISimpleTraining>> perInner : perYear.getValue().entrySet()) {
                double distance = 0;
                double seconds = 0;
                int heartRate = 0;
                int countHeartIsZero = 0;
                Date date = null;
                for (final ISimpleTraining training : perInner.getValue()) {
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
                result.add(ModelFactory.createSimpleTraining(distance, seconds, date, avgHeartRate, 0, 0));
            }
        }
        return result;
    }

    public List<ISimpleTraining> getTrainingsPerDay() {
        return Collections.unmodifiableList(all);
    }

    public List<ISimpleTraining> getTrainingsPerMonth() {
        return Collections.unmodifiableList(trainingsPerMonth);
    }

    public List<ISimpleTraining> getTrainingsPerWeek() {
        return Collections.unmodifiableList(trainingsPerWeek);
    }

    public List<ISimpleTraining> getTrainingsPerYear() {
        return Collections.unmodifiableList(trainingsPerYear);
    }

}
