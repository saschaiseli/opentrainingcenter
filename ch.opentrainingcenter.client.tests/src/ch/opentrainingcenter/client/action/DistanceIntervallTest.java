package ch.opentrainingcenter.client.action;

import org.junit.Before;
import org.junit.Test;

import ch.opentrainingcenter.model.training.Intervall;
import static org.junit.Assert.assertEquals;

@SuppressWarnings("nls")
public class DistanceIntervallTest {
    private static final double DEFAULT = -1d;
    private DistanceIntervall intervall;

    @Before
    public void before() {
        intervall = new DistanceIntervall();
    }

    @Test
    public void testGetMaxDefault() {
        for (final Intervall inter : Intervall.values()) {
            assertEquals("Default wert ist " + DEFAULT, DEFAULT, intervall.getMax(inter), 0.001);
        }
    }

    @Test
    public void test() {
        setAndTest(5.42, Intervall.KLEINER_10);
        setAndTest(10.42, Intervall.VON10_BIS_15);
        setAndTest(15.42, Intervall.VON15_BIS_20);
        setAndTest(20.42, Intervall.VON20_BIS_25);
        setAndTest(25.42, Intervall.PLUS25);
    }

    private void setAndTest(final double pace, final Intervall i) {
        intervall.addPace(pace, i.getVon() * 1000 + 1);
        for (final Intervall inter : Intervall.values()) {
            if (i.equals(inter)) {
                assertEquals("Wert ist gesetzt ", pace, intervall.getMax(i), 0.001);
            }
        }
    }
}
