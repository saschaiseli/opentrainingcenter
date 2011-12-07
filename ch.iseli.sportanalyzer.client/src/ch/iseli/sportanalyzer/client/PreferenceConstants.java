package ch.iseli.sportanalyzer.client;

/**
 * Constant definitions for plug-in preferences
 */
public class PreferenceConstants {

    private static final String COLOR_POSTFIX = "_color";
    /**
     * Ort wo die Daten auf dem Compi gespeichert sind
     */
    public final static String GPS_FILE_LOCATION = "gps_path";
    /**
     * Ort, wo OTC die Daten absichert.
     */
    public static final String GPS_FILE_LOCATION_PROG = "gps_path_prog";
    public static final String ATHLETE_ID = "athlete_id";
    public static final String SB = "spitzenbereich";
    public static final String EXTDL = "extensiver_dauerlauf";
    public static final String INTDL = "intensiver_dauerlauf";
    public static final String EXTINTERVALL = "exttensiver_intervall";
    public static final String AEROBE = "aerobe";
    public static final String AEROBE_COLOR = AEROBE + COLOR_POSTFIX;
    public static final String SCHWELLENZONE = "schwelle";
    public static final String SCHWELLENZONE_COLOR = SCHWELLENZONE + COLOR_POSTFIX;
    public static final String ANAEROBE = "anaerobe";
    public static final String ANAEROBE_COLOR = ANAEROBE + COLOR_POSTFIX;

}
