package ch.iseli.sportanalyzer.client.cache;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import org.eclipse.core.runtime.ListenerList;

import ch.iseli.sportanalyzer.client.model.ISimpleTraining;
import ch.iseli.sportanalyzer.client.model.TrainingOverviewFactory;
import ch.iseli.sportanalyzer.tcx.ActivityListT;
import ch.iseli.sportanalyzer.tcx.ActivityT;
import ch.iseli.sportanalyzer.tcx.TrainingCenterDatabaseT;
import ch.opentrainingcenter.transfer.IAthlete;

public class TrainingCenterDataCache {

    private ListenerList listeners;

    private IAthlete selectedProfile;

    private static TrainingCenterDataCache INSTANCE = null;

    private ActivityT selectedActivity;

    private Object[] selectedItems;

    private boolean cacheLoaded;

    private final TrainingCenterDatabaseT database;

    private TrainingCenterDataCache() {
        database = new TrainingCenterDatabaseT();
        final ActivityListT activityList = new ActivityListT();
        database.setActivities(activityList);
    }

    public static TrainingCenterDataCache getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new TrainingCenterDataCache();
        }
        return INSTANCE;
    }

    /**
     * @param activities
     *            eine Liste von Aktivitäten.
     */
    public void addAll(final List<ActivityT> activities) {
        database.getActivities().getActivity().addAll(activities);
        fireRecordAdded(null);
    }

    /**
     * @return eine nach Datum sortierte Liste von {@link ActivityT}
     */
    public Collection<ActivityT> getAllActivities() {
        final List<ActivityT> activities = database.getActivities().getActivity();
        Collections.sort(activities, new Comparator<ActivityT>() {

            @Override
            public int compare(final ActivityT o1, final ActivityT o2) {
                return o2.getId().compare(o1.getId());
            }
        });
        return activities;
    }

    public void setSelectedRun(final ActivityT selected) {
        selectedActivity = selected;
    }

    public void setSelectedProfile(final IAthlete athlete) {
        this.selectedProfile = athlete;
        resetCache();
    }

    private void resetCache() {
        database.getActivities().getActivity().clear();
        selectedActivity = null;
        selectedItems = null;
    }

    /**
     * Ist noch keiner selektiert, wird der neuste, also derjenige mit dem jüngsten datum, zurückgegeben. Dieser wird dann auch als selektierten Lauf gesetzt.
     * 
     * @return den selektierten record oder den neusten record.
     */
    public ActivityT getSelected() {
        setIfNothingSelectedTheNewestAsSelected();
        return selectedActivity;
    }

    /**
     * @return eine Übersicht auf das selektierte Training.
     */
    public ISimpleTraining getSelectedOverview() {
        setIfNothingSelectedTheNewestAsSelected();
        return TrainingOverviewFactory.creatSimpleTraining(selectedActivity);
    }

    private void setIfNothingSelectedTheNewestAsSelected() {
        if (selectedActivity == null && !database.getActivities().getActivity().isEmpty()) {
            final ActivityT newest = getLatestRun();
            selectedActivity = newest;
        }
    }

    private ActivityT getLatestRun() {
        final List<ActivityT> activities = database.getActivities().getActivity();
        Collections.sort(activities, new Comparator<ActivityT>() {

            @Override
            public int compare(final ActivityT o1, final ActivityT o2) {
                return o2.getId().compare(o1.getId());
            }
        });
        return activities.get(0);
    }

    public void remove(final List<Date> deletedIds) {
        final List<ActivityT> activities = database.getActivities().getActivity();
        final List<ActivityT> activitiesToDelete = new ArrayList<ActivityT>();
        for (final ActivityT activity : activities) {
            if (deletedIds.contains(activity.getId().toGregorianCalendar().getTime())) {
                activitiesToDelete.add(activity);
            }
        }
        database.getActivities().getActivity().removeAll(activitiesToDelete);
        fireRecordDeleted(activitiesToDelete);
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
        final List<ISimpleTraining> result = new ArrayList<ISimpleTraining>();
        for (final ActivityT activity : database.getActivities().getActivity()) {
            result.add(TrainingOverviewFactory.creatSimpleTraining(activity));
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
}
