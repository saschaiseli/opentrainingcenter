package ch.opentrainingcenter.importer;

import static org.junit.Assert.assertNotNull;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import ch.opentrainingcenter.core.db.IDatabaseAccess;
import ch.opentrainingcenter.core.importer.ConvertContainer;
import ch.opentrainingcenter.transfer.IAthlete;

@SuppressWarnings("nls")
public class GpsFileLoaderFactoryTest {
    private ConvertContainer cc;

    @Before
    public void before() {
        cc = Mockito.mock(ConvertContainer.class);
    }

    @Test
    public void testFileImporter() {
        final IAthlete athlete = Mockito.mock(IAthlete.class);
        final IDatabaseAccess databaseAccess = Mockito.mock(IDatabaseAccess.class);
        final String backup = "";
        final IFileImport fileImport = ImportFactory.createFileImporter(cc, athlete, databaseAccess, backup);
        assertNotNull(fileImport);
    }
}
