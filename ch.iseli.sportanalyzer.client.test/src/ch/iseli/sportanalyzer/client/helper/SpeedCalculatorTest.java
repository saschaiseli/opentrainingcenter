package ch.iseli.sportanalyzer.client.helper;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class SpeedCalculatorTest {
    private static final double DELTA = 0.001;

    @Test
    public void calculateSpeedTest() {
        double calculateSpeedMpS = SpeedCalculator.calculateSpeedMpS(100, 280, 10, 18);
        assertEquals(22.5, calculateSpeedMpS, DELTA);
    }

    @Test
    public void calculateSpeedTestPace() {
        double calculateSpeedPace = SpeedCalculator.calculatePace(100, 280, 10, 18);
        assertEquals(0.44, calculateSpeedPace, DELTA);
    }

    @Test
    public void calculateSpeedTestPace2() {
        double calculateSpeedPace = SpeedCalculator.calculatePace(1000, 2000, 0, 315);
        assertEquals(5.15, calculateSpeedPace, DELTA);
    }

    @Test
    public void calculateSpeedTestPace3() {
        double calculateSpeedPace = SpeedCalculator.calculatePace(2000, 3000, 0, 325);
        assertEquals(5.25, calculateSpeedPace, DELTA);
    }
}
