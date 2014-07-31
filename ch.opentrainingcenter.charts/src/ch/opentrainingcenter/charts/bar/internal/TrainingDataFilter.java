package ch.opentrainingcenter.charts.bar.internal;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import ch.opentrainingcenter.core.db.IDatabaseAccess;
import ch.opentrainingcenter.core.helper.TimeHelper;
import ch.opentrainingcenter.model.ModelFactory;
import ch.opentrainingcenter.model.chart.IStatistikCreator;
import ch.opentrainingcenter.model.chart.SimpleTrainingCalculator;
import ch.opentrainingcenter.model.chart.StatistikFactory;
import ch.opentrainingcenter.model.training.ISimpleTraining;
import ch.opentrainingcenter.transfer.IAthlete;
import ch.opentrainingcenter.transfer.ITraining;
import ch.opentrainingcenter.transfer.TrainingType;

public class TrainingDataFilter {

    private static final Logger LOGGER = Logger.getLogger(TrainingDataFilter.class);

    private final List<ISimpleTraining> trainingsPerDay = new ArrayList<ISimpleTraining>();
    private final List<ISimpleTraining> trainingsPerWeek = new ArrayList<ISimpleTraining>();
    private final List<ISimpleTraining> trainingsPerMonth = new ArrayList<ISimpleTraining>();
    private final List<ISimpleTraining> trainingsPerYear = new ArrayList<ISimpleTraining>();

    private final IStatistikCreator statistik;

    private final IDatabaseAccess databaseAccess;

    private final IAthlete athlete;

    public TrainingDataFilter(final IDatabaseAccess databaseAccess, final IAthlete athlete) {
        this(StatistikFactory.createStatistik(), databaseAccess, athlete);
    }

    public TrainingDataFilter(final IStatistikCreator statistik, final IDatabaseAccess databaseAccess, final IAthlete athlete) {
        this.statistik = statistik;
        this.databaseAccess = databaseAccess;
        this.athlete = athlete;
        filter(null);
    }

    /**
     * Filtert die Daten nach dem angegeben Lauftyp.
     */
    public final void filter(final TrainingType type) {
        LOGGER.debug("update/filter daten vom cache: " + type); //$NON-NLS-1$
        trainingsPerDay.clear();

        final List<ISimpleTraining> allSimpleTrainings = convertToSimpleTraining();

        // trainings per day
        for (final ISimpleTraining training : allSimpleTrainings) {
            if (isTypeMatching(type, training.getType())) {
                trainingsPerDay.add(training);
            }
        }

        // trainings per week
        final Map<Integer, Map<Integer, List<ISimpleTraining>>> trainingsProWoche = statistik.getTrainingsProWoche(allSimpleTrainings);
        trainingsPerWeek.clear();
        trainingsPerWeek.addAll(SimpleTrainingCalculator.createSum(trainingsProWoche, type));

        // trainings per month
        final Map<Integer, Map<Integer, List<ISimpleTraining>>> trainingsProMonat = statistik.getTrainingsProMonat(allSimpleTrainings);
        trainingsPerMonth.clear();
        trainingsPerMonth.addAll(SimpleTrainingCalculator.createSum(trainingsProMonat, type));

        // trainings per year
        final Map<Integer, List<ISimpleTraining>> trainingsProJahr = statistik.getTrainingsProJahr(allSimpleTrainings);
        final Map<Integer, Map<Integer, List<ISimpleTraining>>> tmp = new HashMap<Integer, Map<Integer, List<ISimpleTraining>>>();
        tmp.put(1, trainingsProJahr);
        trainingsPerYear.clear();
        trainingsPerYear.addAll(SimpleTrainingCalculator.createSum(tmp, type));
    }

    private List<ISimpleTraining> convertToSimpleTraining() {
        final List<ISimpleTraining> filteredTrainings = new ArrayList<>();
        final List<ITraining> allImported = databaseAccess.getAllTrainings(athlete);
        for (final ITraining training : allImported) {
            final ISimpleTraining simpleTraining = ModelFactory.convertToSimpleTraining(training);
            simpleTraining.setType(training.getTrainingType());
            filteredTrainings.add(simpleTraining);
        }
        return filteredTrainings;
    }

    private boolean isTypeMatching(final TrainingType filterType, final TrainingType trainingType) {
        if (filterType == null || filterType.equals(trainingType)) {
            return true;
        }
        return false;
    }

    public List<ISimpleTraining> getTrainingsPerDay() {
        return Collections.unmodifiableList(trainingsPerDay);
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

    @Override
    public String toString() {
        final StringBuilder str = new StringBuilder();
        str.append("Übersicht auf die aufbereiteten Daten:\n"); //$NON-NLS-1$

        str.append("Trainings pro Monat:\n"); //$NON-NLS-1$
        printTraining(str, trainingsPerMonth);

        str.append("Trainings pro Woche:\n"); //$NON-NLS-1$
        printTraining(str, trainingsPerWeek);

        str.append("Trainings pro Tag:\n"); //$NON-NLS-1$
        printTraining(str, trainingsPerDay);

        return str.toString();
    }

    private void printTraining(final StringBuilder str, final List<ISimpleTraining> trainings) {
        for (final ISimpleTraining training : trainings) {
            final String date = TimeHelper.convertDateToString(training.getDatum(), false);
            final double distanzInMeter = training.getDistanzInMeter();
            final TrainingType type = training.getType();
            str.append(date).append(' ').append(distanzInMeter).append("[m]").append(" ").append(type).append('\n'); //$NON-NLS-1$ //$NON-NLS-2$
        }
    }
}
