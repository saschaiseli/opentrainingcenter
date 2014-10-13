package ch.opentrainingcenter.model.training.internal;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.util.Locale;
import java.util.TimeZone;

import org.apache.log4j.Logger;
import org.joda.time.DateTimeZone;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import ch.opentrainingcenter.core.data.Pair;
import ch.opentrainingcenter.model.training.GoldMedalTyp;
import ch.opentrainingcenter.model.training.Intervall;

@SuppressWarnings("nls")
public class GoldMedalModelTest {

    private final Logger LOGGER = Logger.getLogger(GoldMedalModelTest.class);

    GoldMedalModel model;

    @Before
    public void before() {
        Locale.setDefault(Locale.GERMAN);
        TimeZone.setDefault(TimeZone.getTimeZone("GMT+1"));

        final DateTimeZone zoneUTC = DateTimeZone.forID("Europe/Berlin");
        DateTimeZone.setDefault(zoneUTC);

        model = new GoldMedalModel();
    }

    @After
    public void after() {
        model = null;
    }

    @Test
    public void testSchnellstePaceNullKey() {
        final Pair<Long, String> pace = new Pair<Long, String>(null, null);
        model.setRecord(GoldMedalTyp.SCHNELLSTE_PACE, pace);
        final Pair<Long, String> result = model.getRecord(GoldMedalTyp.SCHNELLSTE_PACE);
        assertEmptyPair(result);
    }

    @Test
    public void testSchnellstePaceNullValue() {
        final Pair<Long, String> pace = new Pair<Long, String>(1L, null);
        model.setRecord(GoldMedalTyp.SCHNELLSTE_PACE, pace);
        final Pair<Long, String> result = model.getRecord(GoldMedalTyp.SCHNELLSTE_PACE);
        assertEmptyPair(result);
    }

    @Test
    public void testSchnellstePaceValue() {
        final String value = "123";
        final Pair<Long, String> pace = new Pair<Long, String>(1L, value);
        model.setRecord(GoldMedalTyp.SCHNELLSTE_PACE, pace);
        final String result = model.getRecord(GoldMedalTyp.SCHNELLSTE_PACE).getSecond();
        assertEquals("Wert (second)", value, result);
    }

    @Test
    public void testGetSchnellstePaceIntervallValueNotFound() {
        final GoldMedalModel emptyModel = new GoldMedalModel();
        for (final Intervall intervall : Intervall.values()) {
            final Pair<Long, String> result = emptyModel.getSchnellstePace(intervall);
            LOGGER.warn("empty?: " + result);
            assertEmptyPair(result);
        }
    }

    @Test
    public void testGetSchnellstePaceIntervallValueFound() {
        final String value = "123";
        final Pair<Long, String> schnellstePace = new Pair<>(1L, value);
        model.setSchnellstePace(Intervall.KLEINER_10, schnellstePace);
        final Pair<Long, String> result = model.getSchnellstePace(Intervall.KLEINER_10);
        assertEquals("Key vorhanden", 1L, result.getFirst(), 0.00001);
        assertEquals("Wert vorhanden", "123", result.getSecond());
    }

    @Test
    public void testLongestDistanceNullKey() {
        model.setRecord(GoldMedalTyp.LAENGSTE_DISTANZ, new Pair<Long, String>(null, null));
        assertEmptyPair(model.getRecord(GoldMedalTyp.LAENGSTE_DISTANZ));
    }

    @Test
    public void testLongestDistanceNullValue() {
        model.setRecord(GoldMedalTyp.LAENGSTE_DISTANZ, new Pair<Long, String>(1L, null));
        assertEmptyPair(model.getRecord(GoldMedalTyp.LAENGSTE_DISTANZ));
    }

    @Test
    public void testLongestDistanceValue() {
        final String value = "123";
        model.setRecord(GoldMedalTyp.LAENGSTE_DISTANZ, new Pair<Long, String>(1L, value));
        final String result = model.getRecord(GoldMedalTyp.LAENGSTE_DISTANZ).getSecond();
        assertEquals("Wert vorhanden", value, result);
    }

    @Test
    public void testLongestRunNullKey() {
        model.setRecord(GoldMedalTyp.LAENGSTER_LAUF, new Pair<Long, String>(null, null));
        assertEmptyPair(model.getRecord(GoldMedalTyp.LAENGSTER_LAUF));
    }

    @Test
    public void testLongestRunNullValue() {
        model.setRecord(GoldMedalTyp.LAENGSTER_LAUF, new Pair<Long, String>(1L, null));
        assertEmptyPair(model.getRecord(GoldMedalTyp.LAENGSTER_LAUF));
    }

    @Test
    public void testLongestRunValue() {
        final String value = "123";
        model.setRecord(GoldMedalTyp.LAENGSTER_LAUF, new Pair<Long, String>(1L, value));
        final String result = model.getRecord(GoldMedalTyp.LAENGSTER_LAUF).getSecond();
        assertEquals("Wert vorhanden", value, result);
    }

    // Pulse
    @Test
    public void testHighestPulsKeyNull() {
        model.setRecord(GoldMedalTyp.HOECHSTER_PULS, new Pair<Long, String>(null, null));
        assertEmptyPair(model.getRecord(GoldMedalTyp.HOECHSTER_PULS));
    }

    @Test
    public void testHighestPulsNullValue() {
        model.setRecord(GoldMedalTyp.HOECHSTER_PULS, new Pair<Long, String>(1L, null));
        assertEmptyPair(model.getRecord(GoldMedalTyp.HOECHSTER_PULS));
    }

    @Test
    public void testHighestPulsValue() {
        final String value = "123";
        model.setRecord(GoldMedalTyp.HOECHSTER_PULS, new Pair<Long, String>(1L, value));
        final String result = model.getRecord(GoldMedalTyp.HOECHSTER_PULS).getSecond();
        assertEquals("Wert vorhanden", value, result);
    }

    // average pulse
    @Test
    public void testHighestAveragePulsKeyNull() {
        model.setRecord(GoldMedalTyp.HOECHSTER_AVERAGE_PULS, new Pair<Long, String>(null, null));
        assertEmptyPair(model.getRecord(GoldMedalTyp.HOECHSTER_AVERAGE_PULS));
    }

    @Test
    public void testHighestAveragePulsNullValue() {
        model.setRecord(GoldMedalTyp.HOECHSTER_AVERAGE_PULS, new Pair<Long, String>(1L, null));
        assertEmptyPair(model.getRecord(GoldMedalTyp.HOECHSTER_AVERAGE_PULS));
    }

    @Test
    public void testHighestAveragePulsValue() {
        final String value = "123";
        model.setRecord(GoldMedalTyp.HOECHSTER_AVERAGE_PULS, new Pair<Long, String>(1L, value));
        final String result = model.getRecord(GoldMedalTyp.HOECHSTER_AVERAGE_PULS).getSecond();
        assertEquals("Wert vorhanden", value, result);
    }

    // lowest puls
    @Test
    public void testLowestAveragePulsKeyNull() {
        model.setRecord(GoldMedalTyp.TIEFSTER_AVERAGE_PULS, new Pair<Long, String>(null, null));
        assertEmptyPair(model.getRecord(GoldMedalTyp.TIEFSTER_AVERAGE_PULS));
    }

    @Test
    public void testLowestAveragePulsNullValue() {
        model.setRecord(GoldMedalTyp.TIEFSTER_AVERAGE_PULS, new Pair<Long, String>(1L, null));
        assertEmptyPair(model.getRecord(GoldMedalTyp.TIEFSTER_AVERAGE_PULS));
    }

    @Test
    public void testLowestAveragePulsValue() {
        final String value = "123";
        model.setRecord(GoldMedalTyp.TIEFSTER_AVERAGE_PULS, new Pair<Long, String>(1L, value));
        final String result = model.getRecord(GoldMedalTyp.TIEFSTER_AVERAGE_PULS).getSecond();
        assertEquals("Wert vorhanden", value, result);
    }

    //
    // @Test
    // public void testHasNewRecord_newModelEmpty() {
    // model.setHighestAveragePulse(new Pair<>(100L, "188"));
    // model.setHighestPulse(new Pair<>(101L, "210"));
    // model.setLongestDistance(new Pair<>(102L, "1000"));
    // model.setLongestRun(new Pair<>(103L, "12345"));
    // model.setLowestAveragePulse(new Pair<>(104L, "60"));
    // model.setRecord(GoldMedalTyp.SCHNELLSTE_PACE, new Pair<>(105L, "3"));
    // model.setRecord(GoldMedalTyp.SCHNELLSTE_PACE, Intervall.KLEINER_10, new
    // Pair<>(106L, "4"));
    //
    // final GoldMedalModel newModel = new GoldMedalModel();
    //
    // assertFalse("Neues Model hat noch keine Werte",
    // model.hasNewRecord(newModel));
    // }
    //
    // @Test
    // public void testHasNewRecord_newModelGleich() {
    // model.setHighestAveragePulse(new Pair<>(100L, "188"));
    // model.setHighestPulse(new Pair<>(101L, "210"));
    // model.setLongestDistance(new Pair<>(102L, "1000"));
    // model.setLongestRun(new Pair<>(103L, "12345"));
    // model.setLowestAveragePulse(new Pair<>(104L, "60"));
    // model.setRecord(GoldMedalTyp.SCHNELLSTE_PACE, new Pair<>(105L, "3"));
    // model.setSchnellstePace(GoldMedalTyp.SCHNELLSTE_PACE,
    // Intervall.KLEINER_10, new Pair<>(106L, "4"));
    //
    // final GoldMedalModel newModel = new GoldMedalModel();
    // newModel.setHighestAveragePulse(new Pair<>(100L, "188"));
    // newModel.setHighestPulse(new Pair<>(101L, "210"));
    // newModel.setLongestDistance(new Pair<>(102L, "1000"));
    // newModel.setLongestRun(new Pair<>(103L, "12345"));
    // newModel.setLowestAveragePulse(new Pair<>(104L, "60"));
    // newModel.setRecord(GoldMedalTyp.SCHNELLSTE_PACE, new Pair<>(105L, "3"));
    // newModel.setSchnellstePace(Intervall.KLEINER_10, new Pair<>(106L, "4"));
    //
    // assertFalse("Neues Model gleich wie alt", model.hasNewRecord(newModel));
    // }
    //
    // @Test
    // public void testHasNewRecord_newModelNichtGleich_HighestAveragePuls() {
    // model.setHighestAveragePulse(new Pair<>(100L, "188"));
    // model.setHighestPulse(new Pair<>(101L, "210"));
    // model.setLongestDistance(new Pair<>(102L, "1000"));
    // model.setLongestRun(new Pair<>(103L, "12345"));
    // model.setLowestAveragePulse(new Pair<>(104L, "60"));
    // model.setRecord(GoldMedalTyp.SCHNELLSTE_PACE, new Pair<>(105L, "3"));
    // model.setSchnellstePace(Intervall.KLEINER_10, new Pair<>(106L, "4"));
    //
    // final GoldMedalModel newModel = new GoldMedalModel();
    // newModel.setHighestAveragePulse(new Pair<>(100L, "189")); //
    // <------------------
    // newModel.setHighestPulse(new Pair<>(101L, "210"));
    // newModel.setLongestDistance(new Pair<>(102L, "1000"));
    // newModel.setLongestRun(new Pair<>(103L, "12345"));
    // newModel.setLowestAveragePulse(new Pair<>(104L, "60"));
    // newModel.setRecord(GoldMedalTyp.SCHNELLSTE_PACE, new Pair<>(105L, "3"));
    // newModel.setSchnellstePace(Intervall.KLEINER_10, new Pair<>(106L, "4"));
    //
    // assertFalse("Neues Model gleich wie alt", model.hasNewRecord(newModel));
    // }

    private void assertEmptyPair(final Pair<Long, String> result) {
        assertNull("Key vom EmptyPair", result.getFirst());
        assertEquals("Default value", "-", result.getSecond());
    }
}
