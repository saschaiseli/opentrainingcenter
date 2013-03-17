package ch.opentrainingcenter.core.cache;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.apache.log4j.Logger;

import ch.opentrainingcenter.core.cache.internal.ImportedComparator;
import ch.opentrainingcenter.core.db.DatabaseAccessFactory;
import ch.opentrainingcenter.core.db.IDatabaseAccess;
import ch.opentrainingcenter.tcx.ActivityListT;
import ch.opentrainingcenter.tcx.ActivityT;
import ch.opentrainingcenter.tcx.ExtensionsT;
import ch.opentrainingcenter.tcx.TrainingCenterDatabaseT;
import ch.opentrainingcenter.transfer.ActivityExtension;
import ch.opentrainingcenter.transfer.IImported;
import ch.opentrainingcenter.transfer.ITraining;
import ch.opentrainingcenter.transfer.IWeather;

public final class TrainingCenterDataCache extends AbstractCache<Date, ActivityT> implements Cache {

    private static Cache instance = null;

    private final TrainingCenterDatabaseT trainingCenterModel;

    private final Map<Date, IImported> allImported = new TreeMap<Date, IImported>(new ImportedComparator());

    private final Map<Long, ActivityT> map = new HashMap<Long, ActivityT>();

    public static final Logger LOGGER = Logger.getLogger(TrainingCenterDataCache.class);

    private final IDatabaseAccess dataAccess;

    private TrainingCenterDataCache() {
        this(DatabaseAccessFactory.getDatabaseAccess());
    }

    private TrainingCenterDataCache(final IDatabaseAccess dataAccess) {
        this.dataAccess = dataAccess;
        trainingCenterModel = new TrainingCenterDatabaseT();
        final ActivityListT activityList = new ActivityListT();
        trainingCenterModel.setActivities(activityList);
    }

    public static TrainingCenterDataCache getInstanceForTests(final IDatabaseAccess dataAccess) {
        return new TrainingCenterDataCache(dataAccess);
    }

    public static Cache getInstance(final IDatabaseAccess delegate) {
        if (instance == null) {
            instance = new TrainingCenterDataCache(delegate);
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
        trainingCenterModel.getActivities().getActivity().addAll(activities);
        for (final ActivityT activity : activities) {
            final Date key = getKey(activity);

            final IImported imported = dataAccess.getImportedRecord(key);
            if (imported != null) {
                final ITraining training = imported.getTraining();
                final String note = training.getNote();
                final IWeather weather = training.getWeather();
                final ExtensionsT ext = new ExtensionsT();
                ext.getAny().add(new ActivityExtension(note, weather, imported.getRoute()));
                activity.setExtensions(ext);
            }

            allImported.put(key, imported);
            map.put(key.getTime(), activity);
        }
        fireRecordAdded(null);
    }

    @Override
    public ActivityT get(final Date activityId) {
        final long key = activityId.getTime();
        final IImported imported = allImported.get(activityId);
        final ActivityT activity = map.get(key);
        if (imported != null) {
            final ITraining training = imported.getTraining();
            final String note = training.getNote();
            final IWeather weather = training.getWeather();
            final ExtensionsT ext = new ExtensionsT();
            ext.getAny().add(new ActivityExtension(note, weather, imported.getRoute()));
            activity.setExtensions(ext);
        }
        return activity;
    }

    /**
     * Methode für Testzwecke
     */
    @Override
    public void resetCache() {
        trainingCenterModel.getActivities().getActivity().clear();
        allImported.clear();
        map.clear();
    }

    @Override
    public void remove(final List<Date> deletedIds) {
        final List<ActivityT> activities = trainingCenterModel.getActivities().getActivity();
        final List<ActivityT> activitiesToDelete = new ArrayList<ActivityT>();
        for (final ActivityT activity : activities) {
            final Date key = activity.getId().toGregorianCalendar().getTime();
            if (deletedIds.contains(key)) {
                activitiesToDelete.add(activity);
            }
        }
        trainingCenterModel.getActivities().getActivity().removeAll(activitiesToDelete);
        for (final Date key : deletedIds) {
            allImported.remove(key);
        }
        fireRecordDeleted(activitiesToDelete);
    }

    @Override
    public void updateExtension(final Date activityId, final ActivityExtension extension) {
        final long time = activityId.getTime();
        final ActivityT activityT = map.get(time);
        if (activityT != null) {
            final ExtensionsT ext = new ExtensionsT();
            ext.getAny().add(extension);
            activityT.setExtensions(ext);
            map.put(time, activityT);
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
            @SuppressWarnings("unchecked")
            final IRecordListener<ActivityT> listener = (IRecordListener<ActivityT>) rls[i];
            listener.recordChanged(changed);
        }
    }

    private void fireRecordAdded(final Collection<ActivityT> activitiesAdded) {
        if (listeners == null) {
            return;
        }
        final Object[] rls = listeners.getListeners();
        for (int i = 0; i < rls.length; i++) {
            @SuppressWarnings("unchecked")
            final IRecordListener<ActivityT> listener = (IRecordListener<ActivityT>) rls[i];
            listener.recordChanged(activitiesAdded);
        }

    }

    private void fireRecordDeleted(final List<ActivityT> deletedActivities) {
        if (listeners == null) {
            return;
        }
        final Object[] rls = listeners.getListeners();
        for (int i = 0; i < rls.length; i++) {
            @SuppressWarnings("unchecked")
            final IRecordListener<ActivityT> listener = (IRecordListener<ActivityT>) rls[i];
            listener.deleteRecord(deletedActivities);
        }
    }

    @Override
    public boolean contains(final Date activityId) {
        if (activityId != null) {
            final long key = activityId.getTime();
            return map.containsKey(key);
        } else {
            return false;
        }
    }

    @Override
    public String toString() {
        final StringBuffer str = new StringBuffer();
        str.append("Cache: Anzahl Elemente: ").append(map.size()); //$NON-NLS-1$
        return str.toString();
    }

    @Override
    public void remove(final Date key) {
    }

    @Override
    public Date getKey(final ActivityT value) {
        return value.getId().toGregorianCalendar().getTime();
    }
}
