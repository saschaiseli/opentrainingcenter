package ch.iseli.sportanalyzer.client.cache;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.xml.datatype.XMLGregorianCalendar;

import ch.iseli.sportanalyzer.tcx.TrainingCenterDatabaseT;
import ch.opentrainingcenter.transfer.IAthlete;

public class TrainingCenterDatabaseTParent {

    private final TrainingCenterDatabaseT databaseT;

    private final List<TrainingCenterDatabaseTChild> childs = new ArrayList<TrainingCenterDatabaseTChild>();

    private final IAthlete athlete;

    public TrainingCenterDatabaseTParent(TrainingCenterDatabaseT databaseT, IAthlete athlete) {
        this.databaseT = databaseT;
        this.athlete = athlete;
        childs.add(new TrainingCenterDatabaseTChild(this, "Geschwindigkeit", ChildTyp.SPEED));
        childs.add(new TrainingCenterDatabaseTChild(this, "Herz", ChildTyp.CARDIO));
        childs.add(new TrainingCenterDatabaseTChild(this, "Höhe", ChildTyp.LEVEL_ABOUT_SEA));
    }

    @Override
    public String toString() {
        XMLGregorianCalendar date = databaseT.getActivities().getActivity().get(0).getId();
        return date.getYear() + "." + addZero(date.getMonth()) + "." + addZero(date.getDay()) + " " + addZero(date.getHour()) + ":" + addZero(date.getMinute());
    }

    private String addZero(int number) {
        if (number < 10) {
            return "0" + number;
        } else {
            return String.valueOf(number);
        }
    }

    public TrainingCenterDatabaseT getTrainingCenterDatabase() {
        return databaseT;
    }

    public List<TrainingCenterDatabaseTChild> getChilds() {
        return Collections.unmodifiableList(childs);
    }
}
