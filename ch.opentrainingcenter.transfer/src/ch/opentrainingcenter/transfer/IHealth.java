package ch.opentrainingcenter.transfer;

import java.util.Date;

public interface IHealth {

    int getId();

    void setId(int id);

    IAthlete getAthlete();

    void setAthlete(IAthlete athlete);

    Double getWeight();

    void setWeight(Double weight);

    Integer getCardio();

    void setCardio(Integer cardio);

    Date getDateofmeasure();

    void setDateofmeasure(Date dateofmeasure);

}