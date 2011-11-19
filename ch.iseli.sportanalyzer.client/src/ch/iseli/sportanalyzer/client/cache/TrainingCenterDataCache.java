package ch.iseli.sportanalyzer.client.cache;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.core.runtime.ListenerList;

import ch.iseli.sportanalyzer.client.model.SimpleTraining;
import ch.iseli.sportanalyzer.client.model.TrainingOverview;
import ch.iseli.sportanalyzer.tcx.TrainingCenterDatabaseT;

public class TrainingCenterDataCache {

    private ListenerList listeners;

    private static TrainingCenterDataCache INSTANCE = null;

    private final Map<Integer, TrainingCenterDatabaseTParent> list = new HashMap<Integer, TrainingCenterDatabaseTParent>();

    private static TrainingCenterDatabaseTParent selected;

    private Object[] selectedItems;

    private TrainingCenterDataCache() {
    }

    public static TrainingCenterDataCache getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new TrainingCenterDataCache();
        }
        return INSTANCE;
    }

    public void setSelectedRun(TrainingCenterDatabaseTParent selected) {
        TrainingCenterDataCache.selected = selected;
    }

    public TrainingCenterDatabaseTParent getSelected() {
        return selected;
    }

    /**
     * @return eine Übersicht auf das selektierte Training.
     */
    public TrainingOverview getSelectedOverview() {
        return new TrainingOverview(selected.getTrainingCenterDatabase());
    }

    public Collection<TrainingCenterDatabaseTParent> getAllRuns() {
        return list.values();
    }

    public void addAll(Map<Integer, TrainingCenterDatabaseT> records) {
        for (Map.Entry<Integer, TrainingCenterDatabaseT> record : records.entrySet()) {
            list.put(record.getKey(), new TrainingCenterDatabaseTParent(record.getKey(), record.getValue(), null));
        }
        fireRecordAdded(records.values());
    }

    private void fireRecordAdded(Collection<TrainingCenterDatabaseT> collection) {
        if (listeners == null)
            return;
        Object[] rls = listeners.getListeners();
        for (int i = 0; i < rls.length; i++) {
            IRecordListener listener = (IRecordListener) rls[i];
            listener.recordChanged(collection);
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

    public void remove(Integer id) {
        list.remove(id);
        fireRecordAdded(null);
    }

    public List<SimpleTraining> getAllSimpleTrainings() {
        Collection<TrainingCenterDatabaseTParent> values = list.values();
        List<SimpleTraining> result = new ArrayList<SimpleTraining>();
        for (TrainingCenterDatabaseTParent t : values) {
            TrainingOverview over = new TrainingOverview(t.getTrainingCenterDatabase());
            result.add(over.getSimpleTraining());
        }
        return result;
    }

    public void setSelection(Object[] selectedItems) {
        this.selectedItems = selectedItems;
    }

    public List<?> getSelection() {
        return Collections.unmodifiableList(Arrays.asList(selectedItems));
    }
}
