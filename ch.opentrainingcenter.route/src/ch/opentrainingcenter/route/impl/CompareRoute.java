package ch.opentrainingcenter.route.impl;

import java.util.List;

import org.apache.log4j.Logger;

import ch.opentrainingcenter.core.assertions.Assertions;
import ch.opentrainingcenter.model.geo.Track;
import ch.opentrainingcenter.model.geo.TrackPoint;
import ch.opentrainingcenter.route.ICompareRoute;

/**
 * Vergleicht eine Strecke mit einer Referenzstrecke. Der Algorithmus zum
 * vergleich ist wie folgt:
 * 
 * <pre>
 * 1. Länge der totalen Strecke vergleichen 
 * 2. elementweise von reference das nächste passende von track suchen und delta
 *    auf x/y abfragen.
 * 3. Schritt 2 aber mit track ist die referenz
 * </pre>
 * 
 * @author sascha
 * 
 */
public class CompareRoute implements ICompareRoute {

    private static final Logger LOGGER = Logger.getLogger(CompareRoute.class);

    private static final int DISTANZ_TOLERANZ_PROZENT = 5;

    @Override
    public boolean compareRoute(final Track reference, final Track track) {
        checkParameters(reference, track);
        boolean result = false;
        final double referenceDistance = calculateDistanz(reference);
        LOGGER.info("Referenzdistanz: " + referenceDistance); //$NON-NLS-1$
        final double otherDistance = calculateDistanz(track);
        LOGGER.info("Vergleich distanz: " + otherDistance); //$NON-NLS-1$
        if (!isDistanceDifferenceTooBig(referenceDistance, otherDistance)) {
            result = true;
        }
        // result = comparePoints(reference, track);
        return result;
    }

    protected boolean isDistanceDifferenceTooBig(final double referenceDistance, final double otherDistance) {
        return Math.abs(referenceDistance - otherDistance) > referenceDistance * ((DISTANZ_TOLERANZ_PROZENT) / 100.0);
    }

    private boolean comparePoints(final Track reference, final Track track) {
        boolean result = false;
        final List<TrackPoint> points = reference.getPoints();
        for (final TrackPoint point : points) {
            final double currentPosition = point.getDistance();
            final TrackPoint closestPoint = TrackPointSupport.getClosestPoint(currentPosition, track);
            final boolean closeEnough = TrackPointSupport.comparePoints(point, closestPoint);
            if (closeEnough) {
                result = true;
            } else {
                break;
            }
        }
        return result;
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
