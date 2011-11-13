package ch.iseli.sportanalyzer.client.helper;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class DistanceHelperTest {
    @Test
    public void testConvert() {
        String roundDistance = DistanceHelper.roundDistanceFromMeterToKmMitEinheit(10123.4567890);
        assertEquals("10.123km", roundDistance);
    }

    @Test
    public void testConvertBig() {
        String roundDistance = DistanceHelper.roundDistanceFromMeterToKmMitEinheit(10123456.7890);
        assertEquals("10123.457km", roundDistance);
    }

    @Test
    public void testConvertSmall() {
        String roundDistance = DistanceHelper.roundDistanceFromMeterToKmMitEinheit(12.34567890);
        assertEquals("0.012km", roundDistance);
    }

    @Test
    public void testConvertSmallRoundUp() {
        String roundDistance = DistanceHelper.roundDistanceFromMeterToKmMitEinheit(12.64567890);
        assertEquals("0.013km", roundDistance);
    }

    @Test
    public void testCalculatePace() {
        String pace = DistanceHelper.calculatePace(1000, 300);
        assertEquals("5:00", pace);
    }

    @Test
    public void testCalculatePaceSekunden() {
        String pace = DistanceHelper.calculatePace(1000, 315);
        assertEquals("5:15", pace);
    }

    @Test
    public void testCalculatePaceFromMperSecond() {
        String pace = DistanceHelper.calculatePace(6.32446337);
        assertEquals("2:38", pace);
    }
}
