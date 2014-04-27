package ch.opentrainingcenter.core.cache;

import java.util.List;

/**
 * Cache
 * 
 * 
 * @param <K>
 *            Key mit dem der Wert abgelegt wird.
 * @param <V>
 *            Werte die gespeichert werden.
 */
public interface ICache<K, V> {

    /**
     * Holt ein Element aus dem Cache. Wenn das Element mit dem Key nicht
     * gefunden wird, wird null zurückgegeben.
     */
    V get(final K key);

    /**
     * Fügt dem Cache eine Liste von neuen Elementen hinzu.
     */
    void addAll(final List<V> values);

    /**
     * Informiert alle registrierten Listeners
     */
    void notifyListeners();

    void remove(final K key);

    void remove(final List<K> keys);

    boolean contains(final K key);

    void addListener(final IRecordListener<V> listener);

    void removeListener(final IRecordListener<V> listener);
}
