package ch.opentrainingcenter.model.planing;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import ch.opentrainingcenter.core.helper.RunType;
import ch.opentrainingcenter.model.planing.internal.PastPlanungImpl;
import ch.opentrainingcenter.transfer.IAthlete;
import ch.opentrainingcenter.transfer.IPlanungWoche;
import ch.opentrainingcenter.transfer.ITraining;
import ch.opentrainingcenter.transfer.ITrainingType;
import ch.opentrainingcenter.transfer.factory.CommonTransferFactory;
import static org.junit.Assert.assertEquals;

@SuppressWarnings("nls")
public class PastPlanungTest {
    private PastPlanungImpl planung;
    private IPlanungWoche pl;
    private List<ITraining> effective;

    @Before
    public void before() {
        effective = new ArrayList<ITraining>();

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
        planung = new PastPlanungImpl(null, new ArrayList<ITraining>());

        assertEquals(null, planung.getPlanung());
        assertEquals(0, planung.getKmEffective());
        assertEquals(0, planung.getLangerLaufEffective());
        assertEquals(false, planung.hasInterval());
    }

    @Test
    public void testExtInterval() {

        final ITraining recordA = createRecord(100001.0d, RunType.EXT_INTERVALL);
        effective.add(recordA);

        planung = new PastPlanungImpl(pl, effective);

        assertEquals(pl, planung.getPlanung());
        assertEquals(100, planung.getKmEffective());
        assertEquals(100, planung.getLangerLaufEffective());
        assertEquals(true, planung.hasInterval());
    }

    @Test
    public void testIntInterval() {

        final ITraining recordA = createRecord(100001.0d, RunType.EXT_INTERVALL);
        final ITraining recordB = createRecord(200001.0d, RunType.EXT_INTERVALL);
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

        final ITraining recordA = createRecord(100001.0d, RunType.LONG_JOG);
        final ITraining recordB = createRecord(200001.0d, RunType.NONE);
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

        final ITraining recordA = createRecord(100001.0d, RunType.EXT_INTERVALL);
        final ITraining recordB = createRecord(200001.0d, RunType.INT_INTERVALL);
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

        final ITraining recordA = createRecord(100001.0d, RunType.EXT_INTERVALL);
        final ITraining recordB = createRecord(200001.0d, RunType.INT_INTERVALL);
        effective.add(recordB);
        effective.add(recordA);

        planung = new PastPlanungImpl(pl, effective);

        assertEquals(pl, planung.getPlanung());
        assertEquals(300, planung.getKmEffective());
        assertEquals(200, planung.getLangerLaufEffective());
        assertEquals(true, planung.hasInterval());
    }

    @Test
    public void testRechnenFehlerMitRunden() {

        final ITraining recordA = createRecord(10200.0d, RunType.EXT_INTERVALL);
        final ITraining recordB = createRecord(20300.0d, RunType.INT_INTERVALL);
        final ITraining recordC = createRecord(20400.0d, RunType.INT_INTERVALL);
        final ITraining recordD = createRecord(20400.0d, RunType.INT_INTERVALL);

        effective.add(recordA);
        effective.add(recordB);
        effective.add(recordC);
        effective.add(recordD);

        planung = new PastPlanungImpl(pl, effective);

        assertEquals(pl, planung.getPlanung());
        assertEquals(71, planung.getKmEffective());
    }

    @Test
    public void testSuccess() {
        final ITraining recordA = createRecord(100001.0d, RunType.EXT_INTERVALL);
        final ITraining recordB = createRecord(100001.0d, RunType.EXT_INTERVALL);
        final ITraining recordC = createRecord(100001.0d, RunType.EXT_INTERVALL);
        effective.add(recordA);
        effective.add(recordB);
        effective.add(recordC);

        // 100km
        // 88km langer;
        // intervall
        planung = new PastPlanungImpl(pl, effective);
        assertEquals("Überall mehr geleistet als geplant", PlanungStatus.ERFOLGREICH, planung.isSuccess());
    }

    @Test
    public void testNotSuccess() {
        final ITraining recordA = createRecord(50001.0d, RunType.EXT_INTERVALL);
        final ITraining recordB = createRecord(50001.0d, RunType.EXT_INTERVALL);
        final ITraining recordC = createRecord(50001.0d, RunType.EXT_INTERVALL);
        effective.add(recordA);
        effective.add(recordB);
        effective.add(recordC);

        // 100km
        // 88km langer;
        // intervall
        planung = new PastPlanungImpl(pl, effective);
        assertEquals("Längster Lauf zu kurz", PlanungStatus.NICHT_ERFOLGREICH, planung.isSuccess());
    }

    @Test
    public void testNotSuccess2() {
        final ITraining recordA = createRecord(100001.0d, RunType.LONG_JOG);
        final ITraining recordB = createRecord(100001.0d, RunType.LONG_JOG);
        final ITraining recordC = createRecord(100001.0d, RunType.LONG_JOG);
        effective.add(recordA);
        effective.add(recordB);
        effective.add(recordC);

        // 100km
        // 88km langer;
        // intervall
        planung = new PastPlanungImpl(pl, effective);
        assertEquals("Kein Intervall", PlanungStatus.NICHT_ERFOLGREICH, planung.isSuccess());
    }

    @Test
    public void testNotSuccess3() {
        final ITraining recordA = createRecord(1001.0d, RunType.LONG_JOG);
        final ITraining recordB = createRecord(1001.0d, RunType.EXT_INTERVALL);
        final ITraining recordC = createRecord(1001.0d, RunType.LONG_JOG);
        effective.add(recordA);
        effective.add(recordB);
        effective.add(recordC);

        // 100km
        // 88km langer;
        // intervall
        planung = new PastPlanungImpl(pl, effective);
        assertEquals("Zuwenig Kilometer", PlanungStatus.NICHT_ERFOLGREICH, planung.isSuccess());
    }

    @Test
    public void testNoPlanung() {
        final ITraining recordA = createRecord(1001.0d, RunType.LONG_JOG);
        final ITraining recordB = createRecord(1001.0d, RunType.LONG_JOG);
        final ITraining recordC = createRecord(1001.0d, RunType.LONG_JOG);
        effective.add(recordA);
        effective.add(recordB);
        effective.add(recordC);

        final IAthlete athlete = Mockito.mock(IAthlete.class);
        final IPlanungWoche tmp = CommonTransferFactory.createIPlanungWoche(athlete, 2012, 0, 0, false, 0);
        // 100km
        // 88km langer;
        // intervall
        planung = new PastPlanungImpl(tmp, effective);
        assertEquals("Keine Planung, also alles erfolgreich", PlanungStatus.UNBEKANNT, planung.isSuccess());
    }

    private ITraining createRecord(final double km, final RunType t) {
        final ITraining recordA = Mockito.mock(ITraining.class);
        Mockito.when(recordA.getLaengeInMeter()).thenReturn(km);
        final ITrainingType type = Mockito.mock(ITrainingType.class);
        Mockito.when(type.getId()).thenReturn(t.getIndex());
        Mockito.when(recordA.getTrainingType()).thenReturn(type);
        return recordA;
    }
}
