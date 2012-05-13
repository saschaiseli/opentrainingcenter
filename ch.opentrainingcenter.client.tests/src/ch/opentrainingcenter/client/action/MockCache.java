package ch.opentrainingcenter.client.action;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import ch.opentrainingcenter.client.cache.Cache;
import ch.opentrainingcenter.client.cache.IRecordListener;
import ch.opentrainingcenter.client.model.ISimpleTraining;
import ch.opentrainingcenter.client.model.RunType;
import ch.opentrainingcenter.tcx.ActivityT;
import ch.opentrainingcenter.transfer.IAthlete;
import ch.opentrainingcenter.transfer.IImported;

public class MockCache implements Cache {

    private List<IImported> changedRecords;
    private RunType type;
    private Object[] selectedItems;
    private List<Date> deletedIds;

    @Override
    public void add(final ActivityT activity) {

    }

    @Override
    public void addAll(final List<ActivityT> activities) {

    }

    @Override
    public Collection<IImported> getAllImportedRecords() {

        return null;
    }

    @Override
    public void setSelectedRun(final IImported selected) {

    }

    @Override
    public void setSelectedProfile(final IAthlete athlete) {

    }

    @Override
    public IImported getSelected() {

        return null;
    }

    @Override
    public ISimpleTraining getSelectedOverview() {

        return null;
    }

    @Override
    public void remove(final List<Date> deletedIds) {
        this.deletedIds = deletedIds;
    }

    @Override
    public void changeType(final List<IImported> changedRecords, final RunType type) {
        this.changedRecords = changedRecords;
        this.type = type;
    }

    @Override
    public void update() {

    }

    @Override
    public void addListener(final IRecordListener listener) {

    }

    @Override
    public void removeListener(final IRecordListener listener) {

    }

    @Override
    public List<ISimpleTraining> getAllSimpleTrainings() {

        return null;
    }

    @Override
    public void setSelection(final Object[] selectedItems) {
        this.selectedItems = selectedItems;
    }

    @Override
    public List<?> getSelection() {
        return Collections.unmodifiableList(Arrays.asList(selectedItems));
    }

    @Override
    public IAthlete getSelectedProfile() {

        return null;
    }

    @Override
    public void setCacheLoaded(final boolean loaded) {

    }

    @Override
    public boolean isCacheLoaded() {

        return false;
    }

    @Override
    public void addAllImported(final List<IImported> records) {

    }

    @Override
    public boolean contains(final Date activityId) {

        return false;
    }

    @Override
    public ActivityT get(final Date activityId) {

        return null;
    }

    public List<IImported> getChangedRecords() {
        return changedRecords;
    }

    public RunType getType() {
        return type;
    }

    public List<Date> getDeletedIds() {
        return deletedIds;
    }

    @Override
    public void update(final IImported record) {

    }
}
