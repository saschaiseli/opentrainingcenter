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
    private Pt pt;

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
        pt = new Pt();
        pt.setEle(BigDecimal.valueOf(510.000));
        pt.setLat(BigDecimal.valueOf(43.123456));
        pt.setLon(BigDecimal.valueOf(7.123456));
        pt.setHr(BigDecimal.valueOf(142.000));
        pt.setTm(BigDecimal.valueOf(42.000));
    }

    @Test
    public void testSimple() {
        final TrackpointT convert = conv.convert(pt);
        assertNotNull(convert);
    }

    @Test
    public void testFull() {
        final TrackpointT convert = conv.convert(pt);
        assertEquals(510.00, convert.getAltitudeMeters().doubleValue(), 0.0001);
        assertEquals(43.123456, convert.getPosition().getLatitudeDegrees(), 0.0001);
        assertEquals(7.123456, convert.getPosition().getLongitudeDegrees(), 0.0001);
        assertEquals(142, convert.getHeartRateBpm().getValue(), 0.0001);
        cal.add(Calendar.SECOND, 42);
        final GregorianCalendar gregorianCalendar = convert.getTime().toGregorianCalendar();
        assertEquals(cal.getTimeInMillis(), gregorianCalendar.getTimeInMillis());
    }
}
