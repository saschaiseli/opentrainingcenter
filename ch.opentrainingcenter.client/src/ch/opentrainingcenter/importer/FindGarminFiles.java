package ch.opentrainingcenter.importer;

import java.io.File;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.jface.preference.IPreferenceStore;

import ch.opentrainingcenter.client.PreferenceConstants;
import ch.opentrainingcenter.client.action.ImportManualGpsFiles;

public final class FindGarminFiles {

    private FindGarminFiles() {
    }

    /**
     * Sucht aus allen gps files dasjenige aus, welches den angegebenen
     * Filenamen hat. Das File wird nur gefunden, wenn der record bereits einmal
     * importiert wurde.
     * 
     * @param fileName
     *            Name des Records.
     * @param store
     * @return das effektive gps file.
     */
    public static File loadAllGPSFile(final String fileName, final IPreferenceStore store) {

        final List<File> all = FindGarminFiles.getGarminFiles(store);
        final Map<String, File> fileNameToFile = new HashMap<String, File>();
        for (final File file : all) {
            fileNameToFile.put(file.getName(), file);
        }
        if (istFileNameVonKeyIn(fileNameToFile, fileName)) {
            final String withoutImportPattern = removeImportPattern(fileName);
            return fileNameToFile.get(withoutImportPattern);
        }
        return null;
    }

    private static String removeImportPattern(String fileName) {
        if (fileName.contains(ImportManualGpsFiles.IMPORT_PATTERN)) {
            fileName = fileName.substring(0, fileName.indexOf(ImportManualGpsFiles.IMPORT_PATTERN));
        }
        return fileName;
    }

    /**
     * Gibt alle Files zurück, welche im Verzeichnis sind, wo das Programm die
     * GPS Files ablegt.
     */
    private static List<File> getGarminFiles(final IPreferenceStore store) {

        final String defaultLocation = store.getString(PreferenceConstants.GPS_FILE_LOCATION_PROG);

        final File f = new File(defaultLocation);
        final File[] listFiles = f.listFiles();
        if (listFiles != null && listFiles.length > 0) {
            return Arrays.asList(listFiles);
        }
        return Collections.emptyList();
    }

    private static boolean istFileNameVonKeyIn(final Map<String, File> fileNameToFile, final String fileName) {
        if (fileName == null) {
            return false;
        }
        final String tmpFileName = removeImportPattern(fileName);
        return fileNameToFile.keySet().contains(tmpFileName);
    }

}
