package ch.iseli.sportanalyzer.client.action;

import java.io.File;
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
import ch.iseli.sportanalyzer.client.helper.FileCopy;
import ch.iseli.sportanalyzer.client.views.IImageKeys;
import ch.iseli.sportanalyzer.db.DatabaseAccessFactory;
import ch.iseli.sportanalyzer.importer.IConvert2Tcx;
import ch.iseli.sportanalyzer.tcx.TrainingCenterDatabaseT;
import ch.opentrainingcenter.transfer.IAthlete;

public class ImportManualGpsFiles extends Action implements ISelectionListener, IWorkbenchAction {

    public static final String ID = "ch.iseli.sportanalyzer.client.importManualFiles";

    private static final Logger logger = Logger.getLogger(ImportManualGpsFiles.class.getName());

    private final IWorkbenchWindow window;

    private IAthlete athlete;

    private final String defaultLocation;

    private final String locationForBackupFiles;

    public ImportManualGpsFiles(final IWorkbenchWindow window, final String toolTipText) {
        this.window = window;
        setId(ID);
        setToolTipText(toolTipText);
        final String athleteId = Activator.getDefault().getPreferenceStore().getString(PreferenceConstants.ATHLETE_ID);
        if (validId(athleteId)) {
            final int id = Integer.parseInt(athleteId);
            athlete = DatabaseAccessFactory.getDatabaseAccess().getAthlete(id);
        } else {
            athlete = null;
            logger.error("Athlete ist nicht gesetzt....");
        }
        defaultLocation = Activator.getDefault().getPreferenceStore().getString(PreferenceConstants.GPS_FILE_LOCATION);
        locationForBackupFiles = Activator.getDefault().getPreferenceStore().getString(PreferenceConstants.GPS_FILE_LOCATION_PROG);
        setImageDescriptor(AbstractUIPlugin.imageDescriptorFromPlugin(Application.ID, IImageKeys.IMPORT_GPS));
        window.getSelectionService().addSelectionListener(this);
    }

    @Override
    public void selectionChanged(final IWorkbenchPart part, final ISelection selection) {

    }

    @Override
    public void dispose() {
        window.getSelectionService().removeSelectionListener(this);
    }

    private boolean validId(final String athleteId) {
        return athleteId != null && athleteId.length() > 0;
    }

    @Override
    public void run() {
        final IConfigurationElement[] configurationElementsFor = Platform.getExtensionRegistry().getConfigurationElementsFor(
                "ch.iseli.sportanalyzer.myimporter");
        final IConvert2Tcx tcx = getConverterImplementation(configurationElementsFor);
        final FileDialog fileDialog = new FileDialog(window.getShell(), SWT.MULTI);
        fileDialog.setFilterPath(defaultLocation);
        fileDialog.setFilterExtensions(new String[] { "*." + tcx.getFilePrefix() });
        fileDialog.setFilterNames(new String[] { "Configuration files (*." + tcx.getFilePrefix() + ")" });
        fileDialog.setText("FileDialog");
        final String s = fileDialog.open();
        if (s != null) {
            final String[] fileNames = fileDialog.getFileNames();
            final String filterPath = fileDialog.getFilterPath();
            for (final String string : fileNames) {
                logger.debug("File " + string + " selektiert");
            }
            final Map<Integer, TrainingCenterRecord> allRecords = new HashMap<Integer, TrainingCenterRecord>();
            final Job job = new Job("Lade GPS Daten") {
                @Override
                protected IStatus run(final IProgressMonitor monitor) {
                    // Set total number of work units
                    monitor.beginTask("Lade GPS Daten...", fileNames.length);
                    try {
                        for (final String fileName : fileNames) {
                            final File file = new File(filterPath, fileName);
                            monitor.setTaskName("importiere File: " + file.getName());
                            logger.info("importiere File: " + file.getName());
                            final TrainingCenterDatabaseT record = tcx.convert(file);
                            logger.info("record: " + record != null ? record.toString() : " record ist null");
                            final Integer importRecordId = DatabaseAccessFactory.getDatabaseAccess().importRecord(athlete.getId(), file.getName());
                            allRecords.put(importRecordId, new TrainingCenterRecord(importRecordId, record));
                            FileCopy.copyFile(file, new File(locationForBackupFiles, file.getName()));
                            monitor.worked(1);
                        }
                    } catch (final Exception e1) {
                        e1.printStackTrace();
                    }
                    Display.getDefault().asyncExec(new Runnable() {

                        @Override
                        public void run() {
                            try {
                                TrainingCenterDataCache.getInstance().addAll(allRecords);

                            } catch (final Exception e) {
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

    private IConvert2Tcx getConverterImplementation(final IConfigurationElement[] configurationElementsFor) {
        for (final IConfigurationElement element : configurationElementsFor) {
            try {
                logger.info("plugin extension suchen: " + "class");
                final String[] attributeNames = element.getAttributeNames();
                for (final String str : attributeNames) {
                    logger.info("Attr name: '" + str + "'");
                }
                return (IConvert2Tcx) element.createExecutableExtension("class");
            } catch (final CoreException e) {
                logger.error(e.getMessage());
            }
        }
        return null;
    }
}
