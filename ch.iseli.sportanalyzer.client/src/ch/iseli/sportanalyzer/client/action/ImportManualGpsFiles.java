package ch.iseli.sportanalyzer.client.action;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.ui.ISelectionListener;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.actions.ActionFactory.IWorkbenchAction;
import org.eclipse.ui.plugin.AbstractUIPlugin;

import ch.iseli.sportanalyzer.client.Activator;
import ch.iseli.sportanalyzer.client.Application;
import ch.iseli.sportanalyzer.client.PreferenceConstants;
import ch.iseli.sportanalyzer.client.cache.TrainingCenterDataCache;
import ch.iseli.sportanalyzer.client.cache.TrainingCenterRecord;
import ch.iseli.sportanalyzer.client.helper.DaoHelper;
import ch.iseli.sportanalyzer.client.images.IImageKeys;
import ch.iseli.sportanalyzer.db.IImportedDao;
import ch.iseli.sportanalyzer.importer.IConvert2Tcx;
import ch.iseli.sportanalyzer.tcx.TrainingCenterDatabaseT;
import ch.opentrainingcenter.transfer.impl.Athlete;

public class ImportManualGpsFiles extends Action implements ISelectionListener, IWorkbenchAction {

    public static final String ID = "ch.iseli.sportanalyzer.client.importManualFiles";

    private static final Logger logger = Logger.getLogger(ImportManualGpsFiles.class.getName());

    private final IImportedDao dao;

    private final IWorkbenchWindow window;

    private Athlete athlete;

    private final String defaultLocation;

    public ImportManualGpsFiles(IWorkbenchWindow window, String toolTipText) {
        this.window = window;
        setId(ID);
        setToolTipText(toolTipText);
        IConfigurationElement[] daos = Platform.getExtensionRegistry().getConfigurationElementsFor("ch.opentrainingdatabase.db");
        dao = (IImportedDao) DaoHelper.getDao(daos, IImportedDao.EXTENSION_POINT_NAME);
        String athleteId = Activator.getDefault().getPreferenceStore().getString(PreferenceConstants.ATHLETE_NAME);
        if (validId(athleteId)) {
            final int id = Integer.parseInt(athleteId);
            athlete = dao.getAthlete(id);
        } else {
            athlete = null;
            logger.error("Athlete ist nicht gesetzt....");
        }
        defaultLocation = Activator.getDefault().getPreferenceStore().getString(PreferenceConstants.GPS_FILE_LOCATION);
        setImageDescriptor(AbstractUIPlugin.imageDescriptorFromPlugin(Application.ID, IImageKeys.IMPORT_GPS));
        window.getSelectionService().addSelectionListener(this);
    }

    @Override
    public void selectionChanged(IWorkbenchPart part, ISelection selection) {

    }

    @Override
    public void dispose() {
        window.getSelectionService().removeSelectionListener(this);
    }

    private boolean validId(String athleteId) {
        return athleteId != null && athleteId.length() > 0;
    }

    @Override
    public void run() {
        final FileDialog fileDialog = new FileDialog(window.getShell(), SWT.MULTI);
        fileDialog.setFilterPath(defaultLocation);
        fileDialog.setFilterExtensions(new String[] { "*.gmn" });
        fileDialog.setFilterNames(new String[] { "Configuration files (*.gmn)" });
        fileDialog.setText("FileDialog");
        final String s = fileDialog.open();
        if (s != null) {
            final String[] fileNames = fileDialog.getFileNames();
            final String filterPath = fileDialog.getFilterPath();
            for (String string : fileNames) {
                logger.debug("File " + string + " selektiert");
            }
            IConfigurationElement[] configurationElementsFor = Platform.getExtensionRegistry().getConfigurationElementsFor("ch.iseli.sportanalyzer.myimporter");
            final IConvert2Tcx tcx = getConverterImplementation(configurationElementsFor);
            final Map<Integer, TrainingCenterRecord> allRecords = new HashMap<Integer, TrainingCenterRecord>();
            final Job job = new Job("Lade GPS Daten") {
                @Override
                protected IStatus run(IProgressMonitor monitor) {
                    // Set total number of work units
                    monitor.beginTask("Lade GPS Daten...", fileNames.length);
                    try {
                        for (String fileName : fileNames) {
                            File file = new File(filterPath, fileName);
                            monitor.setTaskName("importiere File: " + file.getName());
                            TrainingCenterDatabaseT record = tcx.convert(file);
                            Integer importRecordId = dao.importRecord(athlete, file.getName());
                            allRecords.put(importRecordId, new TrainingCenterRecord(importRecordId, record));
                            copyFile(file, new File(defaultLocation, file.getName()));
                            monitor.worked(1);
                        }
                    } catch (Exception e1) {
                        e1.printStackTrace();
                    }
                    Display.getDefault().asyncExec(new Runnable() {

                        @Override
                        public void run() {
                            try {
                                TrainingCenterDataCache.getInstance().addAll(allRecords);

                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    });
                    return Status.OK_STATUS;
                }
            };
            job.schedule();
        }
    }

    private void copyFile(File sourceFile, File destFile) throws IOException {
        if (!destFile.exists()) {
            destFile.createNewFile();
        }

        FileChannel source = null;
        FileChannel destination = null;
        try {
            source = new FileInputStream(sourceFile).getChannel();
            destination = new FileOutputStream(destFile).getChannel();

            long count = 0;
            long size = source.size();
            while ((count += destination.transferFrom(source, 0, size - count)) < size)
                ;
        } finally {
            if (source != null) {
                source.close();
            }
            if (destination != null) {
                destination.close();
            }
        }
    }

    private IConvert2Tcx getConverterImplementation(IConfigurationElement[] configurationElementsFor) {
        for (final IConfigurationElement element : configurationElementsFor) {
            try {
                return (IConvert2Tcx) element.createExecutableExtension("class");
            } catch (CoreException e) {
                logger.error(e.getMessage());
            }
        }
        return null;
    }
}
