package ch.opentrainingcenter.client.action;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.ISelectionListener;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.actions.ActionFactory.IWorkbenchAction;
import org.eclipse.ui.plugin.AbstractUIPlugin;

import ch.opentrainingcenter.client.Activator;
import ch.opentrainingcenter.client.Application;
import ch.opentrainingcenter.client.Messages;
import ch.opentrainingcenter.client.PreferenceConstants;
import ch.opentrainingcenter.client.cache.TrainingCenterDataCache;
import ch.opentrainingcenter.client.helper.FileCopy;
import ch.opentrainingcenter.client.model.IGpsFileModel;
import ch.opentrainingcenter.client.model.IGpsFileModelWrapper;
import ch.opentrainingcenter.client.model.TrainingOverviewFactory;
import ch.opentrainingcenter.client.views.IImageKeys;
import ch.opentrainingcenter.db.DatabaseAccessFactory;
import ch.opentrainingcenter.importer.ConvertContainer;
import ch.opentrainingcenter.importer.ExtensionHelper;
import ch.opentrainingcenter.importer.IConvert2Tcx;
import ch.opentrainingcenter.tcx.ActivityT;
import ch.opentrainingcenter.transfer.IAthlete;
import ch.opentrainingcenter.transfer.ITraining;

public class ImportManualGpsFiles extends Action implements ISelectionListener, IWorkbenchAction {
    public static final String IMPORT_PATTERN = "_$_"; //$NON-NLS-1$

    private static final Logger LOGGER = Logger.getLogger(ImportManualGpsFiles.class.getName());

    public static final String ID = "ch.opentrainingcenter.client.importManualFiles"; //$NON-NLS-1$

    private final IWorkbenchWindow window;

    private IAthlete athlete;

    private final String defaultLocation;

    private final String locationForBackupFiles;

    private final IPreferenceStore preferenceStore;

    private final ConvertContainer cc;

    private final List<String> filePrefixes = new ArrayList<String>();

    public ImportManualGpsFiles(final IWorkbenchWindow window, final String toolTipText) {
        this.window = window;
        setId(ID);
        setToolTipText(toolTipText);
        preferenceStore = Activator.getDefault().getPreferenceStore();
        final String athleteId = preferenceStore.getString(PreferenceConstants.ATHLETE_ID);
        if (validId(athleteId)) {
            final int id = Integer.parseInt(athleteId);
            athlete = DatabaseAccessFactory.getDatabaseAccess().getAthlete(id);
        } else {
            athlete = null;
            LOGGER.error("Athlete ist nicht gesetzt...."); //$NON-NLS-1$
        }
        defaultLocation = preferenceStore.getString(PreferenceConstants.GPS_FILE_LOCATION);
        locationForBackupFiles = preferenceStore.getString(PreferenceConstants.GPS_FILE_LOCATION_PROG);

        cc = new ConvertContainer(ExtensionHelper.getConverters());

        setImageDescriptor(AbstractUIPlugin.imageDescriptorFromPlugin(Application.ID, IImageKeys.IMPORT_GPS_KLEIN));
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
        readCurrentPreferences();
        final Shell sh = window.getShell();
        final IConfigurationElement[] extensions = Platform.getExtensionRegistry().getConfigurationElementsFor(Application.IMPORT_EXTENSION_POINT);
        LOGGER.info("Anzahl Extensions: " + extensions.length); //$NON-NLS-1$
        final FileDialog fileDialog = new FileDialog(window.getShell(), SWT.MULTI);
        fileDialog.setFilterPath(defaultLocation);

        fileDialog.setFilterExtensions(filePrefixes.toArray(new String[0]));
        fileDialog.setText(Messages.ImportManualGpsFiles_FileDialog);
        final String s = fileDialog.open();
        if (s != null) {
            final String[] fileNames = fileDialog.getFileNames();
            final String filterPath = fileDialog.getFilterPath();

            final RunTypeDialog dialog = new RunTypeDialog(sh, fileNames);
            final int open = dialog.open();
            if (open >= 0) {
                final IGpsFileModelWrapper modelWrapper = dialog.getModelWrapper();
                final Job job = new Job(Messages.ImportManualGpsFiles_LadeGpsFiles) {
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
                                    TrainingCenterDataCache.getInstance().addAll(activitiesToImport);
                                } catch (final Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                        return Status.OK_STATUS;
                    }

                    private void importFile(final String filterPath, final IGpsFileModelWrapper modelWrapper, final IProgressMonitor monitor,
                            final List<ActivityT> activitiesToImport, final List<Integer> ids) throws Exception {
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

                    private void importRecords(final List<ActivityT> activitiesToImport, final List<Integer> ids, final IGpsFileModel model, final File file,
                            final List<ActivityT> activities) {
                        for (final ActivityT activity : activities) {
                            final ITraining overview = TrainingOverviewFactory.creatTrainingOverview(activity);
                            final int id = DatabaseAccessFactory.getDatabaseAccess().importRecord(athlete.getId(), file.getName(),
                                    activity.getId().toGregorianCalendar().getTime(), overview, model.getId());
                            if (id > 0) {
                                // neu hinzugefügt
                                activitiesToImport.add(activity);
                                ids.add(id);
                            }
                        }
                    }
                };
                job.schedule();
            }

        }

    }

    private void readCurrentPreferences() {

        for (final IConvert2Tcx tcx : cc.getAllConverter()) {
            if (preferenceStore.getBoolean(PreferenceConstants.FILE_SUFFIX_FOR_BACKUP + tcx.getFilePrefix())) {
                filePrefixes.add("*." + tcx.getFilePrefix()); //$NON-NLS-1$
                filePrefixes.add("*." + tcx.getFilePrefix().toUpperCase()); //$NON-NLS-1$
            }
        }
    }
}
