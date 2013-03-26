package ch.opentrainingcenter.route.impl;

import java.util.List;

import org.apache.log4j.Logger;

import ch.opentrainingcenter.core.assertions.Assertions;
import ch.opentrainingcenter.model.geo.Track;
import ch.opentrainingcenter.model.geo.TrackPoint;

public final class TrackPointSupport {

    private static final Logger LOG = Logger.getLogger(TrackPointSupport.class);
    private static final double COORDINATEN_TOLERANCE = 0.0005; // ca. 50m

    private TrackPointSupport() {

    }

    /**
     * sucht in den Tracks den nächsten Punkt.
     * 
     * <pre>
     * |-----------------4.4---------------|
     * -----3.1-----------------4.8--------|
     * ------------------------MATCH-------|
     */
    public static TrackPoint getClosestPoint(final double position, final Track track) {
        TrackPoint result;
        Assertions.notNull(track);
        final List<TrackPoint> points = track.getPoints();
        Assertions.equals(points.isEmpty(), "Es müssen punkte im track vorhanden sein"); //$NON-NLS-1$
        // final double position = handleNegativePosition(currentPosition);
        int index = 0;
        int i = 0;
        for (final TrackPoint point : points) {
            final double currentDistance = point.getDistance();
            if (position <= currentDistance) {
                index = i;
                break;
            }
            i++;
        }
        if (index > 0) {
            final TrackPoint min = points.get(index - 1);
            final TrackPoint max = points.get(index);
            final double difA = Math.abs(position - min.getDistance());
            final double difB = Math.abs(position - max.getDistance());
            if (difA < difB) {
                result = min;
            } else {
                result = max;
            }

        } else {
            if (points.get(0).getDistance() > position) {
                result = points.get(0);
            } else {
                result = points.get(points.size() - 1);
            }
        }
        return result;
    }

    /**
     * vergleich x und y coordinaten miteinander
     * 
     * @return true wenn der punkt nahe genug ist, ansonsten false.
     */
    public static boolean comparePoints(final TrackPoint point, final TrackPoint closestPoint) {
        final double xAbweichung = Math.abs(point.getxCoordinates() - closestPoint.getxCoordinates());
        final double yAbweichung = Math.abs(point.getyCoordinates() - closestPoint.getyCoordinates());
        boolean result = false;
        if (xAbweichung >= COORDINATEN_TOLERANCE || yAbweichung > COORDINATEN_TOLERANCE) {
            LOG.info("Coordinaten Distanz am Punkt ist zu gross!!! xAbweichung: " + xAbweichung + " yAbweichung: " + yAbweichung); //$NON-NLS-1$ //$NON-NLS-2$
            LOG.info("Point: " + point + " closestPoint: " + closestPoint); //$NON-NLS-1$ //$NON-NLS-2$
        } else {
            LOG.debug("Coordinaten Distanz am Punkt  passt!!! xAbweichung: " + xAbweichung + " yAbweichung: " + yAbweichung); //$NON-NLS-1$ //$NON-NLS-2$
            LOG.debug("Point: " + point + " closestPoint: " + closestPoint); //$NON-NLS-1$ //$NON-NLS-2$
            result = true;
        }
        return result;
    }
}
