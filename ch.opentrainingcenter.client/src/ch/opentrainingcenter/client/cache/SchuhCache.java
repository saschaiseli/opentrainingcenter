package ch.opentrainingcenter.client.cache;

import ch.opentrainingcenter.core.cache.AbstractCache;
import ch.opentrainingcenter.transfer.IShoe;

public class SchuhCache extends AbstractCache<String, IShoe> {

    private static final SchuhCache INSTANCE = new SchuhCache();

    private SchuhCache() {
        // do not create instance. this cache is a singleton
    }

    public static SchuhCache getInstance() {
        return INSTANCE;
    }

    @Override
    public String getKey(final IShoe schuh) {
        return String.valueOf(schuh.getId());
    }

    @Override
    public String getName() {
        return "IShoe"; //$NON-NLS-1$
    }

}
