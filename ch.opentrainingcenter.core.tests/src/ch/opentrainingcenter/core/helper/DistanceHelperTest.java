package ch.opentrainingcenter.core.helper;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import ch.opentrainingcenter.transfer.Sport;

public class DistanceHelperTest {
    @Test
    public void testConvert() {
        final String roundDistance = DistanceHelper.roundDistanceFromMeterToKmMitEinheit(10123.4567890);
        assertEquals("10.123km", roundDistance); //$NON-NLS-1$
    }

    @Test
    public void testConvertLessZeros() {
        final String roundDistance = DistanceHelper.roundDistanceFromMeterToKmMitEinheit(0.0);
        assertEquals("0.000km", roundDistance); //$NON-NLS-1$
    }

    @Test
    public void testConvertNoZeros() {
        final String roundDistance = DistanceHelper.roundDistanceFromMeterToKm(4);
        assertEquals("0.004", roundDistance); //$NON-NLS-1$
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
    public void testCalculateGeschwindigkeit() {
        final String pace = DistanceHelper.calculateGeschwindigkeit(42000, 3600);
        assertEquals("42.0", pace);//$NON-NLS-1$
    }

    @Test
    public void testCalculateGeschwindigkeit_2() {
        final String pace = DistanceHelper.calculateGeschwindigkeit(42111.213, 3600);
        assertEquals("42.1", pace);//$NON-NLS-1$
    }

    @Test
    public void testCalculatePaceBike() {
        final String pace = DistanceHelper.calculatePace(42111.213, 3600, Sport.BIKING);
        assertEquals("42.1", pace);//$NON-NLS-1$
    }

    @Test
    public void testCalculatePaceOTHER() {
        final String pace = DistanceHelper.calculatePace(42111.213, 3600, Sport.OTHER);
        assertEquals("1:25", pace);//$NON-NLS-1$
    }

    @Test
    public void testCalculatePaceRunning() {
        final String pace = DistanceHelper.calculatePace(42111.213, 3600, Sport.RUNNING);
        assertEquals("1:25", pace);//$NON-NLS-1$
    }

    @Test
    public void testCalculatePaceFromMperSecondRunning() {
        final String pace = DistanceHelper.calculatePace(6.32446337, Sport.RUNNING);
        assertEquals("2:38", pace);//$NON-NLS-1$
    }

    @Test
    public void testCalculatePaceFromMperSecondBiking() {
        final String pace = DistanceHelper.calculatePace(6.32446337, Sport.BIKING);
        assertEquals("22.7", pace);//$NON-NLS-1$
    }

    @Test
    public void testCalculatePaceFromMperSecondOTHER() {
        final String pace = DistanceHelper.calculatePace(6.32446337, Sport.OTHER);
        assertEquals("2:38", pace);//$NON-NLS-1$
    }
}
