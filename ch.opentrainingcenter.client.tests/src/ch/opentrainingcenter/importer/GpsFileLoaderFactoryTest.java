package ch.opentrainingcenter.importer;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import ch.opentrainingcenter.core.db.IDatabaseAccess;
import ch.opentrainingcenter.core.importer.ConvertContainer;
import ch.opentrainingcenter.core.importer.IImportedConverter;
import ch.opentrainingcenter.core.importer.ImporterFactory;
import ch.opentrainingcenter.transfer.IAthlete;
import static org.junit.Assert.assertNotNull;

@SuppressWarnings("nls")
public class GpsFileLoaderFactoryTest {
    private ConvertContainer cc;

    @Before
    public void before() {
        cc = Mockito.mock(ConvertContainer.class);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGpsFileLoaderNullStore() {
        ImporterFactory.createGpsFileLoader(cc, null);
    }

    @Test
    public void testGpsFileLoader() {
        final IImportedConverter fileLoader = ImporterFactory.createGpsFileLoader(cc, "");
        assertNotNull(fileLoader);
    }

    @Test
    public void testFileImporter() {
        final ConvertContainer cc = Mockito.mock(ConvertContainer.class);
        final IAthlete athlete = Mockito.mock(IAthlete.class);
        final IDatabaseAccess databaseAccess = Mockito.mock(IDatabaseAccess.class);
        final String backup = "";
        final IFileImport fileImport = ImportFactory.createFileImporter(cc, athlete, databaseAccess, backup);
        assertNotNull(fileImport);
    }
}
