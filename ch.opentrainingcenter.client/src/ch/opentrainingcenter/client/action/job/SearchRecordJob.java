package ch.opentrainingcenter.client.action.job;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;

import org.apache.log4j.Logger;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.core.runtime.jobs.JobChangeAdapter;
import org.eclipse.osgi.util.NLS;
import org.joda.time.DateTime;

import ch.opentrainingcenter.core.geo.MapConverter;
import ch.opentrainingcenter.core.helper.TimeHelper;
import ch.opentrainingcenter.i18n.Messages;
import ch.opentrainingcenter.route.ICompareRoute;
import ch.opentrainingcenter.transfer.ITraining;
import ch.opentrainingcenter.transfer.Track;

public class SearchRecordJob extends Job {

    private static final Logger LOGGER = Logger.getLogger(SearchRecordJob.class);
    private final ITraining referenzTraining;
    private final List<ITraining> all;
    private final ICompareRoute comp;
    private final ExecutorService service;
    private final List<ITraining> selbeRoute = new ArrayList<>();

    public SearchRecordJob(final String name, final ITraining referenzTraining, final List<ITraining> all, final ICompareRoute comp) {
        super(name);
        this.referenzTraining = referenzTraining;
        this.all = all;
        this.comp = comp;
        service = Executors.newScheduledThreadPool(4);
    }

    @Override
    protected IStatus run(final IProgressMonitor monitor) {
        LOGGER.info(String.format("Es werde nun %s Trainings mit der Referenz verglichen", all.size())); //$NON-NLS-1$
        final long start = DateTime.now().getMillis();
        selbeRoute.clear();
        final Track referenzTrack = MapConverter.convert(referenzTraining);
        monitor.beginTask("Vergleiche die Routen", all.size()); //$NON-NLS-1$

        final List<FutureTask<ITraining>> taskList = new ArrayList<FutureTask<ITraining>>();
        for (final ITraining item : all) {
            if (item.getId() != referenzTraining.getId()) {
                final FutureTask<ITraining> task = createCallable(referenzTrack, item, monitor);
                taskList.add(task);
                service.execute(task);
            }
        }

        for (final FutureTask<ITraining> tsk : taskList) {
            if (service.isShutdown()) {
                LOGGER.info("Vergleiche abgebrochen"); //$NON-NLS-1$
                return Status.CANCEL_STATUS;
            }
            try {
                final ITraining same = tsk.get();
                if (same != null) {
                    selbeRoute.add(same);
                }
            } catch (InterruptedException | ExecutionException e) {
                return Status.CANCEL_STATUS;
            }
        }
        this.addJobChangeListener(new JobChangeAdapter() {

        });
        service.shutdown();
        final long end = DateTime.now().getMillis();

        LOGGER.info("######################## Routensouche ##################################################"); //$NON-NLS-1$
        LOGGER.info(String.format("Die Suche bei %s Trainings dauerte %s ms", all.size(), (end - start))); //$NON-NLS-1$
        LOGGER.info(String.format("Anzahl gleiche Routen: %s", selbeRoute.size())); //$NON-NLS-1$
        LOGGER.info("########################################################################################"); //$NON-NLS-1$
        monitor.done();
        return Status.OK_STATUS;
    }

    private FutureTask<ITraining> createCallable(final Track referenzTrack, final ITraining item, final IProgressMonitor monitor) {
        return new FutureTask<ITraining>(create(referenzTrack, item, monitor));
    }

    private Callable<ITraining> create(final Track referenzTrack, final ITraining item, final IProgressMonitor monitor) {
        return new Callable<ITraining>() {

            @Override
            public ITraining call() throws Exception {
                final String date = TimeHelper.convertDateToString(item.getDatum());
                LOGGER.info("Start " + date); //$NON-NLS-1$
                monitor.subTask(NLS.bind(Messages.SearchRecordJob_SubTaskName, date));
                final boolean same = comp.compareRoute(referenzTrack, MapConverter.convert(item));
                LOGGER.info(String.format("Record vom %s ist %s", date, same ? "GLEICH" : "NICHT GLEICH")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
                monitor.worked(1);
                return same ? item : null;
            }
        };
    }

    @Override
    protected void canceling() {
        service.shutdownNow();
    }

    public List<ITraining> getSameRoute() {
        return Collections.unmodifiableList(selbeRoute);
    }
}
