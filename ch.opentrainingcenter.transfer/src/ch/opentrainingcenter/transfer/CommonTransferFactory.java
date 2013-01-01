package ch.opentrainingcenter.transfer;

import java.util.Date;

import ch.opentrainingcenter.transfer.impl.Athlete;
import ch.opentrainingcenter.transfer.impl.Health;
import ch.opentrainingcenter.transfer.impl.Imported;
import ch.opentrainingcenter.transfer.impl.PlanungWoche;
import ch.opentrainingcenter.transfer.impl.Route;
import ch.opentrainingcenter.transfer.impl.Training;
import ch.opentrainingcenter.transfer.impl.TrainingType;
import ch.opentrainingcenter.transfer.impl.Weather;

public class CommonTransferFactory {
    private CommonTransferFactory() {

    }

    public static IImported createIImported() {
        return new Imported();
    }

    public static IAthlete createAthlete(final String name, final Integer age, final Integer maxHeartBeat) {
        return new Athlete(name, age, maxHeartBeat);
    }

    public static ITrainingType createTrainingType(final int id, final String title, final String description) {
        return new TrainingType(id, title, description);
    }

    public static IWeather createDefaultWeather() {
        return createWeather(9);
    }

    public static IWeather createWeather(final int id) {
        return new Weather(id);
    }

    public static IHealth createHealth(final IAthlete athlete, final Double weight, final Integer cardio, final Date dateofmeasure) {
        return new Health(athlete, weight, cardio, dateofmeasure);
    }

    public static ITraining createTraining(final Date dateOfStart, final double timeInSeconds, final double distance, final int avgHeartRate,
            final int maxHeartBeat, final double maximumSpeed, final ActivityExtension activityExtension) {
        return new Training(dateOfStart, timeInSeconds, distance, avgHeartRate, maxHeartBeat, maximumSpeed, activityExtension);
    }

    public static IPlanungWoche createIPlanungWoche(final IAthlete athlete, final int jahr, final int kw, final int kmProWoche, final boolean interval,
            final int langerLauf) {
        return new PlanungWoche(athlete, jahr, kw, kmProWoche, interval, langerLauf);
    }

    public static IPlanungWoche createIPlanungWocheEmpty(final IAthlete athlete, final int jahr, final int kw) {
        return new PlanungWoche(athlete, jahr, kw, 0, false, 0);
    }

    public static IRoute createRoute(final String name, final String beschreibung, final IAthlete athlete) {
        return new Route(name, beschreibung, athlete);
    }
}
