package ch.opentrainingcenter.client.action.job;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.apache.log4j.Logger;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;

import ch.opentrainingcenter.core.db.IDatabaseConnection;
import ch.opentrainingcenter.core.helper.GpsFileNameFilter;
import ch.opentrainingcenter.core.importer.IConvert2Tcx;
import ch.opentrainingcenter.i18n.Messages;

/**
 * Erstellt ein Backup mit allen importierten Daten.
 * 
 */
public class BackupJob extends Job {

    private static final Logger LOG = Logger.getLogger(BackupJob.class.getName());

    private static final int BYTE = 1024;

    private final String[] fileToCopy;

    private final File destFolder;

    private final String source;

    private final IDatabaseConnection dbConnection;

    public BackupJob(final String name, final String source, final File destFolder, final Map<String, IConvert2Tcx> converters, final IDatabaseConnection conn) {
        super(name);
        this.source = source;
        this.destFolder = destFolder;
        this.dbConnection = conn;
        final File f = new File(this.source);
        fileToCopy = f.list(new GpsFileNameFilter(converters));
    }

    @Override
    protected final IStatus run(final IProgressMonitor monitor) {
        monitor.beginTask(Messages.BackupGpsFiles1, fileToCopy.length);

        final String zipFileName = createZipFileName();
        final File zipFile = new File(destFolder, zipFileName);

        ZipOutputStream out = null;
        try {
            out = new ZipOutputStream(new FileOutputStream(zipFile));
        } catch (final FileNotFoundException e) {
            LOG.error(e.getMessage());
        }
        final byte[] buf = new byte[BYTE];
        for (final String fileName : fileToCopy) {
            final File file = new File(source + File.separator + fileName);
            try {
                addToZip(out, buf, file);
            } catch (final IOException e) {
                LOG.error("Fehler beim Zippen: Konnte file nicht finden: " + e.getMessage()); //$NON-NLS-1$
            }
            monitor.worked(1);
        }
        try {
            addToZip(out, buf, dbConnection.backUpDatabase(source));
            out.close();
        } catch (final IOException e) {
            LOG.error(e.getMessage());
        }
        return Status.OK_STATUS;
    }

    private void addToZip(final ZipOutputStream out, final byte[] buf, final File file) throws IOException {
        final FileInputStream in = new FileInputStream(file);

        // Add ZIP entry to output stream.
        out.putNextEntry(new ZipEntry(file.getName()));

        // Transfer bytes from the file to the ZIP file
        int len;
        while ((len = in.read(buf)) > 0) {
            out.write(buf, 0, len);
        }
        // Complete the entry
        out.closeEntry();
        in.close();
    }

    protected String createZipFileName() {
        final Date date = new Date();
        final SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd"); //$NON-NLS-1$
        return df.format(date) + ".zip"; //$NON-NLS-1$
    }

    public String[] getFileToCopy() {
        return fileToCopy;
    }
}
