package ch.opentrainingcenter.client.action.job;

import java.util.ArrayList;
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
import org.joda.time.DateTime;

import ch.opentrainingcenter.core.geo.MapConverter;
import ch.opentrainingcenter.core.helper.TimeHelper;
import ch.opentrainingcenter.route.ICompareRoute;
import ch.opentrainingcenter.transfer.ITraining;
import ch.opentrainingcenter.transfer.Track;

public class SearchRecordJob extends Job {

    private static final Logger LOGGER = Logger.getLogger(SearchRecordJob.class);
    private final ITraining referenzTraining;
    private final List<ITraining> all;
    private final ICompareRoute comp;

    public SearchRecordJob(final String name, final ITraining referenzTraining, final List<ITraining> all, final ICompareRoute comp) {
        super(name);
        this.referenzTraining = referenzTraining;
        this.all = all;
        this.comp = comp;
    }

    @Override
    protected IStatus run(final IProgressMonitor monitor) {
        LOGGER.info(String.format("Es werde nun %s Trainings mit der Referenz verglichen", all.size())); //$NON-NLS-1$
        final long start = DateTime.now().getMillis();
        final List<ITraining> selbeRoute = new ArrayList<>();
        final Track referenzTrack = MapConverter.convert(referenzTraining);
        // final int i = 0;
        monitor.beginTask("Vergleiche die Routen", all.size()); //$NON-NLS-1$

        final List<FutureTask<ITraining>> taskList = new ArrayList<FutureTask<ITraining>>();
        final ExecutorService service = Executors.newSingleThreadExecutor();// newFixedThreadPool(1000);
        for (final ITraining item : all) {
            if (item.getId() != referenzTraining.getId()) {
                final FutureTask<ITraining> task = createCallable(referenzTrack, item);
                taskList.add(task);
                service.execute(task);
            }
        }

        for (final FutureTask<ITraining> tsk : taskList) {
            try {
                final ITraining same = tsk.get();
                if (same != null) {
                    selbeRoute.add(same);
                }
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        }
        service.shutdown();
        final long end = DateTime.now().getMillis();
        // final long start2 = DateTime.now().getMillis();
        // int i = 0;
        // for (final ITraining item : all) {
        //            LOGGER.info("Vergleich: " + i++); //$NON-NLS-1$
        //            monitor.subTask(String.format("Vergleich von Referenztraining vom %s mit Training vom %s", TimeHelper.convertDateToString(referenzTraining //$NON-NLS-1$
        // .getDatum()), TimeHelper.convertDateToString(item.getDatum())));
        // if (item.getId() != referenzTraining.getId() &&
        // comp.compareRoute(referenzTrack, MapConverter.convert(item))) {
        // selbeRoute.add(item);
        // }
        // monitor.worked(1);
        // }
        LOGGER.info("######################## Routensouche ##################################################");
        LOGGER.info(String.format("Die Suche bei %s Trainings dauerte %s ms", all.size(), (end - start))); //$NON-NLS-1$
        LOGGER.info(String.format("Anzahl gleiche Routen: %s", selbeRoute.size()));
        LOGGER.info("########################################################################################");

        // final long end2 = DateTime.now().getMillis();
        // LOGGER.info("######################## Routensouche ##################################################");
        //        LOGGER.info(String.format("Die Suche bei %s Trainings dauerte %s ms", all.size(), (end2 - start2))); //$NON-NLS-1$
        // LOGGER.info(String.format("Anzahl gleiche Routen: %s",
        // selbeRoute.size()));
        // LOGGER.info("########################################################################################");
        return Status.OK_STATUS;
    }

    private FutureTask<ITraining> createCallable(final Track referenzTrack, final ITraining item) {
        return new FutureTask<ITraining>(create(referenzTrack, item));
    }

    private Callable<ITraining> create(final Track referenzTrack, final ITraining item) {
        return new Callable<ITraining>() {

            @Override
            public ITraining call() throws Exception {
                final String date = TimeHelper.convertDateToString(item.getDatum());
                LOGGER.info("Start " + date);
                final boolean same = comp.compareRoute(referenzTrack, MapConverter.convert(item));
                LOGGER.info("End " + date + " Training is same? " + same);
                return same ? item : null;
            }
        };
    }

    @Override
    protected void canceling() {
        super.getThread().interrupt();
    }

}
