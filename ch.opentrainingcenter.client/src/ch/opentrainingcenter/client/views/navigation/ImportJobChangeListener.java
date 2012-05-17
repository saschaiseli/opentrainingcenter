package ch.opentrainingcenter.client.views.navigation;

import org.eclipse.core.runtime.jobs.IJobChangeEvent;
import org.eclipse.core.runtime.jobs.IJobChangeListener;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.widgets.Display;

import ch.opentrainingcenter.client.views.ApplicationContext;
import ch.opentrainingcenter.db.DatabaseAccessFactory;
import ch.opentrainingcenter.db.IDatabaseAccess;

public class ImportJobChangeListener implements IJobChangeListener {

    private final TreeViewer viewer;
    private final IDatabaseAccess databaseAccess;

    ImportJobChangeListener(final TreeViewer viewer) {
        this.viewer = viewer;
        databaseAccess = DatabaseAccessFactory.getDatabaseAccess();
    }

    @Override
    public void aboutToRun(final IJobChangeEvent event) {
    }

    @Override
    public void awake(final IJobChangeEvent event) {

    }

    @Override
    public void done(final IJobChangeEvent event) {
        Display.getDefault().asyncExec(new Runnable() {

            @Override
            public void run() {
                try {
                    viewer.setInput(databaseAccess.getAllImported(ApplicationContext.getApplicationContext().getAthlete()));
                } catch (final Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public void running(final IJobChangeEvent event) {
    }

    @Override
    public void scheduled(final IJobChangeEvent event) {
    }

    @Override
    public void sleeping(final IJobChangeEvent event) {
    }

}
