package ch.opentrainingcenter.client.cache;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import ch.opentrainingcenter.client.SimpleTrainingDescriptor;
import ch.opentrainingcenter.client.charts.IStatistikCreator;
import ch.opentrainingcenter.client.model.ISimpleTraining;
import ch.opentrainingcenter.client.model.RunType;

public class TrainingOverViewDatenAufbereitenTest {

    private TrainingOverviewDatenAufbereiten auf;

    private final List<ISimpleTraining> trainingsProTag = new ArrayList<ISimpleTraining>();

    private final Map<Integer, Map<Integer, List<ISimpleTraining>>> trainingsProWoche = new HashMap<Integer, Map<Integer, List<ISimpleTraining>>>();

    private final Map<Integer, Map<Integer, List<ISimpleTraining>>> trainingsProMonat = new HashMap<Integer, Map<Integer, List<ISimpleTraining>>>();

    private final Map<Integer, List<ISimpleTraining>> trainingsProJahr = new HashMap<Integer, List<ISimpleTraining>>();

    private TrainingCenterDataCache cache;

    @Before
    public void setUp() {
        cache = TrainingCenterDataCache.getInstance(new MockGpsFileLoader());

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
        });
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
}
