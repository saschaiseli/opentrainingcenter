package ch.opentrainingcenter.client.action.job;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.swt.widgets.Display;

import ch.opentrainingcenter.client.Messages;
import ch.opentrainingcenter.client.cache.Cache;
import ch.opentrainingcenter.client.helper.FileCopy;
import ch.opentrainingcenter.client.model.IGpsFileModel;
import ch.opentrainingcenter.client.model.IGpsFileModelWrapper;
import ch.opentrainingcenter.client.model.TrainingOverviewFactory;
import ch.opentrainingcenter.db.DatabaseAccessFactory;
import ch.opentrainingcenter.importer.ConvertContainer;
import ch.opentrainingcenter.tcx.ActivityT;
import ch.opentrainingcenter.transfer.IAthlete;
import ch.opentrainingcenter.transfer.ITraining;

public class ManualImportJob extends Job {
    private static final Logger LOGGER = Logger.getLogger(ManualImportJob.class.getName());
    private final String filterPath;
    private final IGpsFileModelWrapper modelWrapper;
    private final ConvertContainer cc;
    private final String locationForBackupFiles;
    private final IAthlete athlete;
    private final Cache cache;

    public ManualImportJob(final String name, final String filterPath, final IGpsFileModelWrapper modelWrapper, final ConvertContainer cc, final String locationForBackupFiles,
            final IAthlete athlete, final Cache cache) {
        super(name);
        this.filterPath = filterPath;
        this.modelWrapper = modelWrapper;
        this.cc = cc;
        this.locationForBackupFiles = locationForBackupFiles;
        this.athlete = athlete;
        this.cache = cache;

    }

    @Override
    protected IStatus run(final IProgressMonitor monitor) {
        monitor.beginTask(Messages.ImportManualGpsFiles_4, modelWrapper.size());
        final List<ActivityT> activitiesToImport = new ArrayList<ActivityT>();
        final List<Integer> ids = new ArrayList<Integer>();
        try {
            importFile(filterPath, modelWrapper, monitor, activitiesToImport, ids);
        } catch (final Exception e1) {
            e1.printStackTrace();
        }
        Display.getDefault().asyncExec(new Runnable() {

            @Override
            public void run() {
                try {
                    cache.addAll(activitiesToImport);
                } catch (final Exception e) {
                    e.printStackTrace();
                }
            }
        });
        return Status.OK_STATUS;
    }

    private void importFile(final String filterPath, final IGpsFileModelWrapper modelWrapper, final IProgressMonitor monitor, final List<ActivityT> activitiesToImport,
            final List<Integer> ids) throws Exception {
        for (final IGpsFileModel model : modelWrapper.getGpsFileModels()) {
            final File file = new File(filterPath, model.getFileName());
            monitor.setTaskName(Messages.ImportManualGpsFiles_5 + file.getName());
            LOGGER.info("importiere File: " + file.getName()); //$NON-NLS-1$
            final List<ActivityT> activities = cc.getMatchingConverter(file).convertActivity(file);

            importRecords(activitiesToImport, ids, model, file, activities);
            FileCopy.copyFile(file, new File(locationForBackupFiles, file.getName()));
            monitor.worked(1);
        }
    }

    private void importRecords(final List<ActivityT> activitiesToImport, final List<Integer> ids, final IGpsFileModel model, final File file, final List<ActivityT> activities) {
        for (final ActivityT activity : activities) {
            final ITraining overview = TrainingOverviewFactory.creatTrainingOverview(activity);
            final int id = DatabaseAccessFactory.getDatabaseAccess().importRecord(athlete.getId(), file.getName(), activity.getId().toGregorianCalendar().getTime(), overview,
                    model.getId());
            if (id > 0) {
                // neu hinzugefügt
                activitiesToImport.add(activity);
                ids.add(id);
            }
        }
    }
}
