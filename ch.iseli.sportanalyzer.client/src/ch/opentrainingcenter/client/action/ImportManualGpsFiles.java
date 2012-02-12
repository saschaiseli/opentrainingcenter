package ch.opentrainingcenter.client.action;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

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
import ch.opentrainingcenter.importer.ConvertHandler;
import ch.opentrainingcenter.importer.IConvert2Tcx;
import ch.opentrainingcenter.tcx.ActivityT;
import ch.opentrainingcenter.transfer.IAthlete;
import ch.opentrainingcenter.transfer.ITraining;

public class ImportManualGpsFiles extends Action implements ISelectionListener, IWorkbenchAction {
    public static final String IMPORT_PATTERN = "_$_"; //$NON-NLS-1$

    private static final Logger logger = Logger.getLogger(ImportManualGpsFiles.class.getName());

    public static final String ID = "ch.opentrainingcenter.client.importManualFiles"; //$NON-NLS-1$

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
            logger.error("Athlete ist nicht gesetzt...."); //$NON-NLS-1$
        }
        defaultLocation = Activator.getDefault().getPreferenceStore().getString(PreferenceConstants.GPS_FILE_LOCATION);
        locationForBackupFiles = Activator.getDefault().getPreferenceStore().getString(PreferenceConstants.GPS_FILE_LOCATION_PROG);
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

        final Shell sh = window.getShell();
        final IConfigurationElement[] extensions = Platform.getExtensionRegistry().getConfigurationElementsFor(Application.IMPORT_EXTENSION_POINT);
        logger.info("Anzahl Extensions: " + extensions.length); //$NON-NLS-1$
        final ConvertHandler handler = getConverterImplementation(extensions);
        final FileDialog fileDialog = new FileDialog(window.getShell(), SWT.MULTI);
        fileDialog.setFilterPath(defaultLocation);
        final List<String> filePrefixes = handler.getSupportedFileSuffixes();
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
                            for (final IGpsFileModel model : modelWrapper.getGpsFileModels()) {
                                final File file = new File(filterPath, model.getFileName());
                                monitor.setTaskName(Messages.ImportManualGpsFiles_5 + file.getName());
                                logger.info("importiere File: " + file.getName()); //$NON-NLS-1$
                                final List<ActivityT> activities = handler.getMatchingConverter(file).convertActivity(file);

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
                                    TrainingCenterDataCache.getInstance().addAll(activitiesToImport);
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

    }

    private ConvertHandler getConverterImplementation(final IConfigurationElement[] configurationElementsFor) {
        final ConvertHandler handler = new ConvertHandler();
        logger.info("Beginne mit Extensions einzulesen......."); //$NON-NLS-1$
        for (final IConfigurationElement element : configurationElementsFor) {
            try {
                final IConvert2Tcx tcx = (IConvert2Tcx) element.createExecutableExtension(IConvert2Tcx.PROPERETY);
                logger.info("Beginne mit Extensions einzulesen.......: " + element.getAttribute(IConvert2Tcx.PROPERETY)); //$NON-NLS-1$
                handler.addConverter(tcx);
            } catch (final CoreException e) {
                logger.error(e.getMessage());
            }
        }
        return handler;
    }
}
