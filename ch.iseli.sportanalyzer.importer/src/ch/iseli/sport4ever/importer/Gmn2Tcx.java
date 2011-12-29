package ch.iseli.sport4ever.importer;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Collections;
import java.util.List;

import org.apache.log4j.Logger;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.osgi.framework.Bundle;

import ch.iseli.sport4ever.importer.internal.xml.ConvertXml;
import ch.iseli.sportanalyzer.importer.IConvert2Tcx;
import ch.iseli.sportanalyzer.tcx.ActivityT;
import ch.iseli.sportanalyzer.tcx.TrainingCenterDatabaseT;

public class Gmn2Tcx implements IConvert2Tcx {

    private static final Logger logger = Logger.getLogger(Gmn2Tcx.class);

    private final ConvertXml delegate;

    private Bundle bundle;

    public Gmn2Tcx() {
        logger.info("Gmn2Tcx erfolgreich instanziert....");//$NON-NLS-1$
        bundle = Platform.getBundle("ch.iseli.sportanalyzer.importer");//$NON-NLS-1$
        final Path path = new Path("resources/tcx.xsd");//$NON-NLS-1$
        final URL url = FileLocator.find(bundle, path, Collections.EMPTY_MAP);
        URL fileUrl = null;
        try {
            fileUrl = FileLocator.toFileURL(url);
        } catch (final IOException e) {
            logger.error("Fehler beim Instanzieren von Gmn2Tcx: " + e.getMessage());//$NON-NLS-1$
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
    public TrainingCenterDatabaseT convert(final java.io.File file) throws Exception {
        final InputStream convert2Tcx = convert2Tcx(file);
        return delegate.unmarshall(convert2Tcx);
    }

    @Override
    public List<ActivityT> convertActivity(final File file) throws Exception {
        return convert(file).getActivities().getActivity();
    }

    protected InputStream convert2Tcx(final java.io.File file) throws IOException {
        logger.debug("file " + file.getAbsolutePath() + " existiert: " + file.exists());//$NON-NLS-1$//$NON-NLS-2$
        final Path path = new Path("resources/gmn2tcx.sh");//$NON-NLS-1$
        logger.debug("vor url");//$NON-NLS-1$
        final URL url = FileLocator.find(bundle, path, Collections.EMPTY_MAP);
        logger.debug("nach url: " + url.getPath());//$NON-NLS-1$
        URL fileUrl = null;
        try {
            logger.debug("vor fileUrl");//$NON-NLS-1$
            fileUrl = FileLocator.toFileURL(url);
            logger.debug("nach fileUrl: " + fileUrl.getPath());//$NON-NLS-1$
        } catch (final IOException e) {
            logger.error("Konvertieren der GPS Daten fehlgeschlagen: " + e.getMessage());//$NON-NLS-1$
        }
        logger.debug("vor f");//$NON-NLS-1$
        final File f = new File(fileUrl.getPath());
        logger.debug("nach f: " + f.getAbsolutePath());//$NON-NLS-1$
        final ProcessBuilder processBuilder = new ProcessBuilder(f.getAbsolutePath(), file.getAbsolutePath());
        logger.debug("nach processBuilder");//$NON-NLS-1$
        Process process = null;
        try {
            process = processBuilder.start();
        } catch (final IOException ioe) {
            logger.error("Process konnte nicht ausgeführt werden: " + ioe.getMessage()); //$NON-NLS-1$
        }
        logger.debug("nach process und back zurück");//$NON-NLS-1$
        return process.getInputStream();
    }

    @SuppressWarnings("unused")
    private String createCommand() {
        final URL resource = Gmn2Tcx.class.getClassLoader().getResource("gmn2tcx.sh");//$NON-NLS-1$
        final String cmd = resource.getFile().replace("/bin", "/resources");//$NON-NLS-1$//$NON-NLS-2$
        return cmd;
    }

    @Override
    public String getFilePrefix() {
        return "gmn";//$NON-NLS-1$
    }

}
