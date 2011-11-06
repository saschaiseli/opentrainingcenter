package ch.iseli.sport4ever.importer.internal;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.List;

import org.apache.log4j.Logger;

import ch.iseli.sport4ever.importer.internal.xml.ConvertXml;
import ch.iseli.sportanalyzer.client.Activator;
import ch.iseli.sportanalyzer.client.PreferenceConstants;
import ch.iseli.sportanalyzer.importer.IConvert2Tcx;
import ch.iseli.sportanalyzer.tcx.TrainingCenterDatabaseT;

public class Gmn2Tcx implements IConvert2Tcx {

    private static final Logger log = Logger.getLogger(Gmn2Tcx.class);

    private final ConvertXml delegate;

    private final String locationOfScript;

    public Gmn2Tcx() {
        locationOfScript = Activator.getDefault().getPreferenceStore().getString(PreferenceConstants.GPS_FILE_LOCATION).replace("garmin", "resources");
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
}
