package ch.opentrainingcenter.core.cache;

import java.util.Collection;

public interface IRecordListener<V> {

    /**
     * Neuer record zum cache oder weg vom cache gekommen
     */
    void recordChanged(Collection<V> entry);

    /**
     * Ein record wird aus dem Cache gelöscht. Wird verwendet um die
     * entsprechenden Views zu schliessen.
     */
    void deleteRecord(Collection<V> entry);

}
