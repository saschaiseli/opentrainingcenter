package ch.opentrainingcenter.transfer;

import java.util.Date;
import java.util.Set;

public interface IAthlete {

    int getId();

    void setId(int id);

    String getName();

    void setName(String name);

    Set<IHealth> getHealths();

    void setHealths(Set<IHealth> healths);

    Set<ITraining> getTrainings();

    void setTrainings(Set<ITraining> trainings);

    void addTraining(ITraining record);

    Date getBirthday();

    void setBirthday(Date birthday);

    Integer getMaxHeartRate();

    void setMaxHeartRate(Integer maxHeartRate);

}