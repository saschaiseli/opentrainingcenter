package ch.opentrainingcenter.client.helper;

import java.util.Calendar;
import java.util.Locale;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

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

    @Test
    public void getKalenderWoche1() {
        final Calendar cal = Calendar.getInstance(Locale.getDefault());
        cal.set(2012, 0, 1);
        final int kw = TimeHelper.getKalenderWoche(cal.getTime());
        assertEquals(52, kw);
    }

    @Test
    public void getKalenderWoche2() {
        final Calendar cal = Calendar.getInstance(Locale.getDefault());
        cal.set(2012, 1, 1);
        final int kw = TimeHelper.getKalenderWoche(cal.getTime());
        assertEquals(5, kw);
    }

    @Test
    public void getKalenderWoche3() {
        final Calendar cal = Calendar.getInstance(Locale.getDefault());
        cal.set(2012, 2, 1);
        final int kw = TimeHelper.getKalenderWoche(cal.getTime());
        assertEquals(9, kw);
    }

    @Test
    public void getKalenderWoche4() {
        final Calendar cal = Calendar.getInstance(Locale.getDefault());
        cal.set(2012, 3, 1);
        final int kw = TimeHelper.getKalenderWoche(cal.getTime());
        assertEquals(13, kw);
    }

    @Test
    public void getKalenderWoche5() {
        final Calendar cal = Calendar.getInstance(Locale.getDefault());
        cal.set(2012, 4, 1);
        final int kw = TimeHelper.getKalenderWoche(cal.getTime());
        assertEquals(18, kw);
    }

    @Test
    public void getKalenderWoche6() {
        final Calendar cal = Calendar.getInstance(Locale.getDefault());
        cal.set(2012, 5, 1);
        final int kw = TimeHelper.getKalenderWoche(cal.getTime());
        assertEquals(22, kw);
    }

    @Test
    public void getKalenderWoche7() {
        final Calendar cal = Calendar.getInstance(Locale.getDefault());
        cal.set(2012, 6, 1);
        final int kw = TimeHelper.getKalenderWoche(cal.getTime());
        assertEquals(26, kw);
    }

    @Test
    public void getKalenderWoche8() {
        final Calendar cal = Calendar.getInstance(Locale.getDefault());
        cal.set(2012, 7, 1);
        final int kw = TimeHelper.getKalenderWoche(cal.getTime());
        assertEquals(31, kw);
    }

    @Test
    public void getKalenderWoche9() {
        final Calendar cal = Calendar.getInstance(Locale.getDefault());
        cal.set(2012, 8, 1);
        final int kw = TimeHelper.getKalenderWoche(cal.getTime());
        assertEquals(35, kw);
    }

    @Test
    public void getKalenderWoche10() {
        final Calendar cal = Calendar.getInstance(Locale.getDefault());
        cal.set(2012, 9, 1);
        final int kw = TimeHelper.getKalenderWoche(cal.getTime());
        assertEquals(40, kw);
    }

    @Test
    public void getKalenderWoche11() {
        final Calendar cal = Calendar.getInstance(Locale.getDefault());
        cal.set(2012, 10, 1);
        final int kw = TimeHelper.getKalenderWoche(cal.getTime());
        assertEquals(44, kw);
    }

    @Test
    public void getKalenderWoche12() {
        final Calendar cal = Calendar.getInstance(Locale.getDefault());
        cal.set(2012, 11, 1);
        final int kw = TimeHelper.getKalenderWoche(cal.getTime());
        assertEquals(48, kw);
    }
}
