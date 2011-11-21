package ch.iseli.sportanalyzer.client.cache;

import java.util.Collection;

public interface IRecordListener {
    public void recordChanged(Collection<TrainingCenterRecord> entry);
}
