package ch.opentrainingcenter.client.action.job;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PlatformUI;

import ch.opentrainingcenter.client.cache.Cache;
import ch.opentrainingcenter.client.views.ApplicationContext;
import ch.opentrainingcenter.client.views.planung.JahresplanungViewPart;
import ch.opentrainingcenter.db.IDatabaseAccess;

public class LoadJahresplanung extends Job {

    private final Cache cache;
    private final IDatabaseAccess db;
    private final int jahr;

    public LoadJahresplanung(final String title, final int jahr, final Cache cache, final IDatabaseAccess db) {
        super(title + jahr);
        this.jahr = jahr;
        ApplicationContext.getApplicationContext().setSelectedJahr(jahr);
        this.cache = cache;
        this.db = db;
    }

    @Override
    protected IStatus run(final IProgressMonitor monitor) {
        monitor.beginTask("Lade Jahresplanung", 1);
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
