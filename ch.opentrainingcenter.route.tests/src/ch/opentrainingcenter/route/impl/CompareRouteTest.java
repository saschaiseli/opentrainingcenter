package ch.opentrainingcenter.route.impl;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import ch.opentrainingcenter.route.kml.KmlDumper;
import ch.opentrainingcenter.transfer.Track;
import ch.opentrainingcenter.transfer.TrackPoint;

@SuppressWarnings("nls")
public class CompareRouteTest {
    private CompareRoute route;
    private Track reference;
    private KmlDumper kmlDumper;
    private final String kmlDumpPath = ".";

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
        reference = new Track(1, pointsReference);
        kmlDumper = new KmlDumper(kmlDumpPath);
    }

    @Test
    public void testRouteLengthOk() {

        final List<TrackPoint> pointsOther = new ArrayList<TrackPoint>();
        pointsOther.add(new TrackPoint(2, 46.94525943, 7.43019129));
        pointsOther.add(new TrackPoint(11, 46.94523915, 7.43013446));
        pointsOther.add(new TrackPoint(45.3550568, 46.94507109, 7.42970087));
        final Track other = new Track(1, pointsOther);

        route = new CompareRoute(true, kmlDumpPath);

        final boolean result = route.isDistanceDifferenceTooBig(reference, other);

        assertEquals("Die Route ist nicht zu lang", false, result); //$NON-NLS-1$
    }

    @Test
    public void testRouteLengthNOk_1() {

        final List<TrackPoint> pointsOther = new ArrayList<TrackPoint>();
        pointsOther.add(new TrackPoint(2, 46.94525943, 7.43019129));
        pointsOther.add(new TrackPoint(11, 46.94523915, 7.43013446));
        pointsOther.add(new TrackPoint(42.17, 46.94507109, 7.42970087));
        final Track other = new Track(1, pointsOther);

        route = new CompareRoute(true, kmlDumpPath);

        final boolean result = route.isDistanceDifferenceTooBig(reference, other);

        assertEquals("Die zweite Route ist zu lang", true, result); //$NON-NLS-1$
    }

    @Test
    public void testRouteLengthNOk_2() {

        final List<TrackPoint> pointsOther = new ArrayList<TrackPoint>();
        pointsOther.add(new TrackPoint(2, 46.94525943, 7.43019129));
        pointsOther.add(new TrackPoint(11, 46.94523915, 7.43013446));
        pointsOther.add(new TrackPoint(149.1, 46.94507109, 7.42970087));
        final Track other = new Track(1, pointsOther);

        route = new CompareRoute(true, kmlDumpPath);

        final boolean result = route.isDistanceDifferenceTooBig(reference, other);

        assertEquals("Die zweite Route ist zu kurz", true, result); //$NON-NLS-1$
    }

    @Test
    public void testMitSelbenPunkten() {

        route = new CompareRoute(true, kmlDumpPath);

        final List<TrackPoint> pointsReference = new ArrayList<TrackPoint>();
        pointsReference.add(new TrackPoint(1.37483346, 46.94525943, 7.43019129));
        pointsReference.add(new TrackPoint(6.40480947, 46.94523915, 7.43013446));

        final List<TrackPoint> pointsOther = new ArrayList<TrackPoint>();
        pointsOther.add(new TrackPoint(1.37483346, 46.94525943, 7.43019129));
        pointsOther.add(new TrackPoint(6.40480947, 46.94523915, 7.43013446));

        final boolean result = route.compareTrackPoints(new Track(1, pointsReference), new Track(1, pointsOther), kmlDumper);

        assertEquals("Es sind exakt dieselben Strecken", true, result);
    }

    /**
     * Zwischen r2 und r3 sind 0 meter differenz
     */
    @Test
    public void testMitMehrPunkten() {
        route = new CompareRoute(true, kmlDumpPath);
        final List<TrackPoint> pointsReference = new ArrayList<TrackPoint>();
        final TrackPoint r1 = new TrackPoint(1.37483346, 46.94525943, 7.43019129);
        final TrackPoint r2 = new TrackPoint(6.40480947, 46.94523915, 7.43013446);
        final TrackPoint r3 = new TrackPoint(6.40480947, 46.94523915, 7.43013446);

        pointsReference.add(r1);
        pointsReference.add(r2);
        pointsReference.add(r3);

        final List<TrackPoint> pointsOther = new ArrayList<TrackPoint>();
        final TrackPoint p1 = new TrackPoint(1.37483346, 46.94525943, 7.43019129);
        final TrackPoint p2 = new TrackPoint(6.40480947, 46.94523915, 7.43013446);

        pointsOther.add(p1);
        pointsOther.add(p2);

        final boolean result = route.compareTrackPoints(new Track(1, pointsReference), new Track(1, pointsOther), kmlDumper);

        assertEquals("Es sind fast dieselben Strecken", true, result);
    }

    /**
     * Zwischen r2 und r3 sind 0 meter differenz
     */
    @Test
    public void testMitMehrPunkten_2() {
        route = new CompareRoute(true, kmlDumpPath);

        final List<TrackPoint> pointsReference = new ArrayList<TrackPoint>();
        final TrackPoint r1 = new TrackPoint(1.37483346, 46.94525943, 7.43019129);
        final TrackPoint r2 = new TrackPoint(6.40480947, 46.94523915, 7.43013446);
        final TrackPoint r3 = new TrackPoint(20.20, 46.94519866, 7.42995945);
        final TrackPoint r4 = new TrackPoint(24.5831394, 46.94518299, 7.42990639);

        pointsReference.add(r1);
        pointsReference.add(r2);
        pointsReference.add(r3);
        pointsReference.add(r4);

        final List<TrackPoint> pointsOther = new ArrayList<TrackPoint>();
        final TrackPoint p1 = new TrackPoint(1.37483346, 46.94525943, 7.43019129);
        final TrackPoint p2 = new TrackPoint(6.40480947, 46.94523915, 7.43013446);
        final TrackPoint p3 = new TrackPoint(20.2094784, 46.94519866, 7.42995945);

        pointsOther.add(p1);
        pointsOther.add(p2);
        pointsOther.add(p3);

        final boolean result = route.compareTrackPoints(new Track(1, pointsReference), new Track(1, pointsOther), kmlDumper);

        assertEquals("Es sind fast dieselben Strecken", true, result);
    }

    @Test
    public void testMitMehrPunkten_3() {
        route = new CompareRoute(true, kmlDumpPath);

        final List<TrackPoint> pointsReference = new ArrayList<TrackPoint>();
        final TrackPoint r1 = new TrackPoint(1.37483346, 46.94525943, 7.43019129);
        final TrackPoint r2 = new TrackPoint(6.40480947, 46.94523915, 7.43013446);
        final TrackPoint r3 = new TrackPoint(20.2094784, 46.94519866, 7.42995945);
        final TrackPoint r4 = new TrackPoint(24.5831394, 46.94518299, 7.42990639);

        pointsReference.add(r1);
        pointsReference.add(r2);
        pointsReference.add(r3);
        pointsReference.add(r4);

        final List<TrackPoint> pointsOther = new ArrayList<TrackPoint>();
        final TrackPoint p1 = new TrackPoint(1.17483346, 46.94525943, 7.43019129);
        final TrackPoint p2 = new TrackPoint(6.40480947, 46.94523915, 7.43013446);

        pointsOther.add(p1);
        pointsOther.add(p2);

        final boolean resultA = route.compareTrackPoints(new Track(1, pointsReference), new Track(1, pointsOther), kmlDumper);
        // final boolean resultB = route.compareTrackPoints(new
        // Track(pointsOther), new Track(pointsReference));

        assertEquals("Diesen Weg passt die Strecke", true, resultA);
        // assertEquals("Diesen Weg passt die Strecke NICHT", true, resultB);
    }

    @Test
    public void testMitMehrPunkten_3_andererWeg() {
        route = new CompareRoute(true, kmlDumpPath);

        final List<TrackPoint> referencePoints = new ArrayList<TrackPoint>();
        final TrackPoint r1 = new TrackPoint(1.37483347, 46.94525943, 7.43019129);
        final TrackPoint r2 = new TrackPoint(6.40480947, 46.94523915, 7.43013446);

        referencePoints.add(r1);
        referencePoints.add(r2);

        final List<TrackPoint> points = new ArrayList<TrackPoint>();
        final TrackPoint p1 = new TrackPoint(1.37483346, 46.94525943, 7.43019129);
        final TrackPoint p2 = new TrackPoint(6.40480947, 46.94523915, 7.43013446);
        // final TrackPoint p3 = new TrackPoint(20.2094784, 46.94519866,
        // 7.42995945);
        final TrackPoint p4 = new TrackPoint(24.5831394, 47.99548299, 7.42990639);

        points.add(p1);
        points.add(p2);
        // points.add(p3);
        points.add(p4);

        final boolean result = route.compareTrackPoints(new Track(1, referencePoints), new Track(1, points), kmlDumper);

        assertEquals("Diesen Weg passt die Strecke", true, result);
    }
}
