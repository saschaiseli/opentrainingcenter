package ch.iseli.sportanalyzer.client.preferences;

import org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer;
import org.eclipse.core.runtime.preferences.DefaultScope;
import org.eclipse.core.runtime.preferences.IEclipsePreferences;

import ch.iseli.sportanalyzer.client.Application;
import ch.iseli.sportanalyzer.client.PreferenceConstants;

/**
 * Class used to initialize default preference values.
 */
public class PreferenceInitializer extends AbstractPreferenceInitializer {

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer# initializeDefaultPreferences()
     */
    @Override
    public void initializeDefaultPreferences() {
        @SuppressWarnings("deprecation")
        final IEclipsePreferences defaults = new DefaultScope().getNode(Application.ID);
        defaults.putInt(PreferenceConstants.SB, 95);
        defaults.putInt(PreferenceConstants.EXTDL, 75);
        defaults.putInt(PreferenceConstants.INTDL, 80);
        defaults.putInt(PreferenceConstants.EXTINTERVALL, 89);
    }

}
