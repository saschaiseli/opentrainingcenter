package ch.opentrainingcenter.model.training;

import java.util.Date;

import ch.opentrainingcenter.model.strecke.StreckeModel;
import ch.opentrainingcenter.transfer.TrainingType;

public interface ISimpleTraining {

    double getDistanzInMeter();

    double getDauerInSekunden();

    Date getDatum();

    String getZeit();

    int getAvgHeartRate();

    String getFormattedDate();

    String getLaengeInKilometer();

    String getMaxHeartBeat();

    String getPace();

    String getMaxSpeed();

    TrainingType getType();

    void setType(TrainingType type);

    String getNote();

    void setNote(String note);

    Wetter getWetter();

    void setWetter(Wetter type);

    StreckeModel getStrecke();

    void setStrecke(StreckeModel strecke);

    int getUpMeter();

    void setUpMeter(int upMeter);

    int getDownMeter();

    void setDownMeter(int downMeter);
}