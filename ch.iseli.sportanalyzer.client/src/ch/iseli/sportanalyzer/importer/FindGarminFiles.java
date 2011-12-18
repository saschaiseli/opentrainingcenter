package ch.iseli.sportanalyzer.importer;

import java.io.File;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ch.iseli.sportanalyzer.client.Activator;
import ch.iseli.sportanalyzer.client.PreferenceConstants;
import ch.iseli.sportanalyzer.client.action.ImportManualGpsFiles;

public class FindGarminFiles {

    /**
     * Sucht aus allen gps files diejenigen raus, die auch zu dem entsprechenden athleten gehören. Es werden nur Files gefunden, die auch schon einmal importiert wurden.
     * 
     * @param garminFileNamesAlreadyImported
     *            map mit der id und dem filenamen der bereits importiert ist.
     * @return map mit derselben id welche als value das {@link File} hat.
     */
    public static Map<Date, File> loadAllGPSFilesFromAthlete(final Map<Date, String> garminFileNamesAlreadyImported) {
        if (garminFileNamesAlreadyImported == null) {
            return Collections.emptyMap();
        }
        final List<File> all = FindGarminFiles.getGarminFiles();
        final Map<String, File> fileNameToFile = new HashMap<String, File>();
        for (final File file : all) {
            fileNameToFile.put(file.getName(), file);
        }
        final Map<Date, File> result = new HashMap<Date, File>();
        for (final Map.Entry<Date, String> entry : garminFileNamesAlreadyImported.entrySet()) {
            final String fileName = entry.getValue();
            if (istFileNameVonKeyIn(fileNameToFile, fileName)) {
                final String withoutImportPattern = removeImportPattern(fileName);
                result.put(entry.getKey(), fileNameToFile.get(withoutImportPattern));
            }
        }
        return result;
    }

    /**
     * Gibt alle Files zurück, welche im Verzeichnis sind, wo das Programm die GPS Files ablegt.
     */
    private static List<File> getGarminFiles() {

        final String defaultLocation = Activator.getDefault().getPreferenceStore().getString(PreferenceConstants.GPS_FILE_LOCATION_PROG);

        final File f = new File(defaultLocation);
        final File[] listFiles = f.listFiles();
        if (listFiles != null && listFiles.length > 0) {
            return Arrays.asList(listFiles);
        }
        return Collections.emptyList();
    }

    private static boolean istFileNameVonKeyIn(final Map<String, File> fileNameToFile, final String fileName) {
        final String tmpFileName = removeImportPattern(fileName);
        return fileNameToFile.keySet().contains(tmpFileName);
    }

    private static String removeImportPattern(String fileName) {
        if (fileName.contains(ImportManualGpsFiles.IMPORT_PATTERN)) {
            fileName = fileName.substring(0, fileName.indexOf(ImportManualGpsFiles.IMPORT_PATTERN));
        }
        return fileName;
    }
}
