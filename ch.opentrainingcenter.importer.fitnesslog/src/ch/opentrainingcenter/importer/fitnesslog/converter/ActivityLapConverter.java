package ch.opentrainingcenter.importer.fitnesslog.converter;

import java.math.BigDecimal;

import ch.iseli.sportanalyzer.tcx.ActivityLapT;
import ch.iseli.sportanalyzer.tcx.HeartRateInBeatsPerMinuteT;
import ch.opentrainingcenter.importer.fitnesslog.model.Calories;
import ch.opentrainingcenter.importer.fitnesslog.model.Distance;
import ch.opentrainingcenter.importer.fitnesslog.model.HeartRate;
import ch.opentrainingcenter.importer.fitnesslog.model.Lap;

public class ActivityLapConverter implements Convert<Lap, ActivityLapT> {

    @Override
    public ActivityLapT convert(final Lap lap) {
        final ActivityLapT lapT = new ActivityLapT();
        final HeartRate heartRate = lap.getHeartRate();
        if (heartRate != null) {
            lapT.setAverageHeartRateBpm(convert(heartRate.getAverageBPM()));
            lapT.setMaximumHeartRateBpm(convert(heartRate.getMaximumBPM()));
        }
        final Calories calories = lap.getCalories();
        if (calories != null) {
            lapT.setCalories(calories.getTotalCal().intValue());
        }
        final Distance distance = lap.getDistance();
        if (distance != null) {
            lapT.setDistanceMeters(distance.getTotalMeters().intValue());
        }
        lapT.setStartTime(lap.getStartTime());
        final BigDecimal durationSeconds = lap.getDurationSeconds();
        if (durationSeconds != null) {
            lapT.setTotalTimeSeconds(durationSeconds.intValue());
        }
        return lapT;
    }

    protected HeartRateInBeatsPerMinuteT convert(final BigDecimal rate) {
        final HeartRateInBeatsPerMinuteT bpm = new HeartRateInBeatsPerMinuteT();
        bpm.setValue(rate.shortValue());
        return bpm;
    }
}
