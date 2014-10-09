package ch.opentrainingcenter.core.cache;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.log4j.Logger;
import org.eclipse.core.runtime.ListenerList;

import ch.opentrainingcenter.core.assertions.Assertions;

public abstract class AbstractCache<K, V> implements ICache<K, V> {

    private static final Logger LOG = Logger.getLogger(AbstractCache.class);
    private final Map<K, V> cache = new ConcurrentHashMap<K, V>();
    private ListenerList listeners;

    public abstract K getKey(V value);

    public abstract String getName();

    @Override
    public String toString() {
        return String.format("%s-Cache: Anzahl Elemente: %s", getName(), size()); //$NON-NLS-1$
    }

    @Override
    public void addAll(final List<V> values) {
        Assertions.notNull(values);
        for (final V value : values) {
            LOG.info(String.format("Element added in Cache '%s' Size: %s", getName(), cache.size())); //$NON-NLS-1$
            cache.put(getKey(value), value);
        }
        if (!values.isEmpty()) {
            fireRecordAdded(values);
        }
    }

    @Override
    public V get(final K key) {
        return cache.get(key);
    }

    @Override
    public void remove(final K key) {
        final V value = cache.remove(key);
        final List<V> values = new ArrayList<V>();
        if (value != null) {
            LOG.info(String.format("Element removed --> Cache (%s) Size: %s", value.getClass().getName(), cache.size())); //$NON-NLS-1$
            values.add(value);
        }
        fireRecordDeleted(values);
    }

    @Override
    public void remove(final List<K> keys) {
        for (final K key : keys) {
            remove(key);
        }
    }

    @Override
    public boolean contains(final K key) {
        return cache.containsKey(key);
    }

    public int size() {
        return cache.size();
    }

    public List<V> getAll() {
        return new ArrayList<V>(cache.values());
    }

    public List<V> getSortedElements(final Comparator<V> comparator) {
        final List<V> values = new ArrayList<V>(cache.values());
        Collections.sort(values, comparator);
        return values;
    }

    @Override
    public void notifyListeners() {
        if (listeners == null) {
            return;
        }
        final Object[] rls = listeners.getListeners();
        for (int i = 0; i < rls.length; i++) {
            @SuppressWarnings("unchecked")
            final IRecordListener<V> listener = (IRecordListener<V>) rls[i];
            listener.recordChanged(null);
        }
    }

    @Override
    public void addListener(final IRecordListener<V> listener) {
        if (listeners == null) {
            listeners = new ListenerList();
        }
        listeners.add(listener);
    }

    @Override
    public void removeListener(final IRecordListener<V> listener) {
        if (listeners != null && listener != null) {
            listeners.remove(listener);
            if (listeners.isEmpty()) {
                listeners = null;
            }
        }
    }

    private void fireRecordAdded(final Collection<V> values) {
        if (listeners == null) {
            return;
        }
        final Object[] rls = listeners.getListeners();
        for (int i = 0; i < rls.length; i++) {
            @SuppressWarnings("unchecked")
            final IRecordListener<V> listener = (IRecordListener<V>) rls[i];
            listener.recordChanged(values);
        }

    }

    private void fireRecordDeleted(final List<V> values) {
        if (listeners == null) {
            return;
        }
        final Object[] rls = listeners.getListeners();
        for (int i = 0; i < rls.length; i++) {
            @SuppressWarnings("unchecked")
            final IRecordListener<V> listener = (IRecordListener<V>) rls[i];
            listener.deleteRecord(values);
        }
    }

    public void resetCache() {
        cache.clear();
    }

    public boolean isEmpty() {
        return cache.isEmpty();
    }
}
