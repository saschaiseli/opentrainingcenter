package ch.opentrainingcenter.client.helper;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

@SuppressWarnings("nls")
public class SpeedCalculatorTest {
    private static final double DELTA = 0.001;

    @Test
    public void calculateSpeedTestNaN() {
        final double calculateSpeedMpS = SpeedCalculator.calculatePace(0, 0, 0, 0);
        assertEquals("Es kann nichts berechnet werden", -1, calculateSpeedMpS, DELTA);
    }

    @Test
    public void calculatePace() {
        final double calculateSpeedPace = SpeedCalculator.calculatePace(0, 1000, 0, 30);
        assertEquals(0.3, calculateSpeedPace, DELTA);
    }

    @Test
    public void calculateSpeedTest() {
        final double calculateSpeedMpS = SpeedCalculator.calculateSpeedMpS(100, 280, 10, 18);
        assertEquals(22.5, calculateSpeedMpS, DELTA);
    }

    @Test
    public void calculateSpeedTestPace() {
        final double calculateSpeedPace = SpeedCalculator.calculatePace(100, 280, 10, 18);
        assertEquals(0.44, calculateSpeedPace, DELTA);
    }

    @Test
    public void calculateSpeedTestPace2() {
        final double calculateSpeedPace = SpeedCalculator.calculatePace(1000, 2000, 0, 315);
        assertEquals(5.15, calculateSpeedPace, DELTA);
    }

    @Test
    public void calculateSpeedTestPace3() {
        final double calculateSpeedPace = SpeedCalculator.calculatePace(2000, 3000, 0, 325);
        assertEquals(5.25, calculateSpeedPace, DELTA);
    }
}
