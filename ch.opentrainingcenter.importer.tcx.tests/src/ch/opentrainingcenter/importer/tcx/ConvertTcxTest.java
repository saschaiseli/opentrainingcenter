package ch.opentrainingcenter.importer.tcx;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.File;

import org.junit.Before;
import org.junit.Test;

import ch.opentrainingcenter.transfer.ITraining;

@SuppressWarnings("nls")
public class ConvertTcxTest {
    private ConvertTcx converter;
    private File file;

    @Before
    public void setUp() {
        converter = new ConvertTcx();
        file = new File("resources", "simple.tcx");
    }

    @Test
    public void testTrainingCount() throws Exception {
        final ITraining trainings = converter.convert(file);
        assertNotNull("Ein Traininng", trainings);
    }

    @Test
    public void testTrainingMainProperties() throws Exception {
        final ITraining training = converter.convert(file);
        assertEquals("Testen des Startdatums: ", 22, training.getDatum());
        assertEquals("Dauer des Laufes", 1000, training.getDauer(), 0.001);
        assertEquals("Länge des Laufes", 2000, training.getLaengeInMeter(), 0.001);
        assertEquals("MaxSpeed des Laufes", 4, training.getMaxSpeed(), 0.001);
        assertEquals("MaxHeartBeat des Laufes", 150, training.getMaxHeartBeat(), 0.001);
        assertEquals("AverageHeartBeat des Laufes", 170, training.getAverageHeartBeat(), 0.001);
    }
}
