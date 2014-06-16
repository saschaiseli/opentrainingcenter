package ch.opentrainingcenter.core.lapinfo;

import java.util.ArrayList;
import java.util.List;

import static ch.opentrainingcenter.core.lapinfo.TrackPointSupport.assertLapInfo;
import static ch.opentrainingcenter.core.lapinfo.TrackPointSupport.createTrackPoint;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import ch.opentrainingcenter.transfer.ILapInfo;
import ch.opentrainingcenter.transfer.ITrackPointProperty;
import ch.opentrainingcenter.transfer.ITraining;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SuppressWarnings("nls")
public class LapInfoCreatorTest {

    private static final int HEART_BEAT = 120;
    /**
     * 1000m
     */
    private static final int EIN_KILOMETER_IN_METER = 1000;
    /**
     * 315 Sekunden --> 5 Minuten 15 Sekunden
     */
    private static final long FUENF_FUENFZENHN_IN_MILLIS = 315_000L;

    private static final long VIER_MINUTEN_IN_MILLIS = 240_000L;

    LapInfoCreator creator;
    private ITraining training;
    private List<ITrackPointProperty> trackPoints;

    @Before
    public void setUp() {
        training = mock(ITraining.class);
        trackPoints = new ArrayList<>();
        when(training.getTrackPoints()).thenReturn(trackPoints);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructor() {
        new LapInfoCreator(0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructor_negativ() {
        new LapInfoCreator(-0.1);
    }

    @Test
    public void testNoTrackPoints() {
        creator = new LapInfoCreator(42d);
        final List<ILapInfo> result = creator.createLapInfos(training);

        assertTrue(result.isEmpty());
    }

    /**
     * <pre>
     *               1001m
     * |--------------|---  Intervall
     * ------------*------  Punkte
     * </pre>
     */
    @Test
    public void test_One_Element_kleiner() {
        creator = new LapInfoCreator(1001d);
        trackPoints.add(createTrackPoint(EIN_KILOMETER_IN_METER, HEART_BEAT, FUENF_FUENFZENHN_IN_MILLIS));

        final List<ILapInfo> lapInfos = creator.createLapInfos(training);

        final ILapInfo lapInfo = lapInfos.get(0);

        assertLapInfo(0, 0, EIN_KILOMETER_IN_METER, HEART_BEAT, FUENF_FUENFZENHN_IN_MILLIS, "5:15", lapInfo);
    }

    /**
     * <pre>
     *               999m            
     * |--------------|---- Intervall
     * ----------------*--  Punkte
     * </pre>
     */
    @Test
    public void test_One_Element_groesser() {
        creator = new LapInfoCreator(999d);
        trackPoints.add(createTrackPoint(EIN_KILOMETER_IN_METER, HEART_BEAT, FUENF_FUENFZENHN_IN_MILLIS));

        final List<ILapInfo> lapInfos = creator.createLapInfos(training);

        final ILapInfo lapInfo = lapInfos.get(1);

        assertLapInfo(1, 0, EIN_KILOMETER_IN_METER, HEART_BEAT, FUENF_FUENFZENHN_IN_MILLIS, "5:15", lapInfo);
    }

    /**
     * <pre>
     *               999m              1998m
     * |--------------|------------------|----  Intervall
     * ----------------*------------------*---  Punkte
     * 
     * Erwartet:
     * 
     * </pre>
     */
    @Test
    public void test_Zwei_Element() {
        creator = new LapInfoCreator(999d);
        trackPoints.add(createTrackPoint(EIN_KILOMETER_IN_METER, HEART_BEAT, FUENF_FUENFZENHN_IN_MILLIS));
        trackPoints.add(createTrackPoint(2 * EIN_KILOMETER_IN_METER, HEART_BEAT + 20, FUENF_FUENFZENHN_IN_MILLIS + VIER_MINUTEN_IN_MILLIS));

        final List<ILapInfo> lapInfos = creator.createLapInfos(training);

        final ILapInfo lapInfo0 = lapInfos.get(1);
        final ILapInfo lapInfo1 = lapInfos.get(2);

        assertLapInfo(1, 0, EIN_KILOMETER_IN_METER, HEART_BEAT, FUENF_FUENFZENHN_IN_MILLIS, "5:15", lapInfo0);
        assertLapInfo(2, EIN_KILOMETER_IN_METER, 2 * EIN_KILOMETER_IN_METER, HEART_BEAT + 20, VIER_MINUTEN_IN_MILLIS, "4:00", lapInfo1);
    }

    /**
     * <pre>
     *               1000m              2000m
     * |--------------|------------------|----  Intervall
     * ---*-*-*-*-*-**---*-*-*-*-*-*-*-**-----  Punkte
     * 
     * </pre>
     */
    @Test
    public void test_Viele_Elemente() {
        creator = new LapInfoCreator(1000d);
        trackPoints.add(createTrackPoint(0, HEART_BEAT, 0));
        trackPoints.add(createTrackPoint(EIN_KILOMETER_IN_METER / 2, HEART_BEAT, 2 * 60_000));
        trackPoints.add(createTrackPoint((EIN_KILOMETER_IN_METER / 4) * 3, HEART_BEAT, 3 * 60_000));
        trackPoints.add(createTrackPoint(EIN_KILOMETER_IN_METER - 1, HEART_BEAT, VIER_MINUTEN_IN_MILLIS));
        //
        trackPoints.add(createTrackPoint(EIN_KILOMETER_IN_METER, HEART_BEAT, VIER_MINUTEN_IN_MILLIS + 75_000));
        trackPoints.add(createTrackPoint(EIN_KILOMETER_IN_METER + (EIN_KILOMETER_IN_METER / 2), HEART_BEAT, VIER_MINUTEN_IN_MILLIS + 2 * 75_000));
        trackPoints.add(createTrackPoint(EIN_KILOMETER_IN_METER + ((EIN_KILOMETER_IN_METER / 4) * 3), HEART_BEAT, VIER_MINUTEN_IN_MILLIS + 375_000));
        trackPoints.add(createTrackPoint(EIN_KILOMETER_IN_METER + (EIN_KILOMETER_IN_METER - 1), HEART_BEAT, VIER_MINUTEN_IN_MILLIS + 4 * 75_000));

        final List<ILapInfo> lapInfos = creator.createLapInfos(training);

        final ILapInfo lapInfo0 = lapInfos.get(0);
        final ILapInfo lapInfo1 = lapInfos.get(1);

        assertLapInfo(0, 0, EIN_KILOMETER_IN_METER - 1, HEART_BEAT, VIER_MINUTEN_IN_MILLIS, "4:00", lapInfo0);
        assertLapInfo(1, EIN_KILOMETER_IN_METER - 1, EIN_KILOMETER_IN_METER - 1 + EIN_KILOMETER_IN_METER, HEART_BEAT, VIER_MINUTEN_IN_MILLIS + 60_000, "5:00",
                lapInfo1);
    }
}
