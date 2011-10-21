package ch.iseli.sportanalyzer.client.cache;

import java.util.ArrayList;
import java.util.List;

import ch.iseli.sportanalyzer.tcx.TrainingCenterDatabaseT;
import ch.opentrainingcenter.transfer.Athlete;

public class TrainingCenterDataCache {

    private static TrainingCenterDataCache            INSTANCE = null;

    private final List<TrainingCenterDatabaseTParent> list     = new ArrayList<TrainingCenterDatabaseTParent>();

    private static TrainingCenterDatabaseT            selected;

    private TrainingCenterDataCache(List<TrainingCenterDatabaseT> all) {
        for (TrainingCenterDatabaseT db : all) {
            getAllRuns().add(new TrainingCenterDatabaseTParent(db, new Athlete("sascha", "iseli")));
        }
    }

    public static TrainingCenterDataCache initCache(List<TrainingCenterDatabaseT> all) {
        if (INSTANCE == null) {
            INSTANCE = new TrainingCenterDataCache(all);
        }
        return INSTANCE;
    }

    public static void setSelectedRun(TrainingCenterDatabaseT selected) {
        TrainingCenterDataCache.selected = selected;
    }

    public static TrainingCenterDatabaseT getSelected() {
        return selected;
    }

    public List<TrainingCenterDatabaseTParent> getAllRuns() {
        return list;
    }
}
