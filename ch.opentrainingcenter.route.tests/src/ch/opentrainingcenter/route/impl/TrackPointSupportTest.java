package ch.opentrainingcenter.route.impl;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import ch.opentrainingcenter.model.geo.Track;
import ch.opentrainingcenter.model.geo.TrackPoint;
import static org.junit.Assert.assertEquals;

@SuppressWarnings("nls")
public class TrackPointSupportTest {

    @Test(expected = IllegalArgumentException.class)
    public void testException() {
        TrackPointSupport.getClosestPoint(0.5, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testExceptionOhnePunkte() {
        TrackPointSupport.getClosestPoint(0.5, new Track(new ArrayList<TrackPoint>()));
    }

    @Test
    public void testBeiNegativemWert() {
        final List<TrackPoint> points = new ArrayList<TrackPoint>();
        points.add(new TrackPoint(1.1, 1, 1));
        points.add(new TrackPoint(2.0, 2, 2));
        points.add(new TrackPoint(2.5, 3, 3));
        final Track track = new Track(points);

        final TrackPoint closestPoint = TrackPointSupport.getClosestPoint(-0.5, track);

        assertEquals("erster punkt passt", 1, closestPoint.getxCoordinates(), 0.00001);
    }

    @Test
    public void testVorEinemPunkt() {
        final List<TrackPoint> points = new ArrayList<TrackPoint>();
        points.add(new TrackPoint(1.1, 1, 1));
        points.add(new TrackPoint(2.0, 2, 2));
        points.add(new TrackPoint(2.5, 3, 3));
        final Track track = new Track(points);
        final TrackPoint closestPoint = TrackPointSupport.getClosestPoint(0.5, track);

        assertEquals("erster punkt passt", 1, closestPoint.getxCoordinates(), 0.00001);
    }

    @Test
    public void testVorEinemPunktVorher() {
        final List<TrackPoint> points = new ArrayList<TrackPoint>();
        points.add(new TrackPoint(1.1, 1, 1));
        points.add(new TrackPoint(2.0, 2, 2));
        points.add(new TrackPoint(2.5, 3, 3));
        final Track track = new Track(points);
        final TrackPoint closestPoint = TrackPointSupport.getClosestPoint(1.5, track);

        assertEquals("erster punkt passt", 1, closestPoint.getxCoordinates(), 0.00001);
    }

    @Test
    public void testNachEinemPunkt() {
        final List<TrackPoint> points = new ArrayList<TrackPoint>();
        points.add(new TrackPoint(1.1, 1, 1));
        points.add(new TrackPoint(1.6, 2, 2));
        points.add(new TrackPoint(1.9, 3, 3));
        final Track track = new Track(points);
        final TrackPoint closestPoint = TrackPointSupport.getClosestPoint(1.5, track);

        assertEquals("zweiter punkt passt", 2, closestPoint.getxCoordinates(), 0.00001);
    }

    @Test
    public void testLetzterPunkt() {
        final List<TrackPoint> points = new ArrayList<TrackPoint>();
        points.add(new TrackPoint(1.1, 1, 1));
        points.add(new TrackPoint(1.6, 2, 2));
        points.add(new TrackPoint(1.9, 3, 3));
        final Track track = new Track(points);
        final TrackPoint closestPoint = TrackPointSupport.getClosestPoint(2.5, track);

        assertEquals("letzter punkt passt", 3, closestPoint.getxCoordinates(), 0.00001);
    }

    @Test
    public void testNurEinPunkt() {
        final List<TrackPoint> points = new ArrayList<TrackPoint>();
        points.add(new TrackPoint(1.1, 1, 1));
        final Track track = new Track(points);
        final TrackPoint closestPoint = TrackPointSupport.getClosestPoint(42.5, track);
        assertEquals("erster punkt passt", 1, closestPoint.getxCoordinates(), 0.00001);
    }

    @Test
    public void testPointCompare() {
        final TrackPoint a = new TrackPoint(1, 46.9450, 7.43019129);
        final TrackPoint b = new TrackPoint(1, 46.9450, 7.43019129);
        final boolean result = TrackPointSupport.comparePoints(a, b);

        assertEquals("zwei gleiche punkte", true, result);
    }

    @Test
    public void testPointCompareGrenze() {
        final TrackPoint a = new TrackPoint(1, 46.9450, 7.43019129);
        final TrackPoint b = new TrackPoint(1, 46.9454, 7.43019129);
        final boolean result = TrackPointSupport.comparePoints(a, b);

        assertEquals("zwei nahe punkte", true, result);
    }

    @Test
    public void testPointCompareXUeberschrittenGrenze() {
        final TrackPoint a = new TrackPoint(1, 46.9450, 7.430);
        final TrackPoint b = new TrackPoint(1, 46.9456, 7.430);
        final boolean result = TrackPointSupport.comparePoints(a, b);

        assertEquals("Punkt zu weit weg", false, result);
    }

    @Test
    public void testPointCompareGrenzeY() {
        final TrackPoint a = new TrackPoint(1, 46.9450, 7.4300);
        final TrackPoint b = new TrackPoint(1, 46.9450, 7.4304);
        final boolean result = TrackPointSupport.comparePoints(a, b);

        assertEquals("zwei nahe punkte", true, result);
    }

    @Test
    public void testPointCompareGrenzeUeberGrenzeY() {
        final TrackPoint a = new TrackPoint(1, 46.9450, 7.4300);
        final TrackPoint b = new TrackPoint(1, 46.9450, 7.4306);
        final boolean result = TrackPointSupport.comparePoints(a, b);

        assertEquals("Punkt zu weit weg", false, result);
    }
}
