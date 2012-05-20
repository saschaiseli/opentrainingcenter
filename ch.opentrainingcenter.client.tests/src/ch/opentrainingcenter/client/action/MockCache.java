package ch.opentrainingcenter.client.action;

import java.util.Date;
import java.util.List;

import ch.opentrainingcenter.client.cache.Cache;
import ch.opentrainingcenter.client.cache.IRecordListener;
import ch.opentrainingcenter.client.model.RunType;
import ch.opentrainingcenter.tcx.ActivityT;
import ch.opentrainingcenter.transfer.ActivityExtension;
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
    public void remove(final List<Date> deletedIds) {
        this.deletedIds = deletedIds;
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
    public void updateExtension(final Date activityId, final ActivityExtension extension) {
    }
}
