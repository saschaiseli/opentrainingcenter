package ch.opentrainingcenter.transfer;

public interface IShoe {

    void setId(int id);

    int getId();

    void setSchuhname(String schuhname);

    String getSchuhname();

    void setImageicon(String imageicon);

    String getImageicon();

    IAthlete getAthlete();

    void setAthlete(IAthlete athlete);

}
