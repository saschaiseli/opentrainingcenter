package ch.opentrainingcenter.client.action;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Before;
import org.junit.Test;

import ch.opentrainingcenter.model.training.Intervall;

public class DistanceIntervallTest {

    private DistanceIntervall intervall;

    @Before
    public void before() {
        intervall = new DistanceIntervall();
    }

    @Test
    public void testGetMaxDefault() {
        for (final Intervall inter : Intervall.values()) {
            assertNull(intervall.getMax(inter).getFirst());
            assertNull(intervall.getMax(inter).getSecond());
        }
    }

    @Test
    public void testMax() {
        intervall.addPace(1, 10, 10001);
        intervall.addPace(1, 10.1, 10001);

        assertEquals(10.1, intervall.getMax(Intervall.VON10_BIS_15).getSecond(), 0.001);
    }

    @Test
    public void testAllIntervalls() {
        intervall.addPace(1L, 5.42, 1000 * Intervall.KLEINER_10.getVon() + 1);
        intervall.addPace(1L, 10.42, 1000 * Intervall.VON10_BIS_15.getVon() + 1);
        intervall.addPace(1L, 15.42, 1000 * Intervall.VON15_BIS_20.getVon() + 1);
        intervall.addPace(1L, 20.42, 1000 * Intervall.VON20_BIS_25.getVon() + 1);
        intervall.addPace(1L, 25.42, 1000 * Intervall.PLUS25.getVon() + 1);
        for (final Intervall inter : Intervall.values()) {
            assertNotNull(intervall.getMax(inter).getFirst());
        }
    }
}
