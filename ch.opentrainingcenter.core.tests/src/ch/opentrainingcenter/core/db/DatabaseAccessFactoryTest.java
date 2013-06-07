package ch.opentrainingcenter.core.db;

import org.eclipse.core.runtime.CoreException;
import org.junit.After;
import org.junit.Test;

@SuppressWarnings("nls")
public class DatabaseAccessFactoryTest {

    @After
    public void after() {
        DatabaseAccessFactory.reset();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInitialisierung() throws CoreException {
        DatabaseAccessFactory.getDatabaseAccess();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInitialisierungExtensionNotFound() throws CoreException {
        DatabaseAccessFactory.init("blabla", null, null, null);
    }
}
