package ch.opentrainingcenter.client.cache;

import java.util.List;

import org.apache.log4j.Logger;
import org.eclipse.swt.widgets.ProgressBar;

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

    public void initalLoad(final ProgressBar fBar, final List<IImported> records) throws Exception {
        // fBar.setMaximum(records.size());
        // int i = 0;
        for (final IImported record : records) {
            final ActivityT activity = loader.convertImportedToActivity(record);
            cache.add(activity);
            // i++;
            // fBar.setSelection(i);
            LOG.info("Record hinzugefügt"); //$NON-NLS-1$
        }
    }
}
