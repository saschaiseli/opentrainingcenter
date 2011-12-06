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

    public static List<File> getGarminFiles() {

        final String defaultLocation = Activator.getDefault().getPreferenceStore().getString(PreferenceConstants.GPS_FILE_LOCATION);

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

    /**
     * Sucht aus allen gps files diejenigen raus, die auch zu dem entsprechenden athleten gehören
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

    private static boolean istFileNameVonKeyIn(final Map<String, File> fileNameToFile, final Map.Entry<Integer, String> entry) {
        return fileNameToFile.keySet().contains(entry.getValue());
    }

    public static List<File> getGarminFiles(final Collection<String> blacklist) {
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
