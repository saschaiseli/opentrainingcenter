package ch.iseli.sportanalyzer.client.action;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ch.iseli.sportanalyzer.client.Activator;
import ch.iseli.sportanalyzer.client.Application;
import ch.iseli.sportanalyzer.client.PreferenceConstants;
import ch.iseli.sportanalyzer.client.cache.TrainingCenterDataCache;
import ch.iseli.sportanalyzer.client.cache.TrainingCenterRecord;
import ch.iseli.sportanalyzer.client.helper.DaoHelper;
import ch.iseli.sportanalyzer.client.helper.FileNameToDateConverter;
import ch.iseli.sportanalyzer.client.images.IImageKeys;
import ch.iseli.sportanalyzer.db.IImportedDao;
import ch.iseli.sportanalyzer.importer.FindGarminFiles;
import ch.iseli.sportanalyzer.importer.IConvert2Tcx;
import ch.iseli.sportanalyzer.tcx.TrainingCenterDatabaseT;
import ch.opentrainingcenter.transfer.impl.Athlete;

public class ImportGpsFilesAction extends Action implements ISelectionListener, IWorkbenchAction {

    public static final String ID = "ch.iseli.sportanalyzer.client.importFiles";

    private static final Logger log = LoggerFactory.getLogger(ImportGpsFilesAction.class.getName());

    private final IWorkbenchWindow window;

    private final Athlete athlete;

    private final IImportedDao dao;

    public ImportGpsFilesAction(IWorkbenchWindow window, String tooltip) {

        IConfigurationElement[] daos = Platform.getExtensionRegistry().getConfigurationElementsFor("ch.opentrainingdatabase.db");
        dao = (IImportedDao) DaoHelper.getDao(daos, IImportedDao.EXTENSION_POINT_NAME);
        String athleteId = Activator.getDefault().getPreferenceStore().getString(PreferenceConstants.ATHLETE_NAME);
        if (validId(athleteId)) {
            final int id = Integer.parseInt(athleteId);
            athlete = dao.getAthlete(id);
        } else {
            athlete = null;
        }

        this.window = window;
        setId(ID);
        setToolTipText(tooltip);
        setImageDescriptor(AbstractUIPlugin.imageDescriptorFromPlugin(Application.ID, IImageKeys.IMPORT_GPS));
        window.getSelectionService().addSelectionListener(this);
    }

    private boolean validId(String athleteId) {
        return athleteId != null && athleteId.length() > 0;
    }

    @Override
    public void selectionChanged(IWorkbenchPart part, ISelection selection) {
        System.out.println("");
    }

    @Override
    public void dispose() {
        window.getSelectionService().removeSelectionListener(this);
    }

    @Override
    public void run() {

        Map<Integer, String> importedRecordFileNames = dao.getImportedRecords(athlete);
        List<File> garminFiles = FindGarminFiles.getGarminFiles(importedRecordFileNames.values());
        Map<String, File> keyToFile = new TreeMap<String, File>();
        for (File f : garminFiles) {
            keyToFile.put(FileNameToDateConverter.getHumanReadableDate(f.getName()), f);
        }
        IStructuredContentProvider contentProvider = new IStructuredContentProvider() {

            @Override
            public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
            }

            @Override
            public void dispose() {
            }

            @Override
            public Object[] getElements(Object inputElement) {
                @SuppressWarnings("unchecked")
                Set<String> fileNames = (Set<String>) inputElement;
                return fileNames.toArray();
            }
        };
        ILabelProvider labelProvider = new LabelProvider();
        ListSelectionDialog dialog = new ListSelectionDialog(window.getShell(), keyToFile.keySet(), contentProvider, labelProvider, "Lauf nach Datum sortiert.");
        dialog.setTitle("GPS Files auswählen");
        dialog.open();
        Object[] result = dialog.getResult();
        if (result == null || result.length == 0) {
            return;
        }
        final List<File> selectedFilesToImport = new ArrayList<File>();
        for (Object key : result) {
            selectedFilesToImport.add(keyToFile.get(key));
        }

        IConfigurationElement[] configurationElementsFor = Platform.getExtensionRegistry().getConfigurationElementsFor("ch.iseli.sportanalyzer.myimporter");
        final IConvert2Tcx tcx = getConverterImplementation(configurationElementsFor);
        final Map<Integer, TrainingCenterRecord> allRecords = new HashMap<Integer, TrainingCenterRecord>();

        final Job job = new Job("Lade GPS Daten") {
            @Override
            protected IStatus run(IProgressMonitor monitor) {
                // Set total number of work units
                monitor.beginTask("Lade GPS Daten...", selectedFilesToImport.size());
                try {
                    for (File file : selectedFilesToImport) {
                        monitor.setTaskName("importiere File: " + file.getName());
                        TrainingCenterDatabaseT record = tcx.convert(file);
                        Integer importRecordId = dao.importRecord(athlete, file.getName());
                        allRecords.put(importRecordId, new TrainingCenterRecord(importRecordId, record));

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

    private IConvert2Tcx getConverterImplementation(IConfigurationElement[] configurationElementsFor) {
        for (final IConfigurationElement element : configurationElementsFor) {
            try {
                return (IConvert2Tcx) element.createExecutableExtension("class");
            } catch (CoreException e) {
                log.error(e.getMessage());
            }
        }
        return null;
    }
}
