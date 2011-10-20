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

    public CardioImpl(TrainingCenterDatabaseT t) {
	activities = t.getActivities().getActivity();
    }

    @Override
    public int getAverageCardio() {
	List<Short> beats = new ArrayList<Short>();
	for (ActivityT activityT : activities) {
	    List<ActivityLapT> lap = activityT.getLap();
	    for (ActivityLapT activityLapT : lap) {
		List<TrackT> track = activityLapT.getTrack();
		if (track.size() == 0) {
		    return activityLapT.getAverageHeartRateBpm().getValue();
		} else {
		    iterateOverAllTracks(beats, track);
		}
	    }
	}
	return sum(beats) / beats.size();
    }

    private void iterateOverAllTracks(List<Short> beats, List<TrackT> track) {
	for (TrackT trackT : track) {
	    List<TrackpointT> trackpoint = trackT.getTrackpoint();
	    iterateOverEachTrackPoint(beats, trackpoint);
	}
    }

    private void iterateOverEachTrackPoint(List<Short> beats, List<TrackpointT> trackpoint) {
	for (TrackpointT trackpointT : trackpoint) {
	    if (trackpointT.getHeartRateBpm() != null) {
		short value = trackpointT.getHeartRateBpm().getValue();
		beats.add(value);
		logger.fine("Heartbeat found: " + value);
	    }
	}
    }

    private int sum(List<Short> beats) {
	int sum = 0;
	for (Short beat : beats) {
	    sum += beat;
	}
	return sum;
    }
}
