package ch.opentrainingcenter.client.cache;

import java.util.ArrayList;
import java.util.Collection;

import ch.opentrainingcenter.core.cache.IRecordListener;

public class MockRecordListener<ActivityT> implements IRecordListener<ActivityT> {

    private final Collection<ActivityT> changedEntry = new ArrayList<ActivityT>();
    private final Collection<ActivityT> deletedEntry = new ArrayList<ActivityT>();

    @Override
    public void recordChanged(final Collection<ActivityT> entry) {
        this.changedEntry.clear();
        if (entry != null) {
            this.changedEntry.addAll(entry);
        }
    }

    @Override
    public void deleteRecord(final Collection<ActivityT> entry) {
        this.deletedEntry.clear();
        if (entry != null) {
            this.deletedEntry.addAll(entry);
        }
    }

    public Collection<ActivityT> getChangedEntry() {
        return changedEntry;
    }

    public Collection<ActivityT> getDeletedEntry() {
        return deletedEntry;
    }

}
