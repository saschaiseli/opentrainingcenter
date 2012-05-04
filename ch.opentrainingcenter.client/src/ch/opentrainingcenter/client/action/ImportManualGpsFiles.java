package ch.opentrainingcenter.client.action;

import java.util.Map;

import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.ui.ISelectionListener;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.actions.ActionFactory.IWorkbenchAction;
import org.eclipse.ui.plugin.AbstractUIPlugin;

import ch.opentrainingcenter.client.Application;
import ch.opentrainingcenter.client.Messages;
import ch.opentrainingcenter.client.PreferenceConstants;
import ch.opentrainingcenter.client.action.job.ManualImportJob;
import ch.opentrainingcenter.client.cache.Cache;
import ch.opentrainingcenter.client.model.IGpsFileModelWrapper;
import ch.opentrainingcenter.client.views.IImageKeys;
import ch.opentrainingcenter.client.views.dialoge.IFilterDialog;
import ch.opentrainingcenter.client.views.dialoge.ImportFileDialog;
import ch.opentrainingcenter.client.views.dialoge.RunTypeDialog;
import ch.opentrainingcenter.db.IDatabaseAccess;
import ch.opentrainingcenter.importer.ConvertContainer;
import ch.opentrainingcenter.importer.IConvert2Tcx;
import ch.opentrainingcenter.transfer.IAthlete;

public class ImportManualGpsFiles extends Action implements ISelectionListener, IWorkbenchAction {
    public static final String IMPORT_PATTERN = "_$_"; //$NON-NLS-1$

    public static final String ID = "ch.opentrainingcenter.client.importManualFiles"; //$NON-NLS-1$

    private final IWorkbenchWindow window;

    private IAthlete athlete;

    private final String locationForBackupFiles;

    private final ConvertContainer cc;

    private final Cache cache;

    public ImportManualGpsFiles(final IWorkbenchWindow window, final String toolTipText, final IDatabaseAccess databaseAccess, final Cache cache,
            final IPreferenceStore preferenceStore, final Map<String, IConvert2Tcx> converters) {
        this.window = window;
        this.cache = cache;
        setId(ID);
        setToolTipText(toolTipText);
        final String athleteId = preferenceStore.getString(PreferenceConstants.ATHLETE_ID);
        if (validId(athleteId)) {
            final int id = Integer.parseInt(athleteId);
            athlete = databaseAccess.getAthlete(id);
        } else {
            throw new IllegalArgumentException("Athlete ist nicht gesetzt....");
        }
        locationForBackupFiles = preferenceStore.getString(PreferenceConstants.GPS_FILE_LOCATION_PROG);

        cc = new ConvertContainer(converters);
        setImageDescriptor(AbstractUIPlugin.imageDescriptorFromPlugin(Application.ID, IImageKeys.IMPORT_GPS_KLEIN));
        this.window.getSelectionService().addSelectionListener(this);
    }

    @Override
    public void dispose() {
        window.getSelectionService().removeSelectionListener(this);
    }

    @Override
    public void run() {
        final IFilterDialog importDialog = new ImportFileDialog(window.getShell());
        final String s = importDialog.open();
        if (s != null) {
            final String[] fileNames = importDialog.getFileNames();
            final String filterPath = importDialog.getFilterPath();

            final RunTypeDialog dialog = new RunTypeDialog(window.getShell(), fileNames);
            final int open = dialog.open();
            if (open >= 0) {
                final IGpsFileModelWrapper modelWrapper = dialog.getModelWrapper();
                final Job job = new ManualImportJob(Messages.ImportManualGpsFiles_LadeGpsFiles, cc, locationForBackupFiles, athlete, modelWrapper, filterPath, cache);
                job.schedule();
            }

        }

    }

    @Override
    public void selectionChanged(final IWorkbenchPart part, final ISelection selection) {
    }

    private boolean validId(final String athleteId) {
        return athleteId != null && athleteId.length() > 0;
    }
}
