package ch.opentrainingcenter.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import ch.opentrainingcenter.model.importer.IGpsFileModel;
import ch.opentrainingcenter.model.importer.IGpsFileModelWrapper;
import ch.opentrainingcenter.model.planing.IPlanungModel;
import ch.opentrainingcenter.model.planing.IPlanungWocheModel;
import ch.opentrainingcenter.model.strecke.StreckeModel;
import ch.opentrainingcenter.transfer.IAthlete;
import ch.opentrainingcenter.transfer.IRoute;
import ch.opentrainingcenter.transfer.ITraining;
import ch.opentrainingcenter.transfer.TrainingType;

@SuppressWarnings("nls")
public class ModelFactoryTest {

    private ITraining overview;
    private final Double distanz = Double.valueOf(1042);
    private final Double dauer = Double.valueOf(42);
    private final Date date = new Date();
    private final int avgHeart = 145;
    private final int maxHeart = 189;
    private final double maxSpeed = 3.45d;
    private IAthlete athlete;

    @Before
    public void before() {
        overview = mock(ITraining.class);
        when(overview.getLaengeInMeter()).thenReturn(distanz);
        when(overview.getDauer()).thenReturn(dauer);
        when(overview.getDatum()).thenReturn(date.getTime());
        when(overview.getAverageHeartBeat()).thenReturn(avgHeart);
        when(overview.getMaxHeartBeat()).thenReturn(maxHeart);
        when(overview.getMaxSpeed()).thenReturn(maxSpeed);
        final TrainingType trainingType = TrainingType.NONE;
        when(overview.getTrainingType()).thenReturn(trainingType);
        final IRoute route = mock(IRoute.class);
        athlete = mock(IAthlete.class);
        when(route.getAthlete()).thenReturn(athlete);
        when(route.getName()).thenReturn("blabla");
        when(overview.getRoute()).thenReturn(route);
        when(overview.getAthlete()).thenReturn(athlete);
    }

    @Test
    public void testCreateStreckeModel() {
        final IRoute route = mock(IRoute.class);
        when(route.getId()).thenReturn(11);
        final ITraining training = mock(ITraining.class);
        when(route.getReferenzTrack()).thenReturn(training);
        when(route.getName()).thenReturn("junit");
        when(training.getId()).thenReturn(1042);

        final StreckeModel model = ModelFactory.createStreckeModel(route, athlete, 42);

        assertEquals(42, model.getReferenzTrainingId());
        assertEquals(11, model.getId());
    }

    @Test
    public void testGpsFileModel() {
        final String fileName = "test.gmn";
        final IGpsFileModel model = ModelFactory.createGpsFileModel(fileName);
        assertEquals(TrainingType.NONE, model.getTyp());
        assertEquals(true, model.isImportFile());
        assertEquals(fileName, model.getFileName());
        assertEquals(TrainingType.NONE.getIndex(), model.getId());
    }

    @Test
    public void testGpsFileModelWrapper() {
        final List<IGpsFileModel> list = new ArrayList<IGpsFileModel>();
        final IGpsFileModel fileModelImp = mock(IGpsFileModel.class);
        list.add(fileModelImp);
        when(fileModelImp.isImportFile()).thenReturn(true);

        final IGpsFileModel fileModelNotImp = mock(IGpsFileModel.class);
        list.add(fileModelNotImp);
        when(fileModelNotImp.isImportFile()).thenReturn(false);

        final IGpsFileModelWrapper model = ModelFactory.createGpsFileModelWrapper(list);

        assertEquals("Nur ein File model zum importieren", 1, model.size());

    }

    @Test
    public void testPlanung() {

        final IAthlete athlete = null;
        final int jahr = 2012;
        final int kw = 40;
        final IPlanungModel model = ModelFactory.createPlanungModel(athlete, jahr, kw, 444, true, 20);

        assertNotNull(model);

    }

    @Test
    public void testPlanungWocheModel() {

        final List<IPlanungModel> planungen = null;
        final IAthlete athlete = null;
        final int jahr = 2012;
        final int kw = 40;
        final int anzahl = 10;
        final IPlanungWocheModel model = ModelFactory.createPlanungWochenModel(planungen, athlete, jahr, kw, anzahl);

        assertEquals("10 Records populated", 10, model.size());

    }
}
