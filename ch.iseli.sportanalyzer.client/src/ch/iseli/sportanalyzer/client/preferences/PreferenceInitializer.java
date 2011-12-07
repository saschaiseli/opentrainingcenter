package ch.iseli.sportanalyzer.client.preferences;

import org.eclipse.core.runtime.Platform;
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
        final String location = Platform.getBundle(Application.ID).getLocation();
        defaults.put(PreferenceConstants.GPS_FILE_LOCATION, location);
        defaults.put(PreferenceConstants.GPS_FILE_LOCATION_PROG, location);
        defaults.putInt(PreferenceConstants.SB, 95);
        defaults.putInt(PreferenceConstants.EXTDL, 75);
        defaults.putInt(PreferenceConstants.INTDL, 80);
        defaults.putInt(PreferenceConstants.EXTINTERVALL, 89);
        defaults.putInt(PreferenceConstants.ANAEROBE, 90);
        defaults.put(PreferenceConstants.ANAEROBE_COLOR, "219,76,82");
        defaults.putInt(PreferenceConstants.SCHWELLENZONE, 80);
        defaults.put(PreferenceConstants.SCHWELLENZONE_COLOR, "96,134,216");
        defaults.putInt(PreferenceConstants.AEROBE, 45);
        defaults.put(PreferenceConstants.AEROBE_COLOR, "144,238,144");
    }

}
