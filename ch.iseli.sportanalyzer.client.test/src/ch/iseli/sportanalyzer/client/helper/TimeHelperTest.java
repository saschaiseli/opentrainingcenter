package ch.iseli.sportanalyzer.client.helper;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class TimeHelperTest {

    @Test
    public void testConvertSekundenInReadableFormat() {
        final String t = TimeHelper.convertSecondsToHumanReadableZeit(6273.27);
        assertEquals("1:44:33", t);//$NON-NLS-1$
    }
}
