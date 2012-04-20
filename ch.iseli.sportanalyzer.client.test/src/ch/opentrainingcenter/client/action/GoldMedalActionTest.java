package ch.opentrainingcenter.client.action;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import ch.opentrainingcenter.client.model.IGoldMedalModel;
import ch.opentrainingcenter.transfer.CommonTransferFactory;
import ch.opentrainingcenter.transfer.IImported;
import ch.opentrainingcenter.transfer.ITraining;

@SuppressWarnings("nls")
public class GoldMedalActionTest {
    private GoldMedalAction action;
    private List<IImported> allImported;

    private ITraining trainingA;
    private IImported impA;
    private IImported impB;
    private ITraining trainingB;

    @Before
    public void setUp() {
        action = new GoldMedalAction();
        allImported = new ArrayList<IImported>();
        impA = CommonTransferFactory.createIImported();
        trainingA = CommonTransferFactory.createTrainingDatabaseRecord(new Date(), 0, 0, 0, 0, 0);
        impA.setTraining(trainingA);
        allImported.add(impA);
    }

    @Test
    public void testEmptyList() {
        final IGoldMedalModel model = action.getModel(allImported);

        assertNotNull(model);
    }

    @Test
    public void testMaxHeartRate1() {
        trainingA.setMaxHeartBeat(-1);

        final IGoldMedalModel model = action.getModel(allImported);

        assertEquals("Bei einem negativen Wert soll nichts ausgegeben werden", "-", model.getHighestPulse());
    }

    @Test
    public void testMaxHeartRate2() {
        trainingA.setMaxHeartBeat(0);

        final IGoldMedalModel model = action.getModel(allImported);

        assertEquals("Bei einem negativen Wert soll nichts ausgegeben werden", "-", model.getHighestPulse());
    }

    @Test
    public void testMaxHeartRate3() {
        trainingA.setMaxHeartBeat(2);

        final IGoldMedalModel model = action.getModel(allImported);

        assertEquals("Wert muss ausgegeben werden", "2", model.getHighestPulse());
    }

    @Test
    public void testMaxHeartRate4() {
        impB = CommonTransferFactory.createIImported();
        trainingB = CommonTransferFactory.createTrainingDatabaseRecord(new Date(), 0, 0, 0, 0, 0);
        impB.setTraining(trainingB);

        trainingA.setMaxHeartBeat(2);
        trainingB.setMaxHeartBeat(4);

        allImported.add(impB);
        final IGoldMedalModel model = action.getModel(allImported);

        assertEquals("Grösserer Wert muss ausgegeben werden", "4", model.getHighestPulse());
    }

    @Test
    public void testMaxHeartRate5() {
        impB = CommonTransferFactory.createIImported();
        trainingB = CommonTransferFactory.createTrainingDatabaseRecord(new Date(), 0, 0, 0, 0, 0);
        impB.setTraining(trainingB);

        trainingA.setMaxHeartBeat(-1);
        trainingB.setMaxHeartBeat(4);

        allImported.add(impB);
        final IGoldMedalModel model = action.getModel(allImported);

        assertEquals("Grösserer Wert muss ausgegeben werden", "4", model.getHighestPulse());
    }

    @Test
    public void testAverageHeartRate1() {
        trainingA.setAverageHeartBeat(-1);

        final IGoldMedalModel model = action.getModel(allImported);

        assertEquals("Bei einem negativen Wert soll nichts ausgegeben werden", "-", model.getHighestAveragePulse());
    }

    @Test
    public void testAverageHeartRate2() {
        trainingA.setAverageHeartBeat(0);

        final IGoldMedalModel model = action.getModel(allImported);

        assertEquals("Bei einem negativen Wert soll nichts ausgegeben werden", "-", model.getHighestAveragePulse());
    }

    @Test
    public void testAverageHeartRate3() {
        trainingA.setAverageHeartBeat(2);

        final IGoldMedalModel model = action.getModel(allImported);

        assertEquals("Wert muss ausgegeben werden", "2", model.getHighestAveragePulse());
    }

    @Test
    public void testAverageHeartRate4() {
        impB = CommonTransferFactory.createIImported();
        trainingB = CommonTransferFactory.createTrainingDatabaseRecord(new Date(), 0, 0, 0, 0, 0);
        impB.setTraining(trainingB);

        trainingA.setAverageHeartBeat(2);
        trainingB.setAverageHeartBeat(4);

        allImported.add(impB);
        final IGoldMedalModel model = action.getModel(allImported);

        assertEquals("Grösserer Wert muss ausgegeben werden", "4", model.getHighestAveragePulse());
    }

    @Test
    public void testAverageHeartRate5() {
        impB = CommonTransferFactory.createIImported();
        trainingB = CommonTransferFactory.createTrainingDatabaseRecord(new Date(), 0, 0, 0, 0, 0);
        impB.setTraining(trainingB);

        trainingA.setAverageHeartBeat(-1);
        trainingB.setAverageHeartBeat(4);

        allImported.add(impB);
        final IGoldMedalModel model = action.getModel(allImported);

        assertEquals("Grösserer Wert muss ausgegeben werden", "4", model.getHighestAveragePulse());
    }

    @Test
    public void testLowestAverageHeartRate1() {
        trainingA.setAverageHeartBeat(-1);

        final IGoldMedalModel model = action.getModel(allImported);

        assertEquals("Bei einem negativen Wert soll nichts ausgegeben werden", "-", model.getLowestAveragePulse());
    }

    @Test
    public void testLowestLowestAverageHeartRate2() {
        trainingA.setAverageHeartBeat(0);

        final IGoldMedalModel model = action.getModel(allImported);

        assertEquals("Bei einem negativen Wert soll nichts ausgegeben werden", "-", model.getLowestAveragePulse());
    }

    @Test
    public void testLowestLowestAverageHeartRate3() {
        trainingA.setAverageHeartBeat(2);

        final IGoldMedalModel model = action.getModel(allImported);

        assertEquals("Wert muss ausgegeben werden", "2", model.getLowestAveragePulse());
    }

    @Test
    public void testLowestLowestAverageHeartRate4() {
        impB = CommonTransferFactory.createIImported();
        trainingB = CommonTransferFactory.createTrainingDatabaseRecord(new Date(), 0, 0, 0, 0, 0);
        impB.setTraining(trainingB);

        trainingA.setAverageHeartBeat(2);
        trainingB.setAverageHeartBeat(4);

        allImported.add(impB);
        final IGoldMedalModel model = action.getModel(allImported);

        assertEquals("Grösserer Wert muss ausgegeben werden", "2", model.getLowestAveragePulse());
    }

    @Test
    public void testLowestLowestAverageHeartRate5() {
        impB = CommonTransferFactory.createIImported();
        trainingB = CommonTransferFactory.createTrainingDatabaseRecord(new Date(), 0, 0, 0, 0, 0);
        impB.setTraining(trainingB);

        trainingA.setAverageHeartBeat(-1);
        trainingB.setAverageHeartBeat(4);

        allImported.add(impB);
        final IGoldMedalModel model = action.getModel(allImported);

        assertEquals("Grösserer Wert muss ausgegeben werden", "4", model.getLowestAveragePulse());
    }

    @Test
    public void testDauer1() {
        trainingA.setDauerInSekunden(-1);

        final IGoldMedalModel model = action.getModel(allImported);

        assertEquals("Bei einem negativen Wert soll nichts ausgegeben werden", "--:--:--", model.getLongestRun());
    }

    @Test
    public void testDauer2() {
        trainingA.setAverageHeartBeat(0);

        final IGoldMedalModel model = action.getModel(allImported);

        assertEquals("Bei einem negativen Wert soll nichts ausgegeben werden", "0:00:00", model.getLongestRun());
    }

    @Test
    public void testDauer3() {
        trainingA.setDauerInSekunden(2);

        final IGoldMedalModel model = action.getModel(allImported);

        assertEquals("Wert muss ausgegeben werden", "0:00:02", model.getLongestRun());
    }

    @Test
    public void testDauer4() {
        impB = CommonTransferFactory.createIImported();
        trainingB = CommonTransferFactory.createTrainingDatabaseRecord(new Date(), 0, 0, 0, 0, 0);
        impB.setTraining(trainingB);

        trainingA.setDauerInSekunden(2);
        trainingB.setDauerInSekunden(4);

        allImported.add(impB);
        final IGoldMedalModel model = action.getModel(allImported);

        assertEquals("Grösserer Wert muss ausgegeben werden", "0:00:04", model.getLongestRun());
    }

    @Test
    public void testDauer5() {
        impB = CommonTransferFactory.createIImported();
        trainingB = CommonTransferFactory.createTrainingDatabaseRecord(new Date(), 0, 0, 0, 0, 0);
        impB.setTraining(trainingB);

        trainingA.setDauerInSekunden(-1);
        trainingB.setDauerInSekunden(4);

        allImported.add(impB);
        final IGoldMedalModel model = action.getModel(allImported);

        assertEquals("Grösserer Wert muss ausgegeben werden", "0:00:04", model.getLongestRun());
    }

    @Test
    public void testPace1() {

        final IGoldMedalModel model = action.getModel(allImported);

        assertEquals("Wenn nichts sinnvolles berechnet werden kann, soll auch nichts angezeigt werden", "-", model.getSchnellstePace());
    }

    @Test
    public void testPaceNegativ() {
        trainingA.setDauerInSekunden(-60);
        trainingA.setLaengeInMeter(10000);

        final IGoldMedalModel model = action.getModel(allImported);

        assertEquals("Bei einem negativen Wert soll nichts ausgegeben werden", "-", model.getSchnellstePace());
    }

    @Test
    public void testPace2() {
        trainingA.setDauerInSekunden(60);
        trainingA.setLaengeInMeter(1000);

        final IGoldMedalModel model = action.getModel(allImported);

        assertEquals("Bei einem negativen Wert soll nichts ausgegeben werden", "1.0", model.getSchnellstePace());
    }

    @Test
    public void testPace4() {
        impB = CommonTransferFactory.createIImported();
        trainingB = CommonTransferFactory.createTrainingDatabaseRecord(new Date(), 120, 1000, 0, 0, 0);
        impB.setTraining(trainingB);

        trainingA.setDauerInSekunden(60);
        trainingA.setLaengeInMeter(1000);

        allImported.add(impB);
        final IGoldMedalModel model = action.getModel(allImported);

        assertEquals("kleinere Pace muss ausgegeben werden", "1.0", model.getSchnellstePace());
    }

    @Test
    public void testPace5() {
        impB = CommonTransferFactory.createIImported();
        trainingB = CommonTransferFactory.createTrainingDatabaseRecord(new Date(), 30, 1000, 0, 0, 0);
        impB.setTraining(trainingB);

        trainingA.setDauerInSekunden(60);
        trainingA.setLaengeInMeter(1000);

        allImported.add(impB);
        final IGoldMedalModel model = action.getModel(allImported);

        assertEquals("kleinere Pace muss ausgegeben werden", "0.3", model.getSchnellstePace());
    }

    @Test
    public void testLongestDistance1() {
        trainingA.setLaengeInMeter(-1);

        final IGoldMedalModel model = action.getModel(allImported);

        assertEquals("Bei einem negativen Wert soll nichts ausgegeben werden", "-", model.getLongestDistance());
    }

    @Test
    public void testLongestDistance2() {
        trainingA.setLaengeInMeter(0);

        final IGoldMedalModel model = action.getModel(allImported);

        assertEquals("Bei einem negativen Wert soll nichts ausgegeben werden", "-", model.getLongestDistance());
    }

    @Test
    public void testLongestDistance3() {
        trainingA.setLaengeInMeter(2);

        final IGoldMedalModel model = action.getModel(allImported);

        assertEquals("Wert muss ausgegeben werden", "0.0020", model.getLongestDistance());
    }

    @Test
    public void testLongestDistance4() {
        impB = CommonTransferFactory.createIImported();
        trainingB = CommonTransferFactory.createTrainingDatabaseRecord(new Date(), 0, 0, 0, 0, 0);
        impB.setTraining(trainingB);

        trainingA.setLaengeInMeter(2);
        trainingB.setLaengeInMeter(4);

        allImported.add(impB);
        final IGoldMedalModel model = action.getModel(allImported);

        assertEquals("Grösserer Wert muss ausgegeben werden", "0.0040", model.getLongestDistance());
    }

    @Test
    public void testLongestDistance5() {
        impB = CommonTransferFactory.createIImported();
        trainingB = CommonTransferFactory.createTrainingDatabaseRecord(new Date(), 0, 0, 0, 0, 0);
        impB.setTraining(trainingB);

        trainingA.setLaengeInMeter(-1);
        trainingB.setLaengeInMeter(4);

        allImported.add(impB);
        final IGoldMedalModel model = action.getModel(allImported);

        assertEquals("Grösserer Wert muss ausgegeben werden", "0.0040", model.getLongestDistance());
    }
}
