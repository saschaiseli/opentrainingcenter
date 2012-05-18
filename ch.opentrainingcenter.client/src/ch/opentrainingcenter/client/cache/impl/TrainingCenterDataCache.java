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
import ch.opentrainingcenter.db.DatabaseAccessFactory;
import ch.opentrainingcenter.db.IDatabaseAccess;
import ch.opentrainingcenter.importer.IConvert2Tcx;
import ch.opentrainingcenter.importer.IImportedConverter;
import ch.opentrainingcenter.tcx.ActivityListT;
import ch.opentrainingcenter.tcx.ActivityT;
import ch.opentrainingcenter.tcx.ExtensionsT;
import ch.opentrainingcenter.tcx.TrainingCenterDatabaseT;
import ch.opentrainingcenter.transfer.IImported;
import ch.opentrainingcenter.transfer.ITraining;
import ch.opentrainingcenter.transfer.IWeather;

public final class TrainingCenterDataCache implements Cache {

    private ListenerList listeners;

    private static Cache instance = null;

    private Object[] selectedItems;

    private final TrainingCenterDatabaseT database;

    private final Map<Date, IImported> allImported = new TreeMap<Date, IImported>(new ImportedComparator());

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
            final Date key = activity.getId().toGregorianCalendar().getTime();

            final IImported imported = dataAccess.getImportedRecord(key);
            if (imported != null) {
                final ITraining training = imported.getTraining();
                activity.setNotes(training.getNote());
                final ExtensionsT ext = new ExtensionsT();
                ext.getAny().add(training.getWeather());
                activity.setExtensions(ext);
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
        fireRecordDeleted(activitiesToDelete);
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
    public void updateNote(final Date activityId, final String note) {
        final long time = activityId.getTime();
        final ActivityT activityT = cache.get(time);
        if (activityT != null) {
            activityT.setNotes(note);
            cache.put(time, activityT);
        }
        notifyListeners(activityT);
    }

    private void notifyListeners(final ActivityT activityT) {
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

    @Override
    public void updateWetter(final Date activityId, final IWeather wetter) {
        final long time = activityId.getTime();
        final ActivityT activityT = cache.get(time);
        if (activityT != null) {
            final ExtensionsT ext = new ExtensionsT();
            ext.getAny().add(wetter);
            activityT.setExtensions(ext);
            cache.put(time, activityT);
        }
        notifyListeners(activityT);
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
    public void setSelection(final Object[] selectedItems) {
        this.selectedItems = Arrays.copyOf(selectedItems, selectedItems.length);
    }

    @Override
    public List<?> getSelection() {
        return Collections.unmodifiableList(Arrays.asList(selectedItems));
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
