package ch.opentrainingcenter.charts.bar.internal;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Matchers;

import ch.opentrainingcenter.core.db.IDatabaseAccess;
import ch.opentrainingcenter.core.helper.TimeHelper;
import ch.opentrainingcenter.model.chart.IStatistikCreator;
import ch.opentrainingcenter.model.training.ISimpleTraining;
import ch.opentrainingcenter.transfer.IAthlete;
import ch.opentrainingcenter.transfer.IRoute;
import ch.opentrainingcenter.transfer.ITraining;
import ch.opentrainingcenter.transfer.TrainingType;
import ch.opentrainingcenter.transfer.factory.CommonTransferFactory;

@SuppressWarnings("nls")
public class TrainingDataFilterTest {

    private static final String EXPECTED = "Übersicht auf die aufbereiteten Daten:\n" + "Trainings pro Monat:\n" + "Trainings pro Woche:\n"
            + "Trainings pro Tag:\n";

    private TrainingDataFilter auf;

    private final List<ISimpleTraining> trainingsProTag = new ArrayList<ISimpleTraining>();

    private final Map<Integer, Map<Integer, List<ISimpleTraining>>> trainingsProWoche = new HashMap<Integer, Map<Integer, List<ISimpleTraining>>>();

    private final Map<Integer, Map<Integer, List<ISimpleTraining>>> trainingsProMonat = new HashMap<Integer, Map<Integer, List<ISimpleTraining>>>();

    private final Map<Integer, List<ISimpleTraining>> trainingsProJahr = new HashMap<Integer, List<ISimpleTraining>>();

    private IDatabaseAccess access;

    private IAthlete athlete;

    @Before
    public void setUp() {

        access = mock(IDatabaseAccess.class);
        auf = new TrainingDataFilter(new IStatistikCreator() {

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
        }, access, athlete);
        athlete = createAthlete("name", 42);
    }

    @Test
    public void testNONE() {

        final List<ITraining> values = new ArrayList<ITraining>();
        final ITraining mockA = mock(ITraining.class);
        final ITraining training = mock(ITraining.class);
        final TrainingType type = TrainingType.NONE;
        when(mockA.getTrainingType()).thenReturn(type);
        when(mockA.getAthlete()).thenReturn(athlete);
        values.add(mockA);

        final ITraining mockB = mock(ITraining.class);
        when(mockB.getTrainingType()).thenReturn(type);
        when(mockB.getAthlete()).thenReturn(athlete);
        values.add(mockB);

        final IRoute route = CommonTransferFactory.createRoute("name", "beschreibung", mockA);
        when(training.getRoute()).thenReturn(route);

        when(mockA.getRoute()).thenReturn(route);
        when(mockB.getRoute()).thenReturn(route);

        when(access.getAllTrainings((IAthlete) Matchers.any())).thenReturn(values);

        // execute
        auf.filter(TrainingType.NONE);

        // assert
        final List<ISimpleTraining> trainingsPerDay = auf.getTrainingsPerDay();
        assertEquals("2 Trainings mit dem Type NONE: ", 2, trainingsPerDay.size()); //$NON-NLS-1$
    }

    @Test
    public void testEXT() {
        // prepare
        final List<ITraining> values = new ArrayList<ITraining>();
        final ITraining mockA = mock(ITraining.class);
        final ITraining training = mock(ITraining.class);
        final TrainingType type = TrainingType.EXT_INTERVALL;
        when(mockA.getTrainingType()).thenReturn(type);
        when(mockA.getAthlete()).thenReturn(athlete);

        final IRoute route = CommonTransferFactory.createRoute("name", "beschreibung", mockA);
        when(training.getRoute()).thenReturn(route);
        when(mockA.getRoute()).thenReturn(route);

        values.add(mockA);
        when(access.getAllTrainings((IAthlete) Matchers.any())).thenReturn(values);
        // execute
        auf.filter(TrainingType.EXT_INTERVALL);

        // assert
        final List<ISimpleTraining> trainingsPerDay = auf.getTrainingsPerDay();
        assertEquals("1 Trainings mit dem Type EXT_INTERVALL: ", 1, trainingsPerDay.size()); //$NON-NLS-1$
    }

    @Test
    public void testINT_INTERVALL() {
        // prepare

        final List<ITraining> values = new ArrayList<ITraining>();
        final ITraining mockA = mock(ITraining.class);
        final ITraining training = mock(ITraining.class);
        final TrainingType type = TrainingType.INT_INTERVALL;
        when(mockA.getTrainingType()).thenReturn(type);
        when(mockA.getAthlete()).thenReturn(athlete);

        values.add(mockA);

        final IRoute route = CommonTransferFactory.createRoute("name", "beschreibung", mockA);
        when(training.getRoute()).thenReturn(route);

        when(mockA.getRoute()).thenReturn(route);

        when(access.getAllTrainings((IAthlete) Matchers.any())).thenReturn(values);

        // execute
        auf.filter(TrainingType.INT_INTERVALL);

        // assert
        final List<ISimpleTraining> trainingsPerDay = auf.getTrainingsPerDay();
        assertEquals("1 Trainings mit dem Type INT_INTERVALL: ", 1, trainingsPerDay.size()); //$NON-NLS-1$
    }

    @Test
    public void testLONG_JOG() {
        // prepare
        final List<ITraining> values = new ArrayList<ITraining>();
        final ITraining mockA = mock(ITraining.class);
        final ITraining training = mock(ITraining.class);
        final TrainingType type = TrainingType.LONG_JOG;
        when(mockA.getTrainingType()).thenReturn(type);
        when(mockA.getAthlete()).thenReturn(athlete);

        values.add(mockA);
        final IRoute route = CommonTransferFactory.createRoute("name", "beschreibung", mockA);
        when(training.getRoute()).thenReturn(route);

        when(mockA.getRoute()).thenReturn(route);
        when(access.getAllTrainings((IAthlete) Matchers.any())).thenReturn(values);

        // execute
        auf.filter(TrainingType.LONG_JOG);

        // assert
        final List<ISimpleTraining> trainingsPerDay = auf.getTrainingsPerDay();
        assertEquals("2 Trainings mit dem Type LONG_JOG: ", 1, trainingsPerDay.size()); //$NON-NLS-1$
    }

    @Test
    public void testPOWER_LONG_JOG() {
        // prepare
        final List<ITraining> values = new ArrayList<ITraining>();
        final ITraining mockA = mock(ITraining.class);
        final ITraining training = mock(ITraining.class);
        final TrainingType type = TrainingType.POWER_LONG_JOG;
        when(mockA.getTrainingType()).thenReturn(type);
        when(mockA.getAthlete()).thenReturn(athlete);

        values.add(mockA);
        final IRoute route = CommonTransferFactory.createRoute("name", "beschreibung", mockA);
        when(training.getRoute()).thenReturn(route);

        when(mockA.getRoute()).thenReturn(route);
        when(access.getAllTrainings((IAthlete) Matchers.any())).thenReturn(values);

        // execute
        auf.filter(TrainingType.POWER_LONG_JOG);

        // assert
        final List<ISimpleTraining> trainingsPerDay = auf.getTrainingsPerDay();
        assertEquals("2 Trainings mit dem Type POWER_LONG_JOG: ", 1, trainingsPerDay.size()); //$NON-NLS-1$
    }

    @Test
    public void testTEMPO() {
        // prepare
        final List<ITraining> values = new ArrayList<ITraining>();
        final ITraining mockA = mock(ITraining.class);
        final ITraining training = mock(ITraining.class);
        final TrainingType type = TrainingType.TEMPO_JOG;
        when(mockA.getTrainingType()).thenReturn(type);
        when(mockA.getAthlete()).thenReturn(athlete);

        values.add(mockA);
        final IRoute route = CommonTransferFactory.createRoute("name", "beschreibung", mockA);
        when(training.getRoute()).thenReturn(route);

        when(mockA.getRoute()).thenReturn(route);

        when(access.getAllTrainings((IAthlete) Matchers.any())).thenReturn(values);

        // execute
        auf.filter(TrainingType.TEMPO_JOG);

        // assert
        final List<ISimpleTraining> trainingsPerDay = auf.getTrainingsPerDay();
        assertEquals("2 Trainings mit dem Type TEMPO_JOG: ", 1, trainingsPerDay.size());
    }

    @Test
    public void testTrainingPerDayNotNull() {
        assertNotNull(auf.getTrainingsPerDay());
    }

    @Test
    public void testTrainingPerDayValues() {
        // prepare
        final List<ITraining> values = new ArrayList<ITraining>();
        final ITraining mockA = mock(ITraining.class);
        final ITraining training = mock(ITraining.class);
        final TrainingType type = TrainingType.LONG_JOG;
        when(mockA.getTrainingType()).thenReturn(type);
        when(mockA.getAthlete()).thenReturn(athlete);

        final IRoute route = CommonTransferFactory.createRoute("name", "beschreibung", mockA);
        when(training.getRoute()).thenReturn(route);

        when(mockA.getRoute()).thenReturn(route);

        values.add(mockA);
        when(access.getAllTrainings((IAthlete) Matchers.any())).thenReturn(values);
        // execute
        auf.filter(TrainingType.LONG_JOG);
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
        final List<ITraining> values = new ArrayList<ITraining>();
        final ITraining training = mock(ITraining.class);
        when(training.getDatum()).thenReturn(date.getTime());
        when(training.getLaengeInMeter()).thenReturn(1042.0);
        when(training.getAthlete()).thenReturn(athlete);

        final TrainingType type = TrainingType.LONG_JOG;
        when(training.getTrainingType()).thenReturn(type);
        when(training.getDatum()).thenReturn(date.getTime());
        values.add(training);
        final IRoute route = CommonTransferFactory.createRoute("name", "beschreibung", training);
        when(training.getRoute()).thenReturn(route);

        when(training.getRoute()).thenReturn(route);

        when(access.getAllTrainings((IAthlete) Matchers.any())).thenReturn(values);
        // execute
        auf.filter(TrainingType.LONG_JOG);
        // assert
        assertEquals(EXPECTED + TimeHelper.convertDateToString(date, false) + " " + "1042.0[m] LONG_JOG\n", auf.toString());
    }

    private static IAthlete createAthlete(final String name, final Integer maxHeartBeat) {
        return CommonTransferFactory.createAthlete(name, DateTime.now().toDate(), maxHeartBeat);
    }
}
