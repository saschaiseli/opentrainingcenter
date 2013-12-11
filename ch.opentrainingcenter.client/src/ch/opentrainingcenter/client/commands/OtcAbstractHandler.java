package ch.opentrainingcenter.client.commands;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.jface.preference.IPreferenceStore;

import ch.opentrainingcenter.client.views.ApplicationContext;
import ch.opentrainingcenter.core.PreferenceConstants;
import ch.opentrainingcenter.core.db.DBSTATE;

/**
 * Abstrakter Handler welcher nur enabled ist, wenn auch ein athlete ausgewählt
 * ist und die Datenbank verfügbar.
 * 
 */
public abstract class OtcAbstractHandler extends AbstractHandler {

    private final IPreferenceStore store;

    public OtcAbstractHandler(final IPreferenceStore store) {
        this.store = store;
    }

    @Override
    public boolean isEnabled() {
        return isUserSelected() && isDatabaseOk();
    }

    private boolean isUserSelected() {
        final String id = store.getString(PreferenceConstants.ATHLETE_ID);
        return id != null && id.length() > 0;
    }

    private boolean isDatabaseOk() {
        return DBSTATE.OK.equals(ApplicationContext.getApplicationContext().getDbState().getState());
    }
}
