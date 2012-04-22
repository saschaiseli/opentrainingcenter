package ch.opentrainingcenter.client.helper;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class TimeHelperTest {

    @Test
    public void testConvertSekundenInReadableFormat() {
        final String t = TimeHelper.convertSecondsToHumanReadableZeit(6273.27);
        assertEquals("1:44:33", t);//$NON-NLS-1$
    }

    @Test
    public void testConvertNegative() {
        final String t = TimeHelper.convertSecondsToHumanReadableZeit(-1);
        assertEquals("--:--:--", t);//$NON-NLS-1$
    }

    @Test
    public void testConvert0() {
        final String t = TimeHelper.convertSecondsToHumanReadableZeit(0);
        assertEquals("0:00:00", t);//$NON-NLS-1$
    }
}
