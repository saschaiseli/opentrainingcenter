package ch.opentrainingcenter.client.cache;

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

import ch.opentrainingcenter.client.model.ISimpleTraining;
import ch.opentrainingcenter.client.model.ModelFactory;
import ch.opentrainingcenter.client.model.RunType;
import ch.opentrainingcenter.client.model.TrainingOverviewFactory;
import ch.opentrainingcenter.db.DatabaseAccessFactory;
import ch.opentrainingcenter.importer.IGpsFileLoader;
import ch.opentrainingcenter.importer.impl.GpsFileLoader;
import ch.opentrainingcenter.tcx.ActivityListT;
import ch.opentrainingcenter.tcx.ActivityT;
import ch.opentrainingcenter.tcx.TrainingCenterDatabaseT;
import ch.opentrainingcenter.transfer.IAthlete;
import ch.opentrainingcenter.transfer.IImported;

public class TrainingCenterDataCache {

    private ListenerList listeners;

    private IAthlete selectedProfile;

    private static TrainingCenterDataCache INSTANCE = null;

    private IImported selectedImport;

    private Object[] selectedItems;

    private boolean cacheLoaded;

    private final TrainingCenterDatabaseT database;

    private final Map<Date, IImported> allImported = new TreeMap<Date, IImported>(new ImportedComparator());

    private final List<ISimpleTraining> simpleTrainings = new ArrayList<ISimpleTraining>();

    private final Map<Long, ActivityT> cache = new HashMap<Long, ActivityT>();

    private final IGpsFileLoader loadGpsFile;

    public static final Logger logger = Logger.getLogger(TrainingCenterDataCache.class);

    private TrainingCenterDataCache() {
        this(new GpsFileLoader());
    }

    private TrainingCenterDataCache(final IGpsFileLoader loadGpsFile) {
        database = new TrainingCenterDatabaseT();
        final ActivityListT activityList = new ActivityListT();
        database.setActivities(activityList);
        this.loadGpsFile = loadGpsFile;
    }

    public static TrainingCenterDataCache getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new TrainingCenterDataCache();
        }
        return INSTANCE;
    }

    protected static TrainingCenterDataCache getInstance(final IGpsFileLoader loadGpsFile) {
        if (INSTANCE == null) {
            INSTANCE = new TrainingCenterDataCache(loadGpsFile);
        }
        return INSTANCE;
    }

    /**
     * Methode um importierte Runs im cache abzulegen.
     * 
     * @param activities
     *            eine Liste von Aktivitäten.
     */
    public void add(final ActivityT activity) {
        final List<ActivityT> tmp = new ArrayList<ActivityT>();
        tmp.add(activity);
        addAll(tmp);
    }

    /**
     * Methode um importierte Runs im cache abzulegen.
     * 
     * @param activities
     *            eine Liste von Aktivitäten.
     */
    public void addAll(final List<ActivityT> activities) {
        database.getActivities().getActivity().addAll(activities);
        for (final ActivityT activity : activities) {
            simpleTrainings.add(TrainingOverviewFactory.creatSimpleTraining(activity));
            final Date key = activity.getId().toGregorianCalendar().getTime();
            cache.put(key.getTime(), activity);

            final IImported imported = DatabaseAccessFactory.getDatabaseAccess().getImportedRecord(key);
            allImported.put(key, imported);
        }
        fireRecordAdded(null);
    }

    /**
     * @return eine nach Datum sortierte Liste von {@link ActivityT}
     */
    public Collection<IImported> getAllActivities() {
        return allImported.values();
    }

    public void setSelectedRun(final IImported selected) {
        selectedImport = selected;
    }

    public void setSelectedProfile(final IAthlete athlete) {
        this.selectedProfile = athlete;
        resetCache();
    }

    private void resetCache() {
        database.getActivities().getActivity().clear();
        selectedImport = null;
        selectedItems = null;
    }

    /**
     * Ist noch keiner selektiert, wird der neuste, also derjenige mit dem jüngsten datum, zurückgegeben. Dieser wird dann auch als selektierten Lauf gesetzt.
     * 
     * @return den selektierten record oder den neusten record.
     */
    public IImported getSelected() {
        setIfNothingSelectedTheNewestAsSelected();
        return selectedImport;
    }

    /**
     * @return eine Übersicht auf das selektierte Training.
     */
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
                activity = loadGpsFile.convertActivity(selectedImport);
            } catch (final Exception e) {
                logger.error(e.getMessage());
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

    public void update(final List<IImported> changedType, final RunType type) {
        for (final IImported record : changedType) {
            final IImported imp = allImported.get(record.getActivityId());
            for (final ISimpleTraining st : simpleTrainings) {
                if (st.getDatum().equals(imp.getActivityId())) {
                    st.setType(type);
                }
            }
        }

    }

    public void update() {
        final Object[] rls = listeners.getListeners();
        for (int i = 0; i < rls.length; i++) {
            final IRecordListener listener = (IRecordListener) rls[i];
            listener.recordChanged(null);
        }
    }

    private void fireRecordAdded(final Collection<ActivityT> activitiesAdded) {
        if (listeners == null)
            return;
        final Object[] rls = listeners.getListeners();
        for (int i = 0; i < rls.length; i++) {
            final IRecordListener listener = (IRecordListener) rls[i];
            listener.recordChanged(activitiesAdded);
        }

    }

    private void fireRecordDeleted(final List<ActivityT> deletedActivities) {
        if (listeners == null)
            return;
        final Object[] rls = listeners.getListeners();
        for (int i = 0; i < rls.length; i++) {
            final IRecordListener listener = (IRecordListener) rls[i];
            listener.deleteRecord(deletedActivities);
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

    public List<ISimpleTraining> getAllSimpleTrainings() {
        return Collections.unmodifiableList(simpleTrainings);
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

    public void addAllImported(final List<IImported> records) {
        for (final IImported record : records) {
            simpleTrainings.add(ModelFactory.createSimpleTraining(record.getTraining(), RunType.getRunType(record.getTrainingType().getId())));
            allImported.put(record.getActivityId(), record);
        }
    }

    public boolean contains(final Date activityId) {
        final long key = activityId.getTime();
        return cache.containsKey(key);
    }

    public ActivityT get(final Date activityId) {
        final long key = activityId.getTime();
        return cache.get(key);
    }

}
