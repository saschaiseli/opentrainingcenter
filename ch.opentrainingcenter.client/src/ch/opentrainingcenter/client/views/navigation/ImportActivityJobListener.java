package ch.opentrainingcenter.client.views.navigation;

import org.eclipse.core.runtime.jobs.IJobChangeEvent;
import org.eclipse.core.runtime.jobs.IJobChangeListener;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;

import ch.opentrainingcenter.client.cache.Cache;
import ch.opentrainingcenter.client.cache.impl.TrainingCenterDataCache;
import ch.opentrainingcenter.client.views.overview.SingleActivityViewPart;
import ch.opentrainingcenter.tcx.ActivityT;
import ch.opentrainingcenter.transfer.IImported;

public class ImportActivityJobListener implements IJobChangeListener {

    @Override
    public void aboutToRun(final IJobChangeEvent event) {
        // TODO Auto-generated method stub

    }

    @Override
    public void awake(final IJobChangeEvent event) {
        // TODO Auto-generated method stub

    }

    @Override
    public void done(final IJobChangeEvent event) {
        Display.getDefault().asyncExec(new Runnable() {

            @Override
            public void run() {
                try {
                    final Cache cache = TrainingCenterDataCache.getInstance();
                    final IImported imported = cache.getSelected();
                    final ActivityT activityT = cache.get(imported.getActivityId());
                    final String hash = getSecondaryId(activityT);
                    try {
                        PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().showView(SingleActivityViewPart.ID, hash, IWorkbenchPage.VIEW_ACTIVATE);
                    } catch (final PartInitException e) {
                        e.printStackTrace();
                    }
                } catch (final Exception e) {
                    e.printStackTrace();
                }
            }

            private String getSecondaryId(final ActivityT record) {
                return String.valueOf(record.getId().hashCode());
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
