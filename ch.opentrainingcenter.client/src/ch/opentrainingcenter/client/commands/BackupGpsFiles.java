package ch.opentrainingcenter.client.commands;

import java.io.File;

import org.apache.log4j.Logger;
import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.jface.preference.IPreferenceStore;

import ch.opentrainingcenter.client.Activator;
import ch.opentrainingcenter.client.Messages;
import ch.opentrainingcenter.client.PreferenceConstants;
import ch.opentrainingcenter.client.action.job.BackupJob;
import ch.opentrainingcenter.importer.ExtensionHelper;

public class BackupGpsFiles extends AbstractHandler {

    public static final String ID = "ch.opentrainingcenter.client.commands.BackupGpsFiles";
    private static final Logger LOG = Logger.getLogger(BackupGpsFiles.class);
    private final IPreferenceStore store;

    public BackupGpsFiles() {
        store = Activator.getDefault().getPreferenceStore();
    }

    @Override
    public Object execute(final ExecutionEvent event) throws ExecutionException {
        final String source = store.getString(PreferenceConstants.GPS_FILE_LOCATION_PROG);
        final String destination = store.getString(PreferenceConstants.BACKUP_FILE_LOCATION);

        final File destFolder = new File(destination);
        if (!destFolder.exists()) {
            destFolder.mkdir();
            LOG.info("Pfad zu Backupfolder erstellt"); //$NON-NLS-1$
        }
        final Job job = new BackupJob(Messages.BackupGpsFiles0, source, destFolder, ExtensionHelper.getConverters());
        job.schedule();
        return null;
    }

}
