package ch.opentrainingcenter.client.action;

import java.util.List;

import ch.opentrainingcenter.core.cache.Cache;
import ch.opentrainingcenter.core.cache.IRecordListener;
import ch.opentrainingcenter.core.helper.RunType;
import ch.opentrainingcenter.transfer.IRoute;
import ch.opentrainingcenter.transfer.ITraining;
import ch.opentrainingcenter.transfer.IWeather;

public class MockCache implements Cache {

    private List<ITraining> changedRecords;
    private RunType type;
    private List<Long> deletedIds;

    @Override
    public void addAll(final List<ITraining> activities) {
        // do nothing
    }

    @Override
    public void remove(final List<Long> ids) {
        this.deletedIds = ids;
    }

    @Override
    public void notifyListeners() {
        // do nothing
    }

    @Override
    public void addListener(final IRecordListener<ITraining> listener) {
        // do nothing
    }

    @Override
    public void removeListener(final IRecordListener<ITraining> listener) {
        // do nothing
    }

    @Override
    public boolean contains(final Long datum) {

        return false;
    }

    @Override
    public ITraining get(final Long activityId) {

        return null;
    }

    public List<ITraining> getChangedRecords() {
        return changedRecords;
    }

    public RunType getType() {
        return type;
    }

    public List<Long> getDeletedIds() {
        return deletedIds;
    }

    @Override
    public void update(final Long activityId, final String note, final IWeather weather, final IRoute route) {
        // do nothing
    }

    @Override
    public void remove(final Long key) {
        // do nothing
    }

    @Override
    public void resetCache() {
        // do nothing
    }
}
