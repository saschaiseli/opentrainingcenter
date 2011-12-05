package ch.opentrainingcenter.transfer;

import java.util.Date;

public interface IImported {

    int getId();

    void setId(int id);

    IWeather getWeather();

    void setWeather(IWeather weather);

    IAthlete getAthlete();

    void setAthlete(IAthlete athlete);

    Date getImportedDate();

    void setImportedDate(Date importedDate);

    String getComments();

    void setComments(String comments);

}