package ch.iseli.sportanalyzer.client.cache;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.xml.datatype.XMLGregorianCalendar;

import ch.iseli.sportanalyzer.client.helper.TimeHelper;
import ch.iseli.sportanalyzer.tcx.TrainingCenterDatabaseT;
import ch.opentrainingcenter.transfer.IAthlete;

public class TrainingCenterDatabaseTParent {

    private final TrainingCenterDatabaseT databaseT;

    private final List<TrainingCenterDatabaseTChild> childs = new ArrayList<TrainingCenterDatabaseTChild>();

    private final IAthlete athlete;

    private final Integer id;

    public TrainingCenterDatabaseTParent(Integer id, TrainingCenterDatabaseT databaseT, IAthlete athlete) {
        this.id = id;
        this.databaseT = databaseT;
        this.athlete = athlete;
        childs.add(new TrainingCenterDatabaseTChild(this, "Geschwindigkeit", ChildTyp.SPEED));
        childs.add(new TrainingCenterDatabaseTChild(this, "Herz", ChildTyp.CARDIO));
        childs.add(new TrainingCenterDatabaseTChild(this, "Höhe", ChildTyp.LEVEL_ABOUT_SEA));
    }

    @Override
    public String toString() {
        XMLGregorianCalendar date = databaseT.getActivities().getActivity().get(0).getId();
        return TimeHelper.convertGregorianDateToString(date);
    }

    public TrainingCenterDatabaseT getTrainingCenterDatabase() {
        return databaseT;
    }

    public List<TrainingCenterDatabaseTChild> getChilds() {
        return Collections.unmodifiableList(childs);
    }

    public Integer getId() {
        return id;
    }

}
