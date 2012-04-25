package ch.opentrainingcenter.client.cache;

import java.util.Collection;

import ch.opentrainingcenter.tcx.ActivityT;

public interface IRecordListener {

    /**
     * Neuer record zum cache oder weg vom cache gekommen
     */
     void recordChanged(Collection<ActivityT> entry);

    /**
     * Ein record wird aus dem Cache gelöscht. Wird verwendet um die entsprechenden Views zu schliessen.
     */
     void deleteRecord(Collection<ActivityT> entry);

}
