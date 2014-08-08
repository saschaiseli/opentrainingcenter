package ch.opentrainingcenter.client.model;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import ch.opentrainingcenter.i18n.Messages;

@SuppressWarnings("nls")
public class UnitsTest {
    @Test
    public void testEnumMitLabels() {
        assertEquals("Beats per minute", Messages.Units0, Units.BEATS_PER_MINUTE.getName());
        assertEquals("Pace", Messages.Units1, Units.PACE.getName());
        assertEquals("Pace", Messages.Units5, Units.GESCHWINDIGKEIT.getName());
        assertEquals("Kilometer", Messages.Units2, Units.KM.getName());
        assertEquals("Stunden minuten sekunden", Messages.Units3, Units.HOUR_MINUTE_SEC.getName());
        assertEquals("Nix Einheit", Messages.Units4, Units.NONE.getName());
    }
}
