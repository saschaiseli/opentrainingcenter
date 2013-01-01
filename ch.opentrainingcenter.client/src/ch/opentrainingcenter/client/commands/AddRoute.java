package ch.opentrainingcenter.client.commands;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.ui.PlatformUI;

import ch.opentrainingcenter.client.Activator;
import ch.opentrainingcenter.client.views.dialoge.RouteDialog;
import ch.opentrainingcenter.core.PreferenceConstants;
import ch.opentrainingcenter.core.db.DatabaseAccessFactory;
import ch.opentrainingcenter.core.db.IDatabaseAccess;
import ch.opentrainingcenter.transfer.IAthlete;

public class AddRoute extends OtcAbstractHandler {

    public static final String ID = "ch.opentrainingcenter.client.commands.AddRoute"; //$NON-NLS-1$
    private final IPreferenceStore store;

    public AddRoute() {
        this(Activator.getDefault().getPreferenceStore());
    }

    public AddRoute(final IPreferenceStore store) {
        super(store);
        this.store = store;
    }

    @Override
    public Object execute(final ExecutionEvent event) throws ExecutionException {
        final String id = store.getString(PreferenceConstants.ATHLETE_ID);
        final IDatabaseAccess db = DatabaseAccessFactory.getDatabaseAccess();
        final IAthlete athlete = db.getAthlete(Integer.valueOf(id));
        final RouteDialog dialog = new RouteDialog(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(), db, athlete);
        dialog.open();
        return null;
    }

}
