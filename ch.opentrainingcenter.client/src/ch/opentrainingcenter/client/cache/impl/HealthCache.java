package ch.opentrainingcenter.client.cache.impl;

import ch.opentrainingcenter.transfer.IHealth;

/**
 * Cache um die Gesundheitszustände zu verwalten. Namentlich sind die Ruhepuls
 * und Gewicht.
 * 
 * @author sascha
 * 
 */
public class HealthCache extends AbstractCache<Integer, IHealth> {

    private static HealthCache INSTANCE = new HealthCache();

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
}
