package ch.iseli.sportanalyzer.client.helper;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class DistanceHelperTest {
    @Test
    public void testConvert() {
        final String roundDistance = DistanceHelper.roundDistanceFromMeterToKmMitEinheit(10123.4567890);
        assertEquals("10.123km", roundDistance); //$NON-NLS-1$
    }

    @Test
    public void testConvertBig() {
        final String roundDistance = DistanceHelper.roundDistanceFromMeterToKmMitEinheit(10123456.7890);
        assertEquals("10123.457km", roundDistance);//$NON-NLS-1$
    }

    @Test
    public void testConvertSmall() {
        final String roundDistance = DistanceHelper.roundDistanceFromMeterToKmMitEinheit(12.34567890);
        assertEquals("0.012km", roundDistance);//$NON-NLS-1$
    }

    @Test
    public void testConvertSmallRoundUp() {
        final String roundDistance = DistanceHelper.roundDistanceFromMeterToKmMitEinheit(12.64567890);
        assertEquals("0.013km", roundDistance);//$NON-NLS-1$
    }

    @Test
    public void testCalculatePace() {
        final String pace = DistanceHelper.calculatePace(1000, 300);
        assertEquals("5:00", pace);//$NON-NLS-1$
    }

    @Test
    public void testCalculatePaceSekunden() {
        final String pace = DistanceHelper.calculatePace(1000, 315);
        assertEquals("5:15", pace);//$NON-NLS-1$
    }

    @Test
    public void testCalculatePaceFromMperSecond() {
        final String pace = DistanceHelper.calculatePace(6.32446337);
        assertEquals("2:38", pace);//$NON-NLS-1$
    }
}
