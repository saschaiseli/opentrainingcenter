package ch.opentrainingcenter.model.training;

import ch.opentrainingcenter.transfer.Sport;

/**
 * Model fuer die Uebersichtsview (Woche/monat)
 * 
 */
public interface IOverviewModel {

    int getAnzahlTrainings(Sport sport);

    double getTotaleDistanzInMeter(Sport sport);

    long getTotaleZeitInSekunden(Sport sport);
}
