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

    ITraining getTraining();

    void setTraining(final ITraining training);

    ITraining getReferenzTrack();

    void setReferenzTrack(final ITraining referenzTrack);
}
