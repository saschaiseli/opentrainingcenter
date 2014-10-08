package ch.opentrainingcenter.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import ch.opentrainingcenter.model.importer.IGpsFileModel;
import ch.opentrainingcenter.model.importer.IGpsFileModelWrapper;
import ch.opentrainingcenter.model.importer.internal.GpsFileModel;
import ch.opentrainingcenter.model.importer.internal.GpsFileModelWrapper;
import ch.opentrainingcenter.model.navigation.ConcreteHealth;
import ch.opentrainingcenter.model.navigation.IKalenderWocheNavigationModel;
import ch.opentrainingcenter.model.navigation.INavigationParent;
import ch.opentrainingcenter.model.navigation.internal.KWTraining;
import ch.opentrainingcenter.model.navigation.internal.NavigationParent;
import ch.opentrainingcenter.model.planing.IPastPlanungModel;
import ch.opentrainingcenter.model.planing.IPlanungModel;
import ch.opentrainingcenter.model.planing.IPlanungWocheModel;
import ch.opentrainingcenter.model.planing.KwJahrKey;
import ch.opentrainingcenter.model.planing.internal.PastPlanungModel;
import ch.opentrainingcenter.model.planing.internal.PlanungModel;
import ch.opentrainingcenter.model.planing.internal.PlanungWocheModel;
import ch.opentrainingcenter.model.strecke.StreckeModel;
import ch.opentrainingcenter.model.training.IGoldMedalModel;
import ch.opentrainingcenter.model.training.IOverviewModel;
import ch.opentrainingcenter.model.training.ISimpleTraining;
import ch.opentrainingcenter.model.training.Wetter;
import ch.opentrainingcenter.model.training.internal.GoldMedalModel;
import ch.opentrainingcenter.model.training.internal.OverviewModel;
import ch.opentrainingcenter.model.training.internal.SimpleTraining;
import ch.opentrainingcenter.transfer.HeartRate;
import ch.opentrainingcenter.transfer.IAthlete;
import ch.opentrainingcenter.transfer.IHealth;
import ch.opentrainingcenter.transfer.IPlanungWoche;
import ch.opentrainingcenter.transfer.IRoute;
import ch.opentrainingcenter.transfer.ITraining;
import ch.opentrainingcenter.transfer.RunData;
import ch.opentrainingcenter.transfer.TrainingType;

public final class ModelFactory {

    private ModelFactory() {

    }

    public static IPlanungWocheModel createPlanungWochenModel(final List<IPlanungModel> planungen, final IAthlete athlete, final int jahr, final int kw,
            final int anzahl) {
        return new PlanungWocheModel(planungen, athlete, jahr, kw, anzahl);
    }

    public static PlanungModel createEmptyPlanungModel(final IAthlete athlete, final int jahr, final int kw) {
        return new PlanungModel(athlete, jahr, kw, 0);
    }

    public static IPlanungModel createPlanungModel(final IAthlete athlete, final int jahr, final int kw, final int kmProWoche) {
        return createPlanungModel(athlete, jahr, kw, kmProWoche, false, 0);
    }

    public static PlanungModel createPlanungModel(final IAthlete athlete, final int jahr, final int kw, final int kmProWoche, final boolean interval,
            final int langerLauf) {
        return new PlanungModel(athlete, jahr, kw, kmProWoche, interval, langerLauf);
    }

    public static ConcreteHealth createConcreteHealth(final IHealth healthToSave, final String image) {
        return new ConcreteHealth(healthToSave, image);
    }

    public static IGpsFileModel createGpsFileModel(final String fileName) {
        return new GpsFileModel(fileName);
    }

    public static IGpsFileModelWrapper createGpsFileModelWrapper(final List<IGpsFileModel> fileModels) {
        return new GpsFileModelWrapper(fileModels);
    }

    /**
     * Erstellt ein SimpleTraining mit dem Lauf Typ NONE
     */
    public static ISimpleTraining convertToSimpleTraining(final ITraining training) {
        final HeartRate heart = new HeartRate(training.getAverageHeartBeat(), training.getMaxHeartBeat());
        final RunData runData = new RunData(training.getDatum(), training.getDauer(), training.getLaengeInMeter(), training.getMaxSpeed());
        final SimpleTraining simpleTraining = new SimpleTraining(runData, heart, TrainingType.NONE, training.getSport(), training.getNote());
        simpleTraining.setType(TrainingType.getByIndex(training.getTrainingType().getIndex()));
        if (training.getWeather() != null) {
            simpleTraining.setWetter(Wetter.getRunType(training.getWeather().getId()));
        }
        final IRoute route = training.getRoute();
        if (route != null) {
            final ITraining referenzTrack = route.getReferenzTrack();
            simpleTraining.setStrecke(ModelFactory.createStreckeModel(route, training.getAthlete(), referenzTrack == null ? 0 : referenzTrack.getId()));
        }
        simpleTraining.setUpMeter(training.getUpMeter());
        simpleTraining.setDownMeter(training.getDownMeter());
        return simpleTraining;
    }

    /**
     * Erstellt ein SimpleTraining mit dem entsprechenden Lauf Typ
     */
    public static List<ISimpleTraining> convertToSimpleTraining(final List<ITraining> allImported) {
        final List<ISimpleTraining> result = new ArrayList<>();
        for (final ITraining training : allImported) {
            result.add(convertToSimpleTraining(training));
        }
        return result;
    }

    public static ISimpleTraining createSimpleTraining(final double distanzInMeter, final double dauerInSekunden, final Date datum, final int avgHeartRate,
            final int maxHeartRate, final double maxSpeed, final TrainingType type, final String note) {
        final RunData runData = new RunData(datum.getTime(), dauerInSekunden, distanzInMeter, maxSpeed);
        final HeartRate heart = new HeartRate(avgHeartRate, maxHeartRate);
        return new SimpleTraining(runData, heart, type, note);
    }

    public static IGoldMedalModel createGoldMedalModel() {
        return new GoldMedalModel();
    }

    public static IKalenderWocheNavigationModel createKwModel() {
        return new KWTraining();
    }

    public static IPastPlanungModel createPastPlanungModel(final List<IPlanungWoche> planungsWoche, final List<ITraining> allImported, final KwJahrKey now) {
        return new PastPlanungModel(planungsWoche, allImported, now);
    }

    public static INavigationParent createNavigationParent() {
        return new NavigationParent();
    }

    public static StreckeModel createStreckeModel(final IRoute route, final IAthlete athlete, final int referenzTrainingId) {
        return new StreckeModel(route.getId(), athlete, route.getName(), route.getBeschreibung(), referenzTrainingId);
    }

    public static IOverviewModel createOverview(final List<ITraining> trainings) {
        return new OverviewModel(trainings);
    }
}
