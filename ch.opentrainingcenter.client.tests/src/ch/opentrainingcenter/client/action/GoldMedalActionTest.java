package ch.opentrainingcenter.client.action;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import org.junit.Before;
import org.junit.Test;

import ch.opentrainingcenter.model.training.GoldMedalTyp;
import ch.opentrainingcenter.model.training.IGoldMedalModel;
import ch.opentrainingcenter.transfer.HeartRate;
import ch.opentrainingcenter.transfer.ITraining;
import ch.opentrainingcenter.transfer.RunData;
import ch.opentrainingcenter.transfer.Sport;
import ch.opentrainingcenter.transfer.factory.CommonTransferFactory;

@SuppressWarnings("nls")
public class GoldMedalActionTest {
    private GoldMedalAction action;
    private List<ITraining> allImported;

    private ITraining trainingA;
    private ITraining trainingB;

    @Before
    public void setUp() {
        Locale.setDefault(Locale.GERMAN);
        TimeZone.setDefault(TimeZone.getTimeZone("GMT+1"));
        action = new GoldMedalAction(Sport.RUNNING);
        allImported = new ArrayList<ITraining>();
        final RunData runData = new RunData(1000L, 0, 0, 0);
        final HeartRate heart = new HeartRate(0, 0);
        trainingA = CommonTransferFactory.createTraining(runData, heart);
        trainingA.setSport(Sport.RUNNING);
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

        assertEquals("Bei einem negativen Wert soll nichts ausgegeben werden", "-", model.getRecord(GoldMedalTyp.HOECHSTER_PULS).getSecond());
    }

    @Test
    public void testMaxHeartRate2() {
        trainingA.setMaxHeartBeat(0);
        allImported.add(trainingA);
        final IGoldMedalModel model = action.getModel(allImported);

        assertEquals("Bei einem negativen Wert soll nichts ausgegeben werden", "-", model.getRecord(GoldMedalTyp.HOECHSTER_PULS).getSecond());
    }

    @Test
    public void testMaxHeartRate3() {
        trainingA.setMaxHeartBeat(2);
        allImported.add(trainingA);
        final IGoldMedalModel model = action.getModel(allImported);

        assertEquals("Wert muss ausgegeben werden", "2", model.getRecord(GoldMedalTyp.HOECHSTER_PULS).getSecond());
    }

    @Test
    public void testMaxHeartRate4() {
        final RunData runData = new RunData(1234, 0, 0, 0);
        final HeartRate heart = new HeartRate(0, 0);
        trainingB = CommonTransferFactory.createTraining(runData, heart);
        trainingB.setSport(Sport.RUNNING);

        trainingA.setMaxHeartBeat(2);
        trainingB.setMaxHeartBeat(4);
        allImported.add(trainingA);
        allImported.add(trainingB);
        final IGoldMedalModel model = action.getModel(allImported);

        assertEquals("Grösserer Wert muss ausgegeben werden", "4", model.getRecord(GoldMedalTyp.HOECHSTER_PULS).getSecond());
    }

    @Test
    public void testMaxHeartRate4_FilterBiking() {
        final RunData runData = new RunData(1234, 0, 0, 0);
        final HeartRate heart = new HeartRate(0, 0);
        trainingB = CommonTransferFactory.createTraining(runData, heart);
        trainingB.setSport(Sport.BIKING);

        trainingA.setMaxHeartBeat(2);
        trainingB.setMaxHeartBeat(4);
        allImported.add(trainingA);
        allImported.add(trainingB);
        final IGoldMedalModel model = action.getModel(allImported);

        assertEquals("Grösserer Wert muss ausgegeben werden", "2", model.getRecord(GoldMedalTyp.HOECHSTER_PULS).getSecond());
    }

    @Test
    public void testMaxHeartRate5() {
        final RunData runData = new RunData(2345, 0, 0, 0);
        final HeartRate heart = new HeartRate(0, 0);
        trainingB = CommonTransferFactory.createTraining(runData, heart);
        trainingB.setSport(Sport.RUNNING);

        trainingA.setMaxHeartBeat(-1);
        trainingB.setMaxHeartBeat(4);
        allImported.add(trainingA);
        allImported.add(trainingB);
        final IGoldMedalModel model = action.getModel(allImported);

        assertEquals("Grösserer Wert muss ausgegeben werden", "4", model.getRecord(GoldMedalTyp.HOECHSTER_PULS).getSecond());
    }

    @Test
    public void testAverageHeartRate1() {
        trainingA.setAverageHeartBeat(-1);
        allImported.add(trainingA);
        final IGoldMedalModel model = action.getModel(allImported);

        assertEquals("Bei einem negativen Wert soll nichts ausgegeben werden", "-", model.getRecord(GoldMedalTyp.HOECHSTER_AVERAGE_PULS).getSecond());
    }

    @Test
    public void testAverageHeartRate2() {
        trainingA.setAverageHeartBeat(0);
        allImported.add(trainingA);
        final IGoldMedalModel model = action.getModel(allImported);

        assertEquals("Bei einem negativen Wert soll nichts ausgegeben werden", "-", model.getRecord(GoldMedalTyp.HOECHSTER_AVERAGE_PULS).getSecond());
    }

    @Test
    public void testAverageHeartRate3() {
        trainingA.setAverageHeartBeat(2);
        allImported.add(trainingA);
        final IGoldMedalModel model = action.getModel(allImported);

        assertEquals("Wert muss ausgegeben werden", "2", model.getRecord(GoldMedalTyp.HOECHSTER_AVERAGE_PULS).getSecond());
    }

    @Test
    public void testAverageHeartRate4() {
        final RunData runData = new RunData(463463, 0, 0, 0);
        final HeartRate heart = new HeartRate(0, 0);
        trainingB = CommonTransferFactory.createTraining(runData, heart);
        trainingB.setSport(Sport.RUNNING);

        trainingA.setAverageHeartBeat(2);
        trainingB.setAverageHeartBeat(4);
        allImported.add(trainingA);
        allImported.add(trainingB);
        final IGoldMedalModel model = action.getModel(allImported);

        assertEquals("Grösserer Wert muss ausgegeben werden", "4", model.getRecord(GoldMedalTyp.HOECHSTER_AVERAGE_PULS).getSecond());
    }

    @Test
    public void testAverageHeartRate5() {
        final RunData runData = new RunData(1234, 0, 0, 0);
        final HeartRate heart = new HeartRate(0, 0);
        trainingB = CommonTransferFactory.createTraining(runData, heart);
        trainingB.setSport(Sport.RUNNING);
        trainingA.setAverageHeartBeat(-1);
        trainingB.setAverageHeartBeat(4);
        allImported.add(trainingA);
        allImported.add(trainingB);
        final IGoldMedalModel model = action.getModel(allImported);

        assertEquals("Grösserer Wert muss ausgegeben werden", "4", model.getRecord(GoldMedalTyp.HOECHSTER_AVERAGE_PULS).getSecond());
    }

    @Test
    public void testLowestAverageHeartRate1() {
        trainingA.setAverageHeartBeat(-1);
        allImported.add(trainingA);
        final IGoldMedalModel model = action.getModel(allImported);

        assertEquals("Bei einem negativen Wert soll nichts ausgegeben werden", "-", model.getRecord(GoldMedalTyp.TIEFSTER_AVERAGE_PULS).getSecond());
    }

    @Test
    public void testLowestLowestAverageHeartRate2() {
        trainingA.setAverageHeartBeat(0);
        allImported.add(trainingA);
        final IGoldMedalModel model = action.getModel(allImported);

        assertEquals("Bei einem negativen Wert soll nichts ausgegeben werden", "-", model.getRecord(GoldMedalTyp.TIEFSTER_AVERAGE_PULS).getSecond());
    }

    @Test
    public void testLowestLowestAverageHeartRate3() {
        trainingA.setAverageHeartBeat(2);

        final IGoldMedalModel model = action.getModel(allImported);

        assertEquals("Wert muss ausgegeben werden", "2", model.getRecord(GoldMedalTyp.TIEFSTER_AVERAGE_PULS).getSecond());
    }

    @Test
    public void testLowestLowestAverageHeartRate4() {
        final RunData runData = new RunData(4567, 0, 0, 0);
        final HeartRate heart = new HeartRate(0, 0);
        trainingB = CommonTransferFactory.createTraining(runData, heart);
        trainingA.setAverageHeartBeat(2);
        trainingB.setAverageHeartBeat(4);
        allImported.add(trainingB);
        final IGoldMedalModel model = action.getModel(allImported);

        assertEquals("Grösserer Wert muss ausgegeben werden", "2", model.getRecord(GoldMedalTyp.TIEFSTER_AVERAGE_PULS).getSecond());
    }

    @Test
    public void testLowestLowestAverageHeartRate5() {
        final RunData runData = new RunData(234234, 0, 0, 0);
        final HeartRate heart = new HeartRate(0, 0);
        trainingB = CommonTransferFactory.createTraining(runData, heart);
        trainingB.setSport(Sport.RUNNING);

        trainingA.setAverageHeartBeat(-1);
        trainingB.setAverageHeartBeat(4);
        allImported.add(trainingB);
        final IGoldMedalModel model = action.getModel(allImported);

        assertEquals("Grösserer Wert muss ausgegeben werden", "4", model.getRecord(GoldMedalTyp.TIEFSTER_AVERAGE_PULS).getSecond());
    }

    @Test
    public void testDauer1() {
        trainingA.setDauer(-1);
        final IGoldMedalModel model = action.getModel(allImported);

        assertEquals("Bei einem negativen Wert soll nichts ausgegeben werden", "--:--:--", model.getRecord(GoldMedalTyp.LAENGSTER_LAUF).getSecond());
    }

    @Test
    public void testDauer2() {
        trainingA.setAverageHeartBeat(0);

        final IGoldMedalModel model = action.getModel(allImported);

        assertEquals("Bei einem negativen Wert soll nichts ausgegeben werden", "00:00:00", model.getRecord(GoldMedalTyp.LAENGSTER_LAUF).getSecond());
    }

    @Test
    public void testDauer3() {
        trainingA.setDauer(2);

        final IGoldMedalModel model = action.getModel(allImported);

        assertEquals("Wert muss ausgegeben werden", "00:00:02", model.getRecord(GoldMedalTyp.LAENGSTER_LAUF).getSecond());
    }

    @Test
    public void testDauer4() {
        final RunData runData = new RunData(353453, 0, 0, 0);
        final HeartRate heart = new HeartRate(0, 0);
        trainingB = CommonTransferFactory.createTraining(runData, heart);
        trainingB.setSport(Sport.RUNNING);

        trainingA.setDauer(2);
        trainingB.setDauer(4);
        allImported.add(trainingB);
        final IGoldMedalModel model = action.getModel(allImported);

        assertEquals("Grösserer Wert muss ausgegeben werden", "00:00:04", model.getRecord(GoldMedalTyp.LAENGSTER_LAUF).getSecond());
    }

    @Test
    public void testDauer5() {
        final RunData runData = new RunData(1234, 0, 0, 0);
        final HeartRate heart = new HeartRate(0, 0);
        trainingB = CommonTransferFactory.createTraining(runData, heart);
        trainingB.setSport(Sport.RUNNING);

        trainingA.setDauer(-1);
        trainingB.setDauer(4);
        allImported.add(trainingB);
        final IGoldMedalModel model = action.getModel(allImported);

        assertEquals("Grösserer Wert muss ausgegeben werden", "00:00:04", model.getRecord(GoldMedalTyp.LAENGSTER_LAUF).getSecond());
    }

    @Test
    public void testPace1() {

        final IGoldMedalModel model = action.getModel(allImported);

        assertEquals("Wenn nichts sinnvolles berechnet werden kann, soll auch nichts angezeigt werden", "-", model.getRecord(GoldMedalTyp.SCHNELLSTE_PACE)
                .getSecond());
    }

    @Test
    public void testPaceNegativ() {
        trainingA.setDauer(-60);
        trainingA.setLaengeInMeter(10000);

        final IGoldMedalModel model = action.getModel(allImported);

        assertEquals("Bei einem negativen Wert soll nichts ausgegeben werden", "-", model.getRecord(GoldMedalTyp.SCHNELLSTE_PACE).getSecond());
    }

    @Test
    public void testPace2() {
        trainingA.setDauer(60);
        trainingA.setLaengeInMeter(1000);

        final IGoldMedalModel model = action.getModel(allImported);

        assertEquals("Bei einem negativen Wert soll nichts ausgegeben werden", "1:00", model.getRecord(GoldMedalTyp.SCHNELLSTE_PACE).getSecond());
    }

    @Test
    public void testPace4() {
        final RunData runData = new RunData(42, 120, 1000, 0);
        final HeartRate heart = new HeartRate(0, 0);
        trainingB = CommonTransferFactory.createTraining(runData, heart);
        trainingA.setDauer(60);
        trainingA.setLaengeInMeter(1000);
        allImported.add(trainingB);

        final IGoldMedalModel model = action.getModel(allImported);

        assertEquals("kleinere Pace muss ausgegeben werden", "1:00", model.getRecord(GoldMedalTyp.SCHNELLSTE_PACE).getSecond());
    }

    @Test
    public void testPace5() {
        final RunData runData = new RunData(42, 30, 1000, 0);
        final HeartRate heart = new HeartRate(0, 0);
        trainingB = CommonTransferFactory.createTraining(runData, heart);
        trainingB.setSport(Sport.RUNNING);

        trainingA.setDauer(60);
        trainingA.setLaengeInMeter(1000);
        allImported.add(trainingB);

        final IGoldMedalModel model = action.getModel(allImported);

        assertEquals("kleinere Pace muss ausgegeben werden", "0:30", model.getRecord(GoldMedalTyp.SCHNELLSTE_PACE).getSecond());
    }

    @Test
    public void testLongestDistance1() {
        trainingA.setLaengeInMeter(-1);

        final IGoldMedalModel model = action.getModel(allImported);

        assertEquals("Bei einem negativen Wert soll nichts ausgegeben werden", "-", model.getRecord(GoldMedalTyp.LAENGSTE_DISTANZ).getSecond());
    }

    @Test
    public void testLongestDistance2() {
        trainingA.setLaengeInMeter(0);

        final IGoldMedalModel model = action.getModel(allImported);

        assertEquals("Bei einem negativen Wert soll nichts ausgegeben werden", "-", model.getRecord(GoldMedalTyp.LAENGSTE_DISTANZ).getSecond());
    }

    @Test
    public void testLongestDistance3() {
        trainingA.setLaengeInMeter(2);

        final IGoldMedalModel model = action.getModel(allImported);

        assertEquals("Wert muss ausgegeben werden", "0.002", model.getRecord(GoldMedalTyp.LAENGSTE_DISTANZ).getSecond());
    }

    @Test
    public void testLongestDistance4() {
        final RunData runData = new RunData(42, 0, 0, 0);
        final HeartRate heart = new HeartRate(0, 0);
        trainingB = CommonTransferFactory.createTraining(runData, heart);
        trainingB.setSport(Sport.RUNNING);

        trainingA.setLaengeInMeter(2);
        trainingB.setLaengeInMeter(4);
        allImported.add(trainingB);
        final IGoldMedalModel model = action.getModel(allImported);

        assertEquals("Grösserer Wert muss ausgegeben werden", "0.004", model.getRecord(GoldMedalTyp.LAENGSTE_DISTANZ).getSecond());
    }

    @Test
    public void testLongestDistance5() {
        final RunData runData = new RunData(42, 0, 0, 0);
        final HeartRate heart = new HeartRate(0, 0);
        trainingB = CommonTransferFactory.createTraining(runData, heart);
        trainingB.setSport(Sport.RUNNING);

        trainingA.setLaengeInMeter(-1);
        trainingB.setLaengeInMeter(4);
        allImported.add(trainingB);
        final IGoldMedalModel model = action.getModel(allImported);

        assertEquals("Grösserer Wert muss ausgegeben werden", "0.004", model.getRecord(GoldMedalTyp.LAENGSTE_DISTANZ).getSecond());
    }
}
