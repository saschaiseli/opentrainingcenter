package ch.opentrainingcenter.client.helper;

import java.util.Calendar;
import java.util.Locale;

import org.joda.time.DateTime;
import org.joda.time.Interval;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

@SuppressWarnings("nls")
public class TimeHelperTest {

    @Test
    public void testConvertSekundenInReadableFormat() {
        final String t = TimeHelper.convertSecondsToHumanReadableZeit(6273.27);
        assertEquals("1:44:33", t);
    }

    @Test
    public void testConvertNegative() {
        final String t = TimeHelper.convertSecondsToHumanReadableZeit(-1);
        assertEquals("--:--:--", t);
    }

    @Test
    public void testConvert0() {
        final String t = TimeHelper.convertSecondsToHumanReadableZeit(0);
        assertEquals("0:00:00", t);
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

    @Test
    public void testIntervallStart() {
        final Interval interval = TimeHelper.getInterval(2012, 44);
        final DateTime start = new DateTime(2012, 10, 29, 0, 0);
        assertDatum(start, interval.getStart());
    }

    @Test
    public void testIntervallEnd() {
        final Interval interval = TimeHelper.getInterval(2012, 44);
        final DateTime end = new DateTime(2012, 11, 4, 0, 0);
        assertDatum(end, interval.getEnd());
    }

    @Test
    public void testIntervall52_2011() {
        final Interval interval = TimeHelper.getInterval(2011, 52);
        final DateTime start = new DateTime(2011, 12, 26, 0, 0);
        final DateTime end = new DateTime(2012, 1, 1, 0, 0);
        assertDatum(start, interval.getStart());
        assertDatum(end, interval.getEnd());
    }

    @Test
    public void testIntervall1_2012() {
        final Interval interval = TimeHelper.getInterval(2012, 1);
        final DateTime start = new DateTime(2012, 1, 2, 0, 0);
        final DateTime end = new DateTime(2012, 1, 8, 0, 0);
        assertDatum(start, interval.getStart());
        assertDatum(end, interval.getEnd());
    }

    @Test
    public void testIntervall52_2012() {
        final Interval interval = TimeHelper.getInterval(2012, 52);
        final DateTime start = new DateTime(2012, 12, 24, 0, 0);
        final DateTime end = new DateTime(2012, 12, 30, 0, 0);
        assertDatum(start, interval.getStart());
        assertDatum(end, interval.getEnd());
    }

    @Test
    public void testIntervall1() {
        final Interval interval = TimeHelper.getInterval(2013, 1);
        final DateTime start = new DateTime(2012, 12, 31, 0, 0);
        final DateTime end = new DateTime(2013, 1, 6, 0, 0);
        assertDatum(start, interval.getStart());
        assertDatum(end, interval.getEnd());
    }

    @Test
    public void testIntervall2() {
        final Interval interval = TimeHelper.getInterval(2013, 2);
        final DateTime start = new DateTime(2013, 1, 7, 0, 0);
        final DateTime end = new DateTime(2013, 1, 13, 0, 0);
        assertDatum(start, interval.getStart());
        assertDatum(end, interval.getEnd());
    }

    private void assertDatum(final DateTime startExpected, final DateTime start) {
        assertEquals(startExpected.getDayOfMonth(), start.getDayOfMonth());
        assertEquals(startExpected.getMonthOfYear(), start.getMonthOfYear());
        assertEquals(startExpected.getYear(), start.getYear());
    }
}
