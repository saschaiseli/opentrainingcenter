package ch.opentrainingcenter.client.cache;

import java.util.Collection;

import ch.opentrainingcenter.tcx.ActivityT;

public class MockRecordListener implements IRecordListener {

    private Collection<ActivityT> changedEntry;
    private Collection<ActivityT> deletedEntry;

    @Override
    public void recordChanged(final Collection<ActivityT> entry) {
        this.changedEntry = entry;

    }

    @Override
    public void deleteRecord(final Collection<ActivityT> entry) {
        this.deletedEntry = entry;
    }

    public Collection<ActivityT> getChangedEntry() {
        return changedEntry;
    }

    public Collection<ActivityT> getDeletedEntry() {
        return deletedEntry;
    }

}
