package ch.opentrainingcenter.route.impl;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.Ignore;
import org.junit.Test;

import ch.opentrainingcenter.model.geo.Track;
import ch.opentrainingcenter.model.geo.TrackPoint;

import com.grum.geocalc.Coordinate;
import com.grum.geocalc.DegreeCoordinate;
import com.grum.geocalc.EarthCalc;
import com.grum.geocalc.Point;

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
    public void testGetPointOnLineMitEinemPunkt() {
        final Coordinate lat = new DegreeCoordinate(46.9450);
        final Coordinate lng = new DegreeCoordinate(7.430);
        final Point p1 = new Point(lat, lng);
        final Point p2 = new Point(lat, lng);
        final Point pointOnLine = TrackPointSupport.getPointOnLine(p1, p2, 0);

        assertEquals("Muss gleich Punkt 1 sein.", p1.getLatitude(), pointOnLine.getLatitude(), 0.00001);
        assertEquals("Muss gleich Punkt 1 sein.", p1.getLongitude(), pointOnLine.getLongitude(), 0.00001);
    }

    @Test
    public void testGetPointOnLineMitEinemPunktUndDistanz() {
        final Coordinate lat = new DegreeCoordinate(46.9450);
        final Coordinate lng = new DegreeCoordinate(7.430);
        final Point p1 = new Point(lat, lng);
        final Point p2 = new Point(lat, lng);
        final Point pointOnLine = TrackPointSupport.getPointOnLine(p1, p2, 10);

        assertEquals("Muss gleich Punkt 1 sein.", p1.getLatitude(), pointOnLine.getLatitude(), 0.00001);
        assertEquals("Muss gleich Punkt 1 sein.", p1.getLongitude(), pointOnLine.getLongitude(), 0.00001);
    }

    @Test
    @Ignore
    public void testGetPointOnLineFuerKarte() {
        final Coordinate lat1 = new DegreeCoordinate(46.94525943);
        final Coordinate lng1 = new DegreeCoordinate(7.43019129);
        final Point p1 = new Point(lat1, lng1);

        final Coordinate lat2 = new DegreeCoordinate(46.94507109);
        final Coordinate lng2 = new DegreeCoordinate(7.42970087);
        final Point p2 = new Point(lat2, lng2);
        final Point pointOnLine = TrackPointSupport.getPointOnLine(p1, p2, 10);

        assertEquals("Muss gleich Punkt 1 sein.", p1.getLatitude(), pointOnLine.getLatitude(), 0.00001);
        assertEquals("Muss gleich Punkt 1 sein.", p1.getLongitude(), pointOnLine.getLongitude(), 0.00001);
    }

    @Test
    public void testGetPointOnLineMitZweiPunktDistanz0() {
        final Point p1 = new Point(new DegreeCoordinate(46.9450), new DegreeCoordinate(7.430));
        final Point p2 = new Point(new DegreeCoordinate(46.9450), new DegreeCoordinate(7.4306));
        final Point pointOnLine = TrackPointSupport.getPointOnLine(p1, p2, 0);

        assertEquals("Muss gleich Punkt 1 sein.", p1.getLatitude(), pointOnLine.getLatitude(), 0.00001);
        assertEquals("Muss gleich Punkt 1 sein.", p1.getLongitude(), pointOnLine.getLongitude(), 0.00001);
    }

    @Test
    public void testGetPointOnLineMitZweiPunktDistanzLaengeZwischenDenPunkten() {
        final Point p1 = new Point(new DegreeCoordinate(46.9450), new DegreeCoordinate(7.430));
        final Point p2 = new Point(new DegreeCoordinate(46.9450), new DegreeCoordinate(7.4306));
        final double distance = EarthCalc.getDistance(p1, p2);
        final Point pointOnLine = TrackPointSupport.getPointOnLine(p1, p2, distance);

        assertEquals("Muss gleich Punkt 2 sein.", p2.getLatitude(), pointOnLine.getLatitude(), 0.00001);
        assertEquals("Muss gleich Punkt 2 sein.", p2.getLongitude(), pointOnLine.getLongitude(), 0.00001);
    }

    @Test
    public void testGetPointOnLineMitZweiPunktDistanzZwischenDenPunkten() {
        final Point p1 = new Point(new DegreeCoordinate(46.9450), new DegreeCoordinate(7.430));
        final Point p2 = new Point(new DegreeCoordinate(46.9450), new DegreeCoordinate(7.4306));
        final double distance = EarthCalc.getDistance(p1, p2) / 2.0;
        final Point pointOnLine = TrackPointSupport.getPointOnLine(p1, p2, distance);

        assertEquals("Latidute muss gleich wie p1 und p2 sein.", p2.getLatitude(), pointOnLine.getLatitude(), 0.00001);
        assertEquals("halbe distanz, daher die haelfte von y2-y1.", 7.4303, pointOnLine.getLongitude(), 0.00001);
    }

    @Test
    public void testGetPointOnLineMitZweiPunktDistanzZwischenDenPunktenLatidute() {
        final Point p1 = new Point(new DegreeCoordinate(46.9450), new DegreeCoordinate(7.430));
        final Point p2 = new Point(new DegreeCoordinate(46.9451), new DegreeCoordinate(7.430));
        final double distance = EarthCalc.getDistance(p1, p2) / 2.0;
        final Point pointOnLine = TrackPointSupport.getPointOnLine(p1, p2, distance);

        assertEquals("Latidute muss zischen x2-x1/2 sein.", 46.94505, pointOnLine.getLatitude(), 0.00001);
        assertEquals("gleich wie y2 und y1.", p2.getLongitude(), pointOnLine.getLongitude(), 0.00001);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetFirstPointException() {
        TrackPointSupport.getFirstPoint(null, new ArrayList<TrackPoint>());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetFirstPointEmptyTracks() {
        final TrackPoint reference = new TrackPoint(1.1, 1, 1);
        TrackPointSupport.getFirstPoint(reference, new ArrayList<TrackPoint>());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNurEinElement() {
        final TrackPoint reference = new TrackPoint(1.1, 1, 1);
        final ArrayList<TrackPoint> points = new ArrayList<TrackPoint>();
        points.add(new TrackPoint(0.5, 1, 1));
        TrackPointSupport.getFirstPoint(reference, points);
    }

    /**
     * <pre>
     *               2
     * --------------X------- reference
     * ---X-----X------------ track
     *    0.5   1.5
     * </pre>
     */
    @Test
    public void testZweiElementBeideKleiner() {
        final TrackPoint reference = new TrackPoint(2, 1, 1);
        final ArrayList<TrackPoint> points = new ArrayList<TrackPoint>();
        points.add(new TrackPoint(0.5, 1, 1));
        final TrackPoint p2 = new TrackPoint(1.5, 2, 1);
        points.add(p2);
        final TrackPoint firstPoint = TrackPointSupport.getFirstPoint(reference, points);
        assertPoint(p2, firstPoint);
    }

    /**
     * <pre>
     *       2
     * ------X--------------------- reference
     * ---------X-----X------------ track
     *         2.5   3.5
     *          p1    p2
     * </pre>
     */
    @Test
    public void testZweiElement() {
        final TrackPoint reference = new TrackPoint(2, 1, 1);
        final ArrayList<TrackPoint> points = new ArrayList<TrackPoint>();

        final TrackPoint p1 = new TrackPoint(2.5, 1, 1);
        final TrackPoint p2 = new TrackPoint(3.5, 2, 1);

        points.add(p1);
        points.add(p2);

        final TrackPoint firstPoint = TrackPointSupport.getFirstPoint(reference, points);
        assertPoint(p1, firstPoint);
    }

    /**
     * <pre>
     *       2
     * ------X--------------------- reference
     * ---X-----X-----X------------ track
     *    1.5     2.5   3.5
     *    p1      p2    p3
     * </pre>
     */
    @Test
    public void testDreiElement() {
        final TrackPoint reference = new TrackPoint(2, 0, 0);
        final ArrayList<TrackPoint> points = new ArrayList<TrackPoint>();

        final TrackPoint p1 = new TrackPoint(1.5, 0, 0);
        final TrackPoint p2 = new TrackPoint(2.5, 0, 0);
        final TrackPoint p3 = new TrackPoint(3.5, 0, 0);

        points.add(p1);
        points.add(p2);
        points.add(p3);

        final TrackPoint firstPoint = TrackPointSupport.getFirstPoint(reference, points);
        assertPoint(p1, firstPoint);
    }

    /**
     * <pre>
     *          2.5
     * ---------X--------------------- reference
     * ---X-----X-----X------------ track
     *    1.5   2.5   3.5
     *    p1    p2    p3
     * </pre>
     */
    @Test
    public void testDreiElement_2() {
        final TrackPoint reference = new TrackPoint(2.5, 0, 0);
        final ArrayList<TrackPoint> points = new ArrayList<TrackPoint>();

        final TrackPoint p1 = new TrackPoint(1.5, 0, 0);
        final TrackPoint p2 = new TrackPoint(2.5, 0, 0);
        final TrackPoint p3 = new TrackPoint(3.5, 0, 0);

        points.add(p1);
        points.add(p2);
        points.add(p3);

        final TrackPoint firstPoint = TrackPointSupport.getFirstPoint(reference, points);
        assertPoint(p1, firstPoint);
    }

    /**
     * <pre>
     *          2.6
     * ---------X--------------------- reference
     * ---X-----X-----X------------ track
     *    1.5     2.5   3.5
     *    p1      p2    p3
     * </pre>
     */
    @Test
    public void testDreiElement_3() {
        final TrackPoint reference = new TrackPoint(2.6, 0, 0);
        final ArrayList<TrackPoint> points = new ArrayList<TrackPoint>();

        final TrackPoint p1 = new TrackPoint(1.5, 0, 0);
        final TrackPoint p2 = new TrackPoint(2.5, 0, 0);
        final TrackPoint p3 = new TrackPoint(3.5, 0, 0);

        points.add(p1);
        points.add(p2);
        points.add(p3);

        final TrackPoint firstPoint = TrackPointSupport.getFirstPoint(reference, points);
        assertPoint(p2, firstPoint);
    }

    /**
     * <pre>
     *                    4.6
     * -------------------X-------- reference
     * ---X-----X-----X------------ track
     *    1.5     2.5   3.5
     *    p1      p2    p3
     * </pre>
     */
    @Test
    public void testDreiElement_4() {
        final TrackPoint reference = new TrackPoint(4.6, 0, 0);
        final ArrayList<TrackPoint> points = new ArrayList<TrackPoint>();

        final TrackPoint p1 = new TrackPoint(1.5, 0, 0);
        final TrackPoint p2 = new TrackPoint(2.5, 0, 0);
        final TrackPoint p3 = new TrackPoint(3.5, 0, 0);

        points.add(p1);
        points.add(p2);
        points.add(p3);

        final TrackPoint firstPoint = TrackPointSupport.getFirstPoint(reference, points);
        assertPoint(p3, firstPoint);
    }

    // -------------- LAST

    @Test(expected = IllegalArgumentException.class)
    public void testGetLastPointException() {
        TrackPointSupport.getLastPoint(null, new ArrayList<TrackPoint>());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetLastPointEmptyTracks() {
        final TrackPoint reference = new TrackPoint(1.1, 1, 1);
        TrackPointSupport.getLastPoint(reference, new ArrayList<TrackPoint>());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testLastNurEinElement() {
        final TrackPoint reference = new TrackPoint(1.1, 1, 1);
        final ArrayList<TrackPoint> points = new ArrayList<TrackPoint>();
        points.add(new TrackPoint(0.5, 1, 1));
        TrackPointSupport.getLastPoint(reference, points);
    }

    /**
     * <pre>
     *               2
     * --------------X------- reference
     * ---X-----X------------ track
     *    0.5   1.5
     * </pre>
     */
    @Test
    public void testLastZweiElementBeideKleiner() {
        final TrackPoint reference = new TrackPoint(2, 1, 1);
        final ArrayList<TrackPoint> points = new ArrayList<TrackPoint>();
        final TrackPoint p1 = new TrackPoint(0.5, 1, 1);
        final TrackPoint p2 = new TrackPoint(1.5, 2, 1);

        points.add(p1);
        points.add(p2);

        final TrackPoint firstPoint = TrackPointSupport.getLastPoint(reference, points);
        assertPoint(p2, firstPoint);
    }

    /**
     * <pre>
     *       2
     * ------X--------------------- reference
     * ---------X-----X------------ track
     *         2.5   3.5
     *          p1    p2
     * </pre>
     */
    @Test
    public void testLastZweiElement() {
        final TrackPoint reference = new TrackPoint(2, 1, 1);
        final ArrayList<TrackPoint> points = new ArrayList<TrackPoint>();

        final TrackPoint p1 = new TrackPoint(2.5, 1, 1);
        final TrackPoint p2 = new TrackPoint(3.5, 2, 1);

        points.add(p1);
        points.add(p2);

        final TrackPoint firstPoint = TrackPointSupport.getLastPoint(reference, points);
        assertPoint(p1, firstPoint);
    }

    /**
     * <pre>
     *       2
     * ------X--------------------- reference
     * ---X-----X-----X------------ track
     *    1.5     2.5   3.5
     *    p1      p2    p3
     * </pre>
     */
    @Test
    public void testLastDreiElement() {
        final TrackPoint reference = new TrackPoint(2, 0, 0);
        final ArrayList<TrackPoint> points = new ArrayList<TrackPoint>();

        final TrackPoint p1 = new TrackPoint(1.5, 0, 0);
        final TrackPoint p2 = new TrackPoint(2.5, 0, 0);
        final TrackPoint p3 = new TrackPoint(3.5, 0, 0);

        points.add(p1);
        points.add(p2);
        points.add(p3);

        final TrackPoint firstPoint = TrackPointSupport.getLastPoint(reference, points);
        assertPoint(p2, firstPoint);
    }

    /**
     * <pre>
     *          2.5
     * ---------X--------------------- reference
     * ---X-----X-----X------------ track
     *    1.5   2.5   3.5
     *    p1    p2    p3
     * </pre>
     */
    @Test
    public void testLastDreiElement_2() {
        final TrackPoint reference = new TrackPoint(2.5, 0, 0);
        final ArrayList<TrackPoint> points = new ArrayList<TrackPoint>();

        final TrackPoint p1 = new TrackPoint(1.5, 0, 0);
        final TrackPoint p2 = new TrackPoint(2.5, 0, 0);
        final TrackPoint p3 = new TrackPoint(3.5, 0, 0);

        points.add(p1);
        points.add(p2);
        points.add(p3);

        final TrackPoint firstPoint = TrackPointSupport.getLastPoint(reference, points);
        assertPoint(p3, firstPoint);
    }

    /**
     * <pre>
     *          2.6
     * ---------X--------------------- reference
     * ---X-----X-----X------------ track
     *    1.5     2.5   3.5
     *    p1      p2    p3
     * </pre>
     */
    @Test
    public void testLastDreiElement_3() {
        final TrackPoint reference = new TrackPoint(2.6, 0, 0);
        final ArrayList<TrackPoint> points = new ArrayList<TrackPoint>();

        final TrackPoint p1 = new TrackPoint(1.5, 0, 0);
        final TrackPoint p2 = new TrackPoint(2.5, 0, 0);
        final TrackPoint p3 = new TrackPoint(3.5, 0, 0);

        points.add(p1);
        points.add(p2);
        points.add(p3);

        final TrackPoint firstPoint = TrackPointSupport.getLastPoint(reference, points);
        assertPoint(p3, firstPoint);
    }

    /**
     * <pre>
     *                    4.6
     * -------------------X-------- reference
     * ---X-----X-----X------------ track
     *    1.5     2.5   3.5
     *    p1      p2    p3
     * </pre>
     */
    @Test
    public void testLastDreiElement_4() {
        final TrackPoint reference = new TrackPoint(4.6, 0, 0);
        final ArrayList<TrackPoint> points = new ArrayList<TrackPoint>();

        final TrackPoint p1 = new TrackPoint(1.5, 0, 0);
        final TrackPoint p2 = new TrackPoint(2.5, 0, 0);
        final TrackPoint p3 = new TrackPoint(3.5, 0, 0);

        points.add(p1);
        points.add(p2);
        points.add(p3);

        final TrackPoint firstPoint = TrackPointSupport.getLastPoint(reference, points);
        assertPoint(p3, firstPoint);
    }

    private void assertPoint(final TrackPoint expected, final TrackPoint result) {
        assertEquals(expected.getDistance(), result.getDistance(), 0.00001);
        assertEquals(expected.getxCoordinates(), result.getxCoordinates(), 0.00001);
        assertEquals(expected.getyCoordinates(), result.getyCoordinates(), 0.00001);
    }
}
