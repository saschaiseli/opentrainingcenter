package ch.opentrainingcenter.importer.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import ch.opentrainingcenter.client.helper.FileCopy;
import ch.opentrainingcenter.client.helper.GpsFileNameFilter;
import ch.opentrainingcenter.importer.IConvert2Tcx;
import static org.junit.Assert.assertEquals;

public class FileCopyTest {
    private static final String HELLO_FROM_JUNIT = "Hello from junit";
    private File source;
    private File destination;

    @Before
    public void setUp() {

    }

    @After
    public void after() {
        source.deleteOnExit();
        if (destination != null) {
            destination.deleteOnExit();
        }
    }

    @Test
    public void simpleCopy() throws IOException {
        // prepare
        source = File.createTempFile("testa", "testa");
        final FileWriter writer = new FileWriter(source);
        writer.write(HELLO_FROM_JUNIT);
        writer.close();
        destination = File.createTempFile("testb", "testb");

        // action
        FileCopy.copyFile(source, destination);
        // assertion
        assertFileCopy(destination);
    }

    @Test
    public void doNotOverwriterFile() throws IOException {
        // prepare
        source = File.createTempFile("testa", "testa");
        final FileWriter writer = new FileWriter(source);
        writer.write(HELLO_FROM_JUNIT);
        writer.close();

        // action
        FileCopy.copyFile(source, source);
        assertFileCopy(source);
    }

    @Test
    public void copyFiles() throws IOException {
        source = File.createTempFile("testa", "testa.txt");
        final FileWriter writer = new FileWriter(source);
        writer.write(HELLO_FROM_JUNIT);
        writer.close();
        final String absolutePath = source.getAbsolutePath();
        final String sourceFolder = absolutePath.substring(0, absolutePath.lastIndexOf(File.separator));

        final Map<String, IConvert2Tcx> converters = new HashMap<String, IConvert2Tcx>();
        final IConvert2Tcx converter = Mockito.mock(IConvert2Tcx.class);
        converters.put("txt", converter);
        final FilenameFilter filter = new GpsFileNameFilter(converters);
        final String destinationFolder = sourceFolder + File.separator + "junit";
        FileCopy.copyFiles(sourceFolder, destinationFolder, filter);

        assertFileCopy(new File(destinationFolder, source.getName()));
    }

    private void assertFileCopy(final File file) throws FileNotFoundException, IOException {
        final FileReader reader = new FileReader(file);
        int c;
        final StringBuffer str = new StringBuffer();
        while ((c = reader.read()) != -1) {
            str.append((char) c);
        }
        reader.close();
        assertEquals("Text '" + HELLO_FROM_JUNIT + "' sollte kopiert worden sein", HELLO_FROM_JUNIT, str.toString());//$NON-NLS-1$//$NON-NLS-2$
    }
}
