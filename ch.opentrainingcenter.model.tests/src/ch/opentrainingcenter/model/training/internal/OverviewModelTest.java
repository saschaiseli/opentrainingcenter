package ch.opentrainingcenter.model.training.internal;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import ch.opentrainingcenter.transfer.ITraining;
import ch.opentrainingcenter.transfer.Sport;

public class OverviewModelTest {

    private OverviewModel model;
    private List<ITraining> trainings;

    @Before
    public void setUp() {
        trainings = new ArrayList<>();

    }

    @Test
    public void testKeinTraining() {

        model = new OverviewModel(trainings);

        assertEquals(0, model.getAnzahlTrainings(Sport.BIKING));
        assertEquals(0, model.getTotaleDistanzInMeter(Sport.BIKING), 0.0001);
        assertEquals(0, model.getTotaleZeitInSekunden(Sport.BIKING));

        assertEquals(0, model.getAnzahlTrainings(Sport.RUNNING));
        assertEquals(0, model.getTotaleDistanzInMeter(Sport.RUNNING), 0.0001);
        assertEquals(0, model.getTotaleZeitInSekunden(Sport.RUNNING));

        assertEquals(0, model.getAnzahlTrainings(Sport.OTHER));
        assertEquals(0, model.getTotaleDistanzInMeter(Sport.OTHER), 0.0001);
        assertEquals(0, model.getTotaleZeitInSekunden(Sport.OTHER));
    }

    @Test
    public void testEinTraining() {
        final ITraining trainingA = mock(ITraining.class);
        when(trainingA.getDauer()).thenReturn(42D);
        when(trainingA.getLaengeInMeter()).thenReturn(1042D);
        when(trainingA.getSport()).thenReturn(Sport.RUNNING);

        trainings.add(trainingA);

        model = new OverviewModel(trainings);

        assertEquals(1, model.getAnzahlTrainings(Sport.RUNNING));
        assertEquals(42, model.getTotaleZeitInSekunden(Sport.RUNNING));
        assertEquals(1042, model.getTotaleDistanzInMeter(Sport.RUNNING), 0.0001);

        assertEquals(0, model.getAnzahlTrainings(Sport.BIKING));
        assertEquals(0, model.getTotaleDistanzInMeter(Sport.BIKING), 0.0001);
        assertEquals(0, model.getTotaleZeitInSekunden(Sport.BIKING));

        assertEquals(0, model.getAnzahlTrainings(Sport.OTHER));
        assertEquals(0, model.getTotaleDistanzInMeter(Sport.OTHER), 0.0001);
        assertEquals(0, model.getTotaleZeitInSekunden(Sport.OTHER));
    }

    @Test
    public void testZweiTraining() {
        final ITraining trainingA = mock(ITraining.class);
        when(trainingA.getDauer()).thenReturn(42D);
        when(trainingA.getLaengeInMeter()).thenReturn(1042D);
        when(trainingA.getSport()).thenReturn(Sport.RUNNING);

        final ITraining trainingB = mock(ITraining.class);
        when(trainingB.getDauer()).thenReturn(100D);
        when(trainingB.getLaengeInMeter()).thenReturn(1000D);
        when(trainingB.getSport()).thenReturn(Sport.RUNNING);

        trainings.add(trainingA);
        trainings.add(trainingB);

        model = new OverviewModel(trainings);

        assertEquals(2, model.getAnzahlTrainings(Sport.RUNNING));
        assertEquals(142, model.getTotaleZeitInSekunden(Sport.RUNNING));
        assertEquals(2042, model.getTotaleDistanzInMeter(Sport.RUNNING), 0.0001);

        assertEquals(0, model.getAnzahlTrainings(Sport.BIKING));
        assertEquals(0, model.getTotaleDistanzInMeter(Sport.BIKING), 0.0001);
        assertEquals(0, model.getTotaleZeitInSekunden(Sport.BIKING));

        assertEquals(0, model.getAnzahlTrainings(Sport.OTHER));
        assertEquals(0, model.getTotaleDistanzInMeter(Sport.OTHER), 0.0001);
        assertEquals(0, model.getTotaleZeitInSekunden(Sport.OTHER));
    }

    @Test
    public void testZweiTraining_unterschiedlicheSportArten() {
        final ITraining trainingA = mock(ITraining.class);
        when(trainingA.getDauer()).thenReturn(42D);
        when(trainingA.getLaengeInMeter()).thenReturn(1042D);
        when(trainingA.getSport()).thenReturn(Sport.RUNNING);

        final ITraining trainingB = mock(ITraining.class);
        when(trainingB.getDauer()).thenReturn(100D);
        when(trainingB.getLaengeInMeter()).thenReturn(1000D);
        when(trainingB.getSport()).thenReturn(Sport.BIKING);

        trainings.add(trainingA);
        trainings.add(trainingB);

        model = new OverviewModel(trainings);

        assertEquals(1, model.getAnzahlTrainings(Sport.RUNNING));
        assertEquals(42, model.getTotaleZeitInSekunden(Sport.RUNNING));
        assertEquals(1042, model.getTotaleDistanzInMeter(Sport.RUNNING), 0.0001);

        assertEquals(1, model.getAnzahlTrainings(Sport.BIKING));
        assertEquals(1000, model.getTotaleDistanzInMeter(Sport.BIKING), 0.0001);
        assertEquals(100, model.getTotaleZeitInSekunden(Sport.BIKING));

        assertEquals(0, model.getAnzahlTrainings(Sport.OTHER));
        assertEquals(0, model.getTotaleDistanzInMeter(Sport.OTHER), 0.0001);
        assertEquals(0, model.getTotaleZeitInSekunden(Sport.OTHER));
    }
}
