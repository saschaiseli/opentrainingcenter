package ch.opentrainingcenter.transfer;

/**
 * Definition einer Route.
 */
public interface IRoute {

    int getId();

    void setId(int id);

    void setName(String name);

    String getName();

    String getBeschreibung();

    void setBeschreibung(String beschreibung);

    void setAthlete(IAthlete athlete);

    IAthlete getAthlete();

    /**
     * Nur die initiale Route hat kein Referenztrack. Alle anderen haben IMMER
     * eine Referenz.
     */
    ITraining getReferenzTrack();

    /**
     * Nur die initiale Route hat kein Referenztrack. Alle anderen haben IMMER
     * eine Referenz.
     */
    void setReferenzTrack(final ITraining referenzTrack);
}
