package ch.opentrainingcenter.transfer;

import java.util.Set;

/**
 * Definition einer Route.
 */
public interface IRoute {

    int getId();

    void setId(int id);

    IAthlete getAthlete();

    void setAthlete(IAthlete athlete);

    void setName(String name);

    /**
     * @return den eindeutigen namen
     */
    String getName();

    /**
     * @return beschreibung der route. Also wo durch, steigungen, oder was auch
     *         immer.
     */
    String getBeschreibung();

    void setBeschreibung(String beschreibung);

    Set<IImported> getImporteds();

}
