package ch.opentrainingcenter.transfer;

import java.util.Set;

public interface IWeather {

    int getId();

    String getWetter();

    Set<ITraining> getTrainings();

}