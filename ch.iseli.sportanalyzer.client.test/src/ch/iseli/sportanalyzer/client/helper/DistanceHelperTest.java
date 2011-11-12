package ch.iseli.sportanalyzer.client.helper;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class DistanceHelperTest {
    @Test
    public void testConvert() {
        String roundDistance = DistanceHelper.roundDistanceFromMeterToKm(10123.4567890);
        assertEquals("10.123km", roundDistance);
    }

    @Test
    public void testConvertBig() {
        String roundDistance = DistanceHelper.roundDistanceFromMeterToKm(10123456.7890);
        assertEquals("10123.457km", roundDistance);
    }

    @Test
    public void testConvertSmall() {
        String roundDistance = DistanceHelper.roundDistanceFromMeterToKm(12.34567890);
        assertEquals("0.012km", roundDistance);
    }

    @Test
    public void testConvertSmallRoundUp() {
        String roundDistance = DistanceHelper.roundDistanceFromMeterToKm(12.64567890);
        assertEquals("0.013km", roundDistance);
    }
}
