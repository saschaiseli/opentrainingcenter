package ch.opentrainingcenter.client.action.job;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

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

/**
 * Importiert ein GPS File
 * 
 */
public class ImportManualJob extends Job {

    private static final int MAX_SIZE_OF_PRELOAD = 11;
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
        final List<ITraining> trainings = new ArrayList<ITraining>();

        final List<ITraining> importFiles = importer.importFile(filterPath, modelWrapper, monitor);
        trainings.addAll(importFiles);

        Display.getDefault().asyncExec(new Runnable() {

            @Override
            public void run() {
                if (trainings.size() > MAX_SIZE_OF_PRELOAD) {
                    Collections.sort(trainings, new Comparator<ITraining>() {

                        @Override
                        public int compare(final ITraining o1, final ITraining o2) {
                            return Long.valueOf(o1.getDatum()).compareTo(Long.valueOf(o2.getDatum()));
                        }
                    });
                    cache.addAll(trainings.subList(0, MAX_SIZE_OF_PRELOAD - 1));
                } else {
                    cache.addAll(trainings);
                }
            }
        });
        return Status.OK_STATUS;
    }
}
