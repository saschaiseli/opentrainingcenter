package ch.opentrainingcenter.client.model.planing;

import ch.opentrainingcenter.transfer.IPlanungWoche;

public interface IPlanungWocheModel extends Iterable<IPlanungWoche> {

    /**
     * @param woche
     *            legt die planungwoche ab. der eindeutige key der planung woche
     *            ist fachlich die kalenderwoche. Einträge mit der selben KW
     *            werden überschrieben.
     */
    void addOrUpdate(final IPlanungWoche woche);

    IPlanungWoche getPlanung(int jahr, int kw);

    /**
     * Anzahl Planungen.
     */
    int size();

}