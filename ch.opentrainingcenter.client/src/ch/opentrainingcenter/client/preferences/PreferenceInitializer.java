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
import ch.opentrainingcenter.transfer.Sport;

/**
 * Class used to initialize default preference values.
 */
public class PreferenceInitializer extends AbstractPreferenceInitializer {

    private static final int ANZAHL_PLAENE = 6;
    private static final int RECM = 65;
    private static final int GA1 = 75;
    private static final int GA12 = 85;
    private static final int GA2 = 95;
    private static final int WETTKAMPF = 100;
    private static final int EINSTELLIGER_MONAT = 9;

    @SuppressWarnings("nls")
    @Override
    public void initializeDefaultPreferences() {
        final IEclipsePreferences defaults = DefaultScope.INSTANCE.getNode(Application.ID);

        final String userHome = System.getProperty("user.home");
        final Calendar cal = Calendar.getInstance(Locale.getDefault());
        final int month = cal.get(Calendar.MONTH) + 1;
        final String monthString;
        if (month <= EINSTELLIGER_MONAT) {
            monthString = "0" + month;
        } else {
            monthString = String.valueOf(month);
        }
        defaults.put(PreferenceConstants.GPS_FILE_LOCATION, userHome + File.separator + cal.get(Calendar.YEAR) + File.separator + monthString);
        defaults.put(PreferenceConstants.GPS_FILE_LOCATION_PROG, userHome + "/data/otc");
        defaults.putInt(PreferenceConstants.RECOM, RECM);
        defaults.put(PreferenceConstants.RECOM_COLOR, "144,238,144");
        defaults.putInt(PreferenceConstants.GA1, GA1);
        defaults.put(PreferenceConstants.GA1_COLOR, "246,246,108");
        defaults.putInt(PreferenceConstants.GA12, GA12);
        defaults.put(PreferenceConstants.GA12_COLOR, "253,152,90");
        defaults.putInt(PreferenceConstants.GA2, GA2);
        defaults.put(PreferenceConstants.GA2_COLOR, "193,103,73");
        defaults.putInt(PreferenceConstants.WSA, WETTKAMPF);
        defaults.put(PreferenceConstants.WSA_COLOR, "255,64,34");

        defaults.put(PreferenceConstants.BACKUP_FILE_LOCATION, userHome + File.separator + ".otc/backup");
        final ConvertContainer cc = new ConvertContainer(ExtensionHelper.getConverters());
        for (final IConvert2Tcx tcx : cc.getAllConverter()) {
            defaults.putBoolean(PreferenceConstants.FILE_SUFFIX_FOR_BACKUP + tcx.getFilePrefix(), true);
        }

        // training target
        defaults.putInt(PreferenceConstants.WEEK_FOR_PLAN, ANZAHL_PLAENE);
        defaults.put(PreferenceConstants.ZIEL_ERFUELLT_COLOR, "108,233,144");
        defaults.put(PreferenceConstants.ZIEL_NICHT_ERFUELLT_COLOR, "212,62,59");
        defaults.put(PreferenceConstants.ZIEL_NICHT_BEKANNT_COLOR, "173,216,230");

        defaults.put(PreferenceConstants.CHART_DISTANCE_COLOR, "30,144,255");
        defaults.put(PreferenceConstants.CHART_HEART_COLOR, "255,17,14");

        defaults.put(PreferenceConstants.CHART_COLOR_RANGE, "217,197,17");

        defaults.put(PreferenceConstants.RUHEPULS_COLOR, "255,0,26");
        defaults.put(PreferenceConstants.GEWICHT_COLOR, "240,173,50");

        defaults.put(PreferenceConstants.KML_DEBUG_PATH, userHome + File.separator + ".otc/kml");

        // database default
        defaults.put(PreferenceConstants.DB_USER, "sa");
        defaults.put(PreferenceConstants.DB_PASS, "");
        defaults.put(PreferenceConstants.DB_URL, "jdbc:h2:file:~/.otc/otc");
        defaults.put(PreferenceConstants.DB, "H2 Database");

        // charts
        defaults.putInt(PreferenceConstants.CHART_XAXIS_CHART, 1);
        defaults.putInt(PreferenceConstants.CHART_YAXIS_CHART, 0);
        defaults.putInt(PreferenceConstants.CHART_SPORT, Sport.RUNNING.getIndex());
        defaults.putBoolean(PreferenceConstants.CHART_COMPARE, true);
        defaults.putInt(PreferenceConstants.CHART_WEEKS, 16);

        defaults.putBoolean(Sport.RUNNING.getMessage(), true);
        defaults.putBoolean(Sport.BIKING.getMessage(), false);
        defaults.putBoolean(Sport.OTHER.getMessage(), false);

        defaults.putBoolean(PreferenceConstants.SYNTH_RUNDEN, true);
        defaults.put(PreferenceConstants.DEFAULT_SCHUH_1, "0");
    }

}
