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

import ch.iseli.sportanalyzer.tcx.TrackpointT;
import ch.opentrainingcenter.importer.fitnesslog.model.Pt;

public class TrackPointConverterTest {
    private TrackpointConverter conv;
    private Pt previousPoint;
    private Pt currentPoint;

    private XMLGregorianCalendar startTime;
    private GregorianCalendar cal;

    @Before
    public void setUp() throws DatatypeConfigurationException {

        cal = new GregorianCalendar();
        cal.set(Calendar.YEAR, 2011);
        cal.set(Calendar.MONTH, 7);
        cal.set(Calendar.DAY_OF_MONTH, 29);
        cal.set(Calendar.HOUR_OF_DAY, 14);
        cal.set(Calendar.MINUTE, 45);
        cal.set(Calendar.SECOND, 42);
        cal.set(Calendar.MILLISECOND, 22);
        startTime = DatatypeFactory.newInstance().newXMLGregorianCalendar(cal);

        conv = new TrackpointConverter(startTime);
        previousPoint = new Pt();
        previousPoint.setLat(BigDecimal.valueOf(43.123456));
        previousPoint.setLon(BigDecimal.valueOf(7.123456));
        previousPoint.setDist(BigDecimal.valueOf(0));

        currentPoint = new Pt();
        currentPoint.setEle(BigDecimal.valueOf(510.000));
        currentPoint.setLat(BigDecimal.valueOf(43.123456));
        currentPoint.setLon(BigDecimal.valueOf(7.123456));
        currentPoint.setHr(BigDecimal.valueOf(142.000));
        currentPoint.setTm(BigDecimal.valueOf(42.000));
    }

    @Test
    public void testSimple() {
        final TrackpointT convert = conv.convert(previousPoint, currentPoint);
        assertNotNull(convert);
    }

    @Test
    public void testFull() {
        final TrackpointT convert = conv.convert(previousPoint, currentPoint);
        assertEquals(510.00, convert.getAltitudeMeters().doubleValue(), 0.0001);
        assertEquals(43.123456, convert.getPosition().getLatitudeDegrees(), 0.0001);
        assertEquals(7.123456, convert.getPosition().getLongitudeDegrees(), 0.0001);
        assertEquals(142, convert.getHeartRateBpm().getValue(), 0.0001);
        assertEquals(0.0, convert.getDistanceMeters(), 0.0001);
        cal.add(Calendar.SECOND, 42);
        final GregorianCalendar gregorianCalendar = convert.getTime().toGregorianCalendar();
        assertEquals(cal.getTimeInMillis(), gregorianCalendar.getTimeInMillis());
    }

    @Test
    public void testDistance() {

        previousPoint.setLat(BigDecimal.valueOf(49.9917));
        previousPoint.setLon(BigDecimal.valueOf(8.41321));

        currentPoint.setLat(BigDecimal.valueOf(50.0049));
        currentPoint.setLon(BigDecimal.valueOf(8.42182));

        final TrackpointT convert = conv.convert(previousPoint, currentPoint);
        assertEquals(1591.4944, convert.getDistanceMeters(), 0.0001);
    }
}
