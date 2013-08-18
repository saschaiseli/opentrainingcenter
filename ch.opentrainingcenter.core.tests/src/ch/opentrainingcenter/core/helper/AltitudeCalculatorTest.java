package ch.opentrainingcenter.core.helper;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import ch.opentrainingcenter.core.helper.AltitudeCalculator.Ascending;
import ch.opentrainingcenter.transfer.ITrackPointProperty;

@SuppressWarnings("nls")
public class AltitudeCalculatorTest {
    private List<ITrackPointProperty> trackpoints;

    @Before
    public void setUp() {
        trackpoints = new ArrayList<>();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCalculateAscendingNull() {
        AltitudeCalculator.calculateAscending(null);
    }

    @Test
    public void testCalculateAscendingEmpty() {
        final Ascending result = AltitudeCalculator.calculateAscending(trackpoints);
        assertEquals(new Ascending(0, 0), result);
    }

    @Test
    public void testCalculateAscendingOneElement() {
        final ITrackPointProperty point = mock(ITrackPointProperty.class);
        when(point.getAltitude()).thenReturn(42);
        trackpoints.add(point);
        final Ascending result = AltitudeCalculator.calculateAscending(trackpoints);
        assertEquals(new Ascending(0, 0), result);
    }

    @Test
    public void testCalculateAscendingTwoElements() {
        final ITrackPointProperty pointA = mock(ITrackPointProperty.class);
        when(pointA.getAltitude()).thenReturn(42);

        final ITrackPointProperty pointB = mock(ITrackPointProperty.class);
        when(pointB.getAltitude()).thenReturn(41);

        trackpoints.add(pointA);
        trackpoints.add(pointB);
        final Ascending result = AltitudeCalculator.calculateAscending(trackpoints);
        assertEquals("Eins runter", new Ascending(0, 1), result);
    }

    @Test
    public void testCalculateAscendingMoreElements_1() {
        final ITrackPointProperty pointA = mock(ITrackPointProperty.class);
        when(pointA.getAltitude()).thenReturn(42);

        final ITrackPointProperty pointB = mock(ITrackPointProperty.class);
        when(pointB.getAltitude()).thenReturn(41);

        final ITrackPointProperty pointC = mock(ITrackPointProperty.class);
        when(pointC.getAltitude()).thenReturn(44);

        trackpoints.add(pointA);
        trackpoints.add(pointB);
        trackpoints.add(pointC);
        final Ascending result = AltitudeCalculator.calculateAscending(trackpoints);
        assertEquals(new Ascending(3, 1), result);
    }

    @Test
    public void testCalculateAscendingMoreElements_2() {
        trackpoints.add(mockTrackPoint(42));
        trackpoints.add(mockTrackPoint(41));
        trackpoints.add(mockTrackPoint(44));
        trackpoints.add(mockTrackPoint(50));
        trackpoints.add(mockTrackPoint(64));
        trackpoints.add(mockTrackPoint(44));
        final Ascending result = AltitudeCalculator.calculateAscending(trackpoints);
        assertEquals(new Ascending(23, 21), result);
    }

    private ITrackPointProperty mockTrackPoint(final int altitude) {
        final ITrackPointProperty point = mock(ITrackPointProperty.class);
        when(point.getAltitude()).thenReturn(altitude);
        return point;
    }
}
