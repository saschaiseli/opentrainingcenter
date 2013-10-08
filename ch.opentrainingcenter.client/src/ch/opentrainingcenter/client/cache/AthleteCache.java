package ch.opentrainingcenter.client.cache;

import ch.opentrainingcenter.core.cache.AbstractCache;
import ch.opentrainingcenter.transfer.IAthlete;

public class AthleteCache extends AbstractCache<String, IAthlete> {

    private static final AthleteCache INSTANCE = new AthleteCache();

    private AthleteCache() {
        // do not create instance. this cache is a singleton
    }

    public static AthleteCache getInstance() {
        return INSTANCE;
    }

    @Override
    public String getKey(final IAthlete value) {
        return value.getName();
    }

}
