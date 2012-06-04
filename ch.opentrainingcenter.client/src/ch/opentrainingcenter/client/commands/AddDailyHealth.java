package ch.opentrainingcenter.client.commands;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.ui.PlatformUI;

import ch.opentrainingcenter.client.Activator;
import ch.opentrainingcenter.client.PreferenceConstants;
import ch.opentrainingcenter.client.views.dialoge.HealthDialog;

public class AddDailyHealth extends AbstractHandler {

    public static final String ID = "ch.opentrainingcenter.client.commands.AddDailyHealth"; //$NON-NLS-1$
    private final IPreferenceStore store;

    public AddDailyHealth() {
        this(Activator.getDefault().getPreferenceStore());
    }

    public AddDailyHealth(final IPreferenceStore store) {
        this.store = store;
    }

    @Override
    public Object execute(final ExecutionEvent event) throws ExecutionException {
        final HealthDialog dialog = new HealthDialog(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell());
        dialog.open();
        return null;
    }

    @Override
    public boolean isEnabled() {
        final String id = store.getString(PreferenceConstants.ATHLETE_ID);
        return id != null && id.length() > 0;
    }
}
