package ch.iseli.sportanalyzer.client.helper;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class FileNameToDateConverterTest {

    @Test
    public void testGetHumanReadableDate() {
        assertEquals("2011.08.28 13:49", FileNameToDateConverter.getHumanReadableDate("20110828T134900.gmn"));
    }

    @Test
    public void testGetHumanReadableDateTooShort() {
        assertEquals("20110828T134.gmn", FileNameToDateConverter.getHumanReadableDate("20110828T134.gmn"));
    }

    @Test
    public void testGetHumanReadableDateNotADate() {
        assertEquals("201A0828T134900.gmn", FileNameToDateConverter.getHumanReadableDate("201A0828T134900.gmn"));
        assertEquals("20110B28T134900.gmn", FileNameToDateConverter.getHumanReadableDate("20110B28T134900.gmn"));
        assertEquals("2011082CT134900.gmn", FileNameToDateConverter.getHumanReadableDate("2011082CT134900.gmn"));
        assertEquals("20110828T1D4900.gmn", FileNameToDateConverter.getHumanReadableDate("20110828T1D4900.gmn"));
        assertEquals("20110828T134E00.gmn", FileNameToDateConverter.getHumanReadableDate("20110828T134E00.gmn"));
    }
}
