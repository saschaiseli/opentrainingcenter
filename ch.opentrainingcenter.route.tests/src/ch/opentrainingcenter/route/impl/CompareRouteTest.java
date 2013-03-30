package ch.opentrainingcenter.route.impl;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import ch.opentrainingcenter.model.geo.Track;
import ch.opentrainingcenter.model.geo.TrackPoint;
import static org.junit.Assert.assertEquals;

public class CompareRouteTest {
    private CompareRoute route;
    private Track reference;

    @Before
    public void setUp() {
        final List<TrackPoint> pointsReference = new ArrayList<TrackPoint>();
        pointsReference.add(new TrackPoint(1.37483346, 46.94525943, 7.43019129));
        pointsReference.add(new TrackPoint(6.40480947, 46.94523915, 7.43013446));
        pointsReference.add(new TrackPoint(20.2094784, 46.94519866, 7.42995945));
        pointsReference.add(new TrackPoint(24.5831394, 46.94518299, 7.42990639));
        pointsReference.add(new TrackPoint(31.6147308, 46.94516187, 7.42981629));
        pointsReference.add(new TrackPoint(35.3704681, 46.94514477, 7.42977438));
        pointsReference.add(new TrackPoint(39.3866348, 46.9451213, 7.42973557));
        pointsReference.add(new TrackPoint(42.3400879, 46.94509548, 7.4297209));
        pointsReference.add(new TrackPoint(45.3550568, 46.94507109, 7.42970087));
        reference = new Track(pointsReference);
    }

    @Test
    public void testRouteLengthOk() {

        final List<TrackPoint> pointsOther = new ArrayList<TrackPoint>();
        pointsOther.add(new TrackPoint(2, 46.94525943, 7.43019129));
        pointsOther.add(new TrackPoint(11, 46.94523915, 7.43013446));
        pointsOther.add(new TrackPoint(45.3550568, 46.94507109, 7.42970087));
        final Track other = new Track(pointsOther);

        route = new CompareRoute();

        final boolean result = route.compareRoute(reference, other);

        assertEquals("Die Routen müssen gleich sein", true, result); //$NON-NLS-1$
    }

    @Test
    public void testRouteLengthNOk_1() {

        final List<TrackPoint> pointsOther = new ArrayList<TrackPoint>();
        pointsOther.add(new TrackPoint(2, 46.94525943, 7.43019129));
        pointsOther.add(new TrackPoint(11, 46.94523915, 7.43013446));
        pointsOther.add(new TrackPoint(42.17, 46.94507109, 7.42970087));
        final Track other = new Track(pointsOther);

        route = new CompareRoute();

        final boolean result = route.compareRoute(reference, other);

        assertEquals("Die zweite Route ist zu lang", false, result); //$NON-NLS-1$
    }

    @Test
    public void testRouteLengthNOk_2() {

        final List<TrackPoint> pointsOther = new ArrayList<TrackPoint>();
        pointsOther.add(new TrackPoint(2, 46.94525943, 7.43019129));
        pointsOther.add(new TrackPoint(11, 46.94523915, 7.43013446));
        pointsOther.add(new TrackPoint(149.1, 46.94507109, 7.42970087));
        final Track other = new Track(pointsOther);

        route = new CompareRoute();

        final boolean result = route.compareRoute(reference, other);

        assertEquals("Die zweite Route ist zu kurz", false, result); //$NON-NLS-1$
    }
}
