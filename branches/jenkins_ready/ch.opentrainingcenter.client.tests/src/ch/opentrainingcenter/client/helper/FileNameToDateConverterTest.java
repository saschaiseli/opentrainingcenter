package ch.opentrainingcenter.client.helper;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import ch.opentrainingcenter.client.helper.FileNameToDateConverter;

public class FileNameToDateConverterTest {

    @Test
    public void testGetHumanReadableDate() {
        assertEquals("2011.08.28 13:49", FileNameToDateConverter.getHumanReadableDate("20110828T134900.gmn"));//$NON-NLS-1$//$NON-NLS-2$
    }

    @Test
    public void testGetHumanReadableDateTooShort() {
        assertEquals("20110828T134.gmn", FileNameToDateConverter.getHumanReadableDate("20110828T134.gmn"));//$NON-NLS-1$//$NON-NLS-2$
    }

    @Test
    public void testGetHumanReadableDateNotADate() {
        assertEquals("201A0828T134900.gmn", FileNameToDateConverter.getHumanReadableDate("201A0828T134900.gmn"));//$NON-NLS-1$//$NON-NLS-2$
        assertEquals("20110B28T134900.gmn", FileNameToDateConverter.getHumanReadableDate("20110B28T134900.gmn"));//$NON-NLS-1$//$NON-NLS-2$
        assertEquals("2011082CT134900.gmn", FileNameToDateConverter.getHumanReadableDate("2011082CT134900.gmn"));//$NON-NLS-1$//$NON-NLS-2$
        assertEquals("20110828T1D4900.gmn", FileNameToDateConverter.getHumanReadableDate("20110828T1D4900.gmn"));//$NON-NLS-1$//$NON-NLS-2$
        assertEquals("20110828T134E00.gmn", FileNameToDateConverter.getHumanReadableDate("20110828T134E00.gmn"));//$NON-NLS-1$//$NON-NLS-2$
    }
}
