package ch.opentrainingcenter.importer.fit;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.io.File;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import ch.opentrainingcenter.core.exceptions.ConvertException;
import ch.opentrainingcenter.transfer.ITrackPointProperty;
import ch.opentrainingcenter.transfer.ITraining;
import ch.opentrainingcenter.transfer.Sport;

@SuppressWarnings("nls")
public class ConvertFitTest {

    private ConvertFit converter;

    @Before
    public void setUp() {
        converter = new ConvertFit();
    }

    @Test
    public void testRealActivityConvert() throws ConvertException, ParseException {
        final ITraining training = converter.convert(new File("resources/2014_09_11.fit"));

        assertNotNull(training);
        assertNull("Ist null, da dieser Timestamp erst vom importer gesetzt", training.getDateOfImport());
        assertEquals("Lauf startet um 2014-09-11 17:18:35", convertToDate("2014-09-11 17:18:35"), training.getDatum());
        assertEquals("<TotalTimeSeconds>2003.2</TotalTimeSeconds>", 2008.0, training.getDauer(), 0.001);
        assertEquals("<DistanceMeters>5297.08</DistanceMeters>", 5297.08, training.getLaengeInMeter(), 0.001);
        assertEquals("<MaximumSpeed>5.067</MaximumSpeed>", 5.067, training.getMaxSpeed(), 0.001);
        assertEquals("AverageHeartRateBpm", 132, training.getAverageHeartBeat());
        assertEquals("MaximumHeartRateBpm", 159, training.getMaxHeartBeat());

        assertEquals("Positive Höhenmeter", 198, training.getUpMeter().intValue());
        assertEquals("Negative Höhenmeter", 195, training.getDownMeter().intValue());

        assertEquals(Sport.RUNNING, training.getSport());

        final List<ITrackPointProperty> points = training.getTrackPoints();

        assertTrackPoint(0, "2014-09-11 17:18:35", 0.0, 558.0, 46.95485311, 7.449236233, points.get(0));
        assertTrackPoint(1, "2014-09-11 17:18:43", 12.57, 555.0, 46.95496534, 7.449198263, points.get(1));
        assertTrackPoint(2, "2014-09-11 17:18:48", 31.83, 555.0, 46.95501345, 7.448971029, points.get(2));
        assertTrackPoint(3, "2014-09-11 17:18:52", 45.68, 554.0, 46.95496056, 7.448805906, points.get(3));
        assertTrackPoint(4, "2014-09-11 17:18:55", 51.47, 554.0, 46.95492133, 7.448757458, points.get(4));
        // noch letzter punkt
        assertTrackPoint(301, "2014-09-11 17:51:57", 5294.86, 562.0, 46.9547371, 7.448976813, points.get(points.size() - 1));
    }

    private void assertTrackPoint(final int index, final String datum, final double distanz, final double hoehe, final double latitude, final double longitude,
            final ITrackPointProperty point) throws ParseException {
        final String punkt = "[Punkt " + index + "] ";
        final long time = convertToDate(datum);
        final long convertedTime = point.getZeit();
        assertEquals(punkt + "Distanz vom Punkt", distanz, point.getDistance(), 0.001);
        assertEquals(punkt + "Differenz muss 0 [ms] sein", 0, convertedTime - time);
        assertEquals(punkt + "Höhe des Punktes", hoehe, point.getAltitude(), 0.001);
        assertEquals(punkt + "LatitudeDegrees", latitude, point.getStreckenPunkt().getLatitude(), 0.00000001);
        assertEquals(punkt + "LongitudeDegrees", longitude, point.getStreckenPunkt().getLongitude(), 0.00000001);
    }

    private long convertToDate(final String datum) throws ParseException {
        final DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return formatter.parse(datum).getTime();
    }

    @Test
    // @Ignore
    public void testConvert() throws ConvertException {
        converter.convert(new File("resources/Activity.fit"));
    }

    @Test
    public void testGetFilePrefix() {
        assertEquals("fit", converter.getFilePrefix());
    }

}
