package ch.opentrainingcenter.core.cache;

import java.util.List;

import ch.opentrainingcenter.transfer.IHealth;

/**
 * Cache um die Gesundheitszustände zu verwalten. Ruhepuls und Gewicht.
 */
public final class HealthCache extends AbstractCache<Integer, IHealth> {

    private static final HealthCache INSTANCE = new HealthCache();

    private HealthCache() {
        // do not create instance. this cache is a singleton
    }

    public static HealthCache getInstance() {
        return INSTANCE;
    }

    @Override
    public Integer getKey(final IHealth value) {
        return value.getId();
    }

    @Override
    public String toString() {
        final StringBuilder str = new StringBuilder();
        final List<IHealth> all = super.getAll();
        for (final IHealth element : all) {
            str.append(element.getId()).append(' ').append(element.getDateofmeasure()).append("\n"); //$NON-NLS-1$
        }
        return str.toString();
    }

    @Override
    public String getName() {
        return "ConcreteHealth"; //$NON-NLS-1$
    }
}
