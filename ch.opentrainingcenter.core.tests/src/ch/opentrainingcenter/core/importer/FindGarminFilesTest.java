package ch.opentrainingcenter.core.importer;

import java.io.File;
import java.io.IOException;

import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

@SuppressWarnings("nls")
public class FindGarminFilesTest {
    private File garminFile;
    private File patternFile;
    private String dir;

    @Before
    public void before() throws IOException {
        garminFile = File.createTempFile("garmin", ".gmn");
        patternFile = File.createTempFile("test_$_", ".txt");
        dir = getDirectory(garminFile.getPath());
    }

    private String getDirectory(final String path) {
        return path.substring(0, path.lastIndexOf(File.separator));
    }

    @After
    public void after() {
        garminFile.deleteOnExit();
        patternFile.deleteOnExit();
    }

    @Test
    public void testNull() throws IOException {
        final File file = FindGarminFiles.loadAllGPSFile(null, "");
        assertNull("Kein Record wird gefunden", file);
    }

    @Test
    public void falscherDefaultLocation() throws IOException {
        final File file = FindGarminFiles.loadAllGPSFile("", "fantasy");
        assertNull("Kein Record wird gefunden", file);
    }

    @Test
    public void testEinRecord() throws IOException {
        final File file = FindGarminFiles.loadAllGPSFile(garminFile.getName(), dir);
        assertTrue("Ein Record sollte gefunden werden", file.exists());
    }

    @Test
    @Ignore
    public void testPatternRecord() throws IOException {
        final File file = FindGarminFiles.loadAllGPSFile(patternFile.getName(), dir);
        assertTrue("Ein Record sollte gefunden werden", file.exists());
    }
}
