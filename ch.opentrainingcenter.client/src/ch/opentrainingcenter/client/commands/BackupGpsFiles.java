package ch.opentrainingcenter.client.commands;

import java.io.File;

import org.apache.log4j.Logger;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.jface.preference.IPreferenceStore;

import ch.opentrainingcenter.client.Activator;
import ch.opentrainingcenter.client.action.job.BackupJob;
import ch.opentrainingcenter.core.PreferenceConstants;
import ch.opentrainingcenter.core.db.DatabaseAccessFactory;
import ch.opentrainingcenter.core.db.IDatabaseAccess;
import ch.opentrainingcenter.core.importer.ExtensionHelper;
import ch.opentrainingcenter.i18n.Messages;

/**
 * Macht ein Backup aller importierten Daten. Sammelt sie und packt sie in ein
 * ZIP File, danach wird das ZIP File an einen zu definierenden Ort kopiert.
 */
public class BackupGpsFiles extends OtcAbstractHandler {

    public static final String ID = "ch.opentrainingcenter.client.commands.BackupGpsFiles"; //$NON-NLS-1$
    private static final Logger LOG = Logger.getLogger(BackupGpsFiles.class);

    private final IPreferenceStore store;

    public BackupGpsFiles() {
        this(Activator.getDefault().getPreferenceStore());
    }

    public BackupGpsFiles(final IPreferenceStore store) {
        super(store);
        this.store = store;
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
        final IDatabaseAccess db = DatabaseAccessFactory.getDatabaseAccess();
        final Job job = new BackupJob(Messages.BackupGpsFiles0, source, destFolder, ExtensionHelper.getConverters(), db);
        job.schedule();
        return null;
    }

}
