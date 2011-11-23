package ch.iseli.sportanalyzer.client.cache;

import javax.xml.datatype.XMLGregorianCalendar;

import ch.iseli.sportanalyzer.client.helper.TimeHelper;
import ch.iseli.sportanalyzer.tcx.TrainingCenterDatabaseT;

public class TrainingCenterRecord implements Comparable<TrainingCenterRecord> {

    private final TrainingCenterDatabaseT databaseT;

    private final Integer id;

    private final XMLGregorianCalendar date;

    public TrainingCenterRecord(Integer id, TrainingCenterDatabaseT databaseT) {
        this.id = id;
        this.databaseT = databaseT;
        date = databaseT.getActivities().getActivity().get(0).getId();
    }

    @Override
    public String toString() {
        return TimeHelper.convertGregorianDateToString(getDate());
    }

    public TrainingCenterDatabaseT getTrainingCenterDatabase() {
        return databaseT;
    }

    public Integer getId() {
        return id;
    }

    public XMLGregorianCalendar getDate() {
        return date;
    }

    @Override
    public int compareTo(TrainingCenterRecord o) {
        return getDate().compare(o.getDate());
    }

}
