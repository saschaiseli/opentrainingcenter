package ch.opentrainingcenter.client.cache.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.apache.log4j.Logger;
import org.eclipse.core.runtime.ListenerList;
import org.eclipse.jface.preference.IPreferenceStore;

import ch.opentrainingcenter.client.Activator;
import ch.opentrainingcenter.client.cache.Cache;
import ch.opentrainingcenter.client.cache.IRecordListener;
import ch.opentrainingcenter.client.model.ISimpleTraining;
import ch.opentrainingcenter.client.model.ModelFactory;
import ch.opentrainingcenter.client.model.RunType;
import ch.opentrainingcenter.client.model.TrainingOverviewFactory;
import ch.opentrainingcenter.db.DatabaseAccessFactory;
import ch.opentrainingcenter.db.IDatabaseAccess;
import ch.opentrainingcenter.importer.ConvertContainer;
import ch.opentrainingcenter.importer.ExtensionHelper;
import ch.opentrainingcenter.importer.IConvert2Tcx;
import ch.opentrainingcenter.importer.IImportedConverter;
import ch.opentrainingcenter.importer.impl.ImportedConverter;
import ch.opentrainingcenter.tcx.ActivityListT;
import ch.opentrainingcenter.tcx.ActivityT;
import ch.opentrainingcenter.tcx.TrainingCenterDatabaseT;
import ch.opentrainingcenter.transfer.IAthlete;
import ch.opentrainingcenter.transfer.IImported;
import ch.opentrainingcenter.transfer.ITraining;

public final class TrainingCenterDataCache implements Cache {

    private ListenerList listeners;

    private IAthlete selectedProfile;

    private static Cache instance = null;

    private IImported selectedImport;

    private Object[] selectedItems;

    private boolean cacheLoaded;

    private final TrainingCenterDatabaseT database;

    private final Map<Date, IImported> allImported = new TreeMap<Date, IImported>(new ImportedComparator());

    private final List<ISimpleTraining> simpleTrainings = new ArrayList<ISimpleTraining>();

    private final Map<Long, ActivityT> cache = new HashMap<Long, ActivityT>();

    private final IImportedConverter loadGpsFile;

    public static final Logger LOGGER = Logger.getLogger(TrainingCenterDataCache.class);

    private final IDatabaseAccess dataAccess;

    private TrainingCenterDataCache() {
        this(new ImportedConverter(Activator.getDefault().getPreferenceStore(), new ConvertContainer(ExtensionHelper.getConverters())),
                DatabaseAccessFactory.getDatabaseAccess());
    }

    private TrainingCenterDataCache(final IDatabaseAccess delegate, final IPreferenceStore store, final Map<String, IConvert2Tcx> converters) {
        this(new ImportedConverter(store, new ConvertContainer(converters)), delegate);
    }

    private TrainingCenterDataCache(final IImportedConverter loadGpsFile, final IDatabaseAccess dataAccess) {
        this.loadGpsFile = loadGpsFile;
        this.dataAccess = dataAccess;

        database = new TrainingCenterDatabaseT();
        final ActivityListT activityList = new ActivityListT();
        database.setActivities(activityList);
    }

    public static TrainingCenterDataCache getInstanceForTests(final IImportedConverter loadGpsFile, final IDatabaseAccess dataAccess) {
        return new TrainingCenterDataCache(loadGpsFile, dataAccess);
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

    @Override
    public Collection<IImported> getAllImportedRecords() {
        return allImported.values();
    }

    // @Override
    // public void setSelectedRun(final IImported selected) {
    // selectedImport = selected;
    // }

    @Override
    public void setSelectedProfile(final IAthlete athlete) {
        resetCache();
        this.selectedProfile = athlete;
    }

    /**
     * Methode für Testzwecke
     */
    protected void resetCache() {
        database.getActivities().getActivity().clear();
        allImported.clear();
        simpleTrainings.clear();
        cache.clear();
        selectedImport = null;
        selectedItems = null;
        selectedProfile = null;
    }

    // @Override
    // public IImported getSelected() {
    // setIfNothingSelectedTheNewestAsSelected();
    // return selectedImport;
    // }

    @Override
    public ISimpleTraining getSelectedOverview() {
        setIfNothingSelectedTheNewestAsSelected();
        if (selectedImport == null) {
            return null;
        } else {
            return createSimpleTraining();
        }
    }

    private ISimpleTraining createSimpleTraining() {
        final ActivityT activity;
        final long key = selectedImport.getActivityId().getTime();
        if (cache.containsKey(key)) {
            activity = cache.get(key);
        } else {
            try {
                activity = loadGpsFile.convertImportedToActivity(selectedImport);
            } catch (final Exception e) {
                LOGGER.error(e.getMessage());
                return null;
            }
        }
        return TrainingOverviewFactory.creatSimpleTraining(activity);
    }

    private void setIfNothingSelectedTheNewestAsSelected() {
        if (selectedImport == null && !database.getActivities().getActivity().isEmpty()) {
            selectedImport = getLatestRun();
        }
    }

    private IImported getLatestRun() {
        final List<ActivityT> activities = database.getActivities().getActivity();
        Collections.sort(activities, new Comparator<ActivityT>() {

            @Override
            public int compare(final ActivityT o1, final ActivityT o2) {
                return o2.getId().compare(o1.getId());
            }
        });
        final Date iImportedId = activities.get(0).getId().toGregorianCalendar().getTime();
        return allImported.get(iImportedId);
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

    /*
     * (non-Javadoc)
     * 
     * @see
     * ch.opentrainingcenter.client.cache.Cache#addListener(ch.opentrainingcenter
     * .client.cache.IRecordListener)
     */
    @Override
    public void addListener(final IRecordListener listener) {
        if (listeners == null) {
            listeners = new ListenerList();
        }
        listeners.add(listener);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * ch.opentrainingcenter.client.cache.Cache#removeListener(ch.opentrainingcenter
     * .client.cache.IRecordListener)
     */
    @Override
    public void removeListener(final IRecordListener listener) {
        if (listeners != null) {
            listeners.remove(listener);
            if (listeners.isEmpty()) {
                listeners = null;
            }
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see ch.opentrainingcenter.client.cache.Cache#getAllSimpleTrainings()
     */
    @Override
    public List<ISimpleTraining> getAllSimpleTrainings() {
        return Collections.unmodifiableList(simpleTrainings);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * ch.opentrainingcenter.client.cache.Cache#setSelection(java.lang.Object[])
     */
    @Override
    public void setSelection(final Object[] selectedItems) {
        this.selectedItems = Arrays.copyOf(selectedItems, selectedItems.length);
    }

    /*
     * (non-Javadoc)
     * 
     * @see ch.opentrainingcenter.client.cache.Cache#getSelection()
     */
    @Override
    public List<?> getSelection() {
        return Collections.unmodifiableList(Arrays.asList(selectedItems));
    }

    /*
     * (non-Javadoc)
     * 
     * @see ch.opentrainingcenter.client.cache.Cache#getSelectedProfile()
     */
    @Override
    public IAthlete getSelectedProfile() {
        return selectedProfile;
    }

    /*
     * (non-Javadoc)
     * 
     * @see ch.opentrainingcenter.client.cache.Cache#setCacheLoaded(boolean)
     */
    @Override
    public void setCacheLoaded(final boolean loaded) {
        cacheLoaded = loaded;
    }

    /*
     * (non-Javadoc)
     * 
     * @see ch.opentrainingcenter.client.cache.Cache#isCacheLoaded()
     */
    @Override
    public boolean isCacheLoaded() {
        return cacheLoaded;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * ch.opentrainingcenter.client.cache.Cache#addAllImported(java.util.List)
     */
    @Override
    public void addAllImported(final List<IImported> records) {
        for (final IImported record : records) {
            final ITraining training = record.getTraining();
            simpleTrainings.add(ModelFactory.createSimpleTraining(training, RunType.getRunType(record.getTrainingType().getId()), training
                    .getNote()));
            allImported.put(record.getActivityId(), record);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see ch.opentrainingcenter.client.cache.Cache#contains(java.util.Date)
     */
    @Override
    public boolean contains(final Date activityId) {
        if (activityId != null) {
            final long key = activityId.getTime();
            return cache.containsKey(key);
        } else {
            return false;
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see ch.opentrainingcenter.client.cache.Cache#toString()
     */
    @Override
    public String toString() {
        final StringBuffer str = new StringBuffer();
        str.append("Cache: Anzahl Elemente: ").append(cache.size()); //$NON-NLS-1$
        return str.toString();
    }

}
