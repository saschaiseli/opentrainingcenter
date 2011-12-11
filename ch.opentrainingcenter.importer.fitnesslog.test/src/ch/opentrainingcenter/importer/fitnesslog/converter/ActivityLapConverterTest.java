package ch.opentrainingcenter.importer.fitnesslog.converter;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.GregorianCalendar;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

import org.junit.Before;
import org.junit.Test;

import ch.iseli.sportanalyzer.tcx.ActivityLapT;
import ch.opentrainingcenter.importer.fitnesslog.model.Calories;
import ch.opentrainingcenter.importer.fitnesslog.model.Distance;
import ch.opentrainingcenter.importer.fitnesslog.model.HeartRate;
import ch.opentrainingcenter.importer.fitnesslog.model.Lap;

public class ActivityLapConverterTest {
    private ActivityLapConverter conv;
    private Lap lap;
    private GregorianCalendar cal;
    private XMLGregorianCalendar startTime;

    @Before
    public void setUp() throws DatatypeConfigurationException {
        conv = new ActivityLapConverter();
        lap = new Lap();

        cal = new GregorianCalendar();
        cal.set(Calendar.YEAR, 2011);
        cal.set(Calendar.MONTH, 7);
        cal.set(Calendar.DAY_OF_MONTH, 29);
        cal.set(Calendar.HOUR_OF_DAY, 14);
        cal.set(Calendar.MINUTE, 45);
        cal.set(Calendar.SECOND, 42);
        cal.set(Calendar.MILLISECOND, 22);
        startTime = DatatypeFactory.newInstance().newXMLGregorianCalendar(cal);
    }

    @Test
    public void testSimple() {
        assertNotNull(conv.convert(lap));
    }

    @Test
    public void testFullLap() {
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
        final ActivityLapT converted = conv.convert(lap);
        assertEquals(42, converted.getCalories());
        assertEquals(1042.00, converted.getDistanceMeters(), 0.0001);
        assertEquals(10042.00, converted.getTotalTimeSeconds(), 0.0001);
        assertEquals(142, converted.getAverageHeartRateBpm().getValue());
        assertEquals(192, converted.getMaximumHeartRateBpm().getValue());
        assertEquals(startTime, converted.getStartTime());
    }
}
