package ch.opentrainingcenter.model.planing;


public interface IPlanungWocheModel extends Iterable<IPlanungModel> {

    /**
     * @param woche
     *            legt die planungwoche ab. der eindeutige key der planung woche
     *            ist fachlich die kalenderwoche. Einträge mit der selben KW
     *            werden überschrieben.
     */
    void addOrUpdate(final IPlanungModel woche);

    IPlanungModel getPlanung(int jahr, int kw);

    /**
     * Anzahl Planungen.
     */
    int size();
}