package ch.opentrainingcenter.tcx.data;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.File;

import org.junit.Before;
import org.junit.Test;

import ch.iseli.sport4ever.importer.internal.Gmn2Tcx;
import ch.iseli.sportanalyzer.tcx.TrainingCenterDatabaseT;
import ch.opentrainingcenter.tcx.data.internal.CardioImpl;

public class CardioTest {
    private static final String GARMIN_FILE = "garmin/20110814T142321.gmn";
    private Gmn2Tcx importer;
    private File file;
    private String path;
    private Cardio cardio;

    @Before
    public void setUp() throws Exception {
	file = new File(GARMIN_FILE);
	path = file.getAbsolutePath();
	path = path.replace(GARMIN_FILE, "resources");
	File f = new File(".txt");
	String absolutePath = f.getAbsolutePath();
	int indexOf = absolutePath.indexOf("ch.");
	absolutePath = absolutePath.substring(0, indexOf) + "ch.iseli.sportanalyzer.importer/resources";
	importer = new Gmn2Tcx(absolutePath);
	TrainingCenterDatabaseT t = importer.convert(new File(GARMIN_FILE));
	cardio = new CardioImpl(t);
    }

    @Test
    public void testFileNotNull() {
	assertNotNull(cardio);
	assertTrue("durchschnitt muss grösser null sein", cardio.getAverageCardio() > 0);
    }

}
