package ch.opentrainingcenter.model.importer;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import ch.opentrainingcenter.core.helper.RunType;
import ch.opentrainingcenter.model.importer.internal.GpsFileModel;

public class GpsFileModelTest {

    private static final String FILENAME = "junit.txt";
    private GpsFileModel model;

    @Test
    public void testFileName() {
        model = new GpsFileModel(FILENAME);
        assertEquals(FILENAME, model.getFileName());
    }

    @Test
    public void testImported() {
        model = new GpsFileModel(FILENAME);
        assertTrue("Ist importiert", model.isImportFile());
    }

    @Test
    public void testTyp() {
        model = new GpsFileModel(FILENAME);
        assertEquals(RunType.NONE.getIndex(), model.getId());
    }

    @Test
    public void testGetTypInit() {
        model = new GpsFileModel(FILENAME);
        assertEquals(RunType.NONE, model.getTyp());
    }

    @Test
    public void testGetTypChanged() {
        model = new GpsFileModel(FILENAME);
        model.setTyp(RunType.EXT_INTERVALL);
        assertEquals(RunType.EXT_INTERVALL, model.getTyp());
    }

    @Test
    public void testImport() {
        model = new GpsFileModel(FILENAME);
        model.setImportFile(false);
        assertFalse(model.isImportFile());
    }
}
