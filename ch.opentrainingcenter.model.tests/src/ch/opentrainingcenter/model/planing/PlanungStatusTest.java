package ch.opentrainingcenter.model.planing;

import org.junit.Test;

import ch.opentrainingcenter.core.PreferenceConstants;
import static org.junit.Assert.assertEquals;

@SuppressWarnings("nls")
public class PlanungStatusTest {

    @Test
    public void testErfolgreich() {
        assertEquals("Ziel erfüllt", PreferenceConstants.ZIEL_ERFUELLT_COLOR, PlanungStatus.ERFOLGREICH.getColorPreference());
    }

    @Test
    public void testNichtErfolgreich() {
        assertEquals("Ziel nicht erfüllt", PreferenceConstants.ZIEL_NICHT_ERFUELLT_COLOR, PlanungStatus.NICHT_ERFOLGREICH.getColorPreference());
    }

    @Test
    public void testUnbekannt() {
        assertEquals("Ziel unbekannt", PreferenceConstants.ZIEL_NICHT_BEKANNT_COLOR, PlanungStatus.UNBEKANNT.getColorPreference());
    }
}
