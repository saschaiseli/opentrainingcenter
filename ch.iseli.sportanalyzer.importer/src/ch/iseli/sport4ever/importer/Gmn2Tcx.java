package ch.iseli.sport4ever.importer;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.osgi.framework.Bundle;

import ch.iseli.sport4ever.importer.internal.xml.ConvertXml;
import ch.iseli.sportanalyzer.importer.FindGarminFiles;
import ch.iseli.sportanalyzer.importer.IConvert2Tcx;
import ch.iseli.sportanalyzer.tcx.TrainingCenterDatabaseT;

public class Gmn2Tcx implements IConvert2Tcx {

    private static final Logger log = Logger.getLogger(Gmn2Tcx.class);

    private final ConvertXml delegate;

    private Bundle bundle;

    public Gmn2Tcx() {
        log.info("Gmn2Tcx erfolgreich instanziert....");
        bundle = Platform.getBundle("ch.iseli.sportanalyzer.importer");
        Path path = new Path("resources/tcx.xsd");
        URL url = FileLocator.find(bundle, path, Collections.EMPTY_MAP);
        URL fileUrl = null;
        try {
            fileUrl = FileLocator.toFileURL(url);
        } catch (IOException e) {
            log.error("Fehler beim Instanzieren von Gmn2Tcx: " + e.getMessage());
            throw new RuntimeException(e);
        }
        File f = new File(fileUrl.getPath());

        delegate = new ConvertXml(f.getAbsolutePath());
    }

    /**
     * Constructor 4 tests
     * 
     * @param locationOfScript
     */
    public Gmn2Tcx(String locationOfScript) {
        delegate = null;// new ConvertXml(new URL(locationOfScript));
    }

    @Override
    public InputStream convert2Tcx(java.io.File file) throws IOException {
        log.debug("file " + file.getAbsolutePath() + " existiert: " + file.exists());
        Path path = new Path("resources/gmn2tcx.sh");
        URL url = FileLocator.find(bundle, path, Collections.EMPTY_MAP);
        URL fileUrl = null;
        try {
            fileUrl = FileLocator.toFileURL(url);
        } catch (IOException e) {
            log.error("Konvertieren der GPS Daten fehlgeschlagen: " + e.getMessage());
        }
        File f = new File(fileUrl.getPath());
        ProcessBuilder processBuilder = new ProcessBuilder(f.getAbsolutePath(), file.getAbsolutePath());

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

    @Override
    public List<File> loadAllGPSFiles() {
        return FindGarminFiles.getGarminFiles();
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

    @Override
    public String getFilePrefix() {
        return "gmn";
    }

    private boolean istFileNameVonKeyIn(Map<String, File> fileNameToFile, Map.Entry<Integer, String> entry) {
        return fileNameToFile.keySet().contains(entry.getValue());
    }
}
