package ch.iseli.sportanalyzer.importer;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

import ch.iseli.sportanalyzer.tcx.TrainingCenterDatabaseT;

public interface IConvert2Tcx {

    public static final String CORE_PLUGIN_ID = "ch.iseli.sportanalyzer.client";

    /**
     * Konvertiert ein *.gmn File in eine Garmin spezifisches XML.
     * 
     * @param file
     * @return das XML als String
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
    List<File> loadAllGPSFiles();

    Map<Integer, File> loadAllGPSFilesFromAthlete(Map<Integer, String> garminFiles);

    List<File> loadAllGPSFiles(List<String> garminFilesBlackList);

}
