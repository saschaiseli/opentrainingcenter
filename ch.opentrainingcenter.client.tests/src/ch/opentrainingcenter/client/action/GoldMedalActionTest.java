package ch.opentrainingcenter.client.action;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import ch.opentrainingcenter.model.training.IGoldMedalModel;
import ch.opentrainingcenter.transfer.CommonTransferFactory;
import ch.opentrainingcenter.transfer.ITraining;

@SuppressWarnings("nls")
public class GoldMedalActionTest {
    private GoldMedalAction action;
    private List<ITraining> allImported;

    private ITraining trainingA;
    private ITraining trainingB;

    @Before
    public void setUp() {
        action = new GoldMedalAction();
        allImported = new ArrayList<ITraining>();
        trainingA = CommonTransferFactory.createTraining(1000L, 0d, 0d, 0, 0, 0d);
        allImported.add(trainingA);
    }

    @Test
    public void testEmptyList() {
        final IGoldMedalModel model = action.getModel(new ArrayList<ITraining>());

        assertNotNull(model);
    }

    @Test
    public void testMaxHeartRate1() {
        trainingA.setMaxHeartBeat(-1);
        allImported.add(trainingA);
        final IGoldMedalModel model = action.getModel(allImported);

        assertEquals("Bei einem negativen Wert soll nichts ausgegeben werden", "-", model.getHighestPulse().getSecond());
    }

    @Test
    public void testMaxHeartRate2() {
        trainingA.setMaxHeartBeat(0);
        allImported.add(trainingA);
        final IGoldMedalModel model = action.getModel(allImported);

        assertEquals("Bei einem negativen Wert soll nichts ausgegeben werden", "-", model.getHighestPulse().getSecond());
    }

    @Test
    public void testMaxHeartRate3() {
        trainingA.setMaxHeartBeat(2);
        allImported.add(trainingA);
        final IGoldMedalModel model = action.getModel(allImported);

        assertEquals("Wert muss ausgegeben werden", "2", model.getHighestPulse().getSecond());
    }

    @Test
    public void testMaxHeartRate4() {
        trainingB = CommonTransferFactory.createTraining(1234, 0, 0, 0, 0, 0);

        trainingA.setMaxHeartBeat(2);
        trainingB.setMaxHeartBeat(4);
        allImported.add(trainingA);
        allImported.add(trainingB);
        final IGoldMedalModel model = action.getModel(allImported);

        assertEquals("Grösserer Wert muss ausgegeben werden", "4", model.getHighestPulse().getSecond());
    }

    @Test
    public void testMaxHeartRate5() {
        trainingB = CommonTransferFactory.createTraining(2345, 0, 0, 0, 0, 0);

        trainingA.setMaxHeartBeat(-1);
        trainingB.setMaxHeartBeat(4);
        allImported.add(trainingA);
        allImported.add(trainingB);
        final IGoldMedalModel model = action.getModel(allImported);

        assertEquals("Grösserer Wert muss ausgegeben werden", "4", model.getHighestPulse().getSecond());
    }

    @Test
    public void testAverageHeartRate1() {
        trainingA.setAverageHeartBeat(-1);
        allImported.add(trainingA);
        final IGoldMedalModel model = action.getModel(allImported);

        assertEquals("Bei einem negativen Wert soll nichts ausgegeben werden", "-", model.getHighestAveragePulse().getSecond());
    }

    @Test
    public void testAverageHeartRate2() {
        trainingA.setAverageHeartBeat(0);
        allImported.add(trainingA);
        final IGoldMedalModel model = action.getModel(allImported);

        assertEquals("Bei einem negativen Wert soll nichts ausgegeben werden", "-", model.getHighestAveragePulse().getSecond());
    }

    @Test
    public void testAverageHeartRate3() {
        trainingA.setAverageHeartBeat(2);
        allImported.add(trainingA);
        final IGoldMedalModel model = action.getModel(allImported);

        assertEquals("Wert muss ausgegeben werden", "2", model.getHighestAveragePulse().getSecond());
    }

    @Test
    public void testAverageHeartRate4() {
        trainingB = CommonTransferFactory.createTraining(463463, 0, 0, 0, 0, 0);
        trainingA.setAverageHeartBeat(2);
        trainingB.setAverageHeartBeat(4);
        allImported.add(trainingA);
        allImported.add(trainingB);
        final IGoldMedalModel model = action.getModel(allImported);

        assertEquals("Grösserer Wert muss ausgegeben werden", "4", model.getHighestAveragePulse().getSecond());
    }

    @Test
    public void testAverageHeartRate5() {
        trainingB = CommonTransferFactory.createTraining(1234, 0, 0, 0, 0, 0);
        trainingA.setAverageHeartBeat(-1);
        trainingB.setAverageHeartBeat(4);
        allImported.add(trainingA);
        allImported.add(trainingB);
        final IGoldMedalModel model = action.getModel(allImported);

        assertEquals("Grösserer Wert muss ausgegeben werden", "4", model.getHighestAveragePulse().getSecond());
    }

    @Test
    public void testLowestAverageHeartRate1() {
        trainingA.setAverageHeartBeat(-1);
        allImported.add(trainingA);
        final IGoldMedalModel model = action.getModel(allImported);

        assertEquals("Bei einem negativen Wert soll nichts ausgegeben werden", "-", model.getLowestAveragePulse().getSecond());
    }

    @Test
    public void testLowestLowestAverageHeartRate2() {
        trainingA.setAverageHeartBeat(0);
        allImported.add(trainingA);
        final IGoldMedalModel model = action.getModel(allImported);

        assertEquals("Bei einem negativen Wert soll nichts ausgegeben werden", "-", model.getLowestAveragePulse().getSecond());
    }

    @Test
    public void testLowestLowestAverageHeartRate3() {
        trainingA.setAverageHeartBeat(2);

        final IGoldMedalModel model = action.getModel(allImported);

        assertEquals("Wert muss ausgegeben werden", "2", model.getLowestAveragePulse().getSecond());
    }

    @Test
    public void testLowestLowestAverageHeartRate4() {
        trainingB = CommonTransferFactory.createTraining(4567, 0, 0, 0, 0, 0);
        trainingA.setAverageHeartBeat(2);
        trainingB.setAverageHeartBeat(4);
        allImported.add(trainingB);
        final IGoldMedalModel model = action.getModel(allImported);

        assertEquals("Grösserer Wert muss ausgegeben werden", "2", model.getLowestAveragePulse().getSecond());
    }

    @Test
    public void testLowestLowestAverageHeartRate5() {
        trainingB = CommonTransferFactory.createTraining(234234, 0, 0, 0, 0, 0);

        trainingA.setAverageHeartBeat(-1);
        trainingB.setAverageHeartBeat(4);
        allImported.add(trainingB);
        final IGoldMedalModel model = action.getModel(allImported);

        assertEquals("Grösserer Wert muss ausgegeben werden", "4", model.getLowestAveragePulse().getSecond());
    }

    @Test
    public void testDauer1() {
        trainingA.setDauer(-1);
        final IGoldMedalModel model = action.getModel(allImported);

        assertEquals("Bei einem negativen Wert soll nichts ausgegeben werden", "--:--:--", model.getLongestRun().getSecond());
    }

    @Test
    public void testDauer2() {
        trainingA.setAverageHeartBeat(0);

        final IGoldMedalModel model = action.getModel(allImported);

        assertEquals("Bei einem negativen Wert soll nichts ausgegeben werden", "0:00:00", model.getLongestRun().getSecond());
    }

    @Test
    public void testDauer3() {
        trainingA.setDauer(2);

        final IGoldMedalModel model = action.getModel(allImported);

        assertEquals("Wert muss ausgegeben werden", "0:00:02", model.getLongestRun().getSecond());
    }

    @Test
    public void testDauer4() {
        trainingB = CommonTransferFactory.createTraining(353453, 0, 0, 0, 0, 0);

        trainingA.setDauer(2);
        trainingB.setDauer(4);
        allImported.add(trainingB);
        final IGoldMedalModel model = action.getModel(allImported);

        assertEquals("Grösserer Wert muss ausgegeben werden", "0:00:04", model.getLongestRun().getSecond());
    }

    @Test
    public void testDauer5() {
        trainingB = CommonTransferFactory.createTraining(1234, 0, 0, 0, 0, 0);

        trainingA.setDauer(-1);
        trainingB.setDauer(4);
        allImported.add(trainingB);
        final IGoldMedalModel model = action.getModel(allImported);

        assertEquals("Grösserer Wert muss ausgegeben werden", "0:00:04", model.getLongestRun().getSecond());
    }

    @Test
    public void testPace1() {

        final IGoldMedalModel model = action.getModel(allImported);

        assertEquals("Wenn nichts sinnvolles berechnet werden kann, soll auch nichts angezeigt werden", "-", model.getSchnellstePace().getSecond());
    }

    @Test
    public void testPaceNegativ() {
        trainingA.setDauer(-60);
        trainingA.setLaengeInMeter(10000);

        final IGoldMedalModel model = action.getModel(allImported);

        assertEquals("Bei einem negativen Wert soll nichts ausgegeben werden", "-", model.getSchnellstePace().getSecond());
    }

    @Test
    public void testPace2() {
        trainingA.setDauer(60);
        trainingA.setLaengeInMeter(1000);

        final IGoldMedalModel model = action.getModel(allImported);

        assertEquals("Bei einem negativen Wert soll nichts ausgegeben werden", "1.0", model.getSchnellstePace().getSecond());
    }

    @Test
    public void testPace4() {
        trainingB = CommonTransferFactory.createTraining(42, 120, 1000, 0, 0, 0);
        trainingA.setDauer(60);
        trainingA.setLaengeInMeter(1000);
        allImported.add(trainingB);
        final IGoldMedalModel model = action.getModel(allImported);

        assertEquals("kleinere Pace muss ausgegeben werden", "1.0", model.getSchnellstePace().getSecond());
    }

    @Test
    public void testPace5() {
        trainingB = CommonTransferFactory.createTraining(42, 30, 1000, 0, 0, 0);

        trainingA.setDauer(60);
        trainingA.setLaengeInMeter(1000);
        allImported.add(trainingB);
        final IGoldMedalModel model = action.getModel(allImported);

        assertEquals("kleinere Pace muss ausgegeben werden", "0.3", model.getSchnellstePace().getSecond());
    }

    @Test
    public void testLongestDistance1() {
        trainingA.setLaengeInMeter(-1);

        final IGoldMedalModel model = action.getModel(allImported);

        assertEquals("Bei einem negativen Wert soll nichts ausgegeben werden", "-", model.getLongestDistance().getSecond());
    }

    @Test
    public void testLongestDistance2() {
        trainingA.setLaengeInMeter(0);

        final IGoldMedalModel model = action.getModel(allImported);

        assertEquals("Bei einem negativen Wert soll nichts ausgegeben werden", "-", model.getLongestDistance().getSecond());
    }

    @Test
    public void testLongestDistance3() {
        trainingA.setLaengeInMeter(2);

        final IGoldMedalModel model = action.getModel(allImported);

        assertEquals("Wert muss ausgegeben werden", "0.002", model.getLongestDistance().getSecond());
    }

    @Test
    public void testLongestDistance4() {
        trainingB = CommonTransferFactory.createTraining(42, 0, 0, 0, 0, 0);

        trainingA.setLaengeInMeter(2);
        trainingB.setLaengeInMeter(4);
        allImported.add(trainingB);
        final IGoldMedalModel model = action.getModel(allImported);

        assertEquals("Grösserer Wert muss ausgegeben werden", "0.004", model.getLongestDistance().getSecond());
    }

    @Test
    public void testLongestDistance5() {
        trainingB = CommonTransferFactory.createTraining(42, 0, 0, 0, 0, 0);

        trainingA.setLaengeInMeter(-1);
        trainingB.setLaengeInMeter(4);
        allImported.add(trainingB);
        final IGoldMedalModel model = action.getModel(allImported);

        assertEquals("Grösserer Wert muss ausgegeben werden", "0.004", model.getLongestDistance().getSecond());
    }
}
