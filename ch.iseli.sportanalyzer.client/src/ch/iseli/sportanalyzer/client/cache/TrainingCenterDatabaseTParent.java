package ch.iseli.sportanalyzer.client.cache;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.xml.datatype.XMLGregorianCalendar;

import ch.iseli.sportanalyzer.tcx.TrainingCenterDatabaseT;
import ch.opentrainingcenter.transfer.Athlete;

public class TrainingCenterDatabaseTParent {

    private final TrainingCenterDatabaseT databaseT;

    private final List<TrainingCenterDatabaseTChild> childs = new ArrayList<TrainingCenterDatabaseTChild>();

    private final Athlete athlete;

    public TrainingCenterDatabaseTParent(TrainingCenterDatabaseT databaseT, Athlete athlete) {
	this.databaseT = databaseT;
	this.athlete = athlete;
	childs.add(new TrainingCenterDatabaseTChild(this, "Geschwindigkeit", ChildTyp.SPEED));
	childs.add(new TrainingCenterDatabaseTChild(this, "Herz", ChildTyp.CARDIO));
	childs.add(new TrainingCenterDatabaseTChild(this, "Höhe", ChildTyp.LEVEL_ABOUT_SEA));
    }

    @Override
    public String toString() {
	XMLGregorianCalendar date = databaseT.getActivities().getActivity().get(0).getId();
	return date.getYear() + "-" + date.getMonth() + "-" + date.getDay();
    }

    public TrainingCenterDatabaseT getTrainingCenterDatabase() {
	return databaseT;
    }

    public List<TrainingCenterDatabaseTChild> getChilds() {
	return Collections.unmodifiableList(childs);
    }

}
