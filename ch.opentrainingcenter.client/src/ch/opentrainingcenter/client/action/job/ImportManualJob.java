package ch.opentrainingcenter.client.action.job;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;

import ch.opentrainingcenter.i18n.Messages;
import ch.opentrainingcenter.importer.IFileImport;
import ch.opentrainingcenter.model.importer.IGpsFileModelWrapper;

/**
 * Importiert ein GPS File
 * 
 */
public class ImportManualJob extends Job {

    private final IGpsFileModelWrapper modelWrapper;
    private final String filterPath;
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
     */
    public ImportManualJob(final String name, final IGpsFileModelWrapper modelWrapper, final String filterPath, final IFileImport importer) {
        super(name);
        this.modelWrapper = modelWrapper;
        this.filterPath = filterPath;
        this.importer = importer;
    }

    @Override
    protected IStatus run(final IProgressMonitor monitor) {
        monitor.beginTask(Messages.ImportManualGpsFiles4, modelWrapper.size());
        importer.importFile(filterPath, modelWrapper, monitor);
        return Status.OK_STATUS;
    }
}
