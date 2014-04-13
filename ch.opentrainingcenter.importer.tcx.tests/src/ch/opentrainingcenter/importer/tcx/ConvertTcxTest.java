package ch.opentrainingcenter.importer.tcx;

import java.io.File;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import ch.opentrainingcenter.transfer.ILapInfo;
import ch.opentrainingcenter.transfer.IStreckenPunkt;
import ch.opentrainingcenter.transfer.ITrackPointProperty;
import ch.opentrainingcenter.transfer.ITraining;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

@SuppressWarnings("nls")
public class ConvertTcxTest {
    private static final double DELTA = 0.00000001;
    private ConvertTcx converter;
    private File simple, simple_one_lap, simple_two_lap, simple_out_of_memory, simple_null_values;

    @Before
    public void setUp() {
        converter = new ConvertTcx();
        simple = new File("resources", "simple.tcx");
        simple_one_lap = new File("resources", "simple_one_lap.tcx");
        simple_two_lap = new File("resources", "simple_two_lap.tcx");
        simple_out_of_memory = new File("resources", "simple_out_of_memory.tcx");
        simple_null_values = new File("resources", "simple_null_values.tcx");
    }

    @Test
    public void testName() {
        final String name = converter.getName();
        assertEquals("TCX (mit ANT+ aus der Uhr exportiert)", name);
    }

    @Test
    public void testPrefix() {
        final String name = converter.getFilePrefix();
        assertEquals("tcx", name);
    }

    @Test
    public void testTrainingCount() throws Exception {
        final ITraining trainings = converter.convert(simple);
        assertNotNull("Ein Traininng", trainings);
    }

    @Test
    public void testTrainingMainProperties() throws Exception {
        final ITraining training = converter.convert(simple);
        assertEquals("Testen des Startdatums: ", 1364897192000L, training.getDatum());
        assertEquals("Dauer des Laufes", 810.61, training.getDauer(), DELTA);
        assertEquals("Länge des Laufes", 2443.549074, training.getLaengeInMeter(), DELTA);
        assertEquals("MaxSpeed des Laufes", 5.07697296, training.getMaxSpeed(), DELTA);
        assertEquals("MaxHeartBeat des Laufes", 177, training.getMaxHeartBeat());
        assertEquals("AverageHeartBeat des Laufes", 160, training.getAverageHeartBeat());
    }

    @Test
    public void testTrainingTrackPoints() throws Exception {
        final ITraining training = converter.convert(simple_one_lap);
        final List<ITrackPointProperty> points = training.getTrackPoints();
        assertNotNull(points);
        assertFalse("Liste der punkte darf nicht leer sein.", points.isEmpty());
        assertEquals(3, points.size());

        assertPoint(points.get(0), 543, 0.576151550, 85, 1, 1364897192000L, 7.43025357, 46.94510135);
        assertPoint(points.get(1), 544, 1.55577779, 86, 1, 1364897193000L, 7.43026547, 46.94510487);
        assertPoint(points.get(2), 545, 2.89205694, 87, 1, 1364897194000L, 7.43028106, 46.94511082);
    }

    @Test
    public void testTrainingTrackPointsTwoLaps() throws Exception {
        final ITraining training = converter.convert(simple_two_lap);
        final List<ITrackPointProperty> points = training.getTrackPoints();
        assertNotNull(points);
        assertFalse("Liste der punkte darf nicht leer sein.", points.isEmpty());
        assertEquals(6, points.size());

        assertPoint(points.get(0), 543, 0.576151550, 85, 1, 1364897192000L, 7.43025357, 46.94510135);
        assertPoint(points.get(1), 544, 1.55577779, 86, 1, 1364897193000L, 7.43026547, 46.94510487);
        assertPoint(points.get(2), 545, 2.89205694, 87, 1, 1364897194000L, 7.43028106, 46.94511082);

        assertPoint(points.get(3), 543, 0.576151550, 85, 2, 1364897192000L, 7.43025357, 46.94510135);
        assertPoint(points.get(4), 544, 1.55577779, 86, 2, 1364897193000L, 7.43026547, 46.94510487);
        assertPoint(points.get(5), 545, 2.89205694, 87, 2, 1364897194000L, 7.43028106, 46.94511082);

        final List<ILapInfo> lap = training.getLapInfos();
        assertEquals("2 Runden sind vorhanden", 2, lap.size());
        final ILapInfo lap1 = lap.get(0);
        assertEquals(1, lap1.getLap());
        assertEquals(154, lap1.getHeartBeat());
        assertEquals(300000, lap1.getTime());
        assertEquals(1000, lap1.getDistance());
        assertEquals("5:00", lap1.getPace());

        final ILapInfo lap2 = lap.get(1);
        assertEquals(2, lap2.getLap());
        assertEquals(254, lap2.getHeartBeat());
        assertEquals(240000, lap2.getTime());
        assertEquals(1000, lap2.getDistance());
        assertEquals("4:00", lap2.getPace());
    }

    @Test
    public void testTrainingEmptyTrackPoints() throws Exception {
        final ITraining training = converter.convert(simple_null_values);
        final List<ITrackPointProperty> points = training.getTrackPoints();
        assertNotNull(points);
        assertFalse("Liste der punkte darf nicht leer sein.", points.isEmpty());
        assertEquals(1, points.size());

        assertPoint(points.get(0), 543, 0.576151550, 85, 1, 1364897192000L, 7.43025357, 46.94510135);
    }

    @Test
    public void testWatchOutOfMemory() throws Exception {
        final ITraining training = converter.convert(simple_out_of_memory);
        assertEquals("Testen des Startdatums: ", 1356702915000L, training.getDatum());
        assertEquals("Dauer des Laufes", 4274.57, training.getDauer(), DELTA);
        assertEquals("Länge des Laufes", 12305.6787, training.getLaengeInMeter(), DELTA);
        assertEquals("MaxSpeed des Laufes", 4.48573303, training.getMaxSpeed(), DELTA);
        assertEquals("MaxHeartBeat des Laufes", 184, training.getMaxHeartBeat());
        assertEquals("AverageHeartBeat des Laufes", 150, training.getAverageHeartBeat());
        assertEquals("Da keine GSP Daten da sind, muss etwas in die Notes geschrieben werden", ConvertTcx.NO_DATA, training.getNote());
    }

    private void assertPoint(final ITrackPointProperty point, final int hoehe, final double distance, final int herz, final int runde, final long zeit,
            final double longitude, final double latitude) {
        assertEquals("Höhe: ", hoehe, point.getAltitude());
        assertEquals("Distanz: ", distance, point.getDistance(), DELTA);
        assertEquals("Herz: ", herz, point.getHeartBeat());
        assertEquals("Runde: ", runde, point.getLap());
        assertEquals("Zeit: ", zeit, point.getZeit());
        final IStreckenPunkt geo = point.getStreckenPunkt();
        assertEquals("Longitude: ", longitude, geo.getLongitude(), DELTA);
        assertEquals("Latitude: ", latitude, geo.getLatitude(), DELTA);
    }
}
