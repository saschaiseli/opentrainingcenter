package ch.opentrainingcenter.importer;

import java.util.List;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;

import ch.opentrainingcenter.client.cache.TrainingCenterDataCache;
import ch.opentrainingcenter.db.DatabaseAccessFactory;
import ch.opentrainingcenter.transfer.IAthlete;
import ch.opentrainingcenter.transfer.IImported;

public class ImportJob extends Job {

    private final IAthlete athlete;

    // private final ConvertHandler convertHandler;

    public ImportJob(final String name, final IAthlete athlete) {
        super(name);
        this.athlete = athlete;
    }

    @Override
    protected IStatus run(final IProgressMonitor monitor) {
        final List<IImported> allImported = DatabaseAccessFactory.getDatabaseAccess().getAllImported(athlete);
        TrainingCenterDataCache.getInstance().setSelectedProfile(athlete);
        TrainingCenterDataCache.getInstance().addAllImported(allImported);
        return Status.OK_STATUS;
    }
}
