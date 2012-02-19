package ch.opentrainingcenter.client.helper;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import ch.opentrainingcenter.client.model.ISimpleTraining;
import ch.opentrainingcenter.client.model.ModelFactory;
import ch.opentrainingcenter.client.model.RunType;

public class SimpleTrainingCalculator {

    /**
     * Erstellt eine neue Liste von Trainings. Der Input ist entweder nach dem
     * 
     * <pre>
     *  Input:   <JAHR,<KalenderWoche,List<ISimpleTraining>>>
     *  Output:  Jedes ISimpleTraining der Liste ist die Summe der Trainings pro KW mit dem Datum des letzten Laufes
     *  
     * Input:   <JAHR,<Monat,List<ISimpleTraining>>>
     * Output:  Jedes ISimpleTraining der Liste ist die Summe der Trainings pro Monat mit dem Datum des letzten Laufes
     * </pre>
     */
    public static List<ISimpleTraining> createSum(final Map<Integer, Map<Integer, List<ISimpleTraining>>> trainings, final RunType filter) {
        final List<ISimpleTraining> result = new ArrayList<ISimpleTraining>();
        for (final Map.Entry<Integer, Map<Integer, List<ISimpleTraining>>> trainingProJahr : trainings.entrySet()) {
            for (final Map.Entry<Integer, List<ISimpleTraining>> trainingsProWocheOderMonat : trainingProJahr.getValue().entrySet()) {
                compressTrainings(result, trainingsProWocheOderMonat, filter);
            }
        }
        return result;
    }

    private static void compressTrainings(final List<ISimpleTraining> result, final Map.Entry<Integer, List<ISimpleTraining>> trainingsProWocheOderMonat,
            final RunType filter) {
        double distance = 0;
        double seconds = 0;
        int heartRate = 0;
        int maxHeart = 0;
        boolean filterResults = true;
        if (filter == null) {
            // do not filter
            filterResults = false;
        }
        int countHeartIsZero = 0;
        Date date = null;
        int count = 0;
        for (final ISimpleTraining training : trainingsProWocheOderMonat.getValue()) {
            if (matchFilter(filter, filterResults, training)) {
                distance += training.getDistanzInMeter();
                seconds += training.getDauerInSekunden();
                maxHeart = getMax(maxHeart, training.getMaxHeartBeat());
                final int avgHeartRate = training.getAvgHeartRate();
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
        result.add(ModelFactory.createSimpleTraining(distance, seconds, date, avgHeartRate, maxHeart, 0, filter));
    }

    /**
     * @return true wenn filter passt oder nicht gefiltert werden soll, andernfalls false.
     */
    private static boolean matchFilter(final RunType filter, final boolean filterResults, final ISimpleTraining training) {
        if (filterResults) {
            return training.getType().equals(filter);
        } else {
            // alle daten zusammenzaehlen
            return true;
        }
    }

    private static int getMax(int maxSpeed, final String speed) {
        try {
            final int geschw = Integer.parseInt(speed);
            if (geschw > maxSpeed) {
                maxSpeed = geschw;
            }
        } catch (final NumberFormatException nfe) {
            // do nothing
        }
        return maxSpeed;
    }
}
