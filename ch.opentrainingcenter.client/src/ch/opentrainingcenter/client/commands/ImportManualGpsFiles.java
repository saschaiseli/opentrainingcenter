package ch.opentrainingcenter.client.commands;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;

import ch.opentrainingcenter.client.Activator;
import ch.opentrainingcenter.client.action.job.ImportManualJob;
import ch.opentrainingcenter.client.views.dialoge.IFilterDialog;
import ch.opentrainingcenter.client.views.dialoge.ImportFileDialog;
import ch.opentrainingcenter.client.views.dialoge.RunTypeDialog;
import ch.opentrainingcenter.core.PreferenceConstants;
import ch.opentrainingcenter.core.cache.Cache;
import ch.opentrainingcenter.core.cache.TrainingCache;
import ch.opentrainingcenter.core.db.IDatabaseAccess;
import ch.opentrainingcenter.core.importer.ConvertContainer;
import ch.opentrainingcenter.core.importer.ExtensionHelper;
import ch.opentrainingcenter.core.importer.IConvert2Tcx;
import ch.opentrainingcenter.core.importer.ImporterFactory;
import ch.opentrainingcenter.core.service.IDatabaseService;
import ch.opentrainingcenter.i18n.Messages;
import ch.opentrainingcenter.importer.IFileImport;
import ch.opentrainingcenter.importer.impl.FileImport;
import ch.opentrainingcenter.model.ModelFactory;
import ch.opentrainingcenter.model.importer.IGpsFileModelWrapper;
import ch.opentrainingcenter.model.strecke.StreckeModel;
import ch.opentrainingcenter.transfer.IAthlete;
import ch.opentrainingcenter.transfer.IRoute;

/**
 * Importiert GPS Files.
 * 
 */
public class ImportManualGpsFiles extends OtcAbstractHandler {

    private final Cache cache;
    private final IPreferenceStore store;
    private final ConvertContainer cc;
    private IDatabaseService service;

    /**
     * Constructor
     */
    public ImportManualGpsFiles() {
        this(Activator.getDefault().getPreferenceStore());

    }

    public ImportManualGpsFiles(final IPreferenceStore store) {
        super(store);
        service = (IDatabaseService) PlatformUI.getWorkbench().getService(IDatabaseService.class);
        this.cache = TrainingCache.getInstance();
        this.store = store;
        this.cc = new ConvertContainer(ExtensionHelper.getConverters());

    }

    /**
     * Constructor für Tests
     */
    public ImportManualGpsFiles(final IDatabaseAccess databaseAccess, final Cache cache, final IPreferenceStore store,
            final Map<String, IConvert2Tcx> converters) {
        super(store);
        this.cache = cache;
        this.store = store;
        this.cc = new ConvertContainer(converters);

    }

    @Override
    public Object execute(final ExecutionEvent event) throws ExecutionException {
        final IDatabaseAccess databaseAccess = service.getDatabaseAccess();
        final String location = store.getString(PreferenceConstants.GPS_FILE_LOCATION_PROG);
        final IWorkbenchWindow window = PlatformUI.getWorkbench().getActiveWorkbenchWindow();
        final IFilterDialog importDialog = new ImportFileDialog(window.getShell());
        final String s = importDialog.open();
        if (s != null) {
            final String[] fileNames = importDialog.getFileNames();
            final String filterPath = importDialog.getFilterPath();

            final String athleteId = store.getString(PreferenceConstants.ATHLETE_ID);
            final int id = Integer.parseInt(athleteId);
            final IAthlete athlete = databaseAccess.getAthlete(id);
            final List<IRoute> routen = databaseAccess.getRoute(athlete);
            final List<StreckeModel> strecken = new ArrayList<StreckeModel>();
            for (final IRoute route : routen) {
                final int idReferenzStrecke = route.getReferenzTrack() != null ? route.getReferenzTrack().getId() : 0;
                strecken.add(ModelFactory.createStreckeModel(route, athlete, idReferenzStrecke));
            }
            final RunTypeDialog dialog = new RunTypeDialog(window.getShell(), fileNames, strecken);
            final int open = dialog.open();
            if (open >= 0) {
                if (validId(athleteId)) {
                    final IFileImport fileImporter = new FileImport(cc, athlete, databaseAccess, location, ImporterFactory.createFileCopy());
                    final IGpsFileModelWrapper model = dialog.getModelWrapper();
                    final Job job = new ImportManualJob(Messages.ImportManualGpsFilesLadeGpsFiles, model, filterPath, fileImporter);
                    job.schedule();
                } else {
                    throw new ExecutionException("Kein Athlete gesetzt."); //$NON-NLS-1$
                }
            }
        }
        return null;
    }

    private boolean validId(final String athleteId) {
        return athleteId != null && athleteId.length() > 0;
    }

}
