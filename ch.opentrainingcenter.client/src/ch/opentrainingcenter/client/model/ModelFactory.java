package ch.opentrainingcenter.client.model;

import java.util.Date;
import java.util.List;

import ch.opentrainingcenter.client.model.impl.GpsFileModel;
import ch.opentrainingcenter.client.model.impl.GpsFileModelWrapper;
import ch.opentrainingcenter.client.model.impl.SimpleTraining;
import ch.opentrainingcenter.client.model.planing.IPlanungWocheModel;
import ch.opentrainingcenter.client.model.planing.impl.PlanungModel;
import ch.opentrainingcenter.client.model.planing.impl.PlanungWocheModel;
import ch.opentrainingcenter.transfer.IAthlete;
import ch.opentrainingcenter.transfer.ITraining;

public final class ModelFactory {

    private ModelFactory() {

    }

    /**
     * Erstellt ein SimpleTraining mit dem Lauf Typ NONE
     */
    public static ISimpleTraining createSimpleTraining(final ITraining overview) {
        final SimpleTraining training = new SimpleTraining(overview.getLaengeInMeter(), overview.getDauerInSekunden(), overview.getDateOfStart(), overview
                .getAverageHeartBeat(), overview.getMaxHeartBeat(), overview.getMaxSpeed(), RunType.NONE, overview.getNote());
        if (overview.getWeather() != null) {
            training.setWetter(Wetter.getRunType(overview.getWeather().getId()));
        }
        return training;
    }

    public static ISimpleTraining createSimpleTraining(final double distanzInMeter, final double dauerInSekunden, final Date datum, final int avgHeartRate,
            final int maxHeartRate, final double maxSpeed, final RunType type, final String note) {
        return new SimpleTraining(distanzInMeter, dauerInSekunden, datum, avgHeartRate, maxHeartRate, maxSpeed, type, note);
    }

    public static ISimpleTraining createSimpleTraining(final ITraining overview, final RunType runType, final String note) {
        return new SimpleTraining(overview.getLaengeInMeter(), overview.getDauerInSekunden(), overview.getDateOfStart(), overview.getAverageHeartBeat(),
                overview.getMaxHeartBeat(), overview.getMaxSpeed(), runType, note);
    }

    public static IGpsFileModel createGpsFileModel(final String fileName) {
        return new GpsFileModel(fileName);
    }

    public static IGpsFileModelWrapper createGpsFileModelWrapper(final List<IGpsFileModel> fileModels) {
        return new GpsFileModelWrapper(fileModels);
    }

    public static IPlanungWocheModel createPlanungWochenModel(final List<PlanungModel> planungen, final IAthlete athlete, final int jahr, final int kw,
            final int anzahl) {
        return new PlanungWocheModel(planungen, athlete, jahr, kw, anzahl);
    }

    public static PlanungModel createEmptyPlanungModel(final IAthlete athlete, final int jahr, final int kw) {
        return new PlanungModel(athlete, jahr, kw, 0);
    }

    public static PlanungModel createPlanungModel(final IAthlete athlete, final int jahr, final int kw, final int kmProWoche) {
        return createPlanungModel(athlete, jahr, kw, kmProWoche, false);
    }

    public static PlanungModel createPlanungModel(final IAthlete athlete, final int jahr, final int kw, final int kmProWoche, final boolean interval) {
        return new PlanungModel(athlete, jahr, kw, kmProWoche, interval);
    }
}
