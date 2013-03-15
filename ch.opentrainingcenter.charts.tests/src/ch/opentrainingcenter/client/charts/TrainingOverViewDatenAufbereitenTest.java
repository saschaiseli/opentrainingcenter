package ch.opentrainingcenter.client.charts;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import ch.opentrainingcenter.charts.bar.IStatistikCreator;
import ch.opentrainingcenter.charts.bar.internal.TrainingOverviewDatenAufbereiten;
import ch.opentrainingcenter.core.db.IDatabaseAccess;
import ch.opentrainingcenter.core.helper.RunType;
import ch.opentrainingcenter.core.helper.TimeHelper;
import ch.opentrainingcenter.model.training.ISimpleTraining;
import ch.opentrainingcenter.transfer.CommonTransferFactory;
import ch.opentrainingcenter.transfer.IAthlete;
import ch.opentrainingcenter.transfer.IImported;
import ch.opentrainingcenter.transfer.IRoute;
import ch.opentrainingcenter.transfer.ITraining;
import ch.opentrainingcenter.transfer.ITrainingType;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@SuppressWarnings("nls")
public class TrainingOverViewDatenAufbereitenTest {

    private static final String EXPECTED = "Übersicht auf die aufbereiteten Daten:\n" + "Trainings pro Monat:\n" + "Trainings pro Woche:\n"
            + "Trainings pro Tag:\n";

    private TrainingOverviewDatenAufbereiten auf;

    private final List<ISimpleTraining> trainingsProTag = new ArrayList<ISimpleTraining>();

    private final Map<Integer, Map<Integer, List<ISimpleTraining>>> trainingsProWoche = new HashMap<Integer, Map<Integer, List<ISimpleTraining>>>();

    private final Map<Integer, Map<Integer, List<ISimpleTraining>>> trainingsProMonat = new HashMap<Integer, Map<Integer, List<ISimpleTraining>>>();

    private final Map<Integer, List<ISimpleTraining>> trainingsProJahr = new HashMap<Integer, List<ISimpleTraining>>();

    private IDatabaseAccess databaseAccess;

    @Before
    public void setUp() {

        databaseAccess = Mockito.mock(IDatabaseAccess.class);
        final IAthlete athlete = Mockito.mock(IAthlete.class);
        auf = new TrainingOverviewDatenAufbereiten(new IStatistikCreator() {

            @Override
            public Map<Integer, Map<Integer, List<ISimpleTraining>>> getTrainingsProWoche(final List<ISimpleTraining> allTrainings) {
                return trainingsProWoche;
            }

            @Override
            public Map<Integer, Map<Integer, List<ISimpleTraining>>> getTrainingsProMonat(final List<ISimpleTraining> allTrainings) {
                return trainingsProMonat;
            }

            @Override
            public Map<Integer, List<ISimpleTraining>> getTrainingsProJahr(final List<ISimpleTraining> allTrainings) {
                return trainingsProJahr;
            }

            @Override
            public List<ISimpleTraining> getTrainingsProTag(final List<ISimpleTraining> allTrainings) {
                return trainingsProTag;
            }
        }, databaseAccess, athlete);
    }

    @Test
    public void testNONE() {
        // prepare
        final List<IImported> values = new ArrayList<IImported>();
        final IImported mockA = Mockito.mock(IImported.class);
        final ITraining training = Mockito.mock(ITraining.class);
        Mockito.when(mockA.getTraining()).thenReturn(training);
        final ITrainingType type = Mockito.mock(ITrainingType.class);
        Mockito.when(type.getId()).thenReturn(RunType.NONE.getIndex());
        Mockito.when(mockA.getTrainingType()).thenReturn(type);
        values.add(mockA);

        final IImported mockB = Mockito.mock(IImported.class);
        Mockito.when(mockB.getTraining()).thenReturn(training);
        Mockito.when(mockB.getTrainingType()).thenReturn(type);
        values.add(mockB);

        final IAthlete athlete = CommonTransferFactory.createAthlete("name", 42, 42);
        final IRoute route = CommonTransferFactory.createRoute(42, "name", "beschreibung", athlete);
        Mockito.when(training.getRoute()).thenReturn(route);

        Mockito.when(mockA.getRoute()).thenReturn(route);
        Mockito.when(mockB.getRoute()).thenReturn(route);

        Mockito.when(databaseAccess.getAllImported((IAthlete) Mockito.any())).thenReturn(values);

        // execute
        auf.update(RunType.NONE);

        // assert
        final List<ISimpleTraining> trainingsPerDay = auf.getTrainingsPerDay();
        assertEquals("2 Trainings mit dem Type NONE: ", 2, trainingsPerDay.size()); //$NON-NLS-1$
    }

    @Test
    public void testEXT() {
        // prepare
        final List<IImported> values = new ArrayList<IImported>();
        final IImported mockA = Mockito.mock(IImported.class);
        final ITraining training = Mockito.mock(ITraining.class);
        Mockito.when(mockA.getTraining()).thenReturn(training);
        final ITrainingType type = Mockito.mock(ITrainingType.class);
        Mockito.when(type.getId()).thenReturn(RunType.EXT_INTERVALL.getIndex());
        Mockito.when(mockA.getTrainingType()).thenReturn(type);

        final IAthlete athlete = CommonTransferFactory.createAthlete("name", 42, 42);

        final IRoute route = CommonTransferFactory.createRoute(42, "name", "beschreibung", athlete);
        Mockito.when(training.getRoute()).thenReturn(route);
        Mockito.when(mockA.getRoute()).thenReturn(route);

        values.add(mockA);
        Mockito.when(databaseAccess.getAllImported((IAthlete) Mockito.any())).thenReturn(values);
        // execute
        auf.update(RunType.EXT_INTERVALL);

        // assert
        final List<ISimpleTraining> trainingsPerDay = auf.getTrainingsPerDay();
        assertEquals("1 Trainings mit dem Type EXT_INTERVALL: ", 1, trainingsPerDay.size()); //$NON-NLS-1$
    }

    @Test
    public void testINT_INTERVALL() {
        // prepare

        final List<IImported> values = new ArrayList<IImported>();
        final IImported mockA = Mockito.mock(IImported.class);
        final ITraining training = Mockito.mock(ITraining.class);
        Mockito.when(mockA.getTraining()).thenReturn(training);
        final ITrainingType type = Mockito.mock(ITrainingType.class);
        Mockito.when(type.getId()).thenReturn(RunType.INT_INTERVALL.getIndex());
        Mockito.when(mockA.getTrainingType()).thenReturn(type);

        values.add(mockA);

        final IAthlete athlete = CommonTransferFactory.createAthlete("name", 42, 42);
        final IRoute route = CommonTransferFactory.createRoute(42, "name", "beschreibung", athlete);
        Mockito.when(training.getRoute()).thenReturn(route);

        Mockito.when(mockA.getRoute()).thenReturn(route);

        Mockito.when(databaseAccess.getAllImported((IAthlete) Mockito.any())).thenReturn(values);

        // execute
        auf.update(RunType.INT_INTERVALL);

        // assert
        final List<ISimpleTraining> trainingsPerDay = auf.getTrainingsPerDay();
        assertEquals("1 Trainings mit dem Type INT_INTERVALL: ", 1, trainingsPerDay.size()); //$NON-NLS-1$
    }

    @Test
    public void testLONG_JOG() {
        // prepare
        final List<IImported> values = new ArrayList<IImported>();
        final IImported mockA = Mockito.mock(IImported.class);
        final ITraining training = Mockito.mock(ITraining.class);
        Mockito.when(mockA.getTraining()).thenReturn(training);
        final ITrainingType type = Mockito.mock(ITrainingType.class);
        Mockito.when(type.getId()).thenReturn(RunType.LONG_JOG.getIndex());
        Mockito.when(mockA.getTrainingType()).thenReturn(type);

        values.add(mockA);
        final IAthlete athlete = CommonTransferFactory.createAthlete("name", 42, 42);
        final IRoute route = CommonTransferFactory.createRoute(42, "name", "beschreibung", athlete);
        Mockito.when(training.getRoute()).thenReturn(route);

        Mockito.when(mockA.getRoute()).thenReturn(route);
        Mockito.when(databaseAccess.getAllImported((IAthlete) Mockito.any())).thenReturn(values);

        // execute
        auf.update(RunType.LONG_JOG);

        // assert
        final List<ISimpleTraining> trainingsPerDay = auf.getTrainingsPerDay();
        assertEquals("2 Trainings mit dem Type LONG_JOG: ", 1, trainingsPerDay.size()); //$NON-NLS-1$
    }

    @Test
    public void testPOWER_LONG_JOG() {
        // prepare
        final List<IImported> values = new ArrayList<IImported>();
        final IImported mockA = Mockito.mock(IImported.class);
        final ITraining training = Mockito.mock(ITraining.class);
        Mockito.when(mockA.getTraining()).thenReturn(training);
        final ITrainingType type = Mockito.mock(ITrainingType.class);
        Mockito.when(type.getId()).thenReturn(RunType.POWER_LONG_JOG.getIndex());
        Mockito.when(mockA.getTrainingType()).thenReturn(type);

        values.add(mockA);
        final IAthlete athlete = CommonTransferFactory.createAthlete("name", 42, 42);
        final IRoute route = CommonTransferFactory.createRoute(42, "name", "beschreibung", athlete);
        Mockito.when(training.getRoute()).thenReturn(route);

        Mockito.when(mockA.getRoute()).thenReturn(route);
        Mockito.when(databaseAccess.getAllImported((IAthlete) Mockito.any())).thenReturn(values);

        // execute
        auf.update(RunType.POWER_LONG_JOG);

        // assert
        final List<ISimpleTraining> trainingsPerDay = auf.getTrainingsPerDay();
        assertEquals("2 Trainings mit dem Type POWER_LONG_JOG: ", 1, trainingsPerDay.size()); //$NON-NLS-1$
    }

    @Test
    public void testTEMPO() {
        // prepare
        final List<IImported> values = new ArrayList<IImported>();
        final IImported mockA = Mockito.mock(IImported.class);
        final ITraining training = Mockito.mock(ITraining.class);
        Mockito.when(mockA.getTraining()).thenReturn(training);
        final ITrainingType type = Mockito.mock(ITrainingType.class);
        Mockito.when(type.getId()).thenReturn(RunType.TEMPO_JOG.getIndex());
        Mockito.when(mockA.getTrainingType()).thenReturn(type);

        values.add(mockA);
        final IAthlete athlete = CommonTransferFactory.createAthlete("name", 42, 42);
        final IRoute route = CommonTransferFactory.createRoute(42, "name", "beschreibung", athlete);
        Mockito.when(training.getRoute()).thenReturn(route);

        Mockito.when(mockA.getRoute()).thenReturn(route);

        Mockito.when(databaseAccess.getAllImported((IAthlete) Mockito.any())).thenReturn(values);

        // execute
        auf.update(RunType.TEMPO_JOG);

        // assert
        final List<ISimpleTraining> trainingsPerDay = auf.getTrainingsPerDay();
        assertEquals("2 Trainings mit dem Type TEMPO_JOG: ", 1, trainingsPerDay.size()); //$NON-NLS-1$
    }

    @Test
    public void testTrainingPerDayNotNull() {
        assertNotNull(auf.getTrainingsPerDay());
    }

    @Test
    public void testTrainingPerDayValues() {
        // prepare
        final List<IImported> values = new ArrayList<IImported>();
        final IImported mockA = Mockito.mock(IImported.class);
        final ITraining training = Mockito.mock(ITraining.class);
        Mockito.when(mockA.getTraining()).thenReturn(training);
        final ITrainingType type = Mockito.mock(ITrainingType.class);
        Mockito.when(type.getId()).thenReturn(RunType.LONG_JOG.getIndex());
        Mockito.when(mockA.getTrainingType()).thenReturn(type);

        final IAthlete athlete = CommonTransferFactory.createAthlete("name", 42, 42);
        final IRoute route = CommonTransferFactory.createRoute(42, "name", "beschreibung", athlete);
        Mockito.when(training.getRoute()).thenReturn(route);

        Mockito.when(mockA.getRoute()).thenReturn(route);

        values.add(mockA);
        Mockito.when(databaseAccess.getAllImported((IAthlete) Mockito.any())).thenReturn(values);
        // execute
        auf.update(RunType.LONG_JOG);
        // assert
        assertEquals(1, auf.getTrainingsPerDay().size());
    }

    @Test
    public void testTrainingPerWeekNotNull() {
        assertNotNull(auf.getTrainingsPerWeek());
    }

    @Test
    public void testTrainingPerMonthNotNull() {
        assertNotNull(auf.getTrainingsPerMonth());
    }

    @Test
    public void testTrainingPerYearNotNull() {
        assertNotNull(auf.getTrainingsPerYear());
    }

    @Test
    public void testToString() {
        assertEquals(EXPECTED, auf.toString());
    }

    @Test
    public void testToStringMitWerten() {
        // prepare
        final Date date = new Date();
        final List<IImported> values = new ArrayList<IImported>();
        final IImported mockA = Mockito.mock(IImported.class);
        final ITraining training = Mockito.mock(ITraining.class);
        Mockito.when(training.getDateOfStart()).thenReturn(date);
        Mockito.when(training.getLaengeInMeter()).thenReturn(1042.0);
        Mockito.when(mockA.getTraining()).thenReturn(training);
        final ITrainingType type = Mockito.mock(ITrainingType.class);
        Mockito.when(type.getId()).thenReturn(RunType.LONG_JOG.getIndex());
        Mockito.when(mockA.getTrainingType()).thenReturn(type);
        Mockito.when(mockA.getActivityId()).thenReturn(date);
        values.add(mockA);
        final IAthlete athlete = CommonTransferFactory.createAthlete("name", 42, 42);
        final IRoute route = CommonTransferFactory.createRoute(42, "name", "beschreibung", athlete);
        Mockito.when(training.getRoute()).thenReturn(route);

        Mockito.when(mockA.getRoute()).thenReturn(route);

        Mockito.when(databaseAccess.getAllImported((IAthlete) Mockito.any())).thenReturn(values);
        // execute
        auf.update(RunType.LONG_JOG);
        // assert
        assertEquals(EXPECTED + TimeHelper.convertDateToString(date, false) + " " + "1042.0[m] LONG_JOG\n", auf.toString());
    }
}
