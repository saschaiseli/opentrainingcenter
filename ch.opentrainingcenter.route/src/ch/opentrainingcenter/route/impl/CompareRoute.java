package ch.opentrainingcenter.route.impl;

import java.util.List;

import org.apache.log4j.Logger;

import ch.opentrainingcenter.core.assertions.Assertions;
import ch.opentrainingcenter.model.geo.Track;
import ch.opentrainingcenter.model.geo.TrackPoint;
import ch.opentrainingcenter.route.ICompareRoute;
import ch.opentrainingcenter.route.kml.KmlDumper;

import com.grum.geocalc.EarthCalc;
import com.grum.geocalc.Point;

/**
 * Vergleicht eine Strecke mit einer Referenzstrecke. Der Algorithmus zum
 * vergleich ist wie folgt:
 * 
 * <pre>
 * 1. Länge der totalen Strecke vergleichen
 * 2. Von jedem element anhand der distanz den korrespondieren punkt auf vergleich suchen.
 * 
 * ----------X---------------------X-----------------X  Referenz
 * 			 | Aufgrund der Distanz zum Ursprung wird Punkt Y auf der Vergleichsstrecke gesucht
 * -----X----Y----------X----------------------			Vergleich
 * 
 * --> Abstand der beiden punkte darf nicht zu gross sein.
 * 
 * 3. Schritt 2 aber umgekehrt, damit auch gesagt werden kann, dass a==b && b==a
 * </pre>
 * 
 * @author sascha
 * 
 */
public class CompareRoute implements ICompareRoute {

    private static final String TRACK_MIN_EIN_PUNKT = "Jeder Track muss mindestens ein Punkt haben";

    private static final String LINE = "------------------------------";

    private static final Logger LOGGER = Logger.getLogger(CompareRoute.class);

    private static final int DISTANZ_TOLERANZ_PROZENT = 5;

    private static final int DISTANZ_DELTA_BEI_GLEICHER_DISTANZ = 250;

    private final boolean debug;

    private final KmlDumper kmlDumper;

    public CompareRoute(final boolean debug, final String kmlDumpPath) {
        this.debug = debug;
        kmlDumper = new KmlDumper(kmlDumpPath);
    }

    @Override
    public boolean compareRoute(final Track reference, final Track track) {
        if (debug) {
            dump(reference, "Referenz", "ff0000ff"); //$NON-NLS-1$ //$NON-NLS-2$
            dump(track, "Other", "ff0cc0ff"); //$NON-NLS-1$ //$NON-NLS-2$
            kmlDumper.dump();
        }
        checkParameters(reference, track);
        if (isDistanceDifferenceTooBig(reference, track)) {
            return false;
        }
        final boolean resultA = compareTrackPoints(reference, track);
        final boolean resultB = compareTrackPoints(track, reference);
        LOGGER.info("Erster Vergleich: " + resultA); //$NON-NLS-1$
        LOGGER.info("Umgekehrter Vergleich: " + resultB); //$NON-NLS-1$
        return resultA && resultB;
    }

    private void dump(final Track track, final String title, final String lineColor) {
        LOGGER.info("-----------------GPS DATEN " + title + "-------------------------"); //$NON-NLS-1$ //$NON-NLS-2$
        LOGGER.info(track.toKml());
        LOGGER.info("##################################################################"); //$NON-NLS-1$
        kmlDumper.addLine(title, lineColor, track.toKml());
    }

    protected boolean compareTrackPoints(final Track reference, final Track track) {
        final List<TrackPoint> points = reference.getPoints();
        final List<TrackPoint> trackPoints = track.getPoints();
        TrackPoint previousReference = null;
        for (final TrackPoint referencePoint : points) {
            final TrackPoint firstPoint = TrackPointSupport.getFirstPoint(referencePoint, trackPoints);
            final TrackPoint lastPoint = TrackPointSupport.getLastPoint(referencePoint, trackPoints);
            final Point p1 = TrackPointSupport.createPoint(firstPoint);
            final Point p2 = TrackPointSupport.createPoint(lastPoint);
            final double distance;
            if (previousReference == null) {
                distance = referencePoint.getDistance();
            } else {
                distance = referencePoint.getDistance() - previousReference.getDistance();
            }
            previousReference = referencePoint;
            final Point pointOnLine = TrackPointSupport.getPointOnLine(p1, p2, distance);
            final double delta = EarthCalc.getDistance(TrackPointSupport.createPoint(referencePoint), pointOnLine);
            if (delta > DISTANZ_DELTA_BEI_GLEICHER_DISTANZ) {
                logReason(referencePoint, firstPoint, lastPoint, pointOnLine, delta);
                return false;
            }
        }
        return true;
    }

    private void logReason(final TrackPoint referencePoint, final TrackPoint firstPoint, final TrackPoint lastPoint, final Point pointOnLine, final double delta) {
        LOGGER.info(LINE);
        LOGGER.info(String.format("Distanz zum punkt: %s", delta)); //$NON-NLS-1$
        LOGGER.info(String.format("Referenzpunkt: %s", referencePoint)); //$NON-NLS-1$
        LOGGER.info(String.format("First: %s", firstPoint)); //$NON-NLS-1$
        LOGGER.info(String.format("Last: %s", lastPoint)); //$NON-NLS-1$
        LOGGER.info(String.format("Berechneter Punkt %s,%s", pointOnLine.getLongitude(), pointOnLine.getLatitude())); //$NON-NLS-1$ 
        LOGGER.info(LINE);
    }

    protected boolean isDistanceDifferenceTooBig(final Track reference, final Track track) {
        final double referenceDistance = calculateDistanz(reference);
        LOGGER.info(String.format("Referenzdistanz: %s", referenceDistance)); //$NON-NLS-1$
        final double otherDistance = calculateDistanz(track);
        LOGGER.info(String.format("Vergleich distanz: ", otherDistance)); //$NON-NLS-1$
        return Math.abs(referenceDistance - otherDistance) > referenceDistance * ((DISTANZ_TOLERANZ_PROZENT) / 100.0);
    }

    private void checkParameters(final Track reference, final Track track) {
        Assertions.notNull(reference, "Referenz Track darf nicht null sein!"); //$NON-NLS-1$
        Assertions.isValid(reference.getPoints().isEmpty(), TRACK_MIN_EIN_PUNKT);
        Assertions.notNull(track, "Track zum Vergleichen darf nicht null sein!"); //$NON-NLS-1$
        Assertions.isValid(track.getPoints().isEmpty(), TRACK_MIN_EIN_PUNKT);
    }

    private double calculateDistanz(final Track track) {
        final List<TrackPoint> points = track.getPoints();
        return points.get(points.size() - 1).getDistance();
    }
}
