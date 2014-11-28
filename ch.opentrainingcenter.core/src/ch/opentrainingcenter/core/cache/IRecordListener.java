package ch.opentrainingcenter.core.cache;

import java.util.Collection;

public interface IRecordListener<V> {

    /**
     * Neuer Records hinzugefuegt
     */
    void recordAdded(Collection<V> entry);

    /**
     * Records aktualisiert.
     */
    void recordChanged(Collection<V> entry);

    /**
     * Records gelöscht.
     */
    void deleteRecord(Collection<V> entry);

    // void onEvent(Collection<V> entry, Event event);

}
