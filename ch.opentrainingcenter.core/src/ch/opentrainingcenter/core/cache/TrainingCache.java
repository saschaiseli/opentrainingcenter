package ch.opentrainingcenter.core.cache;

import ch.opentrainingcenter.transfer.ActivityExtension;
import ch.opentrainingcenter.transfer.ITraining;

public class TrainingCache extends AbstractCache<Long, ITraining> implements Cache {

    private static final TrainingCache INSTANCE = new TrainingCache();

    public static Cache getInstance() {
        return INSTANCE;
    }

    @Override
    public Long getKey(final ITraining value) {
        return value.getDatum();
    }

    @Override
    public void updateExtension(final Long datum, final ActivityExtension extension) {
        final ITraining training = get(datum);
        if (training != null) {
            training.setNote(extension.getNote());
            training.setWeather(extension.getWeather());
            training.setRoute(extension.getRoute());
            add(training);
        }
    }

    @Override
    public void resetCache() {
        super.resetCache();
    }

    @Override
    public String toString() {
        return "Cache: Anzahl Elemente: " + size(); //$NON-NLS-1$
    }

}
