package ch.opentrainingcenter.client.cache.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import org.eclipse.core.runtime.ListenerList;

import ch.opentrainingcenter.core.cache.ICache;
import ch.opentrainingcenter.core.cache.IRecordListener;

public abstract class AbstractCache<K, V> implements ICache<K, V> {

    private final ConcurrentHashMap<K, V> cache = new ConcurrentHashMap<K, V>();

    protected ListenerList listeners;

    public abstract K getKey(V value);

    @Override
    public void addAll(final List<V> values) {
        for (final V v : values) {
            add(v);
        }
    }

    @Override
    public void add(final V value) {
        cache.put(getKey(value), value);
        final List<V> v = new ArrayList<V>();
        v.add(value);
        fireRecordAdded(v);
    }

    @Override
    public V get(final K key) {
        return cache.get(key);
    }

    @Override
    public void remove(final K key) {
        final V value = cache.remove(key);
        final List<V> values = new ArrayList<V>();
        values.add(value);
        if (!values.isEmpty()) {
            fireRecordDeleted(values);
        }
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
        if (listeners != null) {
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
}
