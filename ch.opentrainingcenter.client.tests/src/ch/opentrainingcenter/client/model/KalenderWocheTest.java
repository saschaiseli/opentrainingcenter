package ch.opentrainingcenter.client.model;

import java.util.Calendar;
import java.util.Locale;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class KalenderWocheTest {

    @Test
    public void testOrder() {
        final Calendar cal = Calendar.getInstance(Locale.getDefault());
        cal.set(2012, 0, 4, 13, 22, 59);
        final KalenderWoche kw1 = new KalenderWoche(cal.getTime());

        final Calendar cal2 = Calendar.getInstance(Locale.getDefault());
        cal2.set(2012, 0, 11, 13, 22, 59);
        final KalenderWoche kw2 = new KalenderWoche(cal2.getTime());

        assertEquals("Erstes Element ist älter, daher grösser", 1, kw1.compareTo(kw2));
    }

    @Test
    public void testOrderJahr() {
        final Calendar cal = Calendar.getInstance(Locale.getDefault());
        cal.set(2012, 0, 4, 13, 22, 59);
        final KalenderWoche kw1 = new KalenderWoche(cal.getTime());

        final Calendar cal2 = Calendar.getInstance(Locale.getDefault());
        cal2.set(2013, 0, 5, 13, 22, 59);
        final KalenderWoche kw2 = new KalenderWoche(cal2.getTime());

        assertEquals("Erstes Element ist älter, daher grösser", 1, kw1.compareTo(kw2));
    }
}
