package ch.opentrainingcenter.transfer;

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
    private final int idTrainging;

    public Track(final int idTrainging, final List<TrackPoint> points) {
        this.idTrainging = idTrainging;
        this.points = points;
    }

    public int getIdTrainging() {
        return idTrainging;
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
        final StringBuilder kml = new StringBuilder("\n"); //$NON-NLS-1$
        for (final TrackPoint point : points) {
            kml.append(point.toKml());
        }
        kml.append("\n"); //$NON-NLS-1$
        return kml.toString();
    }

}
