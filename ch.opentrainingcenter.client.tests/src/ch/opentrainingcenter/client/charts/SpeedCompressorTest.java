package ch.opentrainingcenter.client.charts;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class SpeedCompressorTest {

    private SpeedCompressor compressor;

    @Before
    public void before() {
        compressor = new SpeedCompressor(2);
    }

    @Test
    public void notNullTest() {
        assertNotNull("Nicht null zurückgeben", compressor.compressSpeedDataPoints(null));
    }

    @Test
    public void notNullTestBeiLeererListe() {
        assertNotNull("Nicht null zurückgeben", compressor.compressSpeedDataPoints(new ArrayList<PositionPace>()));
    }

    @Test
    public void einWert() {
        final List<PositionPace> values = new ArrayList<PositionPace>();
        final PositionPace positionPace = new PositionPace(1d, 5d);
        values.add(positionPace);

        final List<PositionPace> result = compressor.compressSpeedDataPoints(values);

        assertEquals("Ein Element als Resultat", 1, result.size());
    }

    @Test
    public void einWertTesten() {
        final List<PositionPace> values = new ArrayList<PositionPace>();
        final PositionPace positionPace = new PositionPace(1d, 5d);
        values.add(positionPace);

        final List<PositionPace> result = compressor.compressSpeedDataPoints(values);
        final PositionPace position = result.get(0);
        assertEquals("Distanz 1.0", 1.0, position.getPosition(), 0.001);
        assertEquals("Pace 5.0", 5.0, position.getPace(), 0.001);
    }

    @Test
    public void zweiWerte() {
        final List<PositionPace> values = new ArrayList<PositionPace>();
        final PositionPace positionPaceA = new PositionPace(1d, 5d);
        final PositionPace positionPaceB = new PositionPace(2d, 6d);
        values.add(positionPaceA);
        values.add(positionPaceB);

        final List<PositionPace> result = compressor.compressSpeedDataPoints(values);

        assertEquals("Die Zwei Elemente werden zu einem komprimiert", 1, result.size());
    }

    @Test
    public void zweiWerteTesten() {
        final List<PositionPace> values = new ArrayList<PositionPace>();
        final PositionPace positionPaceA = new PositionPace(1d, 5d);
        final PositionPace positionPaceB = new PositionPace(2d, 6d);
        values.add(positionPaceA);
        values.add(positionPaceB);

        final List<PositionPace> result = compressor.compressSpeedDataPoints(values);
        final PositionPace positionPace = result.get(0);
        assertEquals("Position ist immer die des letzten", 2.0, positionPace.getPosition(), 0.001);
        assertEquals("Position ist immer die des letzten", 5.5, positionPace.getPace(), 0.001);
    }

    @Test
    public void dreiWerte() {
        final List<PositionPace> values = new ArrayList<PositionPace>();
        final PositionPace positionPaceA = new PositionPace(1d, 5d);
        final PositionPace positionPaceB = new PositionPace(2d, 6d);
        final PositionPace positionPaceC = new PositionPace(3d, 7d);

        values.add(positionPaceA);
        values.add(positionPaceB);
        values.add(positionPaceC);

        final List<PositionPace> result = compressor.compressSpeedDataPoints(values);

        assertEquals("Die Zwei Elemente werden zu einem komprimiert", 2, result.size());
    }

    @Test
    public void dreiWerteTesten() {
        final List<PositionPace> values = new ArrayList<PositionPace>();
        final PositionPace positionPaceA = new PositionPace(1d, 5d);
        final PositionPace positionPaceB = new PositionPace(2d, 6d);
        final PositionPace positionPaceC = new PositionPace(3d, 7d);

        values.add(positionPaceA);
        values.add(positionPaceB);
        values.add(positionPaceC);

        final List<PositionPace> result = compressor.compressSpeedDataPoints(values);

        final PositionPace pos1 = result.get(0);
        assertEquals("Position ist immer die des letzten", 2.0, pos1.getPosition(), 0.001);
        assertEquals("Position ist immer die des letzten", 5.5, pos1.getPace(), 0.001);

        final PositionPace pos2 = result.get(1);
        assertEquals("Position ist immer die des letzten", 3.0, pos2.getPosition(), 0.001);
        assertEquals("Position ist immer die des letzten", 7.0, pos2.getPace(), 0.001);
    }

    @Test
    public void vierWerteTesten() {
        final List<PositionPace> values = new ArrayList<PositionPace>();
        final PositionPace positionPaceA = new PositionPace(1d, 5d);
        final PositionPace positionPaceB = new PositionPace(2d, 6d);
        final PositionPace positionPaceC = new PositionPace(3d, 7d);
        final PositionPace positionPaceD = new PositionPace(4d, 6d);

        values.add(positionPaceA);
        values.add(positionPaceB);
        values.add(positionPaceC);
        values.add(positionPaceD);

        final List<PositionPace> result = compressor.compressSpeedDataPoints(values);

        final PositionPace pos1 = result.get(0);
        assertEquals("Position ist immer die des letzten", 2.0, pos1.getPosition(), 0.001);
        assertEquals("Position ist immer die des letzten", 5.5, pos1.getPace(), 0.001);

        final PositionPace pos2 = result.get(1);
        assertEquals("Position ist immer die des letzten", 4.0, pos2.getPosition(), 0.001);
        assertEquals("Position ist immer die des letzten", 6.5, pos2.getPace(), 0.001);
    }
}
