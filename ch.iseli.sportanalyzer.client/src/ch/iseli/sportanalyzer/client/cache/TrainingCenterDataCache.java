package ch.iseli.sportanalyzer.client.cache;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.ListenerList;

import ch.iseli.sportanalyzer.tcx.TrainingCenterDatabaseT;
import ch.opentrainingcenter.transfer.Factory;

public class TrainingCenterDataCache {

    private ListenerList listeners;

    private static TrainingCenterDataCache INSTANCE = null;

    private final List<TrainingCenterDatabaseTParent> list = new ArrayList<TrainingCenterDatabaseTParent>();

    private static TrainingCenterDatabaseT selected;

    private TrainingCenterDataCache(List<TrainingCenterDatabaseT> all) {
        for (TrainingCenterDatabaseT db : all) {
            getAllRuns().add(new TrainingCenterDatabaseTParent(db, Factory.createAthlete("sascha")));
        }
    }

    public static TrainingCenterDataCache getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new TrainingCenterDataCache(new ArrayList<TrainingCenterDatabaseT>());
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

    public void addAll(List<TrainingCenterDatabaseT> records) {
        for (TrainingCenterDatabaseT record : records) {
            list.add(new TrainingCenterDatabaseTParent(record, null));
        }
        fireRecordAdded(records);
    }

    protected void fireRecordAdded(List<TrainingCenterDatabaseT> entry) {
        if (listeners == null)
            return;
        Object[] rls = listeners.getListeners();
        for (int i = 0; i < rls.length; i++) {
            IRecordListener listener = (IRecordListener) rls[i];
            listener.recordChanged(entry);
        }

    }

    public void addListener(IRecordListener listener) {
        if (listeners == null) {
            listeners = new ListenerList();
        }
        listeners.add(listener);
    }

    public void removeListener(IRecordListener listener) {
        if (listeners != null) {
            listeners.remove(listener);
            if (listeners.isEmpty()) {
                listeners = null;
            }
        }

    }
}
