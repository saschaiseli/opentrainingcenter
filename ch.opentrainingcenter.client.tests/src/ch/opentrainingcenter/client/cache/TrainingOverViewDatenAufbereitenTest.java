package ch.opentrainingcenter.client.cache;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.jface.preference.IPreferenceStore;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import ch.opentrainingcenter.client.SimpleTrainingDescriptor;
import ch.opentrainingcenter.client.charts.IStatistikCreator;
import ch.opentrainingcenter.client.helper.TimeHelper;
import ch.opentrainingcenter.client.model.ISimpleTraining;
import ch.opentrainingcenter.client.model.RunType;
import ch.opentrainingcenter.importer.IConvert2Tcx;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class TrainingOverViewDatenAufbereitenTest {

    private static final String EXPECTED = "Übersicht auf die aufbereiteten Daten:\n" + "Trainings pro Monat:\n" + "Trainings pro Woche:\n"
            + "Trainings pro Tag:\n";

    private TrainingOverviewDatenAufbereiten auf;

    private final List<ISimpleTraining> trainingsProTag = new ArrayList<ISimpleTraining>();

    private final Map<Integer, Map<Integer, List<ISimpleTraining>>> trainingsProWoche = new HashMap<Integer, Map<Integer, List<ISimpleTraining>>>();

    private final Map<Integer, Map<Integer, List<ISimpleTraining>>> trainingsProMonat = new HashMap<Integer, Map<Integer, List<ISimpleTraining>>>();

    private final Map<Integer, List<ISimpleTraining>> trainingsProJahr = new HashMap<Integer, List<ISimpleTraining>>();

    private final Map<String, IConvert2Tcx> converters = new HashMap<String, IConvert2Tcx>();

    @Before
    public void setUp() {

        final IPreferenceStore store = Mockito.mock(IPreferenceStore.class);

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
        }, new MockDataAccess(), store, converters);
    }

    @Test
    public void testNONE() {
        // prepare
        final SimpleTrainingDescriptor desc = SimpleTrainingDescriptor.createSimpleTraining(2012, 1, 2);
        desc.setRunType(RunType.NONE);
        trainingsProTag.add(desc.build());

        desc.setRunType(RunType.NONE);
        trainingsProTag.add(desc.build());

        desc.setRunType(RunType.EXT_INTERVALL);
        trainingsProTag.add(desc.build());

        // execute
        auf.update(RunType.NONE);

        // assert
        final List<ISimpleTraining> trainingsPerDay = auf.getTrainingsPerDay();
        assertEquals("2 Trainings mit dem Type NONE: ", 2, trainingsPerDay.size()); //$NON-NLS-1$
    }

    @Test
    public void testEXT() {
        // prepare
        final SimpleTrainingDescriptor desc = SimpleTrainingDescriptor.createSimpleTraining(2012, 1, 2);
        desc.setRunType(RunType.NONE);
        trainingsProTag.add(desc.build());

        desc.setRunType(RunType.NONE);
        trainingsProTag.add(desc.build());

        desc.setRunType(RunType.EXT_INTERVALL);
        trainingsProTag.add(desc.build());

        // execute
        auf.update(RunType.EXT_INTERVALL);

        // assert
        final List<ISimpleTraining> trainingsPerDay = auf.getTrainingsPerDay();
        assertEquals("1 Trainings mit dem Type EXT_INTERVALL: ", 1, trainingsPerDay.size()); //$NON-NLS-1$
    }

    @Test
    public void testINT_INTERVALL() {
        // prepare
        final SimpleTrainingDescriptor desc = SimpleTrainingDescriptor.createSimpleTraining(2012, 1, 2);
        desc.setRunType(RunType.NONE);
        trainingsProTag.add(desc.build());

        desc.setRunType(RunType.NONE);
        trainingsProTag.add(desc.build());

        desc.setRunType(RunType.INT_INTERVALL);
        trainingsProTag.add(desc.build());

        // execute
        auf.update(RunType.INT_INTERVALL);

        // assert
        final List<ISimpleTraining> trainingsPerDay = auf.getTrainingsPerDay();
        assertEquals("1 Trainings mit dem Type INT_INTERVALL: ", 1, trainingsPerDay.size()); //$NON-NLS-1$
    }

    @Test
    public void testLONG_JOG() {
        // prepare
        final SimpleTrainingDescriptor desc = SimpleTrainingDescriptor.createSimpleTraining(2012, 1, 2);
        desc.setRunType(RunType.LONG_JOG);
        trainingsProTag.add(desc.build());

        desc.setRunType(RunType.NONE);
        trainingsProTag.add(desc.build());

        desc.setRunType(RunType.LONG_JOG);
        trainingsProTag.add(desc.build());

        // execute
        auf.update(RunType.LONG_JOG);

        // assert
        final List<ISimpleTraining> trainingsPerDay = auf.getTrainingsPerDay();
        assertEquals("2 Trainings mit dem Type LONG_JOG: ", 2, trainingsPerDay.size()); //$NON-NLS-1$
    }

    @Test
    public void testPOWER_LONG_JOG() {
        // prepare
        final SimpleTrainingDescriptor desc = SimpleTrainingDescriptor.createSimpleTraining(2012, 1, 2);
        desc.setRunType(RunType.LONG_JOG);
        trainingsProTag.add(desc.build());

        desc.setRunType(RunType.POWER_LONG_JOG);
        trainingsProTag.add(desc.build());

        desc.setRunType(RunType.POWER_LONG_JOG);
        trainingsProTag.add(desc.build());

        // execute
        auf.update(RunType.POWER_LONG_JOG);

        // assert
        final List<ISimpleTraining> trainingsPerDay = auf.getTrainingsPerDay();
        assertEquals("2 Trainings mit dem Type POWER_LONG_JOG: ", 2, trainingsPerDay.size()); //$NON-NLS-1$
    }

    @Test
    public void testTEMPO() {
        // prepare
        final SimpleTrainingDescriptor desc = SimpleTrainingDescriptor.createSimpleTraining(2012, 1, 2);
        desc.setRunType(RunType.LONG_JOG);
        trainingsProTag.add(desc.build());

        desc.setRunType(RunType.TEMPO_JOG);
        trainingsProTag.add(desc.build());

        desc.setRunType(RunType.TEMPO_JOG);
        trainingsProTag.add(desc.build());

        // execute
        auf.update(RunType.TEMPO_JOG);

        // assert
        final List<ISimpleTraining> trainingsPerDay = auf.getTrainingsPerDay();
        assertEquals("2 Trainings mit dem Type TEMPO_JOG: ", 2, trainingsPerDay.size()); //$NON-NLS-1$
    }

    @Test
    public void testTrainingPerDayNotNull() {
        assertNotNull(auf.getTrainingsPerDay());
    }

    @Test
    public void testTrainingPerDayValues() {
        // prepare
        final SimpleTrainingDescriptor desc = SimpleTrainingDescriptor.createSimpleTraining(2012, 1, 2);
        desc.setRunType(RunType.LONG_JOG);
        trainingsProTag.add(desc.build());
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
        final SimpleTrainingDescriptor desc = SimpleTrainingDescriptor.createSimpleTraining(2012, 1, 2);
        desc.setRunType(RunType.LONG_JOG);
        desc.setDistanz(1042);
        final Date date = new Date();
        desc.setDate(date);
        trainingsProTag.add(desc.build());
        // execute
        auf.update(RunType.LONG_JOG);
        // assert
        assertEquals(EXPECTED + TimeHelper.convertDateToString(date, false) + " " + "1042.0[m] LONG_JOG\n", auf.toString());
    }
}
