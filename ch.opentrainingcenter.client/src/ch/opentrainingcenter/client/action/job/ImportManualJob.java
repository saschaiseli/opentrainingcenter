package ch.opentrainingcenter.client.action.job;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.swt.widgets.Display;

import ch.opentrainingcenter.core.cache.Cache;
import ch.opentrainingcenter.i18n.Messages;
import ch.opentrainingcenter.importer.IFileImport;
import ch.opentrainingcenter.model.importer.IGpsFileModelWrapper;
import ch.opentrainingcenter.transfer.ITraining;

public class ImportManualJob extends Job {

    private static final Logger LOGGER = Logger.getLogger(ImportManualJob.class);
    private final IGpsFileModelWrapper modelWrapper;
    private final String filterPath;
    private final Cache cache;
    private final IFileImport importer;

    /**
     * @param name
     *            Name des Jobs
     * @param modelWrapper
     *            {@link IGpsFileModelWrapper}
     * @param filterPath
     *            Pfad wo die Files gesucht werden sollen
     * @param importer
     *            Importiert die Files in das System
     * @param cache
     *            Access zu Cache
     */
    public ImportManualJob(final String name, final IGpsFileModelWrapper modelWrapper, final String filterPath, final IFileImport importer, final Cache cache) {
        super(name);
        this.modelWrapper = modelWrapper;
        this.filterPath = filterPath;
        this.importer = importer;
        this.cache = cache;
    }

    @Override
    protected IStatus run(final IProgressMonitor monitor) {
        monitor.beginTask(Messages.ImportManualGpsFiles4, modelWrapper.size());
        final List<ITraining> activitiesToImport = new ArrayList<ITraining>();
        try {
            final List<ITraining> importFiles = importer.importFile(filterPath, modelWrapper, monitor);
            activitiesToImport.addAll(importFiles);
        } catch (final Exception e) {
            LOGGER.error("Fehler beim Importieren: ", e); //$NON-NLS-1$
        }
        Display.getDefault().asyncExec(new Runnable() {

            @Override
            public void run() {
                try {
                    cache.addAll(activitiesToImport);
                } catch (final Exception e) {
                    LOGGER.error(e);
                }
            }
        });
        return Status.OK_STATUS;
    }
}
