package ch.iseli.sportanalyzer.importer;

import java.io.File;
import java.io.FilenameFilter;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ch.iseli.sportanalyzer.client.Activator;
import ch.iseli.sportanalyzer.client.PreferenceConstants;

public class FindGarminFiles {

    /**
     * Sucht aus allen gps files diejenigen raus, die auch zu dem entsprechenden athleten gehören. Es werden nur Files gefunden, die auch schon einmal importiert wurden.
     * 
     * @param garminFileNamesAlreadyImported
     *            map mit der id und dem filenamen der bereits importiert ist.
     * @return map mit derselben id welche als value das {@link File} hat.
     */
    public static Map<Integer, File> loadAllGPSFilesFromAthlete(final Map<Integer, String> garminFileNamesAlreadyImported) {
        if (garminFileNamesAlreadyImported == null) {
            return Collections.emptyMap();
        }
        final List<File> all = FindGarminFiles.getGarminFiles();
        final Map<String, File> fileNameToFile = new HashMap<String, File>();
        for (final File file : all) {
            fileNameToFile.put(file.getName(), file);
        }
        final Map<Integer, File> result = new HashMap<Integer, File>();
        for (final Map.Entry<Integer, String> entry : garminFileNamesAlreadyImported.entrySet()) {
            if (istFileNameVonKeyIn(fileNameToFile, entry)) {
                result.put(entry.getKey(), fileNameToFile.get(entry.getValue()));
            }
        }
        return result;
    }

    /**
     * Sucht alle garmin files welche bereits einmal mit dem otc importiert wurden.
     */
    private static List<File> getGarminFiles() {

        final String defaultLocation = Activator.getDefault().getPreferenceStore().getString(PreferenceConstants.GPS_FILE_LOCATION_PROG);

        final File f = new File(defaultLocation);
        final File[] listFiles = f.listFiles(new FilenameFilter() {

            @Override
            public boolean accept(final File dir, final String name) {
                return name.contains(".gmn");
            }
        });
        if (listFiles != null && listFiles.length > 0) {
            return Arrays.asList(listFiles);
        }
        return Collections.emptyList();
    }

    private static boolean istFileNameVonKeyIn(final Map<String, File> fileNameToFile, final Map.Entry<Integer, String> entry) {
        return fileNameToFile.keySet().contains(entry.getValue());
    }

    /**
     * Lädt alle Files vom Ort wo die Daten von der Uhr aus abgelegt wurden. Dies ist nicht der Ort wo OTC die Datenablegt, sondern nur der Ort wo die Daten initial auf dem Compi
     * sind.
     * 
     * @param blacklist
     * @return
     */
    public static List<File> getGarminFilesFromWatchExportedPlace(final Collection<String> blacklist) {
        final String defaultLocation = Activator.getDefault().getPreferenceStore().getString(PreferenceConstants.GPS_FILE_LOCATION);
        final File f = new File(defaultLocation);
        final File[] listFiles = f.listFiles(new FilenameFilter() {

            @Override
            public boolean accept(final File dir, final String name) {
                return name.contains(".gmn") && !blacklist.contains(name);
            }
        });
        if (listFiles != null && listFiles.length > 0) {
            return Arrays.asList(listFiles);
        }
        return Collections.emptyList();
    }
}
