package ch.opentrainingcenter.route.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import ch.opentrainingcenter.route.ICompareRoute;
import ch.opentrainingcenter.route.kml.KmlDumper;
import ch.opentrainingcenter.transfer.Track;
import ch.opentrainingcenter.transfer.TrackPoint;

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
@SuppressWarnings("nls")
public class CompareRoute implements ICompareRoute {

    private static final String LINE = "------------------------------"; //$NON-NLS-1$

    private static final Logger LOGGER = Logger.getLogger(CompareRoute.class);

    private static final int DISTANZ_TOLERANZ_PROZENT = 5;

    private static final int DISTANZ_DELTA_BEI_GLEICHER_DISTANZ = 250;

    private final boolean debug;

    private final String kmlDumpPath;

    public CompareRoute(final boolean debug, final String kmlDumpPath) {
        this.debug = debug;
        this.kmlDumpPath = kmlDumpPath;

    }

    @Override
    public boolean compareRoute(final Track reference, final Track track) {
        final KmlDumper kmlDumper = new KmlDumper(kmlDumpPath);
        dump(reference, "Referenz", "ff0000ff", kmlDumper);
        dump(track, "Other", "ff0cc0ff", kmlDumper);

        if (reference == null || reference.getPoints().isEmpty()) {
            return false;
        }
        if (track == null || track.getPoints().isEmpty()) {
            return false;
        }
        if (isDistanceDifferenceTooBig(reference, track)) {
            return false;
        }
        final boolean resultA = compareTrackPoints(reference, track, kmlDumper);
        final boolean resultB = compareTrackPoints(track, reference, kmlDumper);
        LOGGER.info("Erster Vergleich: " + resultA); //$NON-NLS-1$
        LOGGER.info("Umgekehrter Vergleich: " + resultB); //$NON-NLS-1$
        final boolean result = resultA && resultB;
        if (debug && result) {
            kmlDumper.dump();
        }
        return result;
    }

    /**
     * visible wegen Tests
     */
    boolean compareTrackPoints(final Track reference, final Track track, final KmlDumper kmlDumper) {
        final List<TrackPoint> points = reference.getPoints();
        final List<TrackPoint> trackPoints = track.getPoints();
        TrackPoint previousReference = null;
        int i = 0;
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
                kmlDumper.addPlacemark("Referenzpunkt_" + i, createExtendedData(referencePoint), referencePoint.toKml()); //$NON-NLS-1$
                kmlDumper.addPlacemark("firstPoint_" + i, createExtendedData(firstPoint), firstPoint.toKml()); //$NON-NLS-1$
                kmlDumper.addPlacemark("lastPoint_" + i, createExtendedData(lastPoint), lastPoint.toKml()); //$NON-NLS-1$
                kmlDumper.addPlacemark("pointOnLine_" + i, createExtendedData(distance), pointOnLine.getLongitude() + "," + pointOnLine.getLatitude()); //$NON-NLS-1$ //$NON-NLS-2$
                i++;
            }
        }
        if (i > 3) {
            LOGGER.info("Die Routen sind nicht gleich"); //$NON-NLS-1$
            return false;
        }
        return true;
    }

    private Map<String, String> createExtendedData(final TrackPoint point) {
        return createExtendedData(point.getDistance());
    }

    private Map<String, String> createExtendedData(final double distanz) {
        final Map<String, String> result = new HashMap<>();
        result.put("Distanz", String.valueOf(distanz)); //$NON-NLS-1$
        return result;
    }

    private void dump(final Track track, final String title, final String lineColor, final KmlDumper kmlDumper) {
        LOGGER.info("-----------------GPS DATEN " + title + "-------------------------"); //$NON-NLS-1$ //$NON-NLS-2$
        LOGGER.info(track.toKml());
        LOGGER.info("##################################################################"); //$NON-NLS-1$
        kmlDumper.addLine(title, lineColor, track.toKml());
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
        LOGGER.info(String.format("Vergleich distanz: %s", otherDistance)); //$NON-NLS-1$
        final double maxDiff = referenceDistance * ((DISTANZ_TOLERANZ_PROZENT) / 100.0);
        LOGGER.info(String.format("Tolerierbare Differenz: %s", maxDiff));
        final boolean isTooBig = Math.abs(referenceDistance - otherDistance) > maxDiff;
        LOGGER.info(String.format("Die Differenz ist %s", isTooBig ? "zu gross" : "OK"));
        return isTooBig;
    }

    private double calculateDistanz(final Track track) {
        final List<TrackPoint> points = track.getPoints();
        return points.get(points.size() - 1).getDistance();
    }
}
