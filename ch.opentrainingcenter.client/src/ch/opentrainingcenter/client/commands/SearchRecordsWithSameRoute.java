package ch.opentrainingcenter.client.commands;

import java.util.List;

import org.apache.log4j.Logger;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.handlers.HandlerUtil;

import ch.opentrainingcenter.client.Activator;
import ch.opentrainingcenter.client.action.job.SearchRecordJob;
import ch.opentrainingcenter.core.PreferenceConstants;
import ch.opentrainingcenter.core.db.IDatabaseAccess;
import ch.opentrainingcenter.core.service.IDatabaseService;
import ch.opentrainingcenter.model.navigation.ConcreteImported;
import ch.opentrainingcenter.route.CompareRouteFactory;
import ch.opentrainingcenter.route.ICompareRoute;
import ch.opentrainingcenter.transfer.ITraining;

public class SearchRecordsWithSameRoute extends OtcAbstractHandler {

    private static final Logger LOGGER = Logger.getLogger(SearchRecordsWithSameRoute.class);

    private final IDatabaseService service;
    private final IPreferenceStore store;
    private final ICompareRoute comp;

    public SearchRecordsWithSameRoute() {
        this(Activator.getDefault().getPreferenceStore(), (IDatabaseService) PlatformUI.getWorkbench().getService(IDatabaseService.class));
    }

    public SearchRecordsWithSameRoute(final IPreferenceStore store, final IDatabaseService service) {
        super(store);
        this.store = store;
        this.service = service;
        comp = CompareRouteFactory.getRouteComparator(true, store.getString(PreferenceConstants.KML_DEBUG_PATH));
    }

    @Override
    public Object execute(final ExecutionEvent event) throws ExecutionException {
        final ISelection selection = HandlerUtil.getCurrentSelection(event);
        final ConcreteImported referenz = (ConcreteImported) ((StructuredSelection) selection).getFirstElement();
        final ITraining referenzTraining = referenz.getTraining();

        final IDatabaseAccess databaseAccess = service.getDatabaseAccess();
        final List<ITraining> all = databaseAccess.getAllTrainings(referenzTraining.getAthlete());

        final SearchRecordJob job = new SearchRecordJob("Suche", referenzTraining, all, comp);
        job.schedule();
        return null;
    }
}
