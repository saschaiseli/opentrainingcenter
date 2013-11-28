package ch.opentrainingcenter.importer.gpx;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.File;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.TimeZone;

import org.junit.Before;
import org.junit.Test;

import ch.opentrainingcenter.core.exceptions.ConvertException;
import ch.opentrainingcenter.transfer.ITrackPointProperty;
import ch.opentrainingcenter.transfer.ITraining;

public class ConvertGpxTest {

    ConvertGpx converter;

    @Before
    public void setUp() {
        converter = new ConvertGpx();
    }

    @Test
    public void testGetFilePrefix() {
        assertEquals(ConvertGpx.PREFIX, converter.getFilePrefix());
    }

    @Test
    public void testGetName() {
        assertEquals(ConvertGpx.NAME, converter.getName());
    }

    @Test
    public void testConvert() throws ConvertException {
        final ITraining result = converter.convert(new File("resources", "twoTrackPoints.gpx")); //$NON-NLS-1$//$NON-NLS-2$
        assertNotNull(result);
    }

    @Test
    public void testTwoPoints() throws ConvertException {
        final Calendar cal = Calendar.getInstance(Locale.getDefault());// 2013-08-29T07:05:00Z
        cal.set(Calendar.YEAR, 2013);
        cal.set(Calendar.MONTH, 7);
        cal.set(Calendar.DAY_OF_MONTH, 29);
        cal.set(Calendar.HOUR_OF_DAY, 7);
        cal.set(Calendar.MINUTE, 5);
        cal.set(Calendar.SECOND, 0);
        cal.setTimeZone(TimeZone.getDefault());

        final ITraining result = converter.convert(new File("resources", "twoTrackPoints.gpx")); //$NON-NLS-1$//$NON-NLS-2$
        assertEquals(80, result.getMaxHeartBeat());
        assertEquals(70, result.getAverageHeartBeat());
        final Date time = cal.getTime();
        System.out.println(time);
        System.out.println(time.getTime());
        System.out.println(result.getDatum());
        System.out.println(new Date(result.getDatum()));
        assertEquals(cal.getTime().getTime() / 1000, result.getDatum() / 1000);
        assertEquals(60, result.getDauer(), 0.0001);
        assertEquals(23.737725, result.getLaengeInMeter(), 0.0001);
    }

    @Test
    public void testMitEinemTrackPoint() throws ConvertException {
        final ITraining result = converter.convert(new File("resources", "oneTrackPoints.gpx")); //$NON-NLS-1$//$NON-NLS-2$
        assertEquals(0, result.getDauer(), 0.001);
        assertEquals(0, result.getLaengeInMeter(), 0.001);

        final Calendar cal = Calendar.getInstance(Locale.getDefault());// 2013-08-29T07:05:00Z
        cal.set(Calendar.YEAR, 2013);
        cal.set(Calendar.MONTH, 7);
        cal.set(Calendar.DAY_OF_MONTH, 29);
        cal.set(Calendar.HOUR_OF_DAY, 7);
        cal.set(Calendar.MINUTE, 5);
        cal.set(Calendar.SECOND, 0);
        cal.setTimeZone(TimeZone.getDefault());

        assertEquals(cal.getTime().getTime() / 1000, result.getDatum() / 1000, 0.001);
    }

    @Test
    public void testZeit() throws ConvertException {
        final ITraining result = converter.convert(new File("resources", "twoTrackPoints.gpx")); //$NON-NLS-1$//$NON-NLS-2$
        assertEquals(60, result.getDauer(), 0.001);
    }

    @Test
    public void testRealTrackOhneHerz() throws ConvertException {
        final ITraining result = converter.convert(new File("resources", "20131120.gpx")); //$NON-NLS-1$//$NON-NLS-2$
        assertNotNull(result);
    }

    @Test
    public void test2Laps() throws ConvertException {
        final ITraining result = converter.convert(new File("resources", "twoLapsTrackPoints.gpx")); //$NON-NLS-1$//$NON-NLS-2$
        final List<ITrackPointProperty> trackPoints = result.getTrackPoints();
        final Set<Integer> laps = new HashSet<>();
        for (final ITrackPointProperty point : trackPoints) {
            laps.add(point.getLap());
        }
        assertEquals("2 Laps sind vorhanden", 2, laps.size()); //$NON-NLS-1$
    }
}
