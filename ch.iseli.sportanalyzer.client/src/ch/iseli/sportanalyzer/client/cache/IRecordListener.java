package ch.iseli.sportanalyzer.client.cache;

import java.util.Collection;

public interface IRecordListener {

    /**
     * Neuer record zum cache oder weg vom cache gekommen
     */
    public void recordChanged(Collection<TrainingCenterRecord> entry);

}
