package ch.opentrainingcenter.client.action.job;

import org.apache.log4j.Logger;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;

import ch.opentrainingcenter.client.views.ApplicationContext;
import ch.opentrainingcenter.client.views.planung.ShowJahresplanungViewPart;
import ch.opentrainingcenter.i18n.Messages;

/**
 * Lädt und zeigt alle Jahrespläne.
 * 
 */
public class ShowJahresplanung extends Job {

    private static final Logger LOGGER = Logger.getLogger(ShowJahresplanung.class);

    public ShowJahresplanung(final String title, final int jahr) {
        super(title + jahr);
        ApplicationContext.getApplicationContext().setSelectedJahr(jahr);
    }

    @Override
    protected IStatus run(final IProgressMonitor monitor) {
        monitor.beginTask(Messages.LoadJahresplanung_0, 1);
        Display.getDefault().asyncExec(new Runnable() {

            @Override
            public void run() {
                try {
                    final IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
                    page.showView(ShowJahresplanungViewPart.ID);
                } catch (final PartInitException e) {
                    LOGGER.error(e);
                }
            }
        });
        return Status.OK_STATUS;
    }
}