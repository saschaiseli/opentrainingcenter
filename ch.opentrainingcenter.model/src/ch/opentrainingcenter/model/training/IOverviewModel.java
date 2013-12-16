package ch.opentrainingcenter.model.training;

/**
 * Model fuer die Uebersichtsview (Woche/monat)
 * 
 */
public interface IOverviewModel {

    int getAnzahlTrainings();

    double getTotaleDistanzInMeter();

    long getTotaleZeitInSekunden();
}
