package ch.opentrainingcenter.client.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import ch.opentrainingcenter.client.helper.DistanceHelper;
import ch.opentrainingcenter.client.model.planing.IPlanungWocheModel;
import ch.opentrainingcenter.client.model.planing.impl.PlanungModel;
import ch.opentrainingcenter.transfer.IAthlete;
import ch.opentrainingcenter.transfer.ITraining;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@SuppressWarnings("nls")
public class ModelFactoryTest {

    private ITraining overview;
    private final Double distanz = Double.valueOf(1042);
    private final Double dauer = Double.valueOf(42);
    private final Date date = new Date();
    private final int avgHeart = 145;
    private final int maxHeart = 189;
    private final double maxSpeed = 3.45d;

    @Before
    public void before() {
        overview = Mockito.mock(ITraining.class);
        Mockito.when(overview.getLaengeInMeter()).thenReturn(distanz);
        Mockito.when(overview.getDauerInSekunden()).thenReturn(dauer);
        Mockito.when(overview.getDateOfStart()).thenReturn(date);
        Mockito.when(overview.getAverageHeartBeat()).thenReturn(avgHeart);
        Mockito.when(overview.getMaxHeartBeat()).thenReturn(maxHeart);
        Mockito.when(overview.getMaxSpeed()).thenReturn(maxSpeed);
    }

    @Test
    public void testSimpleTraining() {
        final ISimpleTraining training = ModelFactory.createSimpleTraining(overview);
        assertTraining(training);
    }

    @Test
    public void testFullTraining() {
        final RunType type = RunType.INT_INTERVALL;
        final ISimpleTraining training = ModelFactory.createSimpleTraining(distanz, dauer, date, avgHeart, maxHeart, maxSpeed, type, null);
        assertTraining(training);
    }

    @Test
    public void testSimpleTrainingMitTyp() {
        final ISimpleTraining training = ModelFactory.createSimpleTraining(overview, RunType.EXT_INTERVALL, "");
        assertTraining(training);
    }

    private void assertTraining(final ISimpleTraining training) {
        assertEquals("Distanz:", distanz, training.getDistanzInMeter(), 0.001);
        assertEquals("Dauer:", dauer, training.getDauerInSekunden(), 0.001);
        assertEquals("Datum:", date, training.getDatum());
        assertEquals("Herzrfequenz:", avgHeart, training.getAvgHeartRate());
        assertEquals("max Herzrfequenz:", Integer.toString(maxHeart), training.getMaxHeartBeat());
        assertEquals("max Speed:", DistanceHelper.calculatePace(maxSpeed), training.getMaxSpeed());
    }

    @Test
    public void testGpsFileModel() {
        final String fileName = "test.gmn";
        final IGpsFileModel model = ModelFactory.createGpsFileModel(fileName);
        assertEquals(RunType.NONE, model.getTyp());
        assertEquals(true, model.isImportFile());
        assertEquals(fileName, model.getFileName());
        assertEquals(RunType.NONE.getIndex(), model.getId());
    }

    @Test
    public void testGpsFileModelWrapper() {
        final List<IGpsFileModel> list = new ArrayList<IGpsFileModel>();
        final IGpsFileModel fileModelImp = Mockito.mock(IGpsFileModel.class);
        list.add(fileModelImp);
        Mockito.when(fileModelImp.isImportFile()).thenReturn(true);

        final IGpsFileModel fileModelNotImp = Mockito.mock(IGpsFileModel.class);
        list.add(fileModelNotImp);
        Mockito.when(fileModelNotImp.isImportFile()).thenReturn(false);

        final IGpsFileModelWrapper model = ModelFactory.createGpsFileModelWrapper(list);

        assertEquals("Nur ein File model zum importieren", 1, model.size());

    }

    @Test
    public void testPlanung() {

        final List<PlanungModel> planungen = null;
        final IAthlete athlete = null;
        final int jahr = 2012;
        final int kw = 40;
        final int anzahl = 10;
        final PlanungModel model = ModelFactory.createPlanungModel(athlete, jahr, kw, 444, true);

        assertNotNull(model);

    }

    @Test
    public void testPlanungWocheModel() {

        final List<PlanungModel> planungen = null;
        final IAthlete athlete = null;
        final int jahr = 2012;
        final int kw = 40;
        final int anzahl = 10;
        final IPlanungWocheModel model = ModelFactory.createPlanungWochenModel(planungen, athlete, jahr, kw, anzahl);

        assertEquals("10 Records populated", 10, model.size());

    }
}
