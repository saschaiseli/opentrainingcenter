package ch.opentrainingcenter.transfer;

import java.util.Set;

public interface IWeather {

    public abstract int getId();

    public abstract String getWetter();

    public abstract Set<ITraining> getTrainings();

}