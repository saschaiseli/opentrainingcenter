package ch.opentrainingcenter.importer;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.Collections;
import java.util.List;

import org.apache.log4j.Logger;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.osgi.framework.Bundle;

import ch.opentrainingcenter.core.importer.IConvert2Tcx;
import ch.opentrainingcenter.i18n.Messages;
import ch.opentrainingcenter.importer.internal.xml.ConvertXml;
import ch.opentrainingcenter.tcx.ActivityT;
import ch.opentrainingcenter.tcx.TrainingCenterDatabaseT;
import ch.opentrainingcenter.transfer.ITraining;

public class Gmn2Tcx implements IConvert2Tcx {

    private static final Logger logger = Logger.getLogger(Gmn2Tcx.class);

    private final ConvertXml delegate;

    private final Bundle bundle;

    public Gmn2Tcx() {
        logger.info("Gmn2Tcx erfolgreich instanziert....");//$NON-NLS-1$
        bundle = Platform.getBundle("ch.opentrainingcenter.importer");//$NON-NLS-1$
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

    // /**
    // * Constructor 4 tests
    // *
    // * @param locationOfScript
    // */
    // public Gmn2Tcx(final String locationOfScript) {
    // delegate = null;// new ConvertXml(new URL(locationOfScript));
    // }

    protected TrainingCenterDatabaseT convert(final java.io.File file) throws Exception {
        final long start = System.currentTimeMillis();
        logger.debug("Start Time: " + start); //$NON-NLS-1$
        // convertForMe();
        final InputStream convert2Tcx = convert2Tcx(file);
        final long nachTcx = System.currentTimeMillis();
        logger.debug("Zeit für das convertieren nach tcx: " + (nachTcx - start)); //$NON-NLS-1$
        final TrainingCenterDatabaseT unmarshall = delegate.unmarshall(convert2Tcx);
        final long nachUnmarshall = System.currentTimeMillis();
        logger.debug("Zeit für das unmarshalling: " + (nachUnmarshall - nachTcx)); //$NON-NLS-1$
        return unmarshall;
    }

    @SuppressWarnings("unused")
    private void convertForMe() throws IOException {
        final File f = new File("/home/sascha/allgmn"); //$NON-NLS-1$
        final File[] listFiles = f.listFiles();
        for (final File file : listFiles) {
            final InputStream convert2Tcx = convert2Tcx(file);

            final String name = file.getName();
            final File outFile = new File("/home/sascha", name.replace("gmn", "tcx")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$

            final OutputStream out = new FileOutputStream(outFile);
            final byte buf[] = new byte[1024];
            int len;
            while ((len = convert2Tcx.read(buf)) > 0) {
                out.write(buf, 0, len);
            }
            out.close();
            convert2Tcx.close();
        }
    }

    @Override
    public List<ITraining> convertActivity(final File file) throws Exception {
        final List<ActivityT> activity = convert(file).getActivities().getActivity();
        return null;
    }

    protected InputStream convert2Tcx(final java.io.File file) {
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

    @Override
    public String getName() {
        return Messages.Gmn2Tcx_0;
    }

}
