package ch.opentrainingcenter.charts.bar.internal;

import org.junit.Test;

import ch.opentrainingcenter.charts.bar.internal.Cross;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@SuppressWarnings("nls")
public class CrossTest {
    @Test
    public void testNotNull() {
        final Cross cross = Cross.createCross();
        assertNotNull(cross);
    }

    @Test
    public void testXPoints() {
        final Cross cross = Cross.createCross();
        final int[] xpoints = cross.xpoints;
        assertEquals("8 Punkte um Cross zu definieren", 8, xpoints.length);
        assertEquals(0, xpoints[0]);
        assertEquals(2, xpoints[1]);
        assertEquals(0, xpoints[2]);
        assertEquals(-2, xpoints[3]);
        assertEquals(0, xpoints[4]);
        assertEquals(-2, xpoints[5]);
        assertEquals(0, xpoints[6]);
        assertEquals(2, xpoints[7]);
    }

    @Test
    public void testYPoints() {
        final Cross cross = Cross.createCross();
        final int[] ypoints = cross.ypoints;
        assertEquals("8 Punkte um Cross zu definieren", 8, ypoints.length);
        assertEquals(0, ypoints[0]);
        assertEquals(2, ypoints[1]);
        assertEquals(0, ypoints[2]);
        assertEquals(2, ypoints[3]);
        assertEquals(0, ypoints[4]);
        assertEquals(-2, ypoints[5]);
        assertEquals(0, ypoints[6]);
        assertEquals(-2, ypoints[7]);
    }
}
