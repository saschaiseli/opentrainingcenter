package ch.opentrainingcenter.model.geo;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

@SuppressWarnings("nls")
public class TrackTest {
    @Test
    public void testSortierung() {
        final List<TrackPoint> points = new ArrayList<TrackPoint>();
        points.add(new TrackPoint(100f, 1, 2));
        points.add(new TrackPoint(90f, 1, 2));
        points.add(new TrackPoint(110f, 1, 2));
        final Track track = new Track(points);
        final List<TrackPoint> result = track.getPoints();
        assertEquals(90, result.get(0).getDistance(), 0.00001);
        assertEquals(100, result.get(1).getDistance(), 0.00001);
        assertEquals(110, result.get(2).getDistance(), 0.00001);
    }

    @Test
    public void testToKml_einPunkt() {
        final List<TrackPoint> points = new ArrayList<TrackPoint>();
        points.add(new TrackPoint(100f, 1, 2));
        final Track track = new Track(points);

        final String kml = track.toKml();

        assertEquals("\n2.0,1.0 \n", kml);
    }

    @Test
    public void testToKml_zweiPunkt() {
        final List<TrackPoint> points = new ArrayList<TrackPoint>();
        points.add(new TrackPoint(100f, 1, 2));
        points.add(new TrackPoint(90f, 1, 2));
        final Track track = new Track(points);

        final String kml = track.toKml();

        assertEquals("\n2.0,1.0 2.0,1.0 \n", kml);
    }
}
