package ch.opentrainingcenter.importer.fitnesslog.converter;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

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
import ch.iseli.sportanalyzer.tcx.TrainingCenterDatabaseT;
import ch.opentrainingcenter.importer.fitnesslog.model.Activity;
import ch.opentrainingcenter.importer.fitnesslog.model.AthleteLog;
import ch.opentrainingcenter.importer.fitnesslog.model.FitnessWorkbook;
import ch.opentrainingcenter.importer.fitnesslog.model.Lap;
import ch.opentrainingcenter.importer.fitnesslog.model.Laps;

public class ConvertWorkbook2TcxTest {
    private FitnessWorkbook workbook;
    private ConvertWorkbook2Tcx converter;
    private GregorianCalendar cal;

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
        final XMLGregorianCalendar startTime = DatatypeFactory.newInstance().newXMLGregorianCalendar(cal);
        activity.setStartTime(startTime);
        final Laps laps = new Laps();
        final Lap lap = new Lap();
        laps.getLap().add(lap);
        activity.setLaps(laps);
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
    }
}
