package ch.opentrainingcenter.core.cache;

import java.util.Collection;

public interface IRecordListener<V> {

    /**
     * Wird vom Cache aufgerufen, wenn sich etwas geaendert hat.
     * 
     * @param entry
     *            {@link Collection} mit Aenderungen.
     * @param event
     *            Art der Aenderung.
     * @see Event
     */
    void onEvent(Collection<V> entry, Event event);

}
