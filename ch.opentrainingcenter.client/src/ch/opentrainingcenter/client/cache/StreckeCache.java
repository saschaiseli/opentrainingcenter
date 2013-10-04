package ch.opentrainingcenter.client.cache;

import ch.opentrainingcenter.core.cache.AbstractCache;
import ch.opentrainingcenter.model.strecke.StreckeModel;

/**
 * Cache für die verschiedenen Strecken.
 * 
 */
public final class StreckeCache extends AbstractCache<String, StreckeModel> {

    private static final StreckeCache INSTANCE = new StreckeCache();

    private StreckeCache() {
        // do not create instance. this cache is a singleton
    }

    public static StreckeCache getInstance() {
        return INSTANCE;
    }

    @Override
    public String getKey(final StreckeModel value) {
        return value.getName();
    }
}
