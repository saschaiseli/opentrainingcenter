package ch.opentrainingcenter.client.model.planing.impl;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import ch.opentrainingcenter.client.model.RunType;
import ch.opentrainingcenter.transfer.IImported;
import ch.opentrainingcenter.transfer.IPlanungWoche;
import ch.opentrainingcenter.transfer.ITraining;
import ch.opentrainingcenter.transfer.ITrainingType;
import static org.junit.Assert.assertEquals;

public class PastPlanungTest {
    private PastPlanungImpl planung;
    private IPlanungWoche pl;
    private List<IImported> effective;

    @Before
    public void before() {
        effective = new ArrayList<IImported>();

        pl = Mockito.mock(IPlanungWoche.class);
        Mockito.when(pl.getJahr()).thenReturn(2012);
        Mockito.when(pl.getKw()).thenReturn(44);
        Mockito.when(pl.getKmProWoche()).thenReturn(100);
        Mockito.when(pl.getLangerLauf()).thenReturn(88);
        Mockito.when(pl.isInterval()).thenReturn(true);
    }

    @Test
    public void testNull() {
        planung = new PastPlanungImpl(null, null);

        assertEquals(null, planung.getPlanung());
        assertEquals(0, planung.getKmEffective());
        assertEquals(0, planung.getLangerLaufEffective());
        assertEquals(false, planung.hasInterval());
    }

    @Test
    public void testEmpty() {
        planung = new PastPlanungImpl(null, new ArrayList<IImported>());

        assertEquals(null, planung.getPlanung());
        assertEquals(0, planung.getKmEffective());
        assertEquals(0, planung.getLangerLaufEffective());
        assertEquals(false, planung.hasInterval());
    }

    @Test
    public void testExtInterval() {

        final IImported recordA = createRecord(100001.0d, RunType.EXT_INTERVALL);
        effective.add(recordA);

        planung = new PastPlanungImpl(pl, effective);

        assertEquals(pl, planung.getPlanung());
        assertEquals(100, planung.getKmEffective());
        assertEquals(100, planung.getLangerLaufEffective());
        assertEquals(true, planung.hasInterval());
    }

    @Test
    public void testIntInterval() {

        final IImported recordA = createRecord(100001.0d, RunType.EXT_INTERVALL);
        final IImported recordB = createRecord(200001.0d, RunType.EXT_INTERVALL);
        effective.add(recordA);
        effective.add(recordB);

        planung = new PastPlanungImpl(pl, effective);

        assertEquals(pl, planung.getPlanung());
        assertEquals(300, planung.getKmEffective());
        assertEquals(200, planung.getLangerLaufEffective());
        assertEquals(true, planung.hasInterval());
    }

    @Test
    public void testKeinInterval() {

        final IImported recordA = createRecord(100001.0d, RunType.LONG_JOG);
        final IImported recordB = createRecord(200001.0d, RunType.NONE);
        effective.add(recordA);
        effective.add(recordB);

        planung = new PastPlanungImpl(pl, effective);

        assertEquals(pl, planung.getPlanung());
        assertEquals(300, planung.getKmEffective());
        assertEquals(200, planung.getLangerLaufEffective());
        assertEquals(false, planung.hasInterval());
    }

    @Test
    public void testExtUndIntInterval() {

        final IImported recordA = createRecord(100001.0d, RunType.EXT_INTERVALL);
        final IImported recordB = createRecord(200001.0d, RunType.INT_INTERVALL);
        effective.add(recordA);
        effective.add(recordB);

        planung = new PastPlanungImpl(pl, effective);

        assertEquals(pl, planung.getPlanung());
        assertEquals(300, planung.getKmEffective());
        assertEquals(200, planung.getLangerLaufEffective());
        assertEquals(true, planung.hasInterval());
    }

    @Test
    public void testLaengerLauf() {

        final IImported recordA = createRecord(100001.0d, RunType.EXT_INTERVALL);
        final IImported recordB = createRecord(200001.0d, RunType.INT_INTERVALL);
        effective.add(recordB);
        effective.add(recordA);

        planung = new PastPlanungImpl(pl, effective);

        assertEquals(pl, planung.getPlanung());
        assertEquals(300, planung.getKmEffective());
        assertEquals(200, planung.getLangerLaufEffective());
        assertEquals(true, planung.hasInterval());
    }

    private IImported createRecord(final double km, final RunType t) {
        final IImported recordA = Mockito.mock(IImported.class);
        final ITraining training = Mockito.mock(ITraining.class);
        Mockito.when(training.getLaengeInMeter()).thenReturn(km);
        Mockito.when(recordA.getTraining()).thenReturn(training);
        final ITrainingType type = Mockito.mock(ITrainingType.class);
        Mockito.when(type.getId()).thenReturn(t.getIndex());
        Mockito.when(recordA.getTrainingType()).thenReturn(type);
        return recordA;
    }
}
