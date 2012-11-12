package ch.opentrainingcenter.model.planing;

import ch.opentrainingcenter.transfer.IPlanungWoche;

/**
 * Model welches die Planung mit den effektiven Werten vergleicht.
 * 
 */
public interface IPastPlanung {

    /**
     * @return die Planung für eine Kalender Woche.
     */
    IPlanungWoche getPlanung();

    /**
     * @return die effektiven KM
     */
    int getKmEffective();

    /**
     * @return den längsten lauf
     */
    int getLangerLaufEffective();

    /**
     * @return true, wenn in dieser woche ein intensives oder extensives
     *         intervall gemacht wurde.
     */
    boolean hasInterval();

    /**
     * Wenn die angegebenen Km / Woche kleiner gleich 0 sind wird angenommen,
     * dass keine planung vorhanden ist. Wenn eine Planung vorhanden ist, wird
     * geschaut ob die km/woche + der längste lauf grösser gleich dem Ziel sind.
     * Intervall muss auch gemacht sein, wenn dies geplant ist. ungeplantes
     * Intervall ist auch ok.
     */
    PlanungStatus isSuccess();
}
