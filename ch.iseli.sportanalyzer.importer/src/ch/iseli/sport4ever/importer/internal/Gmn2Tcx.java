package ch.iseli.sport4ever.importer.internal;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import ch.iseli.sport4ever.importer.internal.xml.ConvertXml;
import ch.iseli.sportanalyzer.client.Activator;
import ch.iseli.sportanalyzer.client.PreferenceConstants;
import ch.iseli.sportanalyzer.importer.FindGarminFiles;
import ch.iseli.sportanalyzer.importer.IConvert2Tcx;
import ch.iseli.sportanalyzer.tcx.TrainingCenterDatabaseT;

public class Gmn2Tcx implements IConvert2Tcx {

    private static final Logger log = Logger.getLogger(Gmn2Tcx.class);

    private final ConvertXml delegate;

    private final String locationOfScript;

    public Gmn2Tcx() {
        locationOfScript = Activator.getDefault().getPreferenceStore().getString(PreferenceConstants.IMPORT_PROGRAMM).replace("garmin", "resources");
        delegate = new ConvertXml(locationOfScript);
    }

    /**
     * Constructor 4 tests
     * 
     * @param locationOfScript
     */
    public Gmn2Tcx(String locationOfScript) {
        this.locationOfScript = locationOfScript;
        delegate = new ConvertXml(locationOfScript);
    }

    @Override
    public InputStream convert2Tcx(java.io.File file) throws IOException {

        String cmd = createCommandFromPlugin();
        log.debug("file " + file.getAbsolutePath() + " existiert: " + file.exists());
        ProcessBuilder processBuilder = new ProcessBuilder(cmd, file.getAbsolutePath());

        Process process = processBuilder.start();

        return process.getInputStream();
    }

    @Override
    public TrainingCenterDatabaseT convert(java.io.File file) throws Exception {
        InputStream convert2Tcx = convert2Tcx(file);
        return delegate.unmarshall(convert2Tcx);
    }

    @SuppressWarnings("unused")
    private String createCommand() {
        URL resource = Gmn2Tcx.class.getClassLoader().getResource("gmn2tcx.sh");
        String cmd = resource.getFile().replace("/bin", "/resources");
        return cmd;
    }

    private String createCommandFromPlugin() {
        return locationOfScript + "/gmn2tcx.sh";
    }

    @Override
    public List<File> loadAllGPSFiles() {
        return FindGarminFiles.getGarminFiles();
    }

    @Override
    public List<File> loadAllGPSFiles(List<String> garminFilesBlackList) {
        if (garminFilesBlackList == null) {
            throw new IllegalArgumentException();
        }
        List<File> all = loadAllGPSFiles();
        List<File> result = new ArrayList<File>();
        for (File file : all) {
            if (garminFilesBlackList.contains(file.getName())) {
                result.add(file);
            }
        }
        return result;
    }

    @Override
    public Map<Integer, File> loadAllGPSFilesFromAthlete(Map<Integer, String> garminFiles) {
        if (garminFiles == null) {
            throw new IllegalArgumentException();
        }
        List<File> all = loadAllGPSFiles();
        Map<String, File> fileNameToFile = new HashMap<String, File>();
        for (File file : all) {
            fileNameToFile.put(file.getName(), file);
        }
        Map<Integer, File> result = new HashMap<Integer, File>();
        for (Map.Entry<Integer, String> entry : garminFiles.entrySet()) {
            if (istFileNameVonKeyIn(fileNameToFile, entry)) {
                result.put(entry.getKey(), fileNameToFile.get(entry.getValue()));
            }
        }
        return result;
    }

    private boolean istFileNameVonKeyIn(Map<String, File> fileNameToFile, Map.Entry<Integer, String> entry) {
        return fileNameToFile.keySet().contains(entry.getValue());
    }
}
