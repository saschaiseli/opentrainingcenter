package ch.iseli.sportanalyzer.importer;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import ch.iseli.sportanalyzer.tcx.TrainingCenterDatabaseT;

public interface IConvert2Tcx {

    public static final String CORE_PLUGIN_ID = "ch.iseli.sportanalyzer.client"; //$NON-NLS-1$

    /**
     * Konvertiert ein *.gmn File in eine Garmin spezifisches XML.
     * 
     * @param das
     *            herstellerspezifische gps file. Für Garmin zum Beispiel ein 20110402T141929.gmn
     * @return das Garmin spezifische XML als Stream
     * @throws IOException
     */
    InputStream convert2Tcx(File file) throws IOException;

    /**
     * Liest ein File mit den GPS Daten irgendeines Gerätes aus und konvertiert diese in das Garmin spezifische Format, welches aus dem tcx.xsd generiert wurde.
     * 
     * @param file
     *            Ursprungsfile
     * @return in Garmin konvertiertes Object, welches einem sportlichen Lauf entspricht.
     * @throws Exception
     *             wenn etwas schiefgeht beim parsen lesen des files,...
     * 
     */
    TrainingCenterDatabaseT convert(File file) throws Exception;

    /**
     * @return alle vorhandenen GPS Files.
     * @throws Exception
     */
    // List<File> loadAllGPSFiles();

    // Map<Integer, File> loadAllGPSFilesFromAthlete(Map<Integer, String> garminFiles);

    /**
     * @return den prefix für ein GPS file. Bei Garmin wird demnach 'gmn' zurückgegeben.
     */
    public String getFilePrefix();

}
