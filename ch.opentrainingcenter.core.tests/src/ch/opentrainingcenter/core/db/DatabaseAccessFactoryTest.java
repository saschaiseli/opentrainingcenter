package ch.opentrainingcenter.core.db;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.Status;
import org.junit.Test;
import org.mockito.Mockito;

import static org.junit.Assert.assertEquals;

@SuppressWarnings("nls")
public class DatabaseAccessFactoryTest {

    @Test
    public void testGetDAo() throws CoreException {
        DatabaseAccessFactory.getDatabaseAccess();
        final DatabaseAccessFactory instance = DatabaseAccessFactory.getInstance();
        final String extensionAttr = "attr";

        final IConfigurationElement[] confItems = new IConfigurationElement[1];
        final IConfigurationElement mockA = Mockito.mock(IConfigurationElement.class);
        Mockito.when(mockA.getName()).thenReturn("Junit");
        Mockito.when(mockA.getNamespaceIdentifier()).thenReturn("namespace");
        final String obj = new String("dao");
        Mockito.when(mockA.createExecutableExtension(extensionAttr)).thenReturn(obj);

        confItems[0] = mockA;
        final String dao = (String) instance.getDao(confItems, extensionAttr);

        assertEquals("Es muss ein dao auch zurückgegeben werden", obj, dao);
    }

    @Test
    public void testGetDAoMitCoreException() throws CoreException {
        DatabaseAccessFactory.getDatabaseAccess();
        final DatabaseAccessFactory instance = DatabaseAccessFactory.getInstance();
        final String extensionAttr = "attr";

        final IConfigurationElement[] confItems = new IConfigurationElement[1];
        final IConfigurationElement mockA = Mockito.mock(IConfigurationElement.class);
        Mockito.when(mockA.getName()).thenReturn("Junit");
        Mockito.when(mockA.getNamespaceIdentifier()).thenReturn("namespace");
        final CoreException ce = new CoreException(Status.CANCEL_STATUS);

        Mockito.when(mockA.createExecutableExtension(extensionAttr)).thenThrow(ce);

        confItems[0] = mockA;
        final String dao = (String) instance.getDao(confItems, extensionAttr);
        assertEquals("Extension wurde nicht gefunden", null, dao);
    }
}
