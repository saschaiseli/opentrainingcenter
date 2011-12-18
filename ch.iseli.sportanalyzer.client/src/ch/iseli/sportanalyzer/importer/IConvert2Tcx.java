package ch.iseli.sportanalyzer.importer;

import java.io.File;
import java.util.List;

import ch.iseli.sportanalyzer.tcx.ActivityT;
import ch.iseli.sportanalyzer.tcx.TrainingCenterDatabaseT;

public interface IConvert2Tcx {

    public static final String PROPERETY = "class"; //$NON-NLS-1$

    public static final String CORE_PLUGIN_ID = "ch.iseli.sportanalyzer.client"; //$NON-NLS-1$

    /**
     * Liest ein File mit den GPS Daten irgendeines Gerätes aus und konvertiert diese in das Garmin spezifische Format, welches aus dem tcx.xsd generiert wurde.
     * 
     * @param file
     *            Ursprungsfile
     * @return in Garmin konvertiertes Object, welches einem sportlichen Lauf entspricht.
     * @throws Exception
     *             wenn etwas schiefgeht beim parsen lesen des files,...
     * @deprecated use {@link IConvert2Tcx#convertActivity(File)}
     */
    @Deprecated
    TrainingCenterDatabaseT convert(File file) throws Exception;

    /**
     * Liest ein GPS File ein und konvertiert dies in eine oder mehrere Aktivitäten.
     * 
     * 
     * @param file
     *            Ursprungsfile
     * @return in Garmin konvertiertes Object, welches einem oder mehreren Läufen entspricht.
     * @throws Exception
     *             wenn etwas schiefgeht beim parsen lesen des files,...
     */
    List<ActivityT> convertActivity(final File file) throws Exception;

    /**
     * @return den prefix für ein GPS file. Bei Garmin wird demnach '*.gmn' zurückgegeben.
     */
    String getFilePrefix();

}
