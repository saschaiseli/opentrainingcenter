package ch.opentrainingcenter.client.action;

import java.util.Date;
import java.util.List;

import ch.opentrainingcenter.core.cache.Cache;
import ch.opentrainingcenter.core.cache.IRecordListener;
import ch.opentrainingcenter.core.helper.RunType;
import ch.opentrainingcenter.tcx.ActivityT;
import ch.opentrainingcenter.transfer.ActivityExtension;
import ch.opentrainingcenter.transfer.IImported;

public class MockCache implements Cache {

    private List<IImported> changedRecords;
    private RunType type;
    private List<Date> deletedIds;

    @Override
    public void add(final ActivityT activity) {

    }

    @Override
    public void addAll(final List<ActivityT> activities) {

    }

    @Override
    public void remove(final List<Date> ids) {
        this.deletedIds = ids;
    }

    @Override
    public void notifyListeners() {

    }

    @Override
    public void addListener(final IRecordListener<ActivityT> listener) {

    }

    @Override
    public void removeListener(final IRecordListener<ActivityT> listener) {

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

    @Override
    public void remove(final Date key) {
        // TODO Auto-generated method stub

    }

    @Override
    public void resetCache() {
        // TODO Auto-generated method stub

    }
}
