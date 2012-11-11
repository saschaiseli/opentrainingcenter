package ch.opentrainingcenter.client.cache.impl;

import java.util.List;

import ch.opentrainingcenter.core.cache.AbstractCache;
import ch.opentrainingcenter.model.navigation.ConcreteHealth;

/**
 * Cache um die Gesundheitszustände zu verwalten. Namentlich sind die Ruhepuls
 * und Gewicht.
 * 
 * @author sascha
 * 
 */
public class HealthCache extends AbstractCache<Integer, ConcreteHealth> {

    private static HealthCache INSTANCE = new HealthCache();

    private HealthCache() {
        // do not create instance. this cache is a singleton
    }

    public static HealthCache getInstance() {
        return INSTANCE;
    }

    @Override
    public Integer getKey(final ConcreteHealth value) {
        return value.getId();
    }

    @Override
    public String toString() {
        final StringBuffer str = new StringBuffer();
        final List<ConcreteHealth> all = super.getAll();
        for (final ConcreteHealth element : all) {
            str.append(element.getId()).append(' ').append(element.getDate()).append("\n"); //$NON-NLS-1$
        }
        return str.toString();
    }
}
