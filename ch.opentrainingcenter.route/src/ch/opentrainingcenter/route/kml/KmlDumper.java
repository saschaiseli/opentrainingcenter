package ch.opentrainingcenter.route.kml;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Calendar;
import java.util.Locale;
import java.util.Map;

import org.apache.log4j.Logger;

import ch.opentrainingcenter.core.helper.TimeHelper;

/**
 * Schreibt ein KML File oder loggt das KML als INFO.
 *
 * @author sascha
 *
 */
public class KmlDumper {

    private static final String LINE = "#####################################################################"; //$NON-NLS-1$
    private static final Logger LOGGER = Logger.getLogger(KmlDumper.class);
    private final KmlFile kmlFile;

    private final String kmlDumpPath;
    private final String fileName;

    /**
     * Dumpt ein KML File.
     *
     * Wenn der kmlDumpPath null ist wird kein File erstellt, sondern nur das
     * file ins logging ausgegeben.
     *
     * @param kmlDumpPath
     */
    public KmlDumper(final String kmlDumpPath) {
        this(TimeHelper.convertDateToFileName(Calendar.getInstance(Locale.getDefault()).getTime()), kmlDumpPath);
    }

    public KmlDumper(final String fileName, final String kmlDumpPath) {
        this.fileName = fileName;
        this.kmlDumpPath = kmlDumpPath;
        kmlFile = new KmlFile(fileName);
    }

    /**
     * @param lineName
     *            Name der Line
     * @param color
     *            Farbe gemäss aa=alpha (00 to ff); bb=blue (00 to ff); gg=green
     *            (00 to ff); rr=red (00 to ff)
     * @param coordinates
     *            alle koordinaten
     */
    public void addLine(final String lineName, final String color, final String coordinates) {
        kmlFile.addKmlLine(lineName, color, coordinates);
    }

    public void addPlacemark(final String name, final Map<String, String> extendedData, final String coordinates) {
        kmlFile.addPlacemark(name, extendedData, coordinates);
    }

    /**
     * Das KML File wird nur auf die Platte geschrieben, wenn auch ein Pfad
     * definiert wurde. Ansonsten wird es nur im LOG ausgegeben.
     */
    public void dump() {
        if (kmlDumpPath != null) {
            FileWriter writer = null;
            BufferedWriter out = null;
            try {
                final File f = new File(kmlDumpPath, fileName + ".kml"); //$NON-NLS-1$
                writer = new FileWriter(f);
                LOGGER.info("Dumpe KML File nach: " + f.getAbsolutePath()); //$NON-NLS-1$
                out = new BufferedWriter(writer);
                out.write(kmlFile.getFile());
            } catch (final IOException ex) {
                LOGGER.error("Kann das File '" + fileName + "' nicht dumpen: " + ex.getMessage()); //$NON-NLS-1$ //$NON-NLS-2$
            } finally {
                if (out != null) {
                    try {
                        out.close();
                    } catch (final IOException e) {
                        LOGGER.error(e);
                    }
                }
            }
        } else {
            LOGGER.info(LINE);
            LOGGER.info("KML File wird geloggt, da kein Pfad für das Speichern angegeben wurde"); //$NON-NLS-1$
            LOGGER.info(kmlFile.getFile());
            LOGGER.info(LINE);
        }
    }
}
