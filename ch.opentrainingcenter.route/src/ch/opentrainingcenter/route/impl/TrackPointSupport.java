package ch.opentrainingcenter.route.impl;

import java.util.List;

import org.apache.log4j.Logger;

import ch.opentrainingcenter.core.assertions.Assertions;
import ch.opentrainingcenter.model.geo.Track;
import ch.opentrainingcenter.model.geo.TrackPoint;

import com.grum.geocalc.DegreeCoordinate;
import com.grum.geocalc.EarthCalc;
import com.grum.geocalc.Point;

public final class TrackPointSupport {

    private static final Logger LOG = Logger.getLogger(TrackPointSupport.class);
    private static final double COORDINATEN_TOLERANCE = 0.0005; // ca. 50m

    private TrackPointSupport() {

    }

    /**
     * sucht in den Tracks den letzten Punkt, der noch gerade eine kleinere
     * Distanz aufweist.
     * 
     * <pre>
     * |-----------------4.4---------------|
     * -----3.1-----------------4.8--------|
     * ----MATCH---------------------------|
     */
    public static TrackPoint getFirstPoint(final TrackPoint reference, final List<TrackPoint> points) {
        checkInput(reference, points);
        TrackPoint previous = null;
        for (final TrackPoint point : points) {
            if (point.getDistance() > reference.getDistance()) {
                if (previous == null) {
                    return point;
                } else {
                    return previous;
                }
            }
            previous = point;
        }
        return previous;
    }

    /**
     * sucht in den Tracks den letzten Punkt, der noch gerade eine kleinere
     * Distanz aufweist.
     * 
     * <pre>
     * |-----------------4.4---------------|
     * -----3.1-----------------4.8--------|
     * -------------------------MATCH------|
     */
    public static TrackPoint getLastPoint(final TrackPoint reference, final List<TrackPoint> points) {
        checkInput(reference, points);
        TrackPoint previous = null;
        for (final TrackPoint point : points) {
            if (point.getDistance() > reference.getDistance()) {
                previous = point;
                break;
            }
        }
        if (previous == null) {
            previous = points.get(points.size() - 1);
        }
        return previous;
    }

    private static void checkInput(final TrackPoint reference, final List<TrackPoint> points) {
        Assertions.notNull(reference, "Referenz Punkt darf nicht null sein"); //$NON-NLS-1$
        Assertions.equals(points.isEmpty() || points.size() < 2, "Es müssen mindestens 2 Punkte im Track vorhanden sein"); //$NON-NLS-1$
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
     * Berechnet den Punkt auf der Linie zwischen p1 und p2 mit der distanz von
     * p1 aus.
     */
    public static Point getPointOnLine(final Point p1, final Point p2, final double distance) {
        if (p1.equals(p2)) {
            return p1;
        }
        final double bearing = EarthCalc.getBearing(p1, p2);
        return EarthCalc.pointRadialDistance(p1, bearing, distance);
    }

    public static Point createPoint(final TrackPoint firstPoint) {
        return new Point(new DegreeCoordinate(firstPoint.getxCoordinates()), new DegreeCoordinate(firstPoint.getyCoordinates()));
    }
}
