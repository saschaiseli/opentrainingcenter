package ch.iseli.sportanalyzer.importer.util;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

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
        FileWriter writer = new FileWriter(source);
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
        FileWriter writer = new FileWriter(source);
        writer.write(HELLO_FROM_JUNIT);
        writer.close();

        // action
        FileCopy.copyFile(source, source);
        assertFileCopy(source);
    }

    private void assertFileCopy(File file) throws FileNotFoundException, IOException {
        FileReader reader = new FileReader(file);
        int c;
        StringBuffer str = new StringBuffer();
        while ((c = reader.read()) != -1) {
            str.append((char) c);
        }
        reader.close();
        assertEquals("Text '" + HELLO_FROM_JUNIT + "' sollte kopiert worden sein", HELLO_FROM_JUNIT, str.toString());
    }
}