package ch.opentrainingcenter.client.cache.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.apache.log4j.Logger;
import org.eclipse.core.runtime.ListenerList;
import org.eclipse.jface.preference.IPreferenceStore;

import ch.opentrainingcenter.client.cache.Cache;
import ch.opentrainingcenter.client.cache.IRecordListener;
import ch.opentrainingcenter.client.model.ISimpleTraining;
import ch.opentrainingcenter.client.model.ModelFactory;
import ch.opentrainingcenter.client.model.RunType;
import ch.opentrainingcenter.client.model.TrainingOverviewFactory;
import ch.opentrainingcenter.db.DatabaseAccessFactory;
import ch.opentrainingcenter.db.IDatabaseAccess;
import ch.opentrainingcenter.importer.IConvert2Tcx;
import ch.opentrainingcenter.importer.IImportedConverter;
import ch.opentrainingcenter.tcx.ActivityListT;
import ch.opentrainingcenter.tcx.ActivityT;
import ch.opentrainingcenter.tcx.TrainingCenterDatabaseT;
import ch.opentrainingcenter.transfer.IImported;
import ch.opentrainingcenter.transfer.ITraining;

public final class TrainingCenterDataCache implements Cache {

    private ListenerList listeners;

    private static Cache instance = null;

    private Object[] selectedItems;

    private final TrainingCenterDatabaseT database;

    private final Map<Date, IImported> allImported = new TreeMap<Date, IImported>(new ImportedComparator());

    private final List<ISimpleTraining> simpleTrainings = new ArrayList<ISimpleTraining>();

    private final Map<Long, ActivityT> cache = new HashMap<Long, ActivityT>();

    public static final Logger LOGGER = Logger.getLogger(TrainingCenterDataCache.class);

    private final IDatabaseAccess dataAccess;

    private TrainingCenterDataCache() {
        this(DatabaseAccessFactory.getDatabaseAccess());
    }

    private TrainingCenterDataCache(final IDatabaseAccess databaseAccess, final IPreferenceStore store,
            final Map<String, IConvert2Tcx> converters) {
        this(databaseAccess);
    }

    private TrainingCenterDataCache(final IDatabaseAccess dataAccess) {
        this.dataAccess = dataAccess;
        database = new TrainingCenterDatabaseT();
        final ActivityListT activityList = new ActivityListT();
        database.setActivities(activityList);
    }

    public static TrainingCenterDataCache getInstanceForTests(final IImportedConverter loadGpsFile, final IDatabaseAccess dataAccess) {
        return new TrainingCenterDataCache(dataAccess);
    }

    public static Cache getInstance(final IDatabaseAccess delegate, final IPreferenceStore store, final Map<String, IConvert2Tcx> converters) {
        if (instance == null) {
            instance = new TrainingCenterDataCache(delegate, store, converters);
        }
        return instance;
    }

    public static Cache getInstance() {
        if (instance == null) {
            instance = new TrainingCenterDataCache();
        }
        return instance;
    }

    @Override
    public void add(final ActivityT activity) {
        final List<ActivityT> tmp = new ArrayList<ActivityT>();
        tmp.add(activity);
        addAll(tmp);
    }

    @Override
    public void addAll(final List<ActivityT> activities) {
        database.getActivities().getActivity().addAll(activities);
        for (final ActivityT activity : activities) {
            simpleTrainings.add(TrainingOverviewFactory.creatSimpleTraining(activity));
            final Date key = activity.getId().toGregorianCalendar().getTime();

            final IImported imported = dataAccess.getImportedRecord(key);
            if (imported != null) {
                final ITraining training = imported.getTraining();
                activity.setNotes(training.getNote());
            }

            allImported.put(key, imported);
            cache.put(key.getTime(), activity);
        }
        fireRecordAdded(null);
    }

    @Override
    public ActivityT get(final Date activityId) {
        final long key = activityId.getTime();
        final IImported imported = allImported.get(activityId);
        final ActivityT activity = cache.get(key);
        if (imported != null) {
            final ITraining training = imported.getTraining();
            activity.setNotes(training.getNote());
        }
        return activity;
    }

    /**
     * Methode für Testzwecke
     */
    public void resetCache() {
        database.getActivities().getActivity().clear();
        allImported.clear();
        simpleTrainings.clear();
        cache.clear();
        selectedItems = null;
    }

    @Override
    public void remove(final List<Date> deletedIds) {
        final List<ActivityT> activities = database.getActivities().getActivity();
        final List<ActivityT> activitiesToDelete = new ArrayList<ActivityT>();
        for (final ActivityT activity : activities) {
            final Date key = activity.getId().toGregorianCalendar().getTime();
            if (deletedIds.contains(key)) {
                activitiesToDelete.add(activity);
            }
        }
        database.getActivities().getActivity().removeAll(activitiesToDelete);
        for (final Date key : deletedIds) {
            allImported.remove(key);
        }
        final List<ISimpleTraining> simpleTrainingsToDelete = new ArrayList<ISimpleTraining>();
        for (final ISimpleTraining st : simpleTrainings) {
            if (deletedIds.contains(st.getDatum())) {
                simpleTrainingsToDelete.add(st);
            }
        }
        simpleTrainings.removeAll(simpleTrainingsToDelete);
        fireRecordDeleted(activitiesToDelete);
    }

    /*
     * (non-Javadoc)
     * 
     * @see ch.opentrainingcenter.client.cache.Cache#changeType(java.util.List,
     * ch.opentrainingcenter.client.model.RunType)
     */
    @Override
    public void changeType(final List<IImported> changedRecords, final RunType type) {
        for (final IImported record : changedRecords) {
            final IImported imp = allImported.get(record.getActivityId());
            for (final ISimpleTraining st : simpleTrainings) {
                if (imp != null && st.getDatum().equals(imp.getActivityId())) {
                    st.setType(type);
                }
            }
        }

    }

    @Override
    public void update() {
        if (listeners == null) {
            return;
        }
        final Object[] rls = listeners.getListeners();
        for (int i = 0; i < rls.length; i++) {
            final IRecordListener listener = (IRecordListener) rls[i];
            listener.recordChanged(null);
        }
    }

    @Override
    public void update(final IImported record) {
        allImported.put(record.getActivityId(), record);
        final long time = record.getActivityId().getTime();
        final ActivityT activityT = cache.get(time);
        if (record != null) {
            final ITraining training = record.getTraining();
            activityT.setNotes(training.getNote());
        }
        cache.put(time, activityT);
        if (listeners == null) {
            return;
        }
        final Object[] rls = listeners.getListeners();
        final List<ActivityT> changed = new ArrayList<ActivityT>();
        changed.add(activityT);
        for (int i = 0; i < rls.length; i++) {
            final IRecordListener listener = (IRecordListener) rls[i];
            listener.recordChanged(changed);
        }
    }

    private void fireRecordAdded(final Collection<ActivityT> activitiesAdded) {
        if (listeners == null) {
            return;
        }
        final Object[] rls = listeners.getListeners();
        for (int i = 0; i < rls.length; i++) {
            final IRecordListener listener = (IRecordListener) rls[i];
            listener.recordChanged(activitiesAdded);
        }

    }

    private void fireRecordDeleted(final List<ActivityT> deletedActivities) {
        if (listeners == null) {
            return;
        }
        final Object[] rls = listeners.getListeners();
        for (int i = 0; i < rls.length; i++) {
            final IRecordListener listener = (IRecordListener) rls[i];
            listener.deleteRecord(deletedActivities);
        }
    }

    @Override
    public void addListener(final IRecordListener listener) {
        if (listeners == null) {
            listeners = new ListenerList();
        }
        listeners.add(listener);
    }

    @Override
    public void removeListener(final IRecordListener listener) {
        if (listeners != null) {
            listeners.remove(listener);
            if (listeners.isEmpty()) {
                listeners = null;
            }
        }
    }

    @Override
    public List<ISimpleTraining> getAllSimpleTrainings() {
        return Collections.unmodifiableList(simpleTrainings);
    }

    @Override
    public void setSelection(final Object[] selectedItems) {
        this.selectedItems = Arrays.copyOf(selectedItems, selectedItems.length);
    }

    @Override
    public List<?> getSelection() {
        return Collections.unmodifiableList(Arrays.asList(selectedItems));
    }

    @Override
    public void addAllImported(final List<IImported> records) {
        for (final IImported record : records) {
            final ITraining training = record.getTraining();
            simpleTrainings.add(ModelFactory.createSimpleTraining(training, RunType.getRunType(record.getTrainingType().getId()), training
                    .getNote()));
            allImported.put(record.getActivityId(), record);
        }
    }

    @Override
    public boolean contains(final Date activityId) {
        if (activityId != null) {
            final long key = activityId.getTime();
            return cache.containsKey(key);
        } else {
            return false;
        }
    }

    @Override
    public String toString() {
        final StringBuffer str = new StringBuffer();
        str.append("Cache: Anzahl Elemente: ").append(cache.size()); //$NON-NLS-1$
        return str.toString();
    }

}
