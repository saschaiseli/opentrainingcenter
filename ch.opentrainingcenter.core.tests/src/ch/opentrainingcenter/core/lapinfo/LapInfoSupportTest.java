package ch.opentrainingcenter.core.lapinfo;

import static ch.opentrainingcenter.core.lapinfo.TrackPointSupport.assertLapInfo;
import static ch.opentrainingcenter.core.lapinfo.TrackPointSupport.createTrackPoint;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import ch.opentrainingcenter.transfer.ILapInfo;
import ch.opentrainingcenter.transfer.ITrackPointProperty;
import ch.opentrainingcenter.transfer.Sport;

@SuppressWarnings("nls")
public class LapInfoSupportTest {

    private List<ITrackPointProperty> points;

    @Before
    public void setUp() {
        points = new ArrayList<>();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNull() {
        LapInfoSupport.createLapInfo(null, Sport.RUNNING);
    }

    @SuppressWarnings("unchecked")
    @Test(expected = IllegalArgumentException.class)
    public void testLeer() {
        LapInfoSupport.createLapInfo(Collections.EMPTY_LIST, Sport.RUNNING);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testEinPoint() {
        points.add(createTrackPoint(1000, 142, 240_000L));

        LapInfoSupport.createLapInfo(points, Sport.RUNNING);
    }

    @Test
    public void testZweiPoint() {
        points.add(createTrackPoint(1000, 142, 240_000L));
        points.add(createTrackPoint(2000, 162, 480_000L));

        final ILapInfo result = LapInfoSupport.createLapInfo(points, Sport.RUNNING);

        assertLapInfo(1, 1000, 2000, 152, 240_000, "4:00", result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void test_mit_neg_initPosition() {
        points.add(createTrackPoint(1000, 142, 240_000L));

        LapInfoSupport.createLapInfo(0, points, -3d, 0, Sport.RUNNING);
    }

    @Test(expected = IllegalArgumentException.class)
    public void test_mit_neg_initTime() {
        points.add(createTrackPoint(1000, 142, 240_000L));

        LapInfoSupport.createLapInfo(0, points, 0, -3, Sport.RUNNING);
    }

    @Test
    public void testEinPoint_mit_init() {
        points.add(createTrackPoint(1000, 142, 240_000L));

        final ILapInfo result = LapInfoSupport.createLapInfo(0, points, 0, 0, Sport.RUNNING);

        assertLapInfo(0, 0, 1000, 142, 240_000, "4:00", result);
    }
}
