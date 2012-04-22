package ch.opentrainingcenter.importer;

import org.apache.log4j.Logger;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;

import ch.opentrainingcenter.client.cache.TrainingCenterDataCache;
import ch.opentrainingcenter.tcx.ActivityT;
import ch.opentrainingcenter.transfer.IImported;

public class ImportActivityJob extends Job {

    public static final Logger logger = Logger.getLogger(ImportActivityJob.class);

    private final IImported record;
    private final IGpsFileLoader loadGpsFile;
    private final TrainingCenterDataCache cache;
    private ActivityT selected;

    public ImportActivityJob(final String name, final IImported record) {
        super(name);
        this.record = record;
        this.loadGpsFile = GpsFileLoaderFactory.createGpsFileLoader();
        this.cache = TrainingCenterDataCache.getInstance();
    }

    @Override
    protected IStatus run(final IProgressMonitor monitor) {
        selected = loadActivity(record);
        return Status.OK_STATUS;
    }

    private ActivityT loadActivity(final IImported selectedRecord) {
        cache.setSelectedRun(selectedRecord);
        ActivityT result = null;
        if (!cache.contains(selectedRecord.getActivityId())) {
            try {
                result = loadGpsFile.convertActivity(selectedRecord);
                cache.add(result);
            } catch (final Exception e) {
                logger.error("Konnte File nicht einlesen"); //$NON-NLS-1$
            }
        } else {
            // read from cache
            result = cache.get(selectedRecord.getActivityId());
        }
        return result;
    }

    public ActivityT getLoaded() {
        return selected;
    }
}
