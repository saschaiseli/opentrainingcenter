package ch.opentrainingcenter.importer.fitnesslog.converter;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

import org.junit.Before;
import org.junit.Test;

import ch.iseli.sportanalyzer.tcx.ActivityLapT;
import ch.iseli.sportanalyzer.tcx.ActivityListT;
import ch.iseli.sportanalyzer.tcx.ActivityT;
import ch.iseli.sportanalyzer.tcx.TrackT;
import ch.iseli.sportanalyzer.tcx.TrackpointT;
import ch.iseli.sportanalyzer.tcx.TrainingCenterDatabaseT;
import ch.opentrainingcenter.importer.fitnesslog.model.Activity;
import ch.opentrainingcenter.importer.fitnesslog.model.AthleteLog;
import ch.opentrainingcenter.importer.fitnesslog.model.Calories;
import ch.opentrainingcenter.importer.fitnesslog.model.Distance;
import ch.opentrainingcenter.importer.fitnesslog.model.FitnessWorkbook;
import ch.opentrainingcenter.importer.fitnesslog.model.HeartRate;
import ch.opentrainingcenter.importer.fitnesslog.model.Lap;
import ch.opentrainingcenter.importer.fitnesslog.model.Laps;
import ch.opentrainingcenter.importer.fitnesslog.model.Pt;
import ch.opentrainingcenter.importer.fitnesslog.model.Track;

public class ConvertWorkbook2TcxTest {
    private FitnessWorkbook workbook;
    private ConvertWorkbook2Tcx converter;
    private GregorianCalendar cal;
    private XMLGregorianCalendar startTime;

    @Before
    public void setUp() throws DatatypeConfigurationException {
        workbook = new FitnessWorkbook();
        final AthleteLog athleteLog = new AthleteLog();
        final Activity activity = new Activity();
        cal = new GregorianCalendar();
        cal.set(Calendar.YEAR, 2011);
        cal.set(Calendar.MONTH, 7);
        cal.set(Calendar.DAY_OF_MONTH, 29);
        cal.set(Calendar.HOUR_OF_DAY, 14);
        cal.set(Calendar.MINUTE, 45);
        cal.set(Calendar.SECOND, 42);
        cal.set(Calendar.MILLISECOND, 22);
        startTime = DatatypeFactory.newInstance().newXMLGregorianCalendar(cal);
        activity.setStartTime(startTime);
        final Laps laps = new Laps();
        final Lap lap = new Lap();

        final Calories value = new Calories();
        value.setTotalCal(BigDecimal.valueOf(42));
        lap.setCalories(value);

        final Distance dist = new Distance();
        dist.setTotalMeters(BigDecimal.valueOf(1042.00));
        lap.setDistance(dist);

        lap.setDurationSeconds(BigDecimal.valueOf(10042.00));

        final HeartRate heartRate = new HeartRate();
        heartRate.setAverageBPM(BigDecimal.valueOf(142.00));
        heartRate.setMaximumBPM(BigDecimal.valueOf(192.00));
        lap.setHeartRate(heartRate);

        lap.setStartTime(startTime);

        laps.getLap().add(lap);
        activity.setLaps(laps);

        final Track track = new Track();
        track.setStartTime(startTime);
        // <pt tm="0" lat="46.9579620361328" lon="7.46280908584595" ele="561.54736328125" hr="140" />
        final Pt pointA = new Pt();
        pointA.setEle(BigDecimal.valueOf(561.54736328125));
        pointA.setHr(BigDecimal.valueOf(140));
        pointA.setLat(BigDecimal.valueOf(46.9579620361328));
        pointA.setLon(BigDecimal.valueOf(7.46280908584595));
        pointA.setTm(BigDecimal.valueOf(0));
        track.getPt().add(pointA);

        // <pt tm="2" lat="46.9579315185547" lon="7.4627890586853" ele="561.066650390625" hr="140" />
        final Pt pointB = new Pt();
        pointB.setEle(BigDecimal.valueOf(561.066650390625));
        pointB.setHr(BigDecimal.valueOf(140));
        pointB.setLat(BigDecimal.valueOf(46.9579315185547));
        pointB.setLon(BigDecimal.valueOf(7.4627890586853));
        pointB.setTm(BigDecimal.valueOf(2));
        track.getPt().add(pointB);
        activity.setTrack(track);
        athleteLog.getActivity().add(activity);
        workbook.getAthleteLog().add(athleteLog);

        converter = new ConvertWorkbook2Tcx();
    }

    @Test
    public void testInstance() {
        final TrainingCenterDatabaseT convert = converter.convert(workbook);
        assertNotNull(convert);
    }

    @Test
    public void testStartDate() {
        final TrainingCenterDatabaseT convert = converter.convert(workbook);
        final ActivityListT activitiesT = convert.getActivities();
        assertNotNull(activitiesT);
        final List<ActivityT> a = activitiesT.getActivity();
        assertEquals(1, a.size());
        assertEquals(cal.getTime(), a.get(0).getId().toGregorianCalendar().getTime());
    }

    @Test
    public void testLaps() {
        final TrainingCenterDatabaseT convert = converter.convert(workbook);
        final ActivityListT activitiesT = convert.getActivities();
        assertNotNull(activitiesT);
        final List<ActivityT> a = activitiesT.getActivity();
        final ActivityT activityT = a.get(0);
        final List<ActivityLapT> laps = activityT.getLap();
        assertNotNull(laps);
        final ActivityLapT lap = laps.get(0);
        assertNotNull(lap);
        assertEquals(42, lap.getCalories());
        assertEquals(1042.00, lap.getDistanceMeters(), 0.0001);
        assertEquals(10042.00, lap.getTotalTimeSeconds(), 0.0001);
        assertEquals(142, lap.getAverageHeartRateBpm().getValue());
        assertEquals(192, lap.getMaximumHeartRateBpm().getValue());
        assertEquals(startTime, lap.getStartTime());
    }

    @Test
    public void testTrackpoints() {
        final TrainingCenterDatabaseT convert = converter.convert(workbook);
        final ActivityListT activitiesT = convert.getActivities();
        assertNotNull(activitiesT);
        final List<ActivityT> a = activitiesT.getActivity();
        final ActivityT activityT = a.get(0);
        final List<ActivityLapT> laps = activityT.getLap();
        assertNotNull(laps);
        final ActivityLapT lap = laps.get(0);
        final List<TrackT> track = lap.getTrack();
        assertNotNull(track);
        assertEquals(1, track.size());
        final TrackT trackT = track.get(0);
        assertNotNull(trackT);
        final List<TrackpointT> trackpoints = trackT.getTrackpoint();
        assertNotNull(trackpoints);
        assertEquals(2, trackpoints.size());
    }
}
