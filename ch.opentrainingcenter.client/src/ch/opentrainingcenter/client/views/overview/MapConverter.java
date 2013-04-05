package ch.opentrainingcenter.client.views.overview;

import java.util.ArrayList;
import java.util.List;

import ch.opentrainingcenter.i18n.Messages;
import ch.opentrainingcenter.model.geo.Track;
import ch.opentrainingcenter.model.geo.TrackPoint;
import ch.opentrainingcenter.transfer.IStreckenPunkt;
import ch.opentrainingcenter.transfer.ITrackPointProperty;
import ch.opentrainingcenter.transfer.ITraining;

public final class MapConverter {

    private MapConverter() {
    }

    public static Track convert(final ITraining training) {
        final List<TrackPoint> trackPoints = new ArrayList<TrackPoint>();
        final List<ITrackPointProperty> points = training.getTrackPoints();
        for (final ITrackPointProperty point : points) {
            trackPoints.add(new TrackPoint(point.getDistance(), point.getStreckenPunkt().getLatitude(), point.getStreckenPunkt().getLongitude()));
        }

        final Track track = new Track(trackPoints);
        return track;
    }

    public static String convertTrackpoints(final ITraining training) {
        final StringBuffer str = new StringBuffer();
        str.append('[');
        final List<ITrackPointProperty> points = training.getTrackPoints();
        for (final ITrackPointProperty point : points) {
            final IStreckenPunkt geoPoint = point.getStreckenPunkt();
            str.append('[').append(geoPoint.getLatitude()).append(',').append(geoPoint.getLongitude()).append("],"); //$NON-NLS-1$
        }
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
