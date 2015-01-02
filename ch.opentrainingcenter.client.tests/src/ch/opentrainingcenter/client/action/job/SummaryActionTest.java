package ch.opentrainingcenter.client.action.job;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;

import ch.opentrainingcenter.model.summary.SummaryModel;
import ch.opentrainingcenter.transfer.ITraining;

@SuppressWarnings("nls")
public class SummaryActionTest {

    private List<ITraining> trainings;

    @Before
    public void setUp() {
        trainings = new ArrayList<>();
    }

    @Test
    public void testCreateModel_EmptyTraining() {
        final SummaryAction action = new SummaryAction(Collections.<ITraining> emptyList());

        assertNotNull(action.calculateSummary());
    }

    @Test
    public void testMit_1_Training() {
        final long datum = 7_000_000L;
        final double laengeInMeter = 12345d;
        final double dauerInSekunden = 100d;
        final int avgHeart = 100;
        final int maxHeart = 150;
        trainings.add(createTraining(datum, laengeInMeter, dauerInSekunden, avgHeart, maxHeart));
        final SummaryAction action = new SummaryAction(trainings);

        final SummaryModel model = action.calculateSummary();

        final int kmTotal = (int) laengeInMeter / 1000;
        assertModel(laengeInMeter, dauerInSekunden, avgHeart, maxHeart, kmTotal, kmTotal, 1f, 1f, model);
    }

    @Test
    public void testMit_1_Training_mehr_sekunden_als_tag() {
        final long datum = 7_000_000L;
        final double laengeInMeter = 12345d;
        final double dauerInSekunden = 86401d;
        final int avgHeart = 100;
        final int maxHeart = 150;
        trainings.add(createTraining(datum, laengeInMeter, dauerInSekunden, avgHeart, maxHeart));
        final SummaryAction action = new SummaryAction(trainings);

        final SummaryModel model = action.calculateSummary();

        final int kmTotal = (int) laengeInMeter / 1000;
        assertModel(laengeInMeter, dauerInSekunden, avgHeart, maxHeart, kmTotal, kmTotal, 1f, 1f, model);
    }

    @Test
    public void testMit_2_Training_in_selber_Woche() {
        final DateTime start = new DateTime(2015, 1, 1, 12, 0);
        final double laengeInMeter = 12345d;
        final double dauerInSekunden = 100d;
        final int avgHeart = 100;
        final int maxHeart = 150;
        trainings.add(createTraining(start.getMillis(), laengeInMeter, dauerInSekunden, avgHeart, maxHeart));
        trainings.add(createTraining(start.getMillis(), laengeInMeter, dauerInSekunden, avgHeart, maxHeart));

        final SummaryAction action = new SummaryAction(trainings);

        final SummaryModel model = action.calculateSummary();

        final int kmTotal = 2 * (int) (laengeInMeter / 1000);
        assertModel(2 * laengeInMeter, 2 * dauerInSekunden, avgHeart, maxHeart, kmTotal, kmTotal, 2f, 2f, model);

        assertEquals(2, action.size());
    }

    @Test
    public void testMit_2_Training_selbe_woche() {
        final DateTime start = new DateTime(2015, 1, 1, 12, 0);
        final double laengeInMeter = 12345d;
        final double dauerInSekunden = 100d;
        final int avgHeart = 100;
        final int maxHeart = 150;
        trainings.add(createTraining(start.getMillis(), laengeInMeter, dauerInSekunden, avgHeart, maxHeart));
        trainings.add(createTraining(start.plusDays(7).getMillis(), laengeInMeter, dauerInSekunden, avgHeart, maxHeart));

        final SummaryAction action = new SummaryAction(trainings);

        final SummaryModel model = action.calculateSummary();

        final int kmTotal = (int) (laengeInMeter / 1000);
        assertModel(2 * laengeInMeter, 2 * dauerInSekunden, avgHeart, maxHeart, 2 * kmTotal, 2 * kmTotal, 2f, 2f, model);

        assertEquals(2, action.size());
    }

    @Test
    public void testMit_2_Training() {
        final DateTime start = new DateTime(2015, 1, 1, 12, 0);
        final double laengeInMeter = 12345d;
        final double dauerInSekunden = 100d;
        final int avgHeart = 100;
        final int maxHeart = 150;
        trainings.add(createTraining(start.getMillis(), laengeInMeter, dauerInSekunden, avgHeart, maxHeart));
        trainings.add(createTraining(start.plusDays(8).getMillis(), laengeInMeter, dauerInSekunden, avgHeart, maxHeart));

        final SummaryAction action = new SummaryAction(trainings);

        final SummaryModel model = action.calculateSummary();

        final int kmTotal = (int) (laengeInMeter / 1000);
        assertModel(2 * laengeInMeter, 2 * dauerInSekunden, avgHeart, maxHeart, kmTotal, 2 * kmTotal, 1f, 2f, model);

        assertEquals(2, action.size());
    }

    @Test
    public void testMit_2_Training_2_pro_monat() {
        final DateTime start = new DateTime(2015, 1, 1, 12, 0);
        final double laengeInMeter = 12345d;
        final double dauerInSekunden = 100d;
        final int avgHeart = 100;
        final int maxHeart = 150;
        trainings.add(createTraining(start.getMillis(), laengeInMeter, dauerInSekunden, avgHeart, maxHeart));
        trainings.add(createTraining(start.plusDays(31).getMillis(), laengeInMeter, dauerInSekunden + 1000, avgHeart, maxHeart));

        final SummaryAction action = new SummaryAction(trainings);

        final SummaryModel model = action.calculateSummary();

        final int kmTotal = 2 * (int) (laengeInMeter / 1000);
        assertModel(2 * laengeInMeter, 2 * dauerInSekunden + 1000, avgHeart, maxHeart, (float) kmTotal / 5, kmTotal, 0.4f, 2f, model);

        assertEquals(2, action.size());
    }

    @Test
    public void testMit_2_Training_1_pro_monat() {
        final DateTime start = new DateTime(2015, 1, 1, 12, 0);
        final double laengeInMeter = 12345d;
        final double dauerInSekunden = 100d;
        final int avgHeart = 100;
        final int maxHeart = 150;
        trainings.add(createTraining(start.getMillis(), laengeInMeter, dauerInSekunden, avgHeart, maxHeart));
        trainings.add(createTraining(start.plusDays(32).getMillis(), laengeInMeter, dauerInSekunden, avgHeart, maxHeart));

        final SummaryAction action = new SummaryAction(trainings);

        final SummaryModel model = action.calculateSummary();

        final int kmTotal = 2 * (int) (laengeInMeter / 1000);
        assertModel(2 * laengeInMeter, 2 * dauerInSekunden, avgHeart, maxHeart, (float) kmTotal / 5, (float) kmTotal / 2, 0.4f, 1f, model);

        assertEquals(2, action.size());
    }

    private ITraining createTraining(final long datum, final double laengeInMeter, final double dauerInSekunden, final int avgHeart, final int maxHeart) {
        final ITraining training = mock(ITraining.class);
        when(training.getDatum()).thenReturn(datum);
        when(training.getLaengeInMeter()).thenReturn(laengeInMeter);
        when(training.getDauer()).thenReturn(dauerInSekunden);
        when(training.getAverageHeartBeat()).thenReturn(avgHeart);
        when(training.getMaxHeartBeat()).thenReturn(maxHeart);
        return training;
    }

    private void assertModel(final double laengeInMeter, final double dauerInSekunden, final int avgHeart, final int maxHeart, final float kmWeek,
            final float kmMonth, final float trainingWeek, final float trainingMonth, final SummaryModel model) {
        assertEquals("Länge: ", laengeInMeter, model.getDistanz(), 0.0001);
        assertEquals("Dauer: ", dauerInSekunden, model.getDauerInSeconds(), 0.0001);
        assertEquals("Avg Heart: ", avgHeart, model.getAvgHeart());
        assertEquals("Max Heart: ", maxHeart, model.getMaxHeart());
        assertEquals("Km / Woche: ", kmWeek, model.getKmPerWeek(), 0.0001);
        assertEquals("km / Monat: ", kmMonth, model.getKmPerMonth(), 0.0001);
        assertEquals("Training / Woche: ", trainingWeek, model.getTrainingPerWeek(), 0.0001);
        assertEquals("Training / Monat: ", trainingMonth, model.getTrainingPerMonth(), 0.0001);
    }
}
