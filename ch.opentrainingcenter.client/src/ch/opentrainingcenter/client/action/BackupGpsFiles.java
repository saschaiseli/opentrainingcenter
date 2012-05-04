package ch.opentrainingcenter.client.action;

import org.apache.log4j.Logger;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.ui.ISelectionListener;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.actions.ActionFactory.IWorkbenchAction;
import org.eclipse.ui.plugin.AbstractUIPlugin;

import ch.opentrainingcenter.client.Application;
import ch.opentrainingcenter.client.views.IImageKeys;

public class BackupGpsFiles extends Action implements ISelectionListener, IWorkbenchAction {

    private static final Logger LOG = Logger.getLogger(BackupGpsFiles.class.getName());

    public static final String ID = "ch.opentrainingcenter.client.backupGpsFiles"; //$NON-NLS-1$

    private final Job job;

    public BackupGpsFiles(final String tooltip, final Job job) {
        this.job = job;
        setId(ID);
        setToolTipText(tooltip);
        setImageDescriptor(AbstractUIPlugin.imageDescriptorFromPlugin(Application.ID, IImageKeys.BACKUP));

    }

    @Override
    public void run() {
        LOG.info("Run the Backup Export Job");
        job.schedule();
    }

    @Override
    public void dispose() {
    }

    @Override
    public void selectionChanged(final IWorkbenchPart part, final ISelection selection) {
    }
}
