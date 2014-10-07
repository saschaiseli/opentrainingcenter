package ch.opentrainingcenter.client.commands;

import java.util.List;

import org.apache.log4j.Logger;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.osgi.util.NLS;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.handlers.HandlerUtil;

import ch.opentrainingcenter.client.Activator;
import ch.opentrainingcenter.client.views.dialoge.RouteDialog;
import ch.opentrainingcenter.core.db.IDatabaseAccess;
import ch.opentrainingcenter.core.service.IDatabaseService;
import ch.opentrainingcenter.i18n.Messages;
import ch.opentrainingcenter.transfer.IRoute;
import ch.opentrainingcenter.transfer.ITraining;

/**
 * Fügt eine neue Route hinzu.
 */
public class AddRoute extends OtcAbstractHandler {

    public static final String ID = "ch.opentrainingcenter.client.commands.AddRoute"; //$NON-NLS-1$
    private static final Logger LOGGER = Logger.getLogger(AddRoute.class);
    private final IDatabaseService service;

    public AddRoute() {
        this(Activator.getDefault().getPreferenceStore(), (IDatabaseService) PlatformUI.getWorkbench().getService(IDatabaseService.class));
    }

    public AddRoute(final IPreferenceStore store, final IDatabaseService service) {
        super(store);
        this.service = service;

    }

    @Override
    public Object execute(final ExecutionEvent event) throws ExecutionException {
        final IDatabaseAccess databaseAccess = service.getDatabaseAccess();
        final ISelection selection = HandlerUtil.getCurrentSelection(event);

        final List<?> tracks = ((StructuredSelection) selection).toList();
        if (isSelectionValid(tracks)) {
            final ITraining training = (ITraining) tracks.get(0);

            final Shell shell = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell();
            final IRoute route = training.getRoute();
            // final boolean delete = true;
            if (isReferenzTrack(training, route)) {
                LOGGER.info("Referenztracks koennen noch nicht bearbeitet oder geändert werden"); //$NON-NLS-1$
                MessageDialog.openError(shell, Messages.AddRoute_ERROR_TITLE, NLS.bind(Messages.AddRoute_ERROR_TEXT, route.getName()));
                //                LOGGER.info("bereits eine Referenzroute auf diesem Training erstellt"); //$NON-NLS-1$
                // delete = MessageDialog.openConfirm(shell,
                // Messages.AddRoute_0, NLS.bind(Messages.AddRoute_1,
                // route.getName()));
                // if (delete) {
                //                    LOGGER.info("Die Referenz wird geloescht"); //$NON-NLS-1$
                // final List<ITraining> trainingsMitRoute =
                // databaseAccess.getAllTrainingByRoute(route.getAthlete(),
                // route);
                // if (trainingsMitRoute.size() <= 1) {
                //                        LOGGER.info("Die Route wird geloescht, da es keine Referenzstrecke mehr gibt"); //$NON-NLS-1$
                // databaseAccess.deleteRoute(route.getId());
                // } else {
                // route.setReferenzTrack(trainingsMitRoute.get(0));
                //                        LOGGER.info("Als Referenzstrecke dient nun ein anderer Track"); //$NON-NLS-1$
                // databaseAccess.saveOrUpdate(route);
                // }
                // }
            } else {
                // if (delete) {
                final RouteDialog dialog = new RouteDialog(shell, databaseAccess, training);
                dialog.open();
            }
        }
        return null;
    }

    private boolean isReferenzTrack(final ITraining training, final IRoute route) {
        return route != null && route.getReferenzTrack() != null && route.getReferenzTrack().getId() == training.getId();
    }

    private boolean isSelectionValid(final List<?> tracks) {
        return tracks != null && !tracks.isEmpty() && tracks.get(0) instanceof ITraining;
    }
}
