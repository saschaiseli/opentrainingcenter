package ch.opentrainingcenter.client.helper;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import ch.opentrainingcenter.client.helper.TimeHelper;

public class TimeHelperTest {

    @Test
    public void testConvertSekundenInReadableFormat() {
        final String t = TimeHelper.convertSecondsToHumanReadableZeit(6273.27);
        assertEquals("1:44:33", t);//$NON-NLS-1$
    }
}
