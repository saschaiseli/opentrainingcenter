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
    private final ConvertContainer cc;
    private final String locationForBackupFiles;
    private final IAthlete athlete;
    private final IGpsFileModelWrapper modelWrapper;
    private final String filterPath;
    private final Cache cache;

    public ManualImportJob(final String name, final ConvertContainer cc, final String locationForBackupFiles, final IAthlete athlete, final IGpsFileModelWrapper modelWrapper,
            final String filterPath, final Cache cache) {
        super(name);
        this.cc = cc;
        this.locationForBackupFiles = locationForBackupFiles;
        this.athlete = athlete;
        this.modelWrapper = modelWrapper;
        this.filterPath = filterPath;
        this.cache = cache;
    }

    @Override
    protected IStatus run(final IProgressMonitor monitor) {
        monitor.beginTask(Messages.ImportManualGpsFiles_4, modelWrapper.size());
        final List<ActivityT> activitiesToImport = new ArrayList<ActivityT>();
        try {
            activitiesToImport.addAll(importFile(filterPath, modelWrapper, monitor));
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

    private List<ActivityT> importFile(final String filterPath, final IGpsFileModelWrapper modelWrapper, final IProgressMonitor monitor) throws Exception {
        final List<ActivityT> activitiesToImport = new ArrayList<ActivityT>();
        for (final IGpsFileModel model : modelWrapper.getGpsFileModels()) {
            final File file = new File(filterPath, model.getFileName());
            monitor.setTaskName(Messages.ImportManualGpsFiles_5 + file.getName());
            LOGGER.info("importiere File: " + file.getName()); //$NON-NLS-1$
            final List<ActivityT> activities = cc.getMatchingConverter(file).convertActivity(file);

            activitiesToImport.addAll(importRecords(model, file, activities));

            FileCopy.copyFile(file, new File(locationForBackupFiles, file.getName()));
            monitor.worked(1);
        }
        return activitiesToImport;
    }

    private List<ActivityT> importRecords(final IGpsFileModel model, final File file, final List<ActivityT> activities) {
        final List<ActivityT> result = new ArrayList<ActivityT>();
        for (final ActivityT activity : activities) {
            final ITraining overview = TrainingOverviewFactory.creatTrainingOverview(activity);
            final int id = DatabaseAccessFactory.getDatabaseAccess().importRecord(athlete.getId(), file.getName(), activity.getId().toGregorianCalendar().getTime(), overview,
                    model.getId());
            if (id > 0) {
                // neu hinzugefügt
                result.add(activity);
            }
        }
        return result;
    }
}
