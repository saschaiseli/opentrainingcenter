package ch.iseli.sportanalyzer.client.cache;

import javax.xml.datatype.XMLGregorianCalendar;

import ch.iseli.sportanalyzer.client.helper.TimeHelper;
import ch.iseli.sportanalyzer.tcx.TrainingCenterDatabaseT;

public class TrainingCenterRecord {

    private final TrainingCenterDatabaseT databaseT;

    private final Integer id;

    public TrainingCenterRecord(Integer id, TrainingCenterDatabaseT databaseT) {
        this.id = id;
        this.databaseT = databaseT;
    }

    @Override
    public String toString() {
        XMLGregorianCalendar date = databaseT.getActivities().getActivity().get(0).getId();
        return TimeHelper.convertGregorianDateToString(date);
    }

    public TrainingCenterDatabaseT getTrainingCenterDatabase() {
        return databaseT;
    }

    public Integer getId() {
        return id;
    }

}
