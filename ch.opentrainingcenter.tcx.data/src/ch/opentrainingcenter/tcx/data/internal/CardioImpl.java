package ch.opentrainingcenter.tcx.data.internal;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import ch.iseli.sportanalyzer.tcx.ActivityLapT;
import ch.iseli.sportanalyzer.tcx.ActivityT;
import ch.iseli.sportanalyzer.tcx.TrackT;
import ch.iseli.sportanalyzer.tcx.TrackpointT;
import ch.iseli.sportanalyzer.tcx.TrainingCenterDatabaseT;
import ch.opentrainingcenter.tcx.data.Cardio;

public class CardioImpl implements Cardio {

    private final static Logger logger = Logger.getLogger(CardioImpl.class.getName());
    private final List<ActivityT> activities;

    public CardioImpl(final TrainingCenterDatabaseT t) {
        activities = t.getActivities().getActivity();
    }

    @Override
    public int getAverageCardio() {
        final List<Short> beats = new ArrayList<Short>();
        for (final ActivityT activityT : activities) {
            final List<ActivityLapT> lap = activityT.getLap();
            for (final ActivityLapT activityLapT : lap) {
                final List<TrackT> track = activityLapT.getTrack();
                if (track.size() == 0) {
                    return activityLapT.getAverageHeartRateBpm().getValue();
                } else {
                    iterateOverAllTracks(beats, track);
                }
            }
        }
        return sum(beats) / beats.size();
    }

    private void iterateOverAllTracks(final List<Short> beats, final List<TrackT> track) {
        for (final TrackT trackT : track) {
            final List<TrackpointT> trackpoint = trackT.getTrackpoint();
            iterateOverEachTrackPoint(beats, trackpoint);
        }
    }

    private void iterateOverEachTrackPoint(final List<Short> beats, final List<TrackpointT> trackpoint) {
        for (final TrackpointT trackpointT : trackpoint) {
            if (trackpointT.getHeartRateBpm() != null) {
                final short value = trackpointT.getHeartRateBpm().getValue();
                beats.add(value);
                logger.fine("Heartbeat found: " + value);//$NON-NLS-1$
            }
        }
    }

    private int sum(final List<Short> beats) {
        int sum = 0;
        for (final Short beat : beats) {
            sum += beat;
        }
        return sum;
    }
}
