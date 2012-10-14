package ch.opentrainingcenter.client.cache;

import java.util.List;

import org.apache.log4j.Logger;

import ch.opentrainingcenter.importer.IImportedConverter;
import ch.opentrainingcenter.tcx.ActivityT;
import ch.opentrainingcenter.transfer.IImported;

public class InitialLoad {

    private static final Logger LOG = Logger.getLogger(InitialLoad.class);

    private final IImportedConverter loader;
    private final Cache cache;

    public InitialLoad(final Cache cache, final IImportedConverter loader) {
        this.cache = cache;
        this.loader = loader;
    }

    public void initalLoad(final List<IImported> records) throws Exception {
        for (final IImported record : records) {
            final ActivityT activity = loader.convertImportedToActivity(record);
            cache.add(activity);
            LOG.info("Record hinzugefügt"); //$NON-NLS-1$
        }
    }
}
