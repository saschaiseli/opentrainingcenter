package ch.opentrainingcenter.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import ch.opentrainingcenter.tcx.ActivityLapT;
import ch.opentrainingcenter.tcx.ActivityT;
import ch.opentrainingcenter.tcx.HeartRateInBeatsPerMinuteT;
import ch.opentrainingcenter.tcx.IntensityT;
import ch.opentrainingcenter.transfer.ITraining;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

@SuppressWarnings("nls")
public class TrainingOverviewFactoryTest {
    private static DatatypeFactory df;

    private final Double distanz = Double.valueOf(1042);
    private final Double dauer = Double.valueOf(42);
    private final int avgHeart = 145;
    private final int maxHeart = 189;
    private final double maxSpeed = 3.45d;

    private ActivityT activity;

    @Before
    public void before() throws DatatypeConfigurationException {
        df = DatatypeFactory.newInstance();
        activity = Mockito.mock(ActivityT.class);
    }

    @Test
    public void testNullActivity() {
        assertNull(TrainingOverviewFactory.creatTrainingOverview(null));
    }

    @Test
    public void testSimpleTrainingNullActivity() {
        assertNull(TrainingOverviewFactory.creatSimpleTraining(null, null));
    }

    @Test
    public void testNoLaps() {

        final Date date = new Date();
        Mockito.when(activity.getId()).thenReturn(convert(date));
        Mockito.when(activity.getLap()).thenReturn(new ArrayList<ActivityLapT>());

        final ITraining training = TrainingOverviewFactory.creatTrainingOverview(activity);
        assertNotNull(training);
    }

    @Test
    public void testOneLapsINACTIV() {

        final Date date = new Date();
        Mockito.when(activity.getId()).thenReturn(convert(date));
        final List<ActivityLapT> laps = new ArrayList<ActivityLapT>();
        final ActivityLapT actLap = Mockito.mock(ActivityLapT.class);
        Mockito.when(actLap.getIntensity()).thenReturn(IntensityT.RESTING);
        laps.add(actLap);
        Mockito.when(activity.getLap()).thenReturn(laps);

        final ITraining training = TrainingOverviewFactory.creatTrainingOverview(activity);
        assertNotNull(training);
    }

    @Test
    public void testOneLapsACTIV() {

        final Date date = new Date();
        Mockito.when(activity.getId()).thenReturn(convert(date));
        final List<ActivityLapT> laps = new ArrayList<ActivityLapT>();
        final ActivityLapT lap = createActivityLap(maxSpeed, true, (short) maxHeart);
        laps.add(lap);
        Mockito.when(activity.getLap()).thenReturn(laps);

        final ITraining training = TrainingOverviewFactory.creatTrainingOverview(activity);
        assertTraining(training);
    }

    @Test
    public void testTwoLapsACTIVEineOhneCardio() {
        final Date date = new Date();
        Mockito.when(activity.getId()).thenReturn(convert(date));
        final List<ActivityLapT> laps = new ArrayList<ActivityLapT>();
        laps.add(createActivityLap(12d, true, (short) maxHeart));
        laps.add(createActivityLap(12d, false, (short) maxHeart));
        Mockito.when(activity.getLap()).thenReturn(laps);

        final ITraining training = TrainingOverviewFactory.creatTrainingOverview(activity);
        assertEquals("Totale Dauer: ", 2 * dauer, training.getDauerInSekunden(), 0.001);
        assertEquals("max Herzrfequenz:", maxHeart, training.getMaxHeartBeat());
    }

    @Test
    public void testTwoLapsACTIVE() {
        final Date date = new Date();
        Mockito.when(activity.getId()).thenReturn(convert(date));
        final List<ActivityLapT> laps = new ArrayList<ActivityLapT>();
        laps.add(createActivityLap(12d, true, (short) (maxHeart + 10)));
        laps.add(createActivityLap(12d, true, (short) maxHeart));
        Mockito.when(activity.getLap()).thenReturn(laps);

        final ITraining training = TrainingOverviewFactory.creatTrainingOverview(activity);
        assertEquals("Totale Dauer: ", 2 * dauer, training.getDauerInSekunden(), 0.001);
        assertEquals("max Herzrfequenz:", maxHeart + 10, training.getMaxHeartBeat());
    }

    @Test
    public void testTwoLapsACTIVEMaxSpeedNull() {
        final Date date = new Date();
        Mockito.when(activity.getId()).thenReturn(convert(date));
        final List<ActivityLapT> laps = new ArrayList<ActivityLapT>();
        laps.add(createActivityLap(null, true, (short) maxHeart));
        laps.add(createActivityLap(12d, true, (short) maxHeart));
        Mockito.when(activity.getLap()).thenReturn(laps);

        final ITraining training = TrainingOverviewFactory.creatTrainingOverview(activity);
        assertEquals("max Speed:", 12d, training.getMaxSpeed(), 0.001);
    }

    private ActivityLapT createActivityLap(final Double speed, final boolean withCardio, final short maxHeartRate) {
        final ActivityLapT mock = Mockito.mock(ActivityLapT.class);
        Mockito.when(mock.getIntensity()).thenReturn(IntensityT.ACTIVE);
        Mockito.when(mock.getDistanceMeters()).thenReturn(distanz.doubleValue());
        Mockito.when(mock.getMaximumSpeed()).thenReturn(speed);
        Mockito.when(mock.getTotalTimeSeconds()).thenReturn(dauer);
        final HeartRateInBeatsPerMinuteT heartAvg = Mockito.mock(HeartRateInBeatsPerMinuteT.class);
        Mockito.when(heartAvg.getValue()).thenReturn((short) avgHeart);
        Mockito.when(mock.getAverageHeartRateBpm()).thenReturn(heartAvg);

        final HeartRateInBeatsPerMinuteT heartMax = Mockito.mock(HeartRateInBeatsPerMinuteT.class);
        Mockito.when(heartMax.getValue()).thenReturn(maxHeartRate);
        if (withCardio) {
            Mockito.when(mock.getMaximumHeartRateBpm()).thenReturn(heartMax);
        } else {
            Mockito.when(mock.getMaximumHeartRateBpm()).thenReturn(null);
        }

        return mock;
    }

    private void assertTraining(final ITraining training) {
        assertEquals("Distanz:", distanz, training.getLaengeInMeter(), 0.001);
        assertEquals("Dauer:", dauer, training.getDauerInSekunden(), 0.001);
        assertEquals("Herzrfequenz:", avgHeart, training.getAverageHeartBeat());
        assertEquals("max Herzrfequenz:", maxHeart, training.getMaxHeartBeat());
        assertEquals("max Speed:", maxSpeed, training.getMaxSpeed(), 0.001);
    }

    private static XMLGregorianCalendar convert(final Date date) {
        if (date == null) {
            return null;
        } else {
            final GregorianCalendar gc = new GregorianCalendar();
            gc.setTimeInMillis(date.getTime());
            return df.newXMLGregorianCalendar(gc);
        }
    }

}
