package ch.opentrainingcenter.model.training.internal;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ch.opentrainingcenter.model.training.IOverviewModel;
import ch.opentrainingcenter.transfer.ITraining;
import ch.opentrainingcenter.transfer.Sport;

public class OverviewModel implements IOverviewModel {

    private final Map<Sport, SportOverviewModel> model = new HashMap<>();

    private class SportOverviewModel {
        private int anzahl = 0;
        private double meter = 0;
        private long zeit = 0;
    }

    public OverviewModel(final List<ITraining> trainings) {
        for (final Sport sport : Sport.values()) {
            model.put(sport, new SportOverviewModel());
        }
        for (final ITraining training : trainings) {
            final SportOverviewModel current = model.get(training.getSport());
            current.anzahl++;
            current.meter += training.getLaengeInMeter();
            current.zeit += training.getDauer();
        }
    }

    @Override
    public int getAnzahlTrainings(final Sport sport) {
        return model.get(sport).anzahl;
    }

    @Override
    public double getTotaleDistanzInMeter(final Sport sport) {
        return model.get(sport).meter;
    }

    @Override
    public long getTotaleZeitInSekunden(final Sport sport) {
        return model.get(sport).zeit;
    }

}
