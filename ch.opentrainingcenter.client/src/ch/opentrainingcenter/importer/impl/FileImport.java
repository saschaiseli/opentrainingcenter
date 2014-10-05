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
import ch.opentrainingcenter.core.importer.ConvertContainer;
import ch.opentrainingcenter.core.importer.IFileCopy;
import ch.opentrainingcenter.i18n.Messages;
import ch.opentrainingcenter.importer.IFileImport;
import ch.opentrainingcenter.model.importer.IGpsFileModel;
import ch.opentrainingcenter.model.importer.IGpsFileModelWrapper;
import ch.opentrainingcenter.model.strecke.StreckeModel;
import ch.opentrainingcenter.transfer.IAthlete;
import ch.opentrainingcenter.transfer.IRoute;
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
    public void importFile(final String filterPath, final IGpsFileModelWrapper modelWrapper, final IProgressMonitor monitor) {
        LOGGER.info("------------------------------- START Converting -------------------------------------------"); //$NON-NLS-1$
        final long startConvert = DateTime.now().getMillis();
        monitor.beginTask(Messages.FileImport_KonvertGpsFile, modelWrapper.size() * 2);
        final List<ITraining> trainings = new ArrayList<ITraining>();
        for (final IGpsFileModel model : modelWrapper.getGpsFileModels()) {
            final ITraining training = convert(filterPath, model, monitor);
            if (training != null) {
                trainings.add(training);
            }
            monitor.worked(1);
        }
        LOGGER.info("------------------------------- END Converting-------------------------------------------"); //$NON-NLS-1$
        final long convertTime = DateTime.now().getMillis() - startConvert;
        LOGGER.info(String.format("Die konvertierten Trainings (%s Stueck) werden in der DB gespeichert.", trainings.size())); //$NON-NLS-1$
        final long startDb = DateTime.now().getMillis();
        monitor.beginTask(Messages.FileImport_SaveGpsFiles, trainings.size());
        for (final ITraining training : trainings) {
            monitor.setTaskName(String.format(Messages.FileImport_SaveGpsFile, training.getFileName()));
            dbAccess.saveOrUpdate(training);
            monitor.worked(1);
        }
        final long dbTime = DateTime.now().getMillis() - startDb;
        LOGGER.info("*******************************************************************************"); //$NON-NLS-1$
        LOGGER.info("Import Report"); //$NON-NLS-1$
        LOGGER.info(String.format("Das Konvertieren aller '%s' GPS Files dauerte  %s[ms]", modelWrapper.size(), convertTime)); //$NON-NLS-1$
        LOGGER.info(String.format("Das Speichern aller '%s' Datensätze dauerte  %s[ms]", trainings.size(), dbTime)); //$NON-NLS-1$
        LOGGER.info("*******************************************************************************"); //$NON-NLS-1$
    }

    private ITraining convert(final String filterPath, final IGpsFileModel model, final IProgressMonitor monitor) {
        final long start = DateTime.now().getMillis();
        final File file = new File(filterPath, model.getFileName());
        final String fileName = file.getName();
        monitor.setTaskName(String.format(Messages.FileImport_KonvertiereGpsFile, fileName));
        LOGGER.info(Messages.FileImport_0 + fileName);

        try {
            fileCopy.copyFile(file, new File(locationBackupFiles, fileName));
        } catch (final IOException e) {
            LOGGER.error(String.format("File %s konnte nicht kopiert werden", fileName), e); //$NON-NLS-1$
            return null;
        }

        final ITraining training;
        try {
            training = cc.getMatchingConverter(file).convert(file);
        } catch (final ConvertException e) {
            LOGGER.error(String.format(Messages.FileImport_2, fileName), e);
            return null;
        }

        training.setAthlete(athlete);
        training.setFileName(fileName);
        training.setDateOfImport(DateTime.now().toDate());
        training.setShoe(model.getSchuh());
        final StreckeModel route = model.getRoute();
        if (route != null) {
            final IRoute strecke = dbAccess.getRoute(route.getName(), athlete);
            training.setRoute(strecke);
        }
        training.setTrainingType(model.getTyp());
        LOGGER.info(String.format("Konvertierung von '%s' dauerte %s[ms]", fileName, (DateTime.now().getMillis() - start))); //$NON-NLS-1$
        return training;
    }
}
