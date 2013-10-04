package ch.opentrainingcenter.client.views.navigation;

import org.eclipse.core.runtime.jobs.IJobChangeEvent;
import org.eclipse.core.runtime.jobs.IJobChangeListener;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;

import ch.opentrainingcenter.client.views.overview.SingleActivityViewPart;
import ch.opentrainingcenter.core.cache.Cache;
import ch.opentrainingcenter.core.cache.TrainingCache;
import ch.opentrainingcenter.transfer.ITraining;

@Deprecated
public class ImportActivityJobListener implements IJobChangeListener {

    private final ITraining record;

    public ImportActivityJobListener(final ITraining record) {
        this.record = record;
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
                final Cache cache = TrainingCache.getInstance();
                final ITraining training = cache.get(record.getDatum());
                final String hash = getSecondaryId(training);
                try {
                    PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage()
                            .showView(SingleActivityViewPart.ID, hash, IWorkbenchPage.VIEW_ACTIVATE);
                } catch (final PartInitException e) {
                    e.printStackTrace();
                }
            }

            private String getSecondaryId(final ITraining training) {
                return String.valueOf(training.getDatum());
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
