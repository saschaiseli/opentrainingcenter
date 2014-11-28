package ch.opentrainingcenter.core.cache;

import java.util.ArrayList;
import java.util.Collection;

public class MockRecordListener<ITraining> implements IRecordListener<ITraining> {

    private final Collection<ITraining> addedEntry = new ArrayList<ITraining>();
    private final Collection<ITraining> changedEntry = new ArrayList<ITraining>();
    private final Collection<ITraining> deletedEntry = new ArrayList<ITraining>();

    @Override
    public void onEvent(final Collection<ITraining> entry, final Event event) {
        switch (event) {
        case ADDED:
            recordAdded(entry);
            break;
        case CHANGED:
            recordChanged(entry);
            break;
        case DELETED:
            deleteRecord(entry);
            break;
        }
    }

    private void recordChanged(final Collection<ITraining> entry) {
        this.changedEntry.clear();
        if (entry != null) {
            this.changedEntry.addAll(entry);
        }
    }

    private void recordAdded(final Collection<ITraining> entry) {
        this.addedEntry.clear();
        if (entry != null) {
            this.addedEntry.addAll(entry);
        }
    }

    private void deleteRecord(final Collection<ITraining> entry) {
        if (entry != null) {
            this.deletedEntry.addAll(entry);
        }
    }

    public Collection<ITraining> getChangedEntry() {
        return changedEntry;
    }

    public Collection<ITraining> getDeletedEntry() {
        return deletedEntry;
    }

    public Collection<ITraining> getAddedEntry() {
        return addedEntry;
    }

}
