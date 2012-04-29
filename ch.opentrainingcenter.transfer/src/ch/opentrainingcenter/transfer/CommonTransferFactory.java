package ch.opentrainingcenter.transfer;

import java.util.Date;

import ch.opentrainingcenter.transfer.impl.Athlete;
import ch.opentrainingcenter.transfer.impl.Imported;
import ch.opentrainingcenter.transfer.impl.Training;
import ch.opentrainingcenter.transfer.impl.TrainingType;

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

    public static ITraining createTraining(final Date dateOfStart, final double timeInSeconds, final double distance, final int avgHeartRate,
            final int maxHeartBeat, final double maximumSpeed) {
        return new Training(dateOfStart, timeInSeconds, distance, avgHeartRate, maxHeartBeat, maximumSpeed);
    }
}
