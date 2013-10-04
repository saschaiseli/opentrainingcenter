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
import ch.opentrainingcenter.client.views.planung.JahresplanungViewPart;
import ch.opentrainingcenter.i18n.Messages;

/**
 * Lädt und zeigt alle Jahrespläne.
 * 
 */
public class LoadJahresplanung extends Job {

    private static final Logger LOGGER = Logger.getLogger(LoadJahresplanung.class);

    public LoadJahresplanung(final String title, final int jahr) {
        super(title + jahr);
        ApplicationContext.getApplicationContext().setSelectedJahr(jahr);
    }

    @Override
    protected IStatus run(final IProgressMonitor monitor) {
        monitor.beginTask(Messages.LoadJahresplanung_0, 1);
        Display.getDefault().asyncExec(new Runnable() {

            @Override
            public void run() {

                final IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
                try {
                    page.showView(JahresplanungViewPart.ID);
                } catch (final PartInitException e) {
                    LOGGER.error(e);
                }
            }
        });
        return Status.OK_STATUS;
    }
}
