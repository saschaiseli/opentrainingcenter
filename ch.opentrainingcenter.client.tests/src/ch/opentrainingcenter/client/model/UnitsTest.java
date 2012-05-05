package ch.opentrainingcenter.client.model;

import org.junit.Test;

import ch.opentrainingcenter.client.Messages;
import static org.junit.Assert.assertEquals;

public class UnitsTest {
    @Test
    public void testEnumMitLabels() {
        assertEquals("Beats per minute", Messages.Units_0, Units.BEATS_PER_MINUTE.getName());
        assertEquals("Pace", Messages.Units_1, Units.PACE.getName());
        assertEquals("Kilometer", Messages.Units_2, Units.KM.getName());
        assertEquals("Stunden minuten sekunden", Messages.Units_3, Units.HOUR_MINUTE_SEC.getName());
        assertEquals("Nix Einheit", Messages.Units_4, Units.NONE.getName());
    }
}
