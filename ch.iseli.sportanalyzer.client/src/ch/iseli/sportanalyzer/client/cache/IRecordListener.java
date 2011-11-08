package ch.iseli.sportanalyzer.client.cache;

import java.util.List;

import ch.iseli.sportanalyzer.tcx.TrainingCenterDatabaseT;

public interface IRecordListener {
    public void recordChanged(List<TrainingCenterDatabaseT> entry);
}
