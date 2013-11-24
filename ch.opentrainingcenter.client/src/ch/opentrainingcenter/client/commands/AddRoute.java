package ch.opentrainingcenter.client.commands;

import java.util.List;

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
import ch.opentrainingcenter.core.cache.TrainingCache;
import ch.opentrainingcenter.core.db.DatabaseAccessFactory;
import ch.opentrainingcenter.core.db.IDatabaseAccess;
import ch.opentrainingcenter.i18n.Messages;
import ch.opentrainingcenter.transfer.IRoute;
import ch.opentrainingcenter.transfer.ITraining;

/**
 * Fügt eine neue Route hinzu.
 */
public class AddRoute extends OtcAbstractHandler {

    public static final String ID = "ch.opentrainingcenter.client.commands.AddRoute"; //$NON-NLS-1$

    public AddRoute() {
        this(Activator.getDefault().getPreferenceStore());
    }

    public AddRoute(final IPreferenceStore store) {
        super(store);
    }

    @Override
    public Object execute(final ExecutionEvent event) throws ExecutionException {

        final ISelection selection = HandlerUtil.getCurrentSelection(event);

        final List<?> tracks = ((StructuredSelection) selection).toList();
        final ITraining training = (ITraining) tracks.get(0);

        final Shell shell = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell();
        final IRoute route = training.getRoute();
        boolean delete = true;
        if (route != null && route.getReferenzTrack() != null && route.getReferenzTrack().getId() == training.getId()) {
            // bereits eine Referenzroute darauf erstellt
            delete = MessageDialog.openConfirm(shell, Messages.AddRoute_0, NLS.bind(
                    Messages.AddRoute_1, route
                            .getName()));
        }
        if (delete) {
            final IDatabaseAccess db = DatabaseAccessFactory.getDatabaseAccess();
            if (route != null) {
                // alle referenzen löschen
                final List<ITraining> all = db.getAllFromRoute(training.getAthlete(), route);
                for (final ITraining tr : all) {
                    tr.setRoute(null);
                    db.saveTraining(tr);
                    TrainingCache.getInstance().add(tr);
                }
            }

            final RouteDialog dialog = new RouteDialog(shell, db, training);
            dialog.open();
        }
        return null;
    }
}
