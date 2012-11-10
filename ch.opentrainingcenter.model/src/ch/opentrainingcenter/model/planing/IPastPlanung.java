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
     * @return true wenn die geplanten Ziele übertroffen wurden.
     */
    boolean isSuccess();
}
