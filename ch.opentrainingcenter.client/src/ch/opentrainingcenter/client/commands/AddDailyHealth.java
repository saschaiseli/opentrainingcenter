package ch.opentrainingcenter.client.commands;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.ui.PlatformUI;

import ch.opentrainingcenter.client.Activator;
import ch.opentrainingcenter.client.views.dialoge.HealthDialog;

/**
 * Handler um die täglichen Wrte (Gewicht & Puls) hinzuzufügen.
 */
public class AddDailyHealth extends OtcAbstractHandler {

    public static final String ID = "ch.opentrainingcenter.client.commands.AddDailyHealth"; //$NON-NLS-1$

    public AddDailyHealth() {
        this(Activator.getDefault().getPreferenceStore());
    }

    public AddDailyHealth(final IPreferenceStore store) {
        super(store);
    }

    @Override
    public Object execute(final ExecutionEvent event) throws ExecutionException {
        final HealthDialog dialog = new HealthDialog(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell());
        dialog.open();
        return null;
    }
}
