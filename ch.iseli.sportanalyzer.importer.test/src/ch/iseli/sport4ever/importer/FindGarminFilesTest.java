package ch.iseli.sport4ever.importer;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.IOException;
import java.net.URISyntaxException;

import org.junit.Test;

import ch.iseli.sportanalyzer.importer.FindGarminFiles;

public class FindGarminFilesTest {

    @Test
    public void testFileFinder() throws IOException, URISyntaxException {
        assertNotNull("Ein File muss vorhanden sein", FindGarminFiles.getGarminFiles());
        assertEquals("Ein File muss vorhanden sein", 1, FindGarminFiles.getGarminFiles().size());
    }
}
