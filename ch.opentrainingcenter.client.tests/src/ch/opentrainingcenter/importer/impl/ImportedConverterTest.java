package ch.opentrainingcenter.importer.impl;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.preference.IPreferenceStore;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import ch.opentrainingcenter.client.PreferenceConstants;
import ch.opentrainingcenter.importer.ConvertContainer;
import ch.opentrainingcenter.importer.IConvert2Tcx;
import ch.opentrainingcenter.tcx.ActivityT;
import ch.opentrainingcenter.transfer.IImported;
import static org.junit.Assert.assertNotNull;

public class ImportedConverterTest {
    private ImportedConverter converter;
    private IPreferenceStore store;
    private IImported record;
    private File garminFile;
    private String dir;
    private ConvertContainer cc;

    @Before
    public void before() throws IOException {
        store = Mockito.mock(IPreferenceStore.class);
        record = Mockito.mock(IImported.class);
        cc = Mockito.mock(ConvertContainer.class);
        garminFile = File.createTempFile("garmin", ".gmn");
        dir = getDirectory(garminFile.getPath());

    }

    private String getDirectory(final String path) {
        return path.substring(0, path.lastIndexOf(File.separator));
    }

    @After
    public void after() {
        garminFile.deleteOnExit();
    }

    @Test
    public void test() {
        converter = new ImportedConverter(store, cc);
        assertNotNull(converter);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNull() {
        new ImportedConverter(null, cc);
    }

    @Test(expected = FileNotFoundException.class)
    public void testConvertFileNotFound() throws Exception {
        // prepare
        Mockito.when(store.getString(PreferenceConstants.GPS_FILE_LOCATION_PROG)).thenReturn(dir);
        Mockito.when(record.getComments()).thenReturn(garminFile.getName());
        final IConvert2Tcx conv = Mockito.mock(IConvert2Tcx.class);
        final List<ActivityT> values = new ArrayList<ActivityT>();
        Mockito.when(conv.convertActivity(garminFile)).thenReturn(values);
        Mockito.when(cc.getMatchingConverter(garminFile)).thenReturn(conv);

        converter = new ImportedConverter(store, cc);
        // execute
        converter.convertImportedToActivity(record);
    }

    @Test
    public void testConvert() throws Exception {
        // prepare
        Mockito.when(store.getString(PreferenceConstants.GPS_FILE_LOCATION_PROG)).thenReturn(dir);
        Mockito.when(record.getComments()).thenReturn(garminFile.getName());
        final IConvert2Tcx conv = Mockito.mock(IConvert2Tcx.class);
        final List<ActivityT> values = new ArrayList<ActivityT>();
        final ActivityT act = Mockito.mock(ActivityT.class);
        values.add(act);
        Mockito.when(conv.convertActivity(garminFile)).thenReturn(values);
        Mockito.when(cc.getMatchingConverter(garminFile)).thenReturn(conv);

        converter = new ImportedConverter(store, cc);
        // execute
        final ActivityT activity = converter.convertImportedToActivity(record);

        assertNotNull("Activity converted", activity);
    }
}
