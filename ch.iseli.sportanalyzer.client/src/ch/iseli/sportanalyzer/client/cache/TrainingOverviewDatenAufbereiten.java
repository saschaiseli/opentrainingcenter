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
import ch.iseli.sportanalyzer.tcx.TrainingCenterDatabaseT;

public class TrainingOverviewDatenAufbereiten {

    private static final Logger logger = Logger.getLogger(TrainingOverviewDatenAufbereiten.class);

    private final List<SimpleTraining> all;
    private final List<SimpleTraining> trainingsPerWeek;
    private final List<SimpleTraining> trainingsPerMonth;

    public TrainingOverviewDatenAufbereiten() {
        super();
        TrainingCenterDataCache cache = TrainingCenterDataCache.getInstance();
        this.all = cache.getAllSimpleTrainings();
        cache.addListener(new IRecordListener() {

            @Override
            public void recordChanged(Collection<TrainingCenterDatabaseT> entry) {
                // Datenstruktur updaten
                logger.debug("update Struktur...");
                trainingsPerWeek.clear();
                trainingsPerWeek.addAll(createMonatsUndWochenMap(Calendar.YEAR, Calendar.WEEK_OF_MONTH));
                //
                trainingsPerMonth.clear();
                trainingsPerMonth.addAll(createMonatsUndWochenMap(Calendar.YEAR, Calendar.MONTH));
            }
        });
        logger.debug("Initialize Woche Start");
        trainingsPerWeek = createMonatsUndWochenMap(Calendar.YEAR, Calendar.WEEK_OF_MONTH);
        logger.debug("Initialize Woche fertig");

        logger.debug("Initialize Monat Start");
        trainingsPerMonth = createMonatsUndWochenMap(Calendar.YEAR, Calendar.MONTH);
        logger.debug("Initialize Monat fertig");
    }

    private List<SimpleTraining> createMonatsUndWochenMap(int outer, int inner) {
        Map<Integer, Map<Integer, List<SimpleTraining>>> trainingsPer = new HashMap<Integer, Map<Integer, List<SimpleTraining>>>();
        for (SimpleTraining training : all) {
            Calendar cal = Calendar.getInstance();
            cal.setTime(training.getDatum());
            int year = cal.get(outer);
            // da monat mit 0 beginnt muss noch eins addiert werden.
            int week = inner == Calendar.MONTH ? cal.get(inner) + 1 : cal.get(inner);
            logger.debug("Lauf aus der inner " + week + " vom Jahr: " + year);
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

    private List<SimpleTraining> createSum(Map<Integer, Map<Integer, List<SimpleTraining>>> trainingsPer) {
        List<SimpleTraining> result = new ArrayList<SimpleTraining>();
        for (Map.Entry<Integer, Map<Integer, List<SimpleTraining>>> perYear : trainingsPer.entrySet()) {
            for (Map.Entry<Integer, List<SimpleTraining>> perInner : perYear.getValue().entrySet()) {
                double distance = 0;
                double seconds = 0;
                int heartRate = 0;
                Date date = null;
                for (SimpleTraining training : perInner.getValue()) {
                    distance += training.getDistanzInMeter();
                    seconds += training.getDauerInSekunden();
                    heartRate += Integer.valueOf(training.getAvgHeartRate());
                    date = training.getDatum();
                }
                result.add(new SimpleTraining(distance, seconds, date, heartRate / perInner.getValue().size()));
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
        // TODO Auto-generated method stub
        return null;
    }

}
