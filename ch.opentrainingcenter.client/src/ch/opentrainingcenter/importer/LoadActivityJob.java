package ch.opentrainingcenter.importer;

import org.apache.log4j.Logger;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;

import ch.opentrainingcenter.client.cache.Cache;
import ch.opentrainingcenter.tcx.ActivityT;
import ch.opentrainingcenter.transfer.IImported;

public class LoadActivityJob extends Job {

    public static final Logger LOGGER = Logger.getLogger(LoadActivityJob.class);

    private final IImported record;
    private final IImportedConverter loader;
    private final Cache cache;
    private ActivityT selected;

    public LoadActivityJob(final String name, final IImported record, final Cache cache, final IImportedConverter loader) {
        super(name);
        this.record = record;
        this.loader = loader;
        this.cache = cache;
    }

    @Override
    protected IStatus run(final IProgressMonitor monitor) {
        try {
            selected = loadActivity(record);
            return Status.OK_STATUS;
        } catch (final LoadImportedException e) {
            return Status.CANCEL_STATUS;
        }
    }

    private ActivityT loadActivity(final IImported selectedRecord) throws LoadImportedException {
        cache.setSelectedRun(selectedRecord);
        ActivityT result = null;
        if (!cache.contains(selectedRecord.getActivityId())) {
            try {
                result = loader.convertImportedToActivity(selectedRecord);
                cache.add(result);
            } catch (final Exception e) {
                LOGGER.error("Konnte File nicht einlesen"); //$NON-NLS-1$
                throw new LoadImportedException("Konnte File nicht einlesen");
            }
        } else {
            // read from cache
            result = cache.get(selectedRecord.getActivityId());
        }
        return result;
    }

    /**
     * Für Testzwecke..
     */
    protected ActivityT getLoaded() {
        return selected;
    }

}
