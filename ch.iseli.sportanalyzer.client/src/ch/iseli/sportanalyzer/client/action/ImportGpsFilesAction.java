package ch.iseli.sportanalyzer.client.action;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import org.apache.log4j.Logger;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.ISelectionListener;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.actions.ActionFactory.IWorkbenchAction;
import org.eclipse.ui.dialogs.ListSelectionDialog;
import org.eclipse.ui.plugin.AbstractUIPlugin;

import ch.iseli.sportanalyzer.client.Activator;
import ch.iseli.sportanalyzer.client.Application;
import ch.iseli.sportanalyzer.client.PreferenceConstants;
import ch.iseli.sportanalyzer.client.cache.TrainingCenterDataCache;
import ch.iseli.sportanalyzer.client.cache.TrainingCenterRecord;
import ch.iseli.sportanalyzer.client.helper.FileNameToDateConverter;
import ch.iseli.sportanalyzer.client.views.IImageKeys;
import ch.iseli.sportanalyzer.db.DatabaseAccessFactory;
import ch.iseli.sportanalyzer.importer.FindGarminFiles;
import ch.iseli.sportanalyzer.importer.IConvert2Tcx;
import ch.iseli.sportanalyzer.tcx.TrainingCenterDatabaseT;
import ch.opentrainingcenter.transfer.IAthlete;

public class ImportGpsFilesAction extends Action implements ISelectionListener, IWorkbenchAction {

    public static final String ID = "ch.iseli.sportanalyzer.client.importFiles";

    private static final Logger logger = Logger.getLogger(ImportGpsFilesAction.class.getName());

    private final IWorkbenchWindow window;

    private final IAthlete athlete;

    public ImportGpsFilesAction(final IWorkbenchWindow window, final String tooltip) {
        logger.debug("Import files....");
        final String athleteId = Activator.getDefault().getPreferenceStore().getString(PreferenceConstants.ATHLETE_ID);
        if (validId(athleteId)) {
            final int id = Integer.parseInt(athleteId);
            athlete = DatabaseAccessFactory.getDatabaseAccess().getAthlete(id);
        } else {
            athlete = null;
            logger.error("Athlete ist nicht gesetzt....");
        }
        this.window = window;
        setId(ID);
        setToolTipText(tooltip);
        setImageDescriptor(AbstractUIPlugin.imageDescriptorFromPlugin(Application.ID, IImageKeys.IMPORT_GPS));
        window.getSelectionService().addSelectionListener(this);
    }

    private boolean validId(final String athleteId) {
        return athleteId != null && athleteId.length() > 0;
    }

    @Override
    public void selectionChanged(final IWorkbenchPart part, final ISelection selection) {

    }

    @Override
    public void dispose() {
        window.getSelectionService().removeSelectionListener(this);
    }

    @Override
    public void run() {

        final Map<Integer, String> importedRecordFileNames = DatabaseAccessFactory.getDatabaseAccess().getImportedRecords(athlete);
        final List<File> garminFiles = FindGarminFiles.getGarminFiles(importedRecordFileNames.values());
        final Map<String, File> keyToFile = new TreeMap<String, File>();
        for (final File f : garminFiles) {
            keyToFile.put(FileNameToDateConverter.getHumanReadableDate(f.getName()), f);
        }
        final IStructuredContentProvider contentProvider = new IStructuredContentProvider() {

            @Override
            public void inputChanged(final Viewer viewer, final Object oldInput, final Object newInput) {
            }

            @Override
            public void dispose() {
            }

            @Override
            public Object[] getElements(final Object inputElement) {
                @SuppressWarnings("unchecked")
                final Set<String> fileNames = (Set<String>) inputElement;
                return fileNames.toArray();
            }
        };
        final ILabelProvider labelProvider = new LabelProvider();
        final ListSelectionDialog dialog = new ListSelectionDialog(window.getShell(), keyToFile.keySet(), contentProvider, labelProvider,
                "Lauf nach Datum sortiert.");
        dialog.setTitle("GPS Files auswählen");
        dialog.open();
        final Object[] result = dialog.getResult();
        if (result == null || result.length == 0) {
            return;
        }
        final List<File> selectedFilesToImport = new ArrayList<File>();
        for (final Object key : result) {
            selectedFilesToImport.add(keyToFile.get(key));
        }

        final IConfigurationElement[] configurationElementsFor = Platform.getExtensionRegistry().getConfigurationElementsFor(
                "ch.iseli.sportanalyzer.myimporter");
        final IConvert2Tcx tcx = getConverterImplementation(configurationElementsFor);
        final Map<Integer, TrainingCenterRecord> allRecords = new HashMap<Integer, TrainingCenterRecord>();

        final Job job = new Job("Lade GPS Daten") {
            @Override
            protected IStatus run(final IProgressMonitor monitor) {
                // Set total number of work units
                monitor.beginTask("Lade GPS Daten...", selectedFilesToImport.size());
                try {
                    for (final File file : selectedFilesToImport) {
                        monitor.setTaskName("importiere File: " + file.getName());
                        final TrainingCenterDatabaseT record = tcx.convert(file);
                        final Integer importRecordId = DatabaseAccessFactory.getDatabaseAccess().importRecord(athlete.getId(), file.getName());
                        allRecords.put(importRecordId, new TrainingCenterRecord(importRecordId, record));

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

    private IConvert2Tcx getConverterImplementation(final IConfigurationElement[] configurationElementsFor) {
        for (final IConfigurationElement element : configurationElementsFor) {
            try {
                return (IConvert2Tcx) element.createExecutableExtension("class");
            } catch (final CoreException e) {
                logger.error(e.getMessage());
            }
        }
        return null;
    }
}
