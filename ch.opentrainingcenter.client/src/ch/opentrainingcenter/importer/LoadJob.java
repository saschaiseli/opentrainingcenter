package ch.opentrainingcenter.importer;

import java.util.List;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;

import ch.opentrainingcenter.client.cache.Cache;
import ch.opentrainingcenter.db.IDatabaseAccess;
import ch.opentrainingcenter.transfer.IAthlete;
import ch.opentrainingcenter.transfer.IImported;

public class LoadJob extends Job {

    private final IAthlete athlete;

    private final Cache cache;

    private final IDatabaseAccess databaseAccess;

    public LoadJob(final String name, final IAthlete athlete, final IDatabaseAccess databaseAccess, final Cache cache) {
        super(name);
        this.athlete = athlete;
        this.databaseAccess = databaseAccess;
        this.cache = cache;
    }

    @Override
    protected IStatus run(final IProgressMonitor monitor) {
        final List<IImported> allImported = databaseAccess.getAllImported(athlete);
        cache.setSelectedProfile(athlete);
        cache.addAllImported(allImported);
        return Status.OK_STATUS;
    }
}
