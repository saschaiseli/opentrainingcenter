package ch.opentrainingcenter.client.commands;

import java.util.List;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.ui.handlers.HandlerUtil;

import ch.opentrainingcenter.client.action.job.StoreCompareRecordJob;
import ch.opentrainingcenter.client.views.overview.MapConverter;
import ch.opentrainingcenter.core.cache.Cache;
import ch.opentrainingcenter.core.cache.TrainingCenterDataCache;
import ch.opentrainingcenter.model.geo.Track;
import ch.opentrainingcenter.tcx.ActivityT;
import ch.opentrainingcenter.transfer.IImported;

public class StoreCompareRecords extends AbstractHandler {

    public static final String ID = "ch.opentrainingcenter.client.commands.StoreCompareRecords"; //$NON-NLS-1$

    @Override
    public Object execute(final ExecutionEvent event) throws ExecutionException {
        final Cache cache = TrainingCenterDataCache.getInstance();

        final ISelection selection = HandlerUtil.getCurrentSelection(event);

        final List<?> records = ((StructuredSelection) selection).toList();
        final IImported record = (IImported) records.get(0);
        final ActivityT activity = cache.get(record.getActivityId());
        final Track track = MapConverter.convert(activity);
        final Job job = new StoreCompareRecordJob("Speichere Referenzstrecke", track);
        job.schedule();
        return null;
    }
}
