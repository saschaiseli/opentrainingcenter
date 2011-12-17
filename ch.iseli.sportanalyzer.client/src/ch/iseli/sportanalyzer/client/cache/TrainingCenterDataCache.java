package ch.iseli.sportanalyzer.client.cache;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.eclipse.core.runtime.ListenerList;

import ch.iseli.sportanalyzer.client.model.ITrainingOverview;
import ch.iseli.sportanalyzer.client.model.SimpleTraining;
import ch.iseli.sportanalyzer.client.model.TrainingOverviewFactory;
import ch.opentrainingcenter.transfer.IAthlete;

public class TrainingCenterDataCache {

    private ListenerList listeners;

    private IAthlete selectedProfile;

    private static TrainingCenterDataCache INSTANCE = null;

    private final Map<Integer, TrainingCenterRecord> list;

    private static TrainingCenterRecord selected;

    private Object[] selectedItems;

    private boolean cacheLoaded;

    private TrainingCenterDataCache() {
        list = new TreeMap<Integer, TrainingCenterRecord>();
    }

    public static TrainingCenterDataCache getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new TrainingCenterDataCache();
        }
        return INSTANCE;
    }

    public void setSelectedRun(final TrainingCenterRecord selected) {
        TrainingCenterDataCache.selected = selected;
    }

    public void setSelectedProfile(final IAthlete athlete) {
        this.selectedProfile = athlete;
        resetCache();
    }

    private void resetCache() {
        list.clear();
        selected = null;
        selectedItems = null;
    }

    /**
     * Ist noch keiner selektiert, wird der neuste, also derjenige mit dem jüngsten datum, zurückgegeben. Dieser wird dann auch als selektierten Lauf gesetzt.
     * 
     * @return den selektierten record oder den neusten record.
     */
    public TrainingCenterRecord getSelected() {
        setIfNothingSelectedTheNewestAsSelected();
        return selected;
    }

    /**
     * @return eine Übersicht auf das selektierte Training.
     */
    public ITrainingOverview getSelectedOverview() {
        setIfNothingSelectedTheNewestAsSelected();
        return TrainingOverviewFactory.creatTrainingOverview(selected);
    }

    private void setIfNothingSelectedTheNewestAsSelected() {
        if (selected == null && !list.isEmpty()) {
            final TrainingCenterRecord newest = getLatestRun();
            selected = newest;
        }
    }

    private TrainingCenterRecord getLatestRun() {
        final TrainingCenterRecord newest = Collections.max(new ArrayList<TrainingCenterRecord>(list.values()), new Comparator<TrainingCenterRecord>() {

            @Override
            public int compare(final TrainingCenterRecord o1, final TrainingCenterRecord o2) {
                return Long.valueOf(o1.getDate().toGregorianCalendar().getTimeInMillis()).compareTo(o2.getDate().toGregorianCalendar().getTimeInMillis());
            }
        });
        return newest;
    }

    /**
     * @return die sortierte liste von records.
     */
    public Collection<TrainingCenterRecord> getAllRuns() {
        final Collection<TrainingCenterRecord> values = list.values();
        final List<TrainingCenterRecord> all = new ArrayList<TrainingCenterRecord>(values);
        Collections.sort(all, new TrainingCenterRecordComparator());
        return all;
    }

    public void addAll(final Map<Integer, TrainingCenterRecord> records) {
        for (final Map.Entry<Integer, TrainingCenterRecord> record : records.entrySet()) {
            list.put(record.getKey(), record.getValue());
        }
        fireRecordAdded(records.values());
    }

    public void addWithoutNotifyAll(final Map<Integer, TrainingCenterRecord> records) {
        for (final Map.Entry<Integer, TrainingCenterRecord> record : records.entrySet()) {
            list.put(record.getKey(), record.getValue());
        }
    }

    public void remove(final List<Integer> ids) {
        final List<TrainingCenterRecord> deletedRecords = new ArrayList<TrainingCenterRecord>();
        for (final Integer id : ids) {
            deletedRecords.add(list.remove(id));
        }
        fireRecordDeleted(deletedRecords);
    }

    private void fireRecordAdded(final Collection<TrainingCenterRecord> collection) {
        if (listeners == null)
            return;
        final Object[] rls = listeners.getListeners();
        for (int i = 0; i < rls.length; i++) {
            final IRecordListener listener = (IRecordListener) rls[i];
            listener.recordChanged(collection);
        }

    }

    private void fireRecordDeleted(final List<TrainingCenterRecord> deletedRecords) {
        if (listeners == null)
            return;
        final Object[] rls = listeners.getListeners();
        for (int i = 0; i < rls.length; i++) {
            final IRecordListener listener = (IRecordListener) rls[i];
            listener.deleteRecord(deletedRecords);
        }
    }

    public void addListener(final IRecordListener listener) {
        if (listeners == null) {
            listeners = new ListenerList();
        }
        listeners.add(listener);
    }

    public void removeListener(final IRecordListener listener) {
        if (listeners != null) {
            listeners.remove(listener);
            if (listeners.isEmpty()) {
                listeners = null;
            }
        }

    }

    public List<SimpleTraining> getAllSimpleTrainings() {
        final Collection<TrainingCenterRecord> values = list.values();
        final List<SimpleTraining> result = new ArrayList<SimpleTraining>();
        for (final TrainingCenterRecord t : values) {
            final ITrainingOverview over = TrainingOverviewFactory.creatTrainingOverview(t);
            result.add(over.getSimpleTraining());
        }
        return result;
    }

    public void setSelection(final Object[] selectedItems) {
        this.selectedItems = selectedItems;
    }

    public List<?> getSelection() {
        return Collections.unmodifiableList(Arrays.asList(selectedItems));
    }

    public IAthlete getSelectedProfile() {
        return selectedProfile;
    }

    public void cacheLoaded() {
        cacheLoaded = true;
    }

    public boolean isCacheLoaded() {
        return cacheLoaded;
    }

    /**
     * setzt im cache den lauf mit der angegeben id oder null, wenn dieser nicht im cache geladen ist.
     */
    public void setSelectedRun(final int id) {
        selected = list.get(id);
    }
}
