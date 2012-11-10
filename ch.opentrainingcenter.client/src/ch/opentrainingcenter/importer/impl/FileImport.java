package ch.opentrainingcenter.importer.impl;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.eclipse.core.runtime.IProgressMonitor;

import ch.opentrainingcenter.core.db.IDatabaseAccess;
import ch.opentrainingcenter.core.importer.ConvertContainer;
import ch.opentrainingcenter.core.importer.IFileCopy;
import ch.opentrainingcenter.importer.IFileImport;
import ch.opentrainingcenter.model.TrainingOverviewFactory;
import ch.opentrainingcenter.model.importer.IGpsFileModel;
import ch.opentrainingcenter.model.importer.IGpsFileModelWrapper;
import ch.opentrainingcenter.tcx.ActivityT;
import ch.opentrainingcenter.transfer.IAthlete;
import ch.opentrainingcenter.transfer.ITraining;

public class FileImport implements IFileImport {

    private static final Logger LOGGER = Logger.getLogger(FileImport.class.getName());

    private final ConvertContainer cc;

    private final String locationBackupFiles;

    private final IAthlete athlete;

    private final IDatabaseAccess dbAccess;

    private final IFileCopy fileCopy;

    /**
     * @param cc
     *            {@link ConvertContainer}
     * @param athlete
     *            der Sportler oder die Sportlerin
     * @param dbAccess
     *            Access auf die Datenbank
     * @param backup
     *            Ort, wo die importieren Files hinkopiert werden
     */
    public FileImport(final ConvertContainer cc, final IAthlete athlete, final IDatabaseAccess dbAccess, final String backup, final IFileCopy fileCopy) {
        this.cc = cc;
        this.athlete = athlete;
        this.dbAccess = dbAccess;
        this.locationBackupFiles = backup;
        this.fileCopy = fileCopy;
    }

    @Override
    public List<ActivityT> importFile(final String filterPath, final IGpsFileModelWrapper modelWrapper, final IProgressMonitor monitor) throws Exception {
        final List<ActivityT> activitiesToImport = new ArrayList<ActivityT>();
        for (final IGpsFileModel model : modelWrapper.getGpsFileModels()) {
            final File file = new File(filterPath, model.getFileName());
            monitor.setTaskName("Importiere File" + file.getName());
            LOGGER.info("importiere File: " + file.getName()); //$NON-NLS-1$
            final List<ActivityT> activities = cc.getMatchingConverter(file).convertActivity(file);

            activitiesToImport.addAll(importRecords(model, file, activities));

            fileCopy.copyFile(file, new File(locationBackupFiles, file.getName()));
            monitor.worked(1);
        }
        return activitiesToImport;
    }

    private List<ActivityT> importRecords(final IGpsFileModel model, final File file, final List<ActivityT> activities) {
        final List<ActivityT> result = new ArrayList<ActivityT>();
        for (final ActivityT activity : activities) {
            final ITraining overview = TrainingOverviewFactory.creatTrainingOverview(activity);

            final int id = dbAccess.importRecord(athlete.getId(), file.getName(), activity.getId().toGregorianCalendar().getTime(), overview, model.getId());
            if (id > 0) {
                // neu hinzugefügt
                result.add(activity);
            }
        }
        return result;
    }

}
