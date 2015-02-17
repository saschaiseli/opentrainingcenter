package ch.opentrainingcenter.model.chart;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import ch.opentrainingcenter.transfer.HeartRate;
import ch.opentrainingcenter.transfer.ITraining;
import ch.opentrainingcenter.transfer.RunData;
import ch.opentrainingcenter.transfer.TrainingType;
import ch.opentrainingcenter.transfer.factory.CommonTransferFactory;

public final class TrainingCalculator {

    private TrainingCalculator() {

    }

    /**
     * Erstellt eine neue Liste von Trainings. Der Input ist entweder nach dem
     * 
     * <pre>
     *  Input:   <JAHR,<KalenderWoche,List<ISimpleTraining>>>
     *  Output:  Jedes ISimpleTraining der Liste ist die Summe der Trainings pro KW mit dem Datum des letzten Laufes
     *  
     *  Input:   <JAHR,<Monat,List<ISimpleTraining>>>
     *  Output:  Jedes ISimpleTraining der Liste ist die Summe der Trainings pro Monat mit dem Datum des letzten Laufes
     * </pre>
     */
    public static List<ITraining> createSum(final Map<Integer, Map<Integer, List<ITraining>>> trainings) {
        return createSum(trainings, null);
    }

    /**
     * Erstellt eine neue Liste von Trainings. Der Input ist entweder nach dem
     * 
     * <pre>
     *  Input:   <JAHR,<KalenderWoche,List<ISimpleTraining>>>
     *  Output:  Jedes ISimpleTraining der Liste ist die Summe der Trainings pro KW mit dem Datum des letzten Laufes
     *  
     *  Input:   <JAHR,<Monat,List<ISimpleTraining>>>
     *  Output:  Jedes ISimpleTraining der Liste ist die Summe der Trainings pro Monat mit dem Datum des letzten Laufes
     * </pre>
     */
    public static List<ITraining> createSum(final Map<Integer, Map<Integer, List<ITraining>>> trainings, final TrainingType filter) {
        final List<ITraining> result = new ArrayList<ITraining>();
        for (final Map.Entry<Integer, Map<Integer, List<ITraining>>> trainingProJahr : trainings.entrySet()) {
            for (final Map.Entry<Integer, List<ITraining>> trainingsProWocheOderMonat : trainingProJahr.getValue().entrySet()) {
                compressTrainings(result, trainingsProWocheOderMonat, filter);
            }
        }
        return result;
    }

    private static void compressTrainings(final List<ITraining> result, final Map.Entry<Integer, List<ITraining>> trainingsProWocheOderMonat,
            final TrainingType filter) {
        double distance = 0;
        long seconds = 0;
        int heartRate = 0;
        int maxHeart = 0;
        double maxSpeed = 0;
        int countHeartIsZero = 0;
        long date = 0;
        int count = 0;
        for (final ITraining training : trainingsProWocheOderMonat.getValue()) {
            if (matchFilter(filter, training)) {
                distance += training.getLaengeInMeter();
                seconds += training.getDauer();
                maxHeart = getMax(maxHeart, training.getMaxHeartBeat());
                maxSpeed = Math.max(maxSpeed, training.getMaxSpeed());
                final int avgHeartRate = training.getAverageHeartBeat();
                if (avgHeartRate <= 0) {
                    countHeartIsZero++;
                }
                training.getMaxHeartBeat();
                heartRate += Integer.valueOf(avgHeartRate);
                date = training.getDatum();
                count++;
            }
        }
        int avgHeartRate;
        if (heartRate > 0) {
            avgHeartRate = heartRate / (count - countHeartIsZero);

        } else {
            avgHeartRate = 0;
        }
        if (count > 0) {
            final RunData runData = new RunData(date, seconds, distance, maxSpeed);
            final HeartRate heart = new HeartRate(avgHeartRate, maxHeart);
            final ITraining tmp = CommonTransferFactory.createTraining(runData, heart);
            tmp.setTrainingType(filter);
            result.add(tmp);
        }
    }

    /**
     * @return true wenn filter passt oder nicht gefiltert werden soll,
     *         andernfalls false.
     */
    private static boolean matchFilter(final TrainingType filter, final ITraining training) {
        if (filter != null) {
            return training.getTrainingType().equals(filter);
        } else {
            // alle daten zusammenzaehlen
            return true;
        }
    }

    private static int getMax(final int maxSpeed, final int speed) {
        int result = maxSpeed;
        if (speed > maxSpeed) {
            result = speed;
        }
        return result;
    }
}
