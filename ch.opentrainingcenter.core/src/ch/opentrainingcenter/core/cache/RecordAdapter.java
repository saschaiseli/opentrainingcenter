package ch.opentrainingcenter.core.cache;

import java.util.Collection;

public abstract class RecordAdapter<V> implements IRecordListener<V> {

    @Override
    public void recordAdded(final java.util.Collection<V> entry) {

    };

    @Override
    public void deleteRecord(final Collection<V> entry) {
        // do nothing
    }

    @Override
    public void recordChanged(final Collection<V> entry) {
        // do nothing
    }
}
