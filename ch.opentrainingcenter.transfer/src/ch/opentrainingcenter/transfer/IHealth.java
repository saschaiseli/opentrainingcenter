package ch.opentrainingcenter.transfer;

import java.util.Date;


public interface IHealth {

    public abstract int getId();

    public abstract void setId(int id);

    public abstract IAthlete getAthlete();

    public abstract void setAthlete(IAthlete athlete);

    public abstract Integer getWeight();

    public abstract void setWeight(Integer weight);

    public abstract Integer getCardio();

    public abstract void setCardio(Integer cardio);

    public abstract Date getDateofmeasure();

    public abstract void setDateofmeasure(Date dateofmeasure);

}