package ch.opentrainingcenter.client.commands;

import java.util.List;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.handlers.HandlerUtil;

import ch.opentrainingcenter.client.Activator;
import ch.opentrainingcenter.client.views.dialoge.RouteDialog;
import ch.opentrainingcenter.core.db.DatabaseAccessFactory;
import ch.opentrainingcenter.core.db.IDatabaseAccess;
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
        final IDatabaseAccess db = DatabaseAccessFactory.getDatabaseAccess();
        final RouteDialog dialog = new RouteDialog(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(), db, training);
        dialog.open();
        return null;
    }
}
