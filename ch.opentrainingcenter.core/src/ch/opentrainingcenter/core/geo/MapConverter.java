package ch.opentrainingcenter.core.geo;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.joda.time.DateTime;

import ch.opentrainingcenter.i18n.Messages;
import ch.opentrainingcenter.transfer.IStreckenPunkt;
import ch.opentrainingcenter.transfer.ITrackPointProperty;
import ch.opentrainingcenter.transfer.ITraining;
import ch.opentrainingcenter.transfer.Track;
import ch.opentrainingcenter.transfer.TrackPoint;

/**
 * Konvertiert die Trainingsdaten in ein Format, dass der MapViewer anzeigen
 * kann.
 */
public final class MapConverter {

    private static final Logger LOGGER = Logger.getLogger(MapConverter.class);

    private MapConverter() {
    }

    public static Track convert(final ITraining training) {
        final List<TrackPoint> trackPoints = new ArrayList<TrackPoint>();
        final List<ITrackPointProperty> points = training.getTrackPoints();
        for (final ITrackPointProperty point : points) {
            final TrackPoint trackPoint = createTrackPoint(point);
            if (trackPoint != null) {
                trackPoints.add(trackPoint);
            }
        }
        return new Track(training.getId(), trackPoints);
    }

    private static TrackPoint createTrackPoint(final ITrackPointProperty point) {
        final double distance = point.getDistance();
        final IStreckenPunkt streckenPunkt = point.getStreckenPunkt();
        if (streckenPunkt != null) {
            return new TrackPoint(distance, streckenPunkt.getLatitude(), streckenPunkt.getLongitude());
        } else {
            LOGGER.warn("Trackpoint ohne Streckenpunkt"); //$NON-NLS-1$
            return null;
        }
    }

    /**
     * Konvertiert Punkte
     * 
     * <pre>
     *          [[25.774252,-80.190262],[18.466465,-66.118292], [46.954, 7.448]]
     *          
     *          dies soll [[25.774252,-80.190262]] zurückgeben.
     * </pre>
     */
    public static String convertTrackpoints(final ITraining training) {
        final long start = DateTime.now().getMillis();
        final StringBuilder str = new StringBuilder();
        str.append('[');
        final List<ITrackPointProperty> points = training.getTrackPoints();
        boolean pointAdded = false;
        for (final ITrackPointProperty point : points) {
            final IStreckenPunkt geoPoint = point.getStreckenPunkt();
            if (geoPoint != null) {
                str.append('[').append(geoPoint.getLatitude()).append(',').append(geoPoint.getLongitude()).append("],"); //$NON-NLS-1$
            }
            pointAdded = true;
        }
        if (pointAdded) {
            str.append(']');
            str.replace(str.length() - 2, str.length() - 1, ""); //$NON-NLS-1$
        } else {
            return null;
        }
        final long end = DateTime.now().getMillis();
        LOGGER.debug(String.format("Dauer convertTrackPoints %s [ms]", (end - start))); //$NON-NLS-1$
        return str.toString();
    }

    /**
     * <pre>
     *          [[25.774252,-80.190262],[18.466465,-66.118292], [46.954, 7.448]]
     *          
     *          dies soll [[25.774252,-80.190262]] zurückgeben.
     * </pre>
     * 
     * @param convertedPoints
     * @return
     */
    public static String getFirstPointToPan(final String convertedPoints) {
        if (convertedPoints != null && convertedPoints.length() > 10) {
            return convertedPoints.substring(2, convertedPoints.indexOf(']'));
        }
        return Messages.MapConverter1;
    }
}
