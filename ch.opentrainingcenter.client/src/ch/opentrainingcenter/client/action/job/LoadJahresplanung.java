package ch.opentrainingcenter.client.action.job;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PlatformUI;

import ch.opentrainingcenter.client.views.ApplicationContext;
import ch.opentrainingcenter.client.views.planung.JahresplanungViewPart;
import ch.opentrainingcenter.i18n.Messages;

public class LoadJahresplanung extends Job {

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
                try {
                    final IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
                    page.showView(JahresplanungViewPart.ID);

                } catch (final Exception e) {
                    e.printStackTrace();
                }
            }
        });
        return Status.OK_STATUS;
    }
}
