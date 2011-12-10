package ch.iseli.sportanalyzer.client.views.overview;

import java.util.List;

import org.apache.log4j.Logger;

import ch.iseli.sportanalyzer.client.cache.TrainingCenterRecord;
import ch.iseli.sportanalyzer.tcx.ActivityLapT;
import ch.iseli.sportanalyzer.tcx.ActivityListT;
import ch.iseli.sportanalyzer.tcx.ActivityT;
import ch.iseli.sportanalyzer.tcx.PositionT;
import ch.iseli.sportanalyzer.tcx.TrackT;
import ch.iseli.sportanalyzer.tcx.TrackpointT;

public final class MapConverter {

    private static final Logger logger = Logger.getLogger(MapConverter.class);

    private MapConverter() {

    }

    public static String convertTrackpoints(final TrainingCenterRecord record) {
        final StringBuffer str = new StringBuffer();
        str.append('[');
        boolean pointAdded = false;
        final ActivityListT activities = record.getTrainingCenterDatabase().getActivities();
        for (final ActivityT activity : activities.getActivity()) {
            final List<ActivityLapT> lap = activity.getLap();
            for (final ActivityLapT activityLapT : lap) {
                final List<TrackT> track = activityLapT.getTrack();
                for (final TrackT trackT : track) {
                    final List<TrackpointT> trackpoint = trackT.getTrackpoint();
                    for (final TrackpointT point : trackpoint) {
                        final PositionT position = point.getPosition();
                        if (position != null) {
                            str.append('[').append(position.getLatitudeDegrees()).append(',').append(position.getLongitudeDegrees()).append("],"); //$NON-NLS-1$
                            logger.debug("Position: " + position.getLatitudeDegrees() + " / " + position.getLongitudeDegrees()); //$NON-NLS-2$
                            pointAdded = true;
                        }
                    }
                }
            }
        }
        if (pointAdded) {
            str.append(']');
            str.replace(str.length() - 2, str.length() - 1, ""); //$NON-NLS-1$
        } else {
            return "[[46.954, 7.448]]"; //$NON-NLS-1$
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
        return "";
    }
}
