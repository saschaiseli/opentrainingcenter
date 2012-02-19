package ch.opentrainingcenter.client.helper;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import ch.opentrainingcenter.client.SimpleTrainingDescriptor;
import ch.opentrainingcenter.client.model.ISimpleTraining;

public class SimpleTrainingCalculatorTest {

    private Map<Integer, Map<Integer, List<ISimpleTraining>>> allTrainings;

    @Before
    public void setUp() {
        allTrainings = new HashMap<Integer, Map<Integer, List<ISimpleTraining>>>();
    }

    @Test
    public void testCalculator() {
        final List<ISimpleTraining> trainingsProWoche = new ArrayList<ISimpleTraining>();
        final HashMap<Integer, List<ISimpleTraining>> trainingMap = new HashMap<Integer, List<ISimpleTraining>>();
        trainingMap.put(1, trainingsProWoche);

        allTrainings.put(2012, trainingMap);

        final SimpleTrainingDescriptor descriptor = SimpleTrainingDescriptor.createSimpleTraining(2012, 1, 1);
        descriptor.setAvgHeartRate(80).setDauerInSekunden(60).setDistanz(1000).setMaxHeartRate(120).setSpeed(5.5);

        final ISimpleTraining training1 = descriptor.build();
        trainingsProWoche.add(training1);

        descriptor.setAvgHeartRate(90).setDauerInSekunden(1420).setDistanz(1042).setMaxHeartRate(130).setSpeed(5.75).setTag(2);
        final ISimpleTraining training2 = descriptor.build();
        trainingsProWoche.add(training2);

        final List<ISimpleTraining> result = SimpleTrainingCalculator.createSum(allTrainings);
        assertEquals("Zu einem Result zusammengefügt", 1, result.size()); //$NON-NLS-1$
        final ISimpleTraining training = result.get(0);
        assertEquals("Herzrate: ", 85, training.getAvgHeartRate()); //$NON-NLS-1$
        assertEquals("Dauer: ", 1480, training.getDauerInSekunden(), 0); //$NON-NLS-1$
        assertEquals("Distanz: ", 2042, training.getDistanzInMeter(), 0); //$NON-NLS-1$
        assertEquals("Max Heart: ", "130", training.getMaxHeartBeat()); //$NON-NLS-1$ //$NON-NLS-2$
    }
}
