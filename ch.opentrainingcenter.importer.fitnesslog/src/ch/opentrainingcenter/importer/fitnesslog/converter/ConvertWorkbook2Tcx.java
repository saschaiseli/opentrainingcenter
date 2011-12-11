package ch.opentrainingcenter.importer.fitnesslog.converter;

import java.math.BigDecimal;
import java.util.List;

import ch.iseli.sportanalyzer.tcx.ActivityLapT;
import ch.iseli.sportanalyzer.tcx.ActivityListT;
import ch.iseli.sportanalyzer.tcx.ActivityT;
import ch.iseli.sportanalyzer.tcx.TrainingCenterDatabaseT;
import ch.opentrainingcenter.importer.fitnesslog.model.Activity;
import ch.opentrainingcenter.importer.fitnesslog.model.AthleteLog;
import ch.opentrainingcenter.importer.fitnesslog.model.Distance;
import ch.opentrainingcenter.importer.fitnesslog.model.FitnessWorkbook;
import ch.opentrainingcenter.importer.fitnesslog.model.HeartRate;
import ch.opentrainingcenter.importer.fitnesslog.model.Lap;
import ch.opentrainingcenter.importer.fitnesslog.model.Laps;

public class ConvertWorkbook2Tcx {
    public TrainingCenterDatabaseT convert(final FitnessWorkbook workbook) {
        final TrainingCenterDatabaseT database = new TrainingCenterDatabaseT();
        final ActivityListT activityList = new ActivityListT();

        final List<AthleteLog> athleteLog = workbook.getAthleteLog();
        for (final AthleteLog log : athleteLog) {
            final List<Activity> activities = log.getActivity();
            for (final Activity activity : activities) {
                final ActivityT activityT = new ActivityT();
                activityT.setId(activity.getStartTime());
                activityList.getActivity().add(activityT);
                final List<ActivityLapT> lap2 = activityT.getLap();
                final ActivityLapT e;
                // final e.get
                // lap2.add(e)
                //
                final Laps laps = activity.getLaps();
                for (final Lap lap : laps.getLap()) {
                    final HeartRate heartRate = lap.getHeartRate();
                    final Distance distance = lap.getDistance();
                    final BigDecimal durationSeconds = lap.getDurationSeconds();
                }
            }
        }
        database.setActivities(activityList);
        return database;
    }
}
