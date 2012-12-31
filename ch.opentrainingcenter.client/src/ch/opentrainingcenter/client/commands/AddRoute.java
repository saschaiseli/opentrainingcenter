package ch.opentrainingcenter.client.commands;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.ui.PlatformUI;

import ch.opentrainingcenter.client.Activator;
import ch.opentrainingcenter.client.views.dialoge.RouteDialog;
import ch.opentrainingcenter.core.db.DatabaseAccessFactory;

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
        final RouteDialog dialog = new RouteDialog(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(), DatabaseAccessFactory.getDatabaseAccess());
        dialog.open();
        return null;
    }

}
