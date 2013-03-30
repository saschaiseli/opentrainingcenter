package ch.opentrainingcenter.route.impl;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import com.grum.geocalc.Coordinate;
import com.grum.geocalc.DegreeCoordinate;
import com.grum.geocalc.EarthCalc;
import com.grum.geocalc.Point;

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
    @Test
    public void testGetPointOnLineMitEinemPunkt(){
    	Coordinate lat = new DegreeCoordinate(46.9450);
    	Coordinate lng = new DegreeCoordinate(7.430);
    	Point p1 = new Point(lat,lng);
		Point p2= new Point(lat,lng);
		Point pointOnLine = TrackPointSupport.getPointOnLine(p1, p2, 0);
		
		assertEquals("Muss gleich Punkt 1 sein.",p1.getLatitude(),pointOnLine.getLatitude(),0.00001);
		assertEquals("Muss gleich Punkt 1 sein.",p1.getLongitude(),pointOnLine.getLongitude(),0.00001);
    }
    @Test
    public void testGetPointOnLineMitZweiPunktDistanz0(){
    	Point p1 = new Point(new DegreeCoordinate(46.9450),new DegreeCoordinate(7.430));
		Point p2= new Point(new DegreeCoordinate(46.9450),new DegreeCoordinate(7.4306));
		Point pointOnLine = TrackPointSupport.getPointOnLine(p1, p2, 0);
		
		assertEquals("Muss gleich Punkt 1 sein.",p1.getLatitude(),pointOnLine.getLatitude(),0.00001);
		assertEquals("Muss gleich Punkt 1 sein.",p1.getLongitude(),pointOnLine.getLongitude(),0.00001);
    }
    
    @Test
    public void testGetPointOnLineMitZweiPunktDistanzLaengeZwischenDenPunkten(){
    	Point p1 = new Point(new DegreeCoordinate(46.9450),new DegreeCoordinate(7.430));
		Point p2= new Point(new DegreeCoordinate(46.9450),new DegreeCoordinate(7.4306));
		double distance = EarthCalc.getDistance(p1, p2);
		Point pointOnLine = TrackPointSupport.getPointOnLine(p1, p2, distance);
		
		assertEquals("Muss gleich Punkt 2 sein.",p2.getLatitude(),pointOnLine.getLatitude(),0.00001);
		assertEquals("Muss gleich Punkt 2 sein.",p2.getLongitude(),pointOnLine.getLongitude(),0.00001);
    }
    
    @Test
    public void testGetPointOnLineMitZweiPunktDistanzZwischenDenPunkten(){
    	Point p1 = new Point(new DegreeCoordinate(46.9450),new DegreeCoordinate(7.430));
		Point p2= new Point(new DegreeCoordinate(46.9450),new DegreeCoordinate(7.4306));
		double distance = EarthCalc.getDistance(p1, p2)/2.0;
		Point pointOnLine = TrackPointSupport.getPointOnLine(p1, p2, distance);
		
		assertEquals("Latidute muss gleich wie p1 und p2 sein.",p2.getLatitude(),pointOnLine.getLatitude(),0.00001);
		assertEquals("halbe distanz, daher die haelfte von y2-y1.",7.4303,pointOnLine.getLongitude(),0.00001);
    }
}
