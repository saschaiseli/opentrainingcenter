package ch.iseli.sportanalyzer.client.cache;

import java.util.Collection;

import ch.iseli.sportanalyzer.tcx.TrainingCenterDatabaseT;

public interface IRecordListener {
    public void recordChanged(Collection<TrainingCenterDatabaseT> entry);
}
