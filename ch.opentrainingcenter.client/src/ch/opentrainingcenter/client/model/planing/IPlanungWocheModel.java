package ch.opentrainingcenter.client.model.planing;

import ch.opentrainingcenter.client.model.planing.impl.PlanungModel;

public interface IPlanungWocheModel extends Iterable<PlanungModel> {

    /**
     * @param woche
     *            legt die planungwoche ab. der eindeutige key der planung woche
     *            ist fachlich die kalenderwoche. Einträge mit der selben KW
     *            werden überschrieben.
     */
    void addOrUpdate(final PlanungModel woche);

    PlanungModel getPlanung(int jahr, int kw);

    /**
     * Anzahl Planungen.
     */
    int size();
}