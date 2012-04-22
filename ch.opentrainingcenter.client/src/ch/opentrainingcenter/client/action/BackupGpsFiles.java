package ch.opentrainingcenter.client.action;

import java.io.File;

import org.apache.log4j.Logger;
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
import ch.opentrainingcenter.client.views.IImageKeys;

public class BackupGpsFiles extends Action implements ISelectionListener, IWorkbenchAction {

    private static final Logger LOG = Logger.getLogger(BackupGpsFiles.class.getName());

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
        final File destFolder = new File(destination);
        if (!destFolder.exists()) {
            destFolder.mkdir();
            LOG.info("Pfad zu Backupfolder erstellt"); //$NON-NLS-1$
        }
        final Job job = new BackupJob(Messages.BackupGpsFiles_0, source, destFolder);
        job.schedule();

    }

    @Override
    public void dispose() {
    }

    @Override
    public void selectionChanged(final IWorkbenchPart part, final ISelection selection) {
    }
}
