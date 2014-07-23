package ch.opentrainingcenter.importer.gpx;

import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.TimeZone;

import org.apache.log4j.Logger;

import ch.opentrainingcenter.transfer.IStreckenPunkt;
import ch.opentrainingcenter.transfer.ITrackPointProperty;
import ch.opentrainingcenter.transfer.ITraining;
import ch.opentrainingcenter.transfer.factory.CommonTransferFactory;

import com.grum.geocalc.DegreeCoordinate;
import com.grum.geocalc.EarthCalc;
import com.grum.geocalc.Point;
import com.topografix.gpx.GpxType;
import com.topografix.gpx.TrkType;
import com.topografix.gpx.TrksegType;
import com.topografix.gpx.WptType;

/**
 * Converter um GPX Daten in ITraining zu konvertieren.
 * 
 */
public class GpxConverter {
    private static final Logger LOGGER = Logger.getLogger(GpxConverter.class);

    private double distance = 0;
    private long timeInSeconds = 0;
    private long start = 0;
    private final HeartRateHandler heartRateHandler = new HeartRateHandler();

    public GpxConverter() {

    }

    public ITraining convert(final GpxType gpxType) {
        final List<TrkType> tracks = gpxType.getTrk();
        final List<ITrackPointProperty> trackPoints = new ArrayList<>();
        for (final TrkType trkType : tracks) {
            final List<TrksegType> trkseg = trkType.getTrkseg();
            int lap = 1;
            for (final TrksegType trksegType : trkseg) {
                if (lap == 1) {
                    start = StartZeitHandler.getStartZeit(trksegType);
                }
                LOGGER.info(String.format("Runde %s wird konvertiert", lap)); //$NON-NLS-1$
                trackPoints.addAll(addTrackPointsFromLaps(trksegType, lap));
                lap++;
            }
        }
        final int avg = heartRateHandler.getAverage();
        final int max = heartRateHandler.getMax();
        final ITraining training = CommonTransferFactory.createTraining(start, timeInSeconds, distance, avg, max, 0d);
        training.setTrackPoints(trackPoints);
        return training;
    }

    private List<ITrackPointProperty> addTrackPointsFromLaps(final TrksegType trkseg, final int lap) {
        final List<ITrackPointProperty> trackPoints = new ArrayList<>();
        WptType previous = null;
        for (final WptType wptType : trkseg.getTrkpt()) {
            trackPoints.add(convertWaypoint2TrackPoint(wptType, previous, lap));
            previous = wptType;
        }
        return trackPoints;
    }

    ITrackPointProperty convertWaypoint2TrackPoint(final WptType currentWayPoint, final WptType previousWayPoint, final int lap) {
        // Herzfrequenz
        final int heartBeat = heartRateHandler.getHeartRate(currentWayPoint);
        if (heartBeat > 0) {
            heartRateHandler.getHearts().add(heartBeat);
        }

        final GregorianCalendar greg = currentWayPoint.getTime().toGregorianCalendar();
        greg.setTimeZone(TimeZone.getDefault());
        final long zeit = greg.getTime().getTime();

        final Point currentPoint = createPoint(currentWayPoint.getLat().doubleValue(), currentWayPoint.getLon().doubleValue());
        final Point previousPoint;
        long timePrevious;
        if (previousWayPoint != null) {
            previousPoint = createPoint(previousWayPoint.getLat().doubleValue(), previousWayPoint.getLon().doubleValue());
            final GregorianCalendar gregPrevious = previousWayPoint.getTime().toGregorianCalendar();
            gregPrevious.setTimeZone(TimeZone.getDefault());
            timePrevious = gregPrevious.getTime().getTime() / 1000;
            final long delta = zeit / 1000 - timePrevious;
            timeInSeconds = timeInSeconds + delta;
        } else {
            previousPoint = currentPoint;
            timePrevious = 0;
        }
        final double distanceFromLastPoint = EarthCalc.getDistance(previousPoint, currentPoint);
        distance += distanceFromLastPoint;

        final IStreckenPunkt sp = CommonTransferFactory.createStreckenPunkt(distance, currentPoint.getLongitude(), currentPoint.getLatitude());
        return CommonTransferFactory.createTrackPointProperty(distance, heartBeat, currentWayPoint.getEle().intValue(), zeit, lap, sp);
    }

    private Point createPoint(final double latitude, final double longitude) {
        return new Point(new DegreeCoordinate(latitude), new DegreeCoordinate(longitude));
    }
}
