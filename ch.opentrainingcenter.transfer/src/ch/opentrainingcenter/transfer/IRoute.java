package ch.opentrainingcenter.transfer;

/**
 * Definition einer Route.
 */
public interface IRoute {

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

    int getId();

    void setId(int id);
}
