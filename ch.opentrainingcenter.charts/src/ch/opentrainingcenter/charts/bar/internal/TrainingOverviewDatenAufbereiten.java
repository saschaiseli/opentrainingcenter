package ch.opentrainingcenter.charts.bar.internal;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import ch.opentrainingcenter.charts.bar.IStatistikCreator;
import ch.opentrainingcenter.core.db.IDatabaseAccess;
import ch.opentrainingcenter.core.helper.RunType;
import ch.opentrainingcenter.core.helper.TimeHelper;
import ch.opentrainingcenter.model.ModelFactory;
import ch.opentrainingcenter.model.training.ISimpleTraining;
import ch.opentrainingcenter.transfer.IAthlete;
import ch.opentrainingcenter.transfer.IImported;

public class TrainingOverviewDatenAufbereiten {

    private static final Logger LOGGER = Logger.getLogger(TrainingOverviewDatenAufbereiten.class);

    private final List<ISimpleTraining> trainingsPerDay = new ArrayList<ISimpleTraining>();
    private final List<ISimpleTraining> trainingsPerWeek = new ArrayList<ISimpleTraining>();
    private final List<ISimpleTraining> trainingsPerMonth = new ArrayList<ISimpleTraining>();
    private final List<ISimpleTraining> trainingsPerYear = new ArrayList<ISimpleTraining>();

    private final IStatistikCreator statistik;

    private final IDatabaseAccess databaseAccess;

    private final IAthlete athlete;

    public TrainingOverviewDatenAufbereiten(final IStatistikCreator statistik, final IDatabaseAccess databaseAccess, final IAthlete athlete) {
        this.statistik = statistik;
        this.databaseAccess = databaseAccess;
        this.athlete = athlete;
        update(null);
    }

    /**
     * Filtert die Daten nach dem angegeben Lauftyp.
     */
    public final void update(final RunType type) {
        LOGGER.debug("update/filter daten vom cache: " + type); //$NON-NLS-1$
        trainingsPerDay.clear();

        final List<ISimpleTraining> allTrainings = new ArrayList<ISimpleTraining>();

        final List<IImported> allImported = databaseAccess.getAllImported(athlete);
        for (final IImported imp : allImported) {
            final ISimpleTraining simpleTraining = ModelFactory.createSimpleTraining(imp.getTraining(), athlete);
            simpleTraining.setType(RunType.getRunType(imp.getTrainingType().getId()));
            allTrainings.add(simpleTraining);
        }

        for (final ISimpleTraining training : allTrainings) {
            if (isTypeMatching(type, training.getType())) {
                trainingsPerDay.add(training);
            }
        }

        trainingsPerWeek.clear();
        final Map<Integer, Map<Integer, List<ISimpleTraining>>> trainingsProWoche = statistik.getTrainingsProWoche(allTrainings);
        trainingsPerWeek.addAll(SimpleTrainingCalculator.createSum(trainingsProWoche, type));

        trainingsPerMonth.clear();
        final Map<Integer, Map<Integer, List<ISimpleTraining>>> trainingsProMonat = statistik.getTrainingsProMonat(allTrainings);
        trainingsPerMonth.addAll(SimpleTrainingCalculator.createSum(trainingsProMonat, type));

        trainingsPerYear.clear();
        final Map<Integer, List<ISimpleTraining>> trainingsProJahr = statistik.getTrainingsProJahr(allTrainings);
        final Map<Integer, Map<Integer, List<ISimpleTraining>>> tmp = new HashMap<Integer, Map<Integer, List<ISimpleTraining>>>();
        tmp.put(1, trainingsProJahr);
        trainingsPerYear.addAll(SimpleTrainingCalculator.createSum(tmp, type));
    }

    private boolean isTypeMatching(final RunType filterType, final RunType trainingType) {
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
        final StringBuffer str = new StringBuffer();
        str.append("Übersicht auf die aufbereiteten Daten:\n"); //$NON-NLS-1$

        str.append("Trainings pro Monat:\n"); //$NON-NLS-1$
        printTraining(str, trainingsPerMonth);

        str.append("Trainings pro Woche:\n"); //$NON-NLS-1$
        printTraining(str, trainingsPerWeek);

        str.append("Trainings pro Tag:\n"); //$NON-NLS-1$
        printTraining(str, trainingsPerDay);

        return str.toString();
    }

    private void printTraining(final StringBuffer str, final List<ISimpleTraining> trainings) {
        for (final ISimpleTraining training : trainings) {
            final String date = TimeHelper.convertDateToString(training.getDatum(), false);
            final double distanzInMeter = training.getDistanzInMeter();
            final RunType type = training.getType();
            str.append(date).append(' ').append(distanzInMeter).append("[m]").append(" ").append(type).append('\n'); //$NON-NLS-1$ //$NON-NLS-2$
        }
    }
}
