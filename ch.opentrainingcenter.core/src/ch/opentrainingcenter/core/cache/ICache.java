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
     * Fügt dem Cache eine Liste von Elementen hinzu. Wenn neue Records
     * hinzukommen, werden die Listener mit einem AddRecord ansonsten mit einem
     * Change notifiziert.
     */
    void addAll(final List<V> values);

    /**
     * Entfernt den Record, falls dieser vorhanden ist und notifiziert alle
     * Listener
     */
    void remove(final K key);

    /**
     * Entfernt die Records, falls diese vorhanden sind und notifiziert alle
     * Listener
     */
    void remove(final List<K> keys);

    boolean contains(final K key);

    void addListener(final IRecordListener<V> listener);

    void removeListener(final IRecordListener<V> listener);
}
