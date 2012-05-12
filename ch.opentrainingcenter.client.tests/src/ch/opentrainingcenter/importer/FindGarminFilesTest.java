package ch.opentrainingcenter.importer;

import java.io.File;
import java.io.IOException;

import org.eclipse.jface.preference.IPreferenceStore;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.mockito.Mockito;

import ch.opentrainingcenter.client.PreferenceConstants;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class FindGarminFilesTest {
    private IPreferenceStore store;
    private File garminFile;
    private File patternFile;
    private String dir;

    @Before
    public void before() throws IOException {
        store = Mockito.mock(IPreferenceStore.class);
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
        Mockito.when(store.getString(PreferenceConstants.GPS_FILE_LOCATION_PROG)).thenReturn(dir);
        final File file = FindGarminFiles.loadAllGPSFile(null, store);
        assertNull("Kein Record wird gefunden", file);
    }

    @Test
    public void falscherDefaultLocation() throws IOException {
        Mockito.when(store.getString(PreferenceConstants.GPS_FILE_LOCATION_PROG)).thenReturn("fantasy");
        final File file = FindGarminFiles.loadAllGPSFile("", store);
        assertNull("Kein Record wird gefunden", file);
    }

    @Test
    public void testEinRecord() throws IOException {
        Mockito.when(store.getString(PreferenceConstants.GPS_FILE_LOCATION_PROG)).thenReturn(dir);
        final File file = FindGarminFiles.loadAllGPSFile(garminFile.getName(), store);
        assertTrue("Ein Record sollte gefunden werden", file.exists());
    }

    @Test
    @Ignore
    public void testPatternRecord() throws IOException {
        Mockito.when(store.getString(PreferenceConstants.GPS_FILE_LOCATION_PROG)).thenReturn(dir);
        final File file = FindGarminFiles.loadAllGPSFile(patternFile.getName(), store);
        assertTrue("Ein Record sollte gefunden werden", file.exists());
    }
}
