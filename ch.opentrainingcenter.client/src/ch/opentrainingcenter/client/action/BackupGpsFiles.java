package ch.opentrainingcenter.client.action;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.apache.log4j.Logger;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.ui.ISelectionListener;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.actions.ActionFactory.IWorkbenchAction;
import org.eclipse.ui.plugin.AbstractUIPlugin;

import ch.opentrainingcenter.client.Activator;
import ch.opentrainingcenter.client.Application;
import ch.opentrainingcenter.client.Messages;
import ch.opentrainingcenter.client.PreferenceConstants;
import ch.opentrainingcenter.client.helper.GpsFileNameFilter;
import ch.opentrainingcenter.client.views.IImageKeys;

public class BackupGpsFiles extends Action implements ISelectionListener, IWorkbenchAction {

    private static final Logger logger = Logger.getLogger(BackupGpsFiles.class.getName());

    public static final String ID = "ch.opentrainingcenter.client.backupGpsFiles"; //$NON-NLS-1$

    private final String source;

    private final String destination;

    public BackupGpsFiles(final String tooltip) {
        setId(ID);
        setToolTipText(tooltip);
        setImageDescriptor(AbstractUIPlugin.imageDescriptorFromPlugin(Application.ID, IImageKeys.BACKUP));
        source = Activator.getDefault().getPreferenceStore().getString(PreferenceConstants.GPS_FILE_LOCATION_PROG);
        destination = Activator.getDefault().getPreferenceStore().getString(PreferenceConstants.BACKUP_FILE_LOCATION);
    }

    @Override
    public void run() {
        final File f = new File(source);

        final String[] fileToCopy = f.list(new GpsFileNameFilter());

        final File destFolder = new File(destination);
        if (!destFolder.exists()) {
            destFolder.mkdir();
            logger.info("Pfad zu Backupfolder erstellt"); //$NON-NLS-1$
        }

        final Job job = new Job(Messages.BackupGpsFiles_0) {

            @Override
            protected IStatus run(final IProgressMonitor monitor) {
                monitor.beginTask(Messages.BackupGpsFiles_1, fileToCopy.length);

                final Date date = new Date();
                final SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd"); //$NON-NLS-1$
                final String zipFileName = df.format(date) + ".zip"; //$NON-NLS-1$
                final File zipFile = new File(destFolder, zipFileName);

                ZipOutputStream out = null;
                try {
                    out = new ZipOutputStream(new FileOutputStream(zipFile));
                } catch (final FileNotFoundException e) {
                    logger.error(e.getMessage());
                }
                final byte[] buf = new byte[1024];
                for (final String fileName : fileToCopy) {
                    final File file = new File(source + File.separator + fileName);
                    try {
                        addToZip(out, buf, file);
                    } catch (final FileNotFoundException e) {
                        logger.error("Fehler beim Zippen: Konnte file nicht finden: " + e.getMessage()); //$NON-NLS-1$
                    } catch (final IOException e) {
                        logger.error("IOException beim Zippen: " + e.getMessage()); //$NON-NLS-1$
                    }
                    monitor.worked(1);
                }
                try {
                    out.close();
                } catch (final IOException e) {
                    logger.error(e.getMessage());
                }
                return Status.OK_STATUS;
            }

            private void addToZip(final ZipOutputStream out, final byte[] buf, final File file) throws FileNotFoundException, IOException {
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
        };
        job.schedule();
    }

    @Override
    public void dispose() {
    }

    @Override
    public void selectionChanged(final IWorkbenchPart part, final ISelection selection) {
    }
}
