package ch.opentrainingcenter.transfer;

import java.util.Date;


public interface IImported {

    public abstract int getId();

    public abstract void setId(int id);

    public abstract IWeather getWeather();

    public abstract void setWeather(IWeather weather);

    public abstract IAthlete getAthlete();

    public abstract void setAthlete(IAthlete athlete);

    public abstract Date getImportedDate();

    public abstract void setImportedDate(Date importedDate);

    public abstract String getComments();

    public abstract void setComments(String comments);

}