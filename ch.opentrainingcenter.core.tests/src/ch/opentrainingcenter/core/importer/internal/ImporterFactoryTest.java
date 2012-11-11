package ch.opentrainingcenter.core.importer.internal;

import org.junit.Test;
import org.mockito.Mockito;

import ch.opentrainingcenter.core.importer.ConvertContainer;
import ch.opentrainingcenter.core.importer.ImporterFactory;
import static org.junit.Assert.assertNotNull;

@SuppressWarnings("nls")
public class ImporterFactoryTest {
    @Test
    public void testSimple1() {
        assertNotNull("Factory zum instanzieren", ImporterFactory.createFileCopy());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testException() {
        final ConvertContainer cc = Mockito.mock(ConvertContainer.class);
        ImporterFactory.createGpsFileLoader(cc, null);
    }

    @Test
    public void testSimple2() {
        final ConvertContainer cc = Mockito.mock(ConvertContainer.class);
        assertNotNull("Factory zum instanzieren", ImporterFactory.createGpsFileLoader(cc, "blabla"));
    }
}
