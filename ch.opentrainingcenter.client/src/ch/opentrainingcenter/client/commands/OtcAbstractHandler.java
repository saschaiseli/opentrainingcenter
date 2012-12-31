package ch.opentrainingcenter.client.commands;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.jface.preference.IPreferenceStore;

import ch.opentrainingcenter.core.PreferenceConstants;

/**
 * Abstrakter Handler welcher nur enabled ist, wenn auch ein athlete ausgewählt
 * ist.
 * 
 */
public abstract class OtcAbstractHandler extends AbstractHandler {

    private final IPreferenceStore store;

    public OtcAbstractHandler(final IPreferenceStore store) {
        this.store = store;
    }

    @Override
    public boolean isEnabled() {
        final String id = store.getString(PreferenceConstants.ATHLETE_ID);
        return id != null && id.length() > 0;
    }
}
