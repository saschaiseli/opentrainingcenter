package ch.iseli.sportanalyzer.client.model;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.apache.log4j.Logger;

import ch.iseli.sportanalyzer.client.cache.IRecordListener;
import ch.iseli.sportanalyzer.client.cache.TrainingCenterDataCache;
import ch.iseli.sportanalyzer.tcx.TrainingCenterDatabaseT;

public class TrainingOverviewDatenAufbereiten {

    private static final Logger logger = Logger.getLogger(TrainingOverviewDatenAufbereiten.class);

    private final List<TrainingOverview> all;

    private final Map<Integer, Map<Integer, List<TrainingOverview>>> trainingsPerWeek;
    private final Map<Integer, Map<Integer, List<TrainingOverview>>> trainingsPerMonth;

    protected TrainingOverviewDatenAufbereiten() {
        super();
        TrainingCenterDataCache cache = TrainingCenterDataCache.getInstance();
        this.all = cache.getAllOverviews();
        cache.addListener(new IRecordListener() {

            @Override
            public void recordChanged(Collection<TrainingCenterDatabaseT> entry) {
                // Datenstruktur updaten
                logger.debug("update Struktur...");
                trainingsPerWeek.clear();
                trainingsPerWeek.putAll(createMonatsUndWochenMap(Calendar.YEAR, Calendar.WEEK_OF_MONTH));
                //
                trainingsPerMonth.clear();
                trainingsPerMonth.putAll(createMonatsUndWochenMap(Calendar.YEAR, Calendar.MONTH));
            }
        });
        logger.debug("Initialize Woche Start");
        trainingsPerWeek = createMonatsUndWochenMap(Calendar.YEAR, Calendar.WEEK_OF_MONTH);
        logger.debug("Initialize Woche fertig");

        logger.debug("Initialize Monat Start");
        trainingsPerMonth = createMonatsUndWochenMap(Calendar.YEAR, Calendar.MONTH);
        logger.debug("Initialize Monat fertig");
    }

    private Map<Integer, Map<Integer, List<TrainingOverview>>> createMonatsUndWochenMap(int outer, int inner) {
        Map<Integer, Map<Integer, List<TrainingOverview>>> trainingsPer = new HashMap<Integer, Map<Integer, List<TrainingOverview>>>();
        for (TrainingOverview training : all) {
            Calendar cal = Calendar.getInstance();
            cal.setTime(training.getDate());
            int year = cal.get(outer);
            // da monat mit 0 beginnt muss noch eins addiert werden.
            int week = inner == Calendar.MONTH ? cal.get(inner) + 1 : cal.get(inner);
            logger.debug("Lauf aus der inner " + week + " vom Jahr: " + year);
            Map<Integer, List<TrainingOverview>> yearMap = trainingsPer.get(year);
            if (yearMap == null) {
                yearMap = new TreeMap<Integer, List<TrainingOverview>>();
            }
            List<TrainingOverview> weekMap = yearMap.get(week);
            if (weekMap == null) {
                weekMap = new ArrayList<TrainingOverview>();
            }
            weekMap.add(training);
        }
        return trainingsPer;
    }

    public Map<Integer, Map<Integer, List<TrainingOverview>>> getTrainingsPerWeek() {
        return Collections.unmodifiableMap(trainingsPerWeek);
    }

    public Map<Integer, Map<Integer, List<TrainingOverview>>> getTrainingsPerMonth() {
        return Collections.unmodifiableMap(trainingsPerMonth);
    }
}
