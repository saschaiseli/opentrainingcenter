package ch.opentrainingcenter.transfer;

/**
 * Laufplanung für eine Woche
 * 
 */
public interface IPlanungWoche {

    int getId();

    void setId(int id);

    IAthlete getAthlete();

    void setAthlete(IAthlete athlete);

    int getKalenderWoche();

    void setKalenderWoche(int kw);

    int getJahr();

    void setJahr(int jahr);

    boolean isActive();

    void setActive(boolean active);

    int getTargetKilometer();

    void setTargetKilometer(int kmProWoche);
}
