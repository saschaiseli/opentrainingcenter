package ch.iseli.sport4ever.importer;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Collections;

import org.apache.log4j.Logger;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.osgi.framework.Bundle;

import ch.iseli.sport4ever.importer.internal.xml.ConvertXml;
import ch.iseli.sportanalyzer.importer.IConvert2Tcx;
import ch.iseli.sportanalyzer.tcx.TrainingCenterDatabaseT;

public class Gmn2Tcx implements IConvert2Tcx {

    private static final Logger log = Logger.getLogger(Gmn2Tcx.class);

    private final ConvertXml delegate;

    private Bundle bundle;

    public Gmn2Tcx() {
        log.info("Gmn2Tcx erfolgreich instanziert....");
        bundle = Platform.getBundle("ch.iseli.sportanalyzer.importer");
        final Path path = new Path("resources/tcx.xsd");
        final URL url = FileLocator.find(bundle, path, Collections.EMPTY_MAP);
        URL fileUrl = null;
        try {
            fileUrl = FileLocator.toFileURL(url);
        } catch (final IOException e) {
            log.error("Fehler beim Instanzieren von Gmn2Tcx: " + e.getMessage());
            throw new RuntimeException(e);
        }
        final File f = new File(fileUrl.getPath());

        delegate = new ConvertXml(f.getAbsolutePath());
    }

    /**
     * Constructor 4 tests
     * 
     * @param locationOfScript
     */
    public Gmn2Tcx(final String locationOfScript) {
        delegate = null;// new ConvertXml(new URL(locationOfScript));
    }

    @Override
    public InputStream convert2Tcx(final java.io.File file) throws IOException {
        log.debug("file " + file.getAbsolutePath() + " existiert: " + file.exists());
        final Path path = new Path("resources/gmn2tcx.sh");
        log.debug("vor url");
        final URL url = FileLocator.find(bundle, path, Collections.EMPTY_MAP);
        log.debug("nach url: " + url.getPath());
        URL fileUrl = null;
        try {
            log.debug("vor fileUrl");
            fileUrl = FileLocator.toFileURL(url);
            log.debug("nach fileUrl: " + fileUrl.getPath());
        } catch (final IOException e) {
            log.error("Konvertieren der GPS Daten fehlgeschlagen: " + e.getMessage());
        }
        log.debug("vor f");
        final File f = new File(fileUrl.getPath());
        log.debug("nach f: " + f.getAbsolutePath());
        final ProcessBuilder processBuilder = new ProcessBuilder(f.getAbsolutePath(), file.getAbsolutePath());
        log.debug("nach processBuilder");
        final Process process = processBuilder.start();
        log.debug("nach process und back zurück");
        return process.getInputStream();
    }

    @Override
    public TrainingCenterDatabaseT convert(final java.io.File file) throws Exception {
        final InputStream convert2Tcx = convert2Tcx(file);
        return delegate.unmarshall(convert2Tcx);
    }

    @SuppressWarnings("unused")
    private String createCommand() {
        final URL resource = Gmn2Tcx.class.getClassLoader().getResource("gmn2tcx.sh");
        final String cmd = resource.getFile().replace("/bin", "/resources");
        return cmd;
    }

    @Override
    public String getFilePrefix() {
        return "gmn";
    }

}
