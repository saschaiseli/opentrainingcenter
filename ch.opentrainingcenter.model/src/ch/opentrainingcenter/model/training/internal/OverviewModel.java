package ch.opentrainingcenter.model.training.internal;

import java.util.List;

import ch.opentrainingcenter.model.training.IOverviewModel;
import ch.opentrainingcenter.transfer.ITraining;

public class OverviewModel implements IOverviewModel {

    private final int anzahl;
    private double meter;
    private long zeit;

    public OverviewModel(final List<ITraining> trainings) {
        anzahl = trainings.size();
        for (final ITraining training : trainings) {
            meter += training.getLaengeInMeter();
            zeit += training.getDauer();
        }
    }

    @Override
    public int getAnzahlTrainings() {
        return anzahl;
    }

    @Override
    public double getTotaleDistanzInMeter() {
        return meter;
    }

    @Override
    public long getTotaleZeitInSekunden() {
        return zeit;
    }

}
