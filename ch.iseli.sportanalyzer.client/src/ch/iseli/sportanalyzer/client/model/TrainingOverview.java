package ch.iseli.sportanalyzer.client.model;

import java.util.Date;
import java.util.List;

import javax.xml.datatype.XMLGregorianCalendar;

import org.apache.log4j.Logger;

import ch.iseli.sportanalyzer.client.cache.TrainingCenterRecord;
import ch.iseli.sportanalyzer.client.helper.DistanceHelper;
import ch.iseli.sportanalyzer.client.helper.TimeHelper;
import ch.iseli.sportanalyzer.tcx.ActivityLapT;
import ch.iseli.sportanalyzer.tcx.ActivityT;
import ch.iseli.sportanalyzer.tcx.IntensityT;

public class TrainingOverview implements ITrainingOverview {

    private static final Logger logger = Logger.getLogger(TrainingOverview.class);

    private final TrainingCenterRecord t;
    private String datum;

    private double distance;

    private String roundDistanceFromMeterToKm;

    private String avgHeartRate;

    private short maxHeartBeat;

    private double maximumSpeed;

    private String dauer;

    private double timeInSeconds;

    private String averageSpeed;

    private String maxPace;

    private Date dateOfStart;

    public TrainingOverview(TrainingCenterRecord t) {
        this.t = t;
        if (t == null) {
            throw new IllegalArgumentException("Trainingdatabase darf nicht null sein!");
        }
        // werte auslesen
        init();
    }

    private void init() {
        // datum
        XMLGregorianCalendar date = t.getTrainingCenterDatabase().getActivities().getActivity().get(0).getId();
        datum = TimeHelper.convertGregorianDateToString(date, false);
        dateOfStart = date.toGregorianCalendar().getTime();
        // lauflänge
        ActivityT activityT = t.getTrainingCenterDatabase().getActivities().getActivity().get(0);
        List<ActivityLapT> laps = activityT.getLap();
        distance = 0.0;
        timeInSeconds = 0.0;
        short averageHeartRateBpm = 0;
        maxHeartBeat = 0;
        int lapWithCardio = 0;
        for (ActivityLapT lap : laps) {
            if (IntensityT.ACTIVE.equals(lap.getIntensity())) {
                distance += lap.getDistanceMeters();
                if (maximumSpeed < lap.getMaximumSpeed()) {
                    maximumSpeed = lap.getMaximumSpeed();
                }
                timeInSeconds += lap.getTotalTimeSeconds();
                if (!hasCardio(lap)) {
                    continue;
                }
                lapWithCardio++;
                averageHeartRateBpm += lap.getAverageHeartRateBpm() != null ? lap.getAverageHeartRateBpm().getValue() : 0;
                if (maxHeartBeat < lap.getMaximumHeartRateBpm().getValue()) {
                    maxHeartBeat = lap.getMaximumHeartRateBpm().getValue();
                }
            }
            logger.debug("lap: " + lap.getIntensity() + " distance: " + distance);
        }
        // in kilometer
        roundDistanceFromMeterToKm = DistanceHelper.roundDistanceFromMeterToKm(distance);
        // durschnittliche herzfrequenz
        if (lapWithCardio > 0) {
            avgHeartRate = String.valueOf(averageHeartRateBpm / lapWithCardio);
        } else {
            avgHeartRate = "-";
        }
        // dauer
        dauer = TimeHelper.convertSecondsToHumanReadableZeit(timeInSeconds);
        // durschnittliche geschwindigkeit
        averageSpeed = DistanceHelper.calculatePace(distance, timeInSeconds);

        maxPace = DistanceHelper.calculatePace(maximumSpeed);
    }

    private boolean hasCardio(ActivityLapT lap) {
        return lap.getMaximumHeartRateBpm() != null;
    }

    @Override
    public String getDatum() {
        return datum;
    }

    @Override
    public String getLaengeInKilometer() {
        return roundDistanceFromMeterToKm;
    }

    @Override
    public double getLaengeInMeter() {
        return distance;
    }

    @Override
    public String getAverageHeartBeat() {
        return avgHeartRate;
    }

    @Override
    public String getMaxHeartBeat() {
        return String.valueOf(maxHeartBeat);
    }

    @Override
    public String getPace() {
        return averageSpeed;
    }

    @Override
    public String getMaxSpeed() {
        return maxPace;
    }

    @Override
    public String getDauer() {
        return dauer;
    }

    public Date getDate() {
        return dateOfStart;
    }

    public double getDauerInSekunden() {
        return timeInSeconds;
    }

    public SimpleTraining getSimpleTraining() {
        return new SimpleTraining(distance, timeInSeconds, dateOfStart, Integer.valueOf(avgHeartRate).intValue());
    }
}
