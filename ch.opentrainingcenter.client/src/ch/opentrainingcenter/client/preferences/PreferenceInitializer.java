package ch.opentrainingcenter.client.preferences;

import java.io.File;
import java.util.Calendar;
import java.util.Locale;

import org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer;
import org.eclipse.core.runtime.preferences.DefaultScope;
import org.eclipse.core.runtime.preferences.IEclipsePreferences;

import ch.opentrainingcenter.client.Application;
import ch.opentrainingcenter.core.PreferenceConstants;
import ch.opentrainingcenter.core.importer.ConvertContainer;
import ch.opentrainingcenter.core.importer.ExtensionHelper;
import ch.opentrainingcenter.core.importer.IConvert2Tcx;

/**
 * Class used to initialize default preference values.
 */
public class PreferenceInitializer extends AbstractPreferenceInitializer {

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer#
     * initializeDefaultPreferences()
     */
    @Override
    public void initializeDefaultPreferences() {
        final IEclipsePreferences defaults = DefaultScope.INSTANCE.getNode(Application.ID);

        final String userHome = System.getProperty("user.home");//$NON-NLS-1$
        final Calendar cal = Calendar.getInstance(Locale.getDefault());
        final int month = cal.get(Calendar.MONTH) + 1;
        final String monthString;
        if (month < 9) {
            monthString = "0" + month; //$NON-NLS-1$
        } else {
            monthString = String.valueOf(month);
        }
        defaults.put(PreferenceConstants.GPS_FILE_LOCATION, userHome + File.separator + cal.get(Calendar.YEAR) + File.separator + monthString);
        defaults.put(PreferenceConstants.GPS_FILE_LOCATION_PROG, userHome + "/data/otc"); //$NON-NLS-1$
        defaults.putInt(PreferenceConstants.RECOM, 65);
        defaults.put(PreferenceConstants.RECOM_COLOR, "144,238,144"); //$NON-NLS-1$
        defaults.putInt(PreferenceConstants.GA1, 75);
        defaults.put(PreferenceConstants.GA1_COLOR, "246,246,108"); //$NON-NLS-1$
        defaults.putInt(PreferenceConstants.GA12, 85);
        defaults.put(PreferenceConstants.GA12_COLOR, "253,152,90"); //$NON-NLS-1$
        defaults.putInt(PreferenceConstants.GA2, 95);
        defaults.put(PreferenceConstants.GA2_COLOR, "193,103,73"); //$NON-NLS-1$
        defaults.putInt(PreferenceConstants.WSA, 100);
        defaults.put(PreferenceConstants.WSA_COLOR, "255,64,34"); //$NON-NLS-1$

        defaults.put(PreferenceConstants.BACKUP_FILE_LOCATION, System.getProperty("user.home") + File.separator + ".otc/backup"); //$NON-NLS-1$ //$NON-NLS-2$
        final ConvertContainer cc = new ConvertContainer(ExtensionHelper.getConverters());
        for (final IConvert2Tcx tcx : cc.getAllConverter()) {
            defaults.putBoolean(PreferenceConstants.FILE_SUFFIX_FOR_BACKUP + tcx.getFilePrefix(), true);
        }

        // training target
        defaults.putInt(PreferenceConstants.WEEK_FOR_PLAN, 6);
        defaults.put(PreferenceConstants.ZIEL_ERFUELLT_COLOR, "108,233,144"); //$NON-NLS-1$
        defaults.put(PreferenceConstants.ZIEL_NICHT_ERFUELLT_COLOR, "212,62,59"); //$NON-NLS-1$
        defaults.put(PreferenceConstants.ZIEL_NICHT_BEKANNT_COLOR, "173,216,230"); //$NON-NLS-1$

        defaults.put(PreferenceConstants.DISTANCE_CHART_COLOR, "40,4,252"); //$NON-NLS-1$
        defaults.put(PreferenceConstants.DISTANCE_HEART_COLOR, "255,17,14"); //$NON-NLS-1$

        defaults.put(PreferenceConstants.RUHEPULS_COLOR, "255,0,26"); //$NON-NLS-1$
        defaults.put(PreferenceConstants.GEWICHT_COLOR, "240,173,50"); //$NON-NLS-1$

        defaults.put(PreferenceConstants.KML_DEBUG_PATH, userHome + "/Dropbox/Public"); //$NON-NLS-1$

        // database default
        defaults.put(PreferenceConstants.DB_USER, "sa"); //$NON-NLS-1$
        defaults.put(PreferenceConstants.DB_PASS, ""); //$NON-NLS-1$
        defaults.put(PreferenceConstants.DB_URL, "jdbc:h2:file:~/.otc/otc"); //$NON-NLS-1$
        defaults.put(PreferenceConstants.DB, "H2 Database"); //$NON-NLS-1$
    }
}
