package ch.opentrainingcenter.model.geo;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Entspricht einer (Jogging) Strecke.
 * 
 * @author sascha
 * 
 */
public class Track {

    private final List<TrackPoint> points;

    public Track(final List<TrackPoint> points) {
        this.points = points;
    }

    /**
     * Eine nach der Distanz sortierten <b>NICHT MODIFIZIERBARE</b> Liste
     */
    public List<TrackPoint> getPoints() {
        Collections.sort(points, new Comparator<TrackPoint>() {

            @Override
            public int compare(final TrackPoint pointA, final TrackPoint pointB) {
                return Double.compare(pointA.getDistance(), pointB.getDistance());
            }
        });
        return Collections.unmodifiableList(points);
    }

    public String toKml() {
        final StringBuffer kml = new StringBuffer("<LineString><coordinates>\n"); //$NON-NLS-1$
        for (final TrackPoint point : points) {
            kml.append(point.toKml());
        }
        kml.append("\n</LineString></coordinates>"); //$NON-NLS-1$
        return kml.toString();
    }
}
