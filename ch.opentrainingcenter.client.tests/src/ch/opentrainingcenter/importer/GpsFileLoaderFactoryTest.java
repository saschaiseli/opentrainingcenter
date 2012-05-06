package ch.opentrainingcenter.importer;

import org.eclipse.jface.preference.IPreferenceStore;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import ch.opentrainingcenter.db.IDatabaseAccess;
import ch.opentrainingcenter.transfer.IAthlete;
import static org.junit.Assert.assertNotNull;

public class GpsFileLoaderFactoryTest {
    private IPreferenceStore store;
    private ConvertContainer cc;

    @Before
    public void before() {
        store = Mockito.mock(IPreferenceStore.class);
        cc = Mockito.mock(ConvertContainer.class);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGpsFileLoaderNullStore() {
        ImporterFactory.createGpsFileLoader(null, cc);
    }

    @Test
    public void testGpsFileLoader() {
        final IImportedConverter fileLoader = ImporterFactory.createGpsFileLoader(store, cc);
        assertNotNull(fileLoader);
    }

    @Test
    public void testFileImporter() {
        final ConvertContainer cc = Mockito.mock(ConvertContainer.class);
        final IAthlete athlete = Mockito.mock(IAthlete.class);
        final IDatabaseAccess databaseAccess = Mockito.mock(IDatabaseAccess.class);
        final String backup = "";
        final IFileImport fileImport = ImporterFactory.createFileImporter(cc, athlete, databaseAccess, backup);
        assertNotNull(fileImport);
    }
}
