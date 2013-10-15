package ch.opentrainingcenter.core.cache;

import ch.opentrainingcenter.transfer.IRoute;
import ch.opentrainingcenter.transfer.ITraining;
import ch.opentrainingcenter.transfer.IWeather;

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
    public void updateExtension(final Long datum, final String note, final IWeather weather, final IRoute route) {
        final ITraining training = get(datum);
        if (training != null) {
            training.setNote(note);
            training.setWeather(weather);
            training.setRoute(route);
            add(training);
        }
    }

    @Override
    public String toString() {
        return "Cache: Anzahl Elemente: " + size(); //$NON-NLS-1$
    }

}
