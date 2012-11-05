package ch.opentrainingcenter.transfer;

/**
 * Laufplanung für eine Woche eines Athleten. Kilometer in der Woche, Intervall
 * oder nicht. KW und Jahr.
 * 
 */
public interface IPlanungWoche {

    int getId();

    void setId(int id);

    IAthlete getAthlete();

    void setAthlete(IAthlete athlete);

    int getKw();

    void setKw(int kw);

    int getJahr();

    void setJahr(int jahr);

    int getKmProWoche();

    void setKmProWoche(int kmProWoche);

    boolean isInterval();

    void setInterval(boolean interval);

    int getLangerLauf();

    void setLangerLauf(int langerLauf);
}
