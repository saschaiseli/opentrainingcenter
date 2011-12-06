package ch.iseli.sportanalyzer.client.cache;

import java.util.Comparator;

public class TrainingCenterRecordComparator implements Comparator<TrainingCenterRecord> {

    @Override
    public int compare(final TrainingCenterRecord o1, final TrainingCenterRecord o2) {
        return o2.getDate().compare(o1.getDate());
    }

}
