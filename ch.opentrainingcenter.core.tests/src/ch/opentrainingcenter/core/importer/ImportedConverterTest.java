package ch.opentrainingcenter.core.importer;

import static org.junit.Assert.assertNotNull;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import ch.opentrainingcenter.core.importer.internal.ImportedConverter;
import ch.opentrainingcenter.transfer.ITraining;

@SuppressWarnings("nls")
public class ImportedConverterTest {
    private ImportedConverter converter;
    private ITraining record;
    private File garminFile;
    private String dir;
    private ConvertContainer cc;

    @Before
    public void before() throws IOException {
        record = Mockito.mock(ITraining.class);
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
        converter = new ImportedConverter(cc, "");
        assertNotNull(converter);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNull() {
        new ImportedConverter(cc, null);
    }

    @Test
    public void testConvertFileNotFound() throws Exception {
        // prepare
        Mockito.when(record.getFileName()).thenReturn(garminFile.getName());
        final IConvert2Tcx conv = Mockito.mock(IConvert2Tcx.class);
        Mockito.when(conv.convert(garminFile)).thenReturn(null);
        Mockito.when(cc.getMatchingConverter(garminFile)).thenReturn(conv);

        converter = new ImportedConverter(cc, dir);
        // execute
        converter.convertImportedToActivity(record);
    }

    @Test
    public void testConvert() throws Exception {
        // prepare
        Mockito.when(record.getFileName()).thenReturn(garminFile.getName());
        final IConvert2Tcx conv = Mockito.mock(IConvert2Tcx.class);
        final List<ITraining> values = new ArrayList<ITraining>();
        final ITraining act = Mockito.mock(ITraining.class);
        values.add(act);
        Mockito.when(conv.convert(garminFile)).thenReturn(act);
        Mockito.when(cc.getMatchingConverter(garminFile)).thenReturn(conv);

        converter = new ImportedConverter(cc, dir);
        // execute
        final ITraining activity = converter.convertImportedToActivity(record);

        assertNotNull("Activity converted", activity);
    }
}
