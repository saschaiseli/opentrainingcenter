package ch.opentrainingcenter.importer.impl;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.eclipse.core.runtime.IProgressMonitor;
import org.joda.time.DateTime;

import ch.opentrainingcenter.core.db.IDatabaseAccess;
import ch.opentrainingcenter.core.exceptions.ConvertException;
import ch.opentrainingcenter.core.helper.RunType;
import ch.opentrainingcenter.core.importer.ConvertContainer;
import ch.opentrainingcenter.core.importer.IFileCopy;
import ch.opentrainingcenter.i18n.Messages;
import ch.opentrainingcenter.importer.IFileImport;
import ch.opentrainingcenter.model.importer.IGpsFileModel;
import ch.opentrainingcenter.model.importer.IGpsFileModelWrapper;
import ch.opentrainingcenter.model.strecke.StreckeModel;
import ch.opentrainingcenter.transfer.CommonTransferFactory;
import ch.opentrainingcenter.transfer.IAthlete;
import ch.opentrainingcenter.transfer.IRoute;
import ch.opentrainingcenter.transfer.ITraining;
import ch.opentrainingcenter.transfer.ITrainingType;

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
    public List<ITraining> importFile(final String filterPath, final IGpsFileModelWrapper modelWrapper, final IProgressMonitor monitor) {
        final List<ITraining> activitiesToImport = new ArrayList<ITraining>();
        for (final IGpsFileModel model : modelWrapper.getGpsFileModels()) {
            final File file = new File(filterPath, model.getFileName());
            final String fileName = file.getName();
            monitor.setTaskName(Messages.FileImport_0 + fileName);
            LOGGER.info(Messages.FileImport_1 + fileName);

            try {
                fileCopy.copyFile(file, new File(locationBackupFiles, fileName));
            } catch (final IOException e) {
                LOGGER.error(String.format("File %s konnte nicht kopiert werden", fileName), e); //$NON-NLS-1$
                continue;
            }

            final ITraining training;
            try {
                training = cc.getMatchingConverter(file).convert(file);
            } catch (final ConvertException e) {
                LOGGER.error(String.format(Messages.FileImport_2, fileName), e);
                continue;
            }

            training.setAthlete(athlete);
            training.setFileName(fileName);
            training.setDateOfImport(DateTime.now().toDate());

            final StreckeModel route = model.getRoute();
            if (route != null) {
                final IRoute strecke = dbAccess.getRoute(route.getName(), athlete);
                training.setRoute(strecke);
            }
            final RunType typ = model.getTyp();
            final ITrainingType tt = CommonTransferFactory.createTrainingType(typ.getIndex(), typ.getTitle(), typ.getTitle());
            training.setTrainingType(tt);
            LOGGER.info(Messages.FileImport_3);
            final long start = DateTime.now().getMillis();
            dbAccess.saveTraining(training);
            LOGGER.info(Messages.FileImport_4 + (DateTime.now().getMillis() - start) + Messages.FileImport_5);
            activitiesToImport.add(training);

            monitor.worked(1);
        }
        return activitiesToImport;
    }
}
