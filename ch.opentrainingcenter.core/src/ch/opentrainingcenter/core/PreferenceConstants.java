package ch.opentrainingcenter.core;

/**
 * Constant definitions for plug-in preferences
 */
@SuppressWarnings("nls")
public final class PreferenceConstants {

    private static final String COLOR_POSTFIX = "_color";
    /**
     * Ort wo die Daten auf dem Compi gespeichert sind
     */
    public static final String GPS_FILE_LOCATION = "gps_path";
    /**
     * Ort, wo OTC die Daten absichert.
     */
    public static final String GPS_FILE_LOCATION_PROG = "gps_path_prog";
    public static final String ATHLETE_ID = "athlete_id";
    public static final String BACKUP_FILE_LOCATION = "backup_path";
    public static final String FILE_SUFFIX_FOR_BACKUP = "filesuffix_";
    public static final String WEEK_FOR_PLAN = "WEEK_FOR_PLAN";

    public static final String CHART_DISTANCE_COLOR = "DISTANCE_CHART_COLOR";
    public static final String CHART_DISTANCE_COLOR_PAST = "DISTANCE_CHART_COLOR_PAST";
    public static final String CHART_HEART_COLOR = "DISTANCE_HEART_COLOR";
    public static final String CHART_HEART_COLOR_PAST = "DISTANCE_HEART_COLOR_PAST";
    public static final String RECOM = "RECOM";
    public static final String RECOM_COLOR = RECOM + COLOR_POSTFIX;
    public static final String GA1 = "GA1";
    public static final String GA1_COLOR = GA1 + COLOR_POSTFIX;
    public static final String GA12 = "GA12";
    public static final String GA12_COLOR = GA12 + COLOR_POSTFIX;
    public static final String GA2 = "GA2";
    public static final String GA2_COLOR = GA2 + COLOR_POSTFIX;
    public static final String WSA = "WSA";
    public static final String WSA_COLOR = WSA + COLOR_POSTFIX;
    public static final String RUHEPULS_COLOR = "RUHEPULS";
    public static final String GEWICHT_COLOR = "GEWICHT";
    public static final String ZIEL_ERFUELLT_COLOR = "ZIEL_ERFUELLT_COLOR";
    public static final String ZIEL_NICHT_ERFUELLT_COLOR = "ZIEL_NICHT_ERFUELLT_COLOR";
    public static final String ZIEL_NICHT_BEKANNT_COLOR = "ZIEL_NICHT_BEKANNT_COLOR";

    public static final String KML_DEBUG_PATH = "KML_DEBUG_PATH";

    public static final String DB_USER = "DB_USER";
    public static final String DB_PASS = "DB_PASS";
    public static final String DB_URL = "DB_URL";

    public static final String DB_ADMIN_USER = "DB_ADMIN_USER";
    public static final String DB_ADMIN_PASS = "DB_ADMIN_PASS";
    public static final String DB_ADMIN_URL = "DB_ADMIN_URL";

    public static final String DB = "DB";

    // ----

    public static final String CHART_XAXIS_CHART = "XAxisChart";
    public static final String CHART_YAXIS_CHART = "CHART_YAXIS_CHART";
    public static final String CHART_COMPARE = "CHART_COMPARE";
    public static final String CHART_WEEKS = "CHART_WEEKS";

    private PreferenceConstants() {

    }

}
