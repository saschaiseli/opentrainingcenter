package ch.opentrainingcenter.model.chart;

import static ch.opentrainingcenter.transfer.TrainingType.EXT_INTERVALL;
import static ch.opentrainingcenter.transfer.TrainingType.INT_INTERVALL;
import static ch.opentrainingcenter.transfer.TrainingType.POWER_LONG_JOG;
import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import ch.opentrainingcenter.transfer.ITraining;

public class TrainingCalculatorTest {

    private Map<Integer, Map<Integer, List<ITraining>>> allTrainings;

    @Before
    public void setUp() {
        allTrainings = new HashMap<Integer, Map<Integer, List<ITraining>>>();
    }

    @Test
    public void testCalculator() {
        final List<ITraining> trainingsProWoche = new ArrayList<ITraining>();
        final HashMap<Integer, List<ITraining>> trainingMap = new HashMap<Integer, List<ITraining>>();
        trainingMap.put(1, trainingsProWoche);

        allTrainings.put(2012, trainingMap);

        final TrainingDescriptor descriptor = TrainingDescriptor.createSimpleTraining(2012, 1, 1);
        descriptor.setAvgHeartRate(80).setDauerInSekunden(60).setDistanz(1000).setMaxHeartRate(120).setSpeed(5.5).setRunType(EXT_INTERVALL);

        final ITraining training1 = descriptor.build();
        trainingsProWoche.add(training1);

        descriptor.setAvgHeartRate(90).setDauerInSekunden(1420).setDistanz(1042).setMaxHeartRate(130).setSpeed(5.75).setTag(2).setRunType(INT_INTERVALL);
        final ITraining training2 = descriptor.build();
        trainingsProWoche.add(training2);

        final List<ITraining> result = TrainingCalculator.createSum(allTrainings, null);
        assertEquals("Zu einem Result zusammengefügt", 1, result.size()); //$NON-NLS-1$
        final ITraining training = result.get(0);
        assertEquals("Herzrate: ", 85, training.getAverageHeartBeat()); //$NON-NLS-1$
        assertEquals("Dauer: ", 1480, training.getDauer(), 0); //$NON-NLS-1$
        assertEquals("Distanz: ", 2042, training.getLaengeInMeter(), 0); //$NON-NLS-1$
        assertEquals("Max Heart: ", 130, training.getMaxHeartBeat()); //$NON-NLS-1$ 
    }

    @Test
    public void testCalculatorFilter() {
        final List<ITraining> trainingsProWoche = new ArrayList<ITraining>();
        final HashMap<Integer, List<ITraining>> trainingMap = new HashMap<Integer, List<ITraining>>();
        trainingMap.put(1, trainingsProWoche);

        allTrainings.put(2012, trainingMap);

        final TrainingDescriptor descriptor = TrainingDescriptor.createSimpleTraining(2012, 1, 1);
        descriptor.setAvgHeartRate(80).setDauerInSekunden(60).setDistanz(1000).setMaxHeartRate(120).setSpeed(5.5).setRunType(EXT_INTERVALL);

        final ITraining training1 = descriptor.build();
        trainingsProWoche.add(training1);

        descriptor.setAvgHeartRate(90).setDauerInSekunden(1420).setDistanz(1042).setMaxHeartRate(130).setSpeed(5.75).setTag(2).setRunType(INT_INTERVALL);
        final ITraining training2 = descriptor.build();
        trainingsProWoche.add(training2);

        final List<ITraining> result = TrainingCalculator.createSum(allTrainings, EXT_INTERVALL);
        assertEquals("Zu einem Result zusammengefügt", 1, result.size()); //$NON-NLS-1$
        final ITraining training = result.get(0);
        assertEquals("Herzrate: ", 80, training.getAverageHeartBeat()); //$NON-NLS-1$
        assertEquals("Dauer: ", 60, training.getDauer(), 0); //$NON-NLS-1$
        assertEquals("Distanz: ", 1000, training.getLaengeInMeter(), 0); //$NON-NLS-1$
        assertEquals("Max Heart: ", 120, training.getMaxHeartBeat()); //$NON-NLS-1$ 
    }

    @Test
    public void testCalculatorEmptyFilter() {
        final List<ITraining> trainingsProWoche = new ArrayList<ITraining>();
        final HashMap<Integer, List<ITraining>> trainingMap = new HashMap<Integer, List<ITraining>>();
        trainingMap.put(1, trainingsProWoche);

        allTrainings.put(2012, trainingMap);

        final TrainingDescriptor descriptor = TrainingDescriptor.createSimpleTraining(2012, 1, 1);
        descriptor.setAvgHeartRate(80).setDauerInSekunden(60).setDistanz(1000).setMaxHeartRate(120).setSpeed(5.5).setRunType(EXT_INTERVALL);

        final ITraining training1 = descriptor.build();
        trainingsProWoche.add(training1);

        descriptor.setAvgHeartRate(90).setDauerInSekunden(1420).setDistanz(1042).setMaxHeartRate(130).setSpeed(5.75).setTag(2).setRunType(INT_INTERVALL);
        final ITraining training2 = descriptor.build();
        trainingsProWoche.add(training2);

        final List<ITraining> result = TrainingCalculator.createSum(allTrainings, POWER_LONG_JOG);
        assertEquals("Filter passte nicht", 0, result.size()); //$NON-NLS-1$
    }
}
