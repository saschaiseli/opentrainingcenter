package ch.opentrainingcenter.model.chart;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import ch.opentrainingcenter.model.ModelFactory;
import ch.opentrainingcenter.model.training.ISimpleTraining;
import ch.opentrainingcenter.transfer.TrainingType;

public final class SimpleTrainingCalculator {

    private static final Logger LOGGER = Logger.getLogger(SimpleTrainingCalculator.class);

    private SimpleTrainingCalculator() {

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
    public static List<ISimpleTraining> createSum(final Map<Integer, Map<Integer, List<ISimpleTraining>>> trainings, final TrainingType filter) {
        final List<ISimpleTraining> result = new ArrayList<ISimpleTraining>();
        for (final Map.Entry<Integer, Map<Integer, List<ISimpleTraining>>> trainingProJahr : trainings.entrySet()) {
            for (final Map.Entry<Integer, List<ISimpleTraining>> trainingsProWocheOderMonat : trainingProJahr.getValue().entrySet()) {
                compressTrainings(result, trainingsProWocheOderMonat, filter);
            }
        }
        return result;
    }

    private static void compressTrainings(final List<ISimpleTraining> result, final Map.Entry<Integer, List<ISimpleTraining>> trainingsProWocheOderMonat,
            final TrainingType filter) {
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
            LOGGER.debug("compress Trainings --> Filter nach " + filter + " matches? " + matchFilter(filter, filterResults, training)); //$NON-NLS-1$ //$NON-NLS-2$
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
        if (count > 0) {
            result.add(ModelFactory.createSimpleTraining(distance, seconds, date, avgHeartRate, maxHeart, 0.0, filter, null));
        }
    }

    /**
     * @return true wenn filter passt oder nicht gefiltert werden soll,
     *         andernfalls false.
     */
    private static boolean matchFilter(final TrainingType filter, final boolean filterResults, final ISimpleTraining training) {
        if (filterResults) {
            return training.getType().equals(filter);
        } else {
            // alle daten zusammenzaehlen
            return true;
        }
    }

    private static int getMax(final int maxSpeed, final String speed) {
        int result = maxSpeed;
        try {
            final int geschw = Integer.parseInt(speed);
            if (geschw > maxSpeed) {
                result = geschw;
            }
        } catch (final NumberFormatException nfe) {
            LOGGER.warn("do nothing", nfe); //$NON-NLS-1$
        }
        return result;
    }
}
