package ch.opentrainingcenter.core.cache;

import java.util.ArrayList;
import java.util.List;

import ch.opentrainingcenter.core.sort.TrainingComparator;
import ch.opentrainingcenter.transfer.IAthlete;
import ch.opentrainingcenter.transfer.IRoute;
import ch.opentrainingcenter.transfer.ITraining;
import ch.opentrainingcenter.transfer.IWeather;

public class TrainingCache extends AbstractCache<Long, ITraining> implements Cache {

    private static final TrainingCache INSTANCE = new TrainingCache();

    public static TrainingCache getInstance() {
        return INSTANCE;
    }

    @Override
    public List<ITraining> getAll() {
        return super.getSortedElements(new TrainingComparator());
    }

    @Override
    public Long getKey(final ITraining value) {
        return value.getDatum();
    }

    @Override
    public void update(final Long datum, final String note, final IWeather weather, final IRoute route) {
        final ITraining training = get(datum);
        if (training != null) {
            training.setNote(note);
            training.setWeather(weather);
            training.setRoute(route);
            final List<ITraining> models = new ArrayList<>();
            models.add(training);
            addAll(models);
        }
    }

    /**
     * @return die gecachten trainings von dem angegeben athleten. Wenn keine
     *         Trainings gefunden werden, wird eine leere Liste zurueckgegeben.
     */
    public List<ITraining> getAll(final IAthlete athlete) {
        final List<ITraining> result = new ArrayList<>();
        final List<ITraining> all = super.getSortedElements(new TrainingComparator());
        for (final ITraining training : all) {
            if (training.getAthlete().equals(athlete)) {
                result.add(training);
            }
        }
        return result;
    }

    @Override
    public String getName() {
        return "Training"; //$NON-NLS-1$
    }

}
