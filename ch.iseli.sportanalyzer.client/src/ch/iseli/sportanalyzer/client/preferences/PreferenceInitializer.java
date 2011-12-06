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
        final IEclipsePreferences defaults = DefaultScope.INSTANCE.getNode(Application.ID);
        defaults.putInt(PreferenceConstants.SB, 95);
        defaults.putInt(PreferenceConstants.EXTDL, 75);
        defaults.putInt(PreferenceConstants.INTDL, 80);
        defaults.putInt(PreferenceConstants.EXTINTERVALL, 89);
        defaults.putInt(PreferenceConstants.ANAEROBE, 90);
        defaults.putInt(PreferenceConstants.SCHWELLENZONE, 80);
        defaults.putInt(PreferenceConstants.AEROBE, 45);
    }

}
