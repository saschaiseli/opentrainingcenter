package ch.opentrainingcenter.client.commands;

import java.util.Map;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;

import ch.opentrainingcenter.client.Activator;
import ch.opentrainingcenter.client.Messages;
import ch.opentrainingcenter.client.PreferenceConstants;
import ch.opentrainingcenter.client.action.job.ImportManualJob;
import ch.opentrainingcenter.client.cache.Cache;
import ch.opentrainingcenter.client.cache.impl.TrainingCenterDataCache;
import ch.opentrainingcenter.client.model.IGpsFileModelWrapper;
import ch.opentrainingcenter.client.views.dialoge.IFilterDialog;
import ch.opentrainingcenter.client.views.dialoge.ImportFileDialog;
import ch.opentrainingcenter.client.views.dialoge.RunTypeDialog;
import ch.opentrainingcenter.db.DatabaseAccessFactory;
import ch.opentrainingcenter.db.IDatabaseAccess;
import ch.opentrainingcenter.importer.ConvertContainer;
import ch.opentrainingcenter.importer.ExtensionHelper;
import ch.opentrainingcenter.importer.IConvert2Tcx;
import ch.opentrainingcenter.importer.IFileImport;
import ch.opentrainingcenter.importer.ImporterFactory;
import ch.opentrainingcenter.transfer.IAthlete;

public class ImportManualGpsFiles extends AbstractHandler {

    public static final String IMPORT_PATTERN = "_$_"; //$NON-NLS-1$
    private final IDatabaseAccess databaseAccess;
    private final Cache cache;
    private final IPreferenceStore store;
    private final ConvertContainer cc;

    /**
     * Constructor für Eclipse
     */
    public ImportManualGpsFiles() {
        this.databaseAccess = DatabaseAccessFactory.getDatabaseAccess();
        this.cache = TrainingCenterDataCache.getInstance();
        this.store = Activator.getDefault().getPreferenceStore();
        this.cc = new ConvertContainer(ExtensionHelper.getConverters());
    }

    /**
     * Constructor für Tests
     */
    public ImportManualGpsFiles(final IDatabaseAccess databaseAccess, final Cache cache, final IPreferenceStore store,
            final Map<String, IConvert2Tcx> converters) {
        this.databaseAccess = databaseAccess;
        this.cache = cache;
        this.store = store;
        this.cc = new ConvertContainer(converters);

    }

    @Override
    public Object execute(final ExecutionEvent event) throws ExecutionException {
        final String location = store.getString(PreferenceConstants.GPS_FILE_LOCATION_PROG);
        final IWorkbenchWindow window = PlatformUI.getWorkbench().getActiveWorkbenchWindow();
        final IFilterDialog importDialog = new ImportFileDialog(window.getShell());
        final String s = importDialog.open();
        if (s != null) {
            final String[] fileNames = importDialog.getFileNames();
            final String filterPath = importDialog.getFilterPath();

            final RunTypeDialog dialog = new RunTypeDialog(window.getShell(), fileNames);
            final int open = dialog.open();
            if (open >= 0) {
                final String athleteId = store.getString(PreferenceConstants.ATHLETE_ID);
                if (validId(athleteId)) {
                    final int id = Integer.parseInt(athleteId);
                    final IAthlete athlete = databaseAccess.getAthlete(id);
                    final IFileImport fileImporter = ImporterFactory.createFileImporter(cc, athlete, databaseAccess, location);

                    final IGpsFileModelWrapper model = dialog.getModelWrapper();
                    final Job job = new ImportManualJob(Messages.ImportManualGpsFilesLadeGpsFiles, model, filterPath, fileImporter, cache);
                    job.schedule();
                } else {
                    throw new ExecutionException("Kein Athlete gesetzt. Fuck you!");
                }
            }
        }
        return null;
    }

    private boolean validId(final String athleteId) {
        return athleteId != null && athleteId.length() > 0;
    }

}
