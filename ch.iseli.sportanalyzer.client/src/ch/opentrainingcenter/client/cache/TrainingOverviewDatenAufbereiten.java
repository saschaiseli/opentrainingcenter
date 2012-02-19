package ch.opentrainingcenter.client.cache;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import ch.opentrainingcenter.client.charts.IStatistikCreator;
import ch.opentrainingcenter.client.helper.SimpleTrainingCalculator;
import ch.opentrainingcenter.client.model.ISimpleTraining;
import ch.opentrainingcenter.tcx.ActivityT;

public class TrainingOverviewDatenAufbereiten {

    private static final Logger logger = Logger.getLogger(TrainingOverviewDatenAufbereiten.class);

    private final List<ISimpleTraining> all;
    private final List<ISimpleTraining> trainingsPerWeek = new ArrayList<ISimpleTraining>();
    private final List<ISimpleTraining> trainingsPerMonth = new ArrayList<ISimpleTraining>();
    private final List<ISimpleTraining> trainingsPerYear = new ArrayList<ISimpleTraining>();

    private final TrainingCenterDataCache cache;

    private final IStatistikCreator statistik;

    public TrainingOverviewDatenAufbereiten(final IStatistikCreator statistik) {
        this.statistik = statistik;
        cache = TrainingCenterDataCache.getInstance();
        all = cache.getAllSimpleTrainings();
        cache.addListener(new IRecordListener() {

            @Override
            public void recordChanged(final Collection<ActivityT> entry) {
                update();
            }

            @Override
            public void deleteRecord(final Collection<ActivityT> entry) {
                update();
            }

        });
        update();
    }

    private void update() {
        logger.debug("update daten vom cache"); //$NON-NLS-1$

        trainingsPerWeek.clear();
        final Map<Integer, Map<Integer, List<ISimpleTraining>>> trainingsProWoche = statistik.getTrainingsProWoche(all);
        trainingsPerWeek.addAll(SimpleTrainingCalculator.createSum(trainingsProWoche, null));

        trainingsPerMonth.clear();
        final Map<Integer, Map<Integer, List<ISimpleTraining>>> trainingsProMonat = statistik.getTrainingsProMonat(all);
        trainingsPerMonth.addAll(SimpleTrainingCalculator.createSum(trainingsProMonat, null));

        trainingsPerYear.clear();
        final Map<Integer, List<ISimpleTraining>> trainingsProJahr = statistik.getTrainingsProJahr(all);
        final Map<Integer, Map<Integer, List<ISimpleTraining>>> tmp = new HashMap<Integer, Map<Integer, List<ISimpleTraining>>>();
        tmp.put(1, trainingsProJahr);
        trainingsPerYear.addAll(SimpleTrainingCalculator.createSum(tmp, null));
    }

    public List<ISimpleTraining> getTrainingsPerDay() {
        return Collections.unmodifiableList(all);
    }

    public List<ISimpleTraining> getTrainingsPerMonth() {
        return Collections.unmodifiableList(trainingsPerMonth);
    }

    public List<ISimpleTraining> getTrainingsPerWeek() {
        return Collections.unmodifiableList(trainingsPerWeek);
    }

    public List<ISimpleTraining> getTrainingsPerYear() {
        return Collections.unmodifiableList(trainingsPerYear);
    }

}
