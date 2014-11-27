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
import ch.opentrainingcenter.client.action.job.ExportKmlJob;
import ch.opentrainingcenter.client.views.ApplicationContext;
import ch.opentrainingcenter.core.PreferenceConstants;
import ch.opentrainingcenter.core.db.IDatabaseAccess;
import ch.opentrainingcenter.core.service.IDatabaseService;
import ch.opentrainingcenter.route.IKmlDumper;
import ch.opentrainingcenter.route.RouteFactory;

public class ExportKml extends OtcAbstractHandler {
    private static final Logger LOGGER = Logger.getLogger(ExportKml.class);
    private final IKmlDumper kmlDumper;
    private final String path;

    private final IDatabaseService service;
    private final ApplicationContext context;

    public ExportKml() {
        this(Activator.getDefault().getPreferenceStore());
    }

    public ExportKml(final IPreferenceStore store) {
        super(store);
        path = store.getString(PreferenceConstants.KML_DEBUG_PATH);
        kmlDumper = RouteFactory.createKmlDumper(path);
        service = (IDatabaseService) PlatformUI.getWorkbench().getService(IDatabaseService.class);
        context = ApplicationContext.getApplicationContext();
    }

    @Override
    public Object execute(final ExecutionEvent event) throws ExecutionException {
        final IDatabaseAccess databaseAccess = service.getDatabaseAccess();
        final ISelection selection = HandlerUtil.getCurrentSelection(event);
        final List<?> records = ((StructuredSelection) selection).toList();

        final ExportKmlJob job = new ExportKmlJob("KML File exportieren", kmlDumper, records, databaseAccess, context.getAthlete()); //$NON-NLS-1$
        job.schedule();

        return null;
    }
}
