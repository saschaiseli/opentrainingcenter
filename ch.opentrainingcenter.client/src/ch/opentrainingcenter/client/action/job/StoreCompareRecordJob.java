package ch.opentrainingcenter.client.action.job;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.joda.time.DateTime;

import ch.opentrainingcenter.client.views.ApplicationContext;
import ch.opentrainingcenter.core.db.DatabaseAccessFactory;
import ch.opentrainingcenter.core.db.IDatabaseAccess;
import ch.opentrainingcenter.model.geo.Track;
import ch.opentrainingcenter.model.geo.TrackPoint;
import ch.opentrainingcenter.transfer.CommonTransferFactory;
import ch.opentrainingcenter.transfer.IAthlete;
import ch.opentrainingcenter.transfer.IStrecke;
import ch.opentrainingcenter.transfer.IStreckenPunkt;

public class StoreCompareRecordJob extends Job {

    private static final Logger LOGGER = Logger.getLogger(StoreCompareRecordJob.class);
    private final IDatabaseAccess access = DatabaseAccessFactory.getDatabaseAccess();
    private final Track track;

    public StoreCompareRecordJob(final String name, final Track track) {
        super(name);
        this.track = track;
    }

    @Override
    protected IStatus run(final IProgressMonitor monitor) {
        monitor.beginTask(getName(), 1);
        final List<IStreckenPunkt> streckenPunkte = new ArrayList<IStreckenPunkt>();
        final List<TrackPoint> points = track.getPoints();
        for (final TrackPoint point : points) {
            streckenPunkte.add(CommonTransferFactory.createStreckenPunkt(point.getDistance(), point.getyCoordinates(), point.getxCoordinates()));
        }
        final IAthlete athlete = ApplicationContext.getApplicationContext().getAthlete();
        final IStrecke strecke = CommonTransferFactory.createStrecke("init", streckenPunkte, athlete); //$NON-NLS-1$
        final long start = DateTime.now().getMillis();
        access.saveStrecke(strecke);
        final long end = DateTime.now().getMillis();
        LOGGER.info("Execution Time: " + (end - start) + "[ms]"); //$NON-NLS-1$//$NON-NLS-2$
        return Status.OK_STATUS;
    }

}
