package ch.opentrainingcenter.route.impl;

import java.util.List;

import org.apache.log4j.Logger;

import ch.opentrainingcenter.core.assertions.Assertions;
import ch.opentrainingcenter.model.geo.Track;
import ch.opentrainingcenter.model.geo.TrackPoint;
import ch.opentrainingcenter.route.ICompareRoute;

import com.grum.geocalc.EarthCalc;
import com.grum.geocalc.Point;

/**
 * Vergleicht eine Strecke mit einer Referenzstrecke. Der Algorithmus zum
 * vergleich ist wie folgt:
 * 
 * <pre>
 * 1. Länge der totalen Strecke vergleichen
 * 2. Von jedem element anhand der distanz den korrespondieren punkt auf vergleich suchen.
 * 
 * ----------X---------------------X-----------------X  Referenz
 * 			 | Aufgrund der Distanz zum Ursprung wird Punkt Y auf der Vergleichsstrecke gesucht
 * -----X----Y----------X----------------------			Vergleich
 * 
 * --> Abstand der beiden punkte darf nicht zu gross sein.
 * 
 * 3. Schritt 2 aber umgekehrt, damit auch gesagt werden kann, dass a==b && b==a
 * </pre>
 * 
 * @author sascha
 * 
 */
public class CompareRoute implements ICompareRoute {

    private static final Logger LOGGER = Logger.getLogger(CompareRoute.class);

    private static final int DISTANZ_TOLERANZ_PROZENT = 5;

    private static final int DISTANZ_DELTA_BEI_GLEICHER_DISTANZ = 20;

    @Override
    public boolean compareRoute(final Track reference, final Track track) {
        checkParameters(reference, track);
        if (isDistanceDifferenceTooBig(reference, track)) {
            return false;
        }
        final boolean result = compareTrackPoints(reference, track) && compareTrackPoints(track, reference);
        return result;
    }

    protected boolean compareTrackPoints(final Track reference, final Track track) {
        final List<TrackPoint> points = reference.getPoints();
        final List<TrackPoint> trackPoints = track.getPoints();
        for (final TrackPoint referencePoint : points) {
            final TrackPoint firstPoint = TrackPointSupport.getFirstPoint(referencePoint, trackPoints);
            final TrackPoint lastPoint = TrackPointSupport.getLastPoint(referencePoint, trackPoints);
            if (isStartOrEnd(firstPoint, lastPoint)) {
                continue;
            }
            final Point p1 = TrackPointSupport.createPoint(firstPoint);
            final Point p2 = TrackPointSupport.createPoint(lastPoint);

            final Point pointOnLine = TrackPointSupport.getPointOnLine(p1, p2, referencePoint.getDistance());
            final double delta = EarthCalc.getDistance(TrackPointSupport.createPoint(referencePoint), pointOnLine);
            if (delta > DISTANZ_DELTA_BEI_GLEICHER_DISTANZ) {
                logReason(referencePoint, firstPoint, lastPoint, pointOnLine, delta);
                return false;
            } else {
                logReason(referencePoint, firstPoint, lastPoint, pointOnLine, delta);
            }
        }
        return true;
    }

    private boolean isStartOrEnd(final TrackPoint firstPoint, final TrackPoint lastPoint) {
        return firstPoint == null || lastPoint == null;
    }

    private void logReason(final TrackPoint referencePoint, final TrackPoint firstPoint, final TrackPoint lastPoint, final Point pointOnLine, final double delta) {
        LOGGER.info("------------------------------"); //$NON-NLS-1$
        LOGGER.info("Distanz zum punkt: " + delta); //$NON-NLS-1$
        LOGGER.info("Referenzpunkt: " + referencePoint); //$NON-NLS-1$
        LOGGER.info("First: " + firstPoint); //$NON-NLS-1$
        LOGGER.info("Last: " + lastPoint); //$NON-NLS-1$
        LOGGER.info("Berechneter Punkt " + pointOnLine.getLongitude() + "," + pointOnLine.getLatitude()); //$NON-NLS-1$ //$NON-NLS-2$
        LOGGER.info("------------------------------"); //$NON-NLS-1$
    }

    protected boolean isDistanceDifferenceTooBig(final Track reference, final Track track) {
        final double referenceDistance = calculateDistanz(reference);
        LOGGER.info("Referenzdistanz: " + referenceDistance); //$NON-NLS-1$
        final double otherDistance = calculateDistanz(track);
        LOGGER.info("Vergleich distanz: " + otherDistance); //$NON-NLS-1$
        return Math.abs(referenceDistance - otherDistance) > referenceDistance * ((DISTANZ_TOLERANZ_PROZENT) / 100.0);
    }

    private void checkParameters(final Track reference, final Track track) {
        Assertions.notNull(reference, "Referenz Track darf nicht null sein!"); //$NON-NLS-1$
        Assertions.equals(reference.getPoints().isEmpty(), "Jeder Track muss mindestens ein Punkt haben"); //$NON-NLS-1$
        Assertions.notNull(track, "Track zum Vergleichen darf nicht null sein!"); //$NON-NLS-1$
        Assertions.equals(track.getPoints().isEmpty(), "Jeder Track muss mindestens ein Punkt haben"); //$NON-NLS-1$
    }

    private double calculateDistanz(final Track track) {
        final List<TrackPoint> points = track.getPoints();
        return points.get(points.size() - 1).getDistance();
    }
}
