package ch.opentrainingcenter.client;

/**
 * Constant definitions for plug-in preferences
 */
public class PreferenceConstants {

    private static final String COLOR_POSTFIX = "_color"; //$NON-NLS-1$
    /**
     * Ort wo die Daten auf dem Compi gespeichert sind
     */
    public final static String GPS_FILE_LOCATION = "gps_path"; //$NON-NLS-1$
    /**
     * Ort, wo OTC die Daten absichert.
     */
    public static final String GPS_FILE_LOCATION_PROG = "gps_path_prog"; //$NON-NLS-1$
    public static final String ATHLETE_ID = "athlete_id"; //$NON-NLS-1$
    public static final String SB = "spitzenbereich"; //$NON-NLS-1$
    public static final String EXTDL = "extensiver_dauerlauf"; //$NON-NLS-1$
    public static final String INTDL = "intensiver_dauerlauf"; //$NON-NLS-1$
    public static final String EXTINTERVALL = "exttensiver_intervall"; //$NON-NLS-1$
    public static final String AEROBE = "aerobe"; //$NON-NLS-1$
    public static final String AEROBE_COLOR = AEROBE + COLOR_POSTFIX;
    public static final String SCHWELLENZONE = "schwelle"; //$NON-NLS-1$
    public static final String SCHWELLENZONE_COLOR = SCHWELLENZONE + COLOR_POSTFIX;
    public static final String ANAEROBE = "anaerobe"; //$NON-NLS-1$
    public static final String ANAEROBE_COLOR = ANAEROBE + COLOR_POSTFIX;
    public static final String BACKUP_FILE_LOCATION = "backup_path"; //$NON-NLS-1$
    public static final String FILE_SUFFIX_FOR_BACKUP = "filesuffix_"; //$NON-NLS-1$
    public static final String KM_PER_WEEK = "KM_PER_WEEK"; //$NON-NLS-1$
    public static final String KM_PER_WEEK_COLOR_BELOW = "KM_PER_WEEK_COLOR_BELOW"; //$NON-NLS-1$
    public static final String KM_PER_WEEK_COLOR_ABOVE = "KM_PER_WEEK_COLOR_ABOVE"; //$NON-NLS-1$
    public static final String DISTANCE_CHART_COLOR = "DISTANCE_CHART_COLOR"; //$NON-NLS-1$
    public static final String DISTANCE_HEART_COLOR = "DISTANCE_HEART_COLOR"; //$NON-NLS-1$

}
