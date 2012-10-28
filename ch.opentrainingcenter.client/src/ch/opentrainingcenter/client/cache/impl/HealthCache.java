package ch.opentrainingcenter.client.cache.impl;

import ch.opentrainingcenter.client.model.navigation.impl.ConcreteHealth;

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
}
