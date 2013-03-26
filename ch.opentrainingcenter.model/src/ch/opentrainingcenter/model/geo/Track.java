package ch.opentrainingcenter.model.geo;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class Track {
    private final List<TrackPoint> points;

    public Track(final List<TrackPoint> points) {
        this.points = points;
    }

    /**
     * Eine nach der distanz sortierten liste
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
}
