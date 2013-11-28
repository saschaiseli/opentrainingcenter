package ch.opentrainingcenter.importer.gpx;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

public class HeartRateHandlerTest {
    private HeartRateHandler handler;

    @Before
    public void setUp() {
        handler = new HeartRateHandler();
    }

    @Test
    public void testMaxOhneHerz() {
        final int max = handler.getMax();
        assertEquals("Maximaler Puls wenn nichts erfasst ist 0", 0, max); //$NON-NLS-1$
    }

    @Test
    public void testAvgOhneHerz() {
        final int avg = handler.getAverage();
        assertEquals("Durchschnittlicher Puls wenn nichts erfasst ist 0", 0, avg); //$NON-NLS-1$
    }

    @Test
    public void testMaxMitHerz() {
        handler.getHearts().add(Integer.valueOf(100));
        handler.getHearts().add(Integer.valueOf(150));

        final int max = handler.getMax();

        assertEquals(150, max);
    }

    @Test
    public void testAvgMitHerz() {
        handler.getHearts().add(Integer.valueOf(100));
        handler.getHearts().add(Integer.valueOf(150));

        final int avg = handler.getAverage();

        assertEquals(125, avg);
    }

}
