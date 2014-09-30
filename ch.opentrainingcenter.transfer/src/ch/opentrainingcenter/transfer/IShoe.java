package ch.opentrainingcenter.transfer;

import java.util.Date;

public interface IShoe {

    void setId(int id);

    int getId();

    void setSchuhname(String schuhname);

    String getSchuhname();

    void setImageicon(String imageicon);

    String getImageicon();

    IAthlete getAthlete();

    void setAthlete(IAthlete athlete);

    int getPreis();

    void setPreis(int preis);

    Date getKaufdatum();

    void setKaufdatum(Date kaufdatum);

}
