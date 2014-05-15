package ch.opentrainingcenter.core.service.internal;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Map;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.junit.Before;
import org.junit.Test;

import ch.opentrainingcenter.core.db.IDatabaseConnection;

public class DatabaseExtensionSupportTest {

    private static final String H2_DATABASE = "H2Database";
    private static final String EXTENSION_ATTR = "extensionAttr";
    private IConfigurationElement element;

    @Before
    public void setUp() {
        element = mock(IConfigurationElement.class);
        when(element.getName()).thenReturn("Junit");
        when(element.getNamespaceIdentifier()).thenReturn("namespace");
    }

    @Test
    public void getDaosOhneConfItems() {
        IConfigurationElement[] confItems = new IConfigurationElement[0];

        Map<String, IDatabaseConnection> dao = DatabaseExtensionSupport.getDao(confItems, EXTENSION_ATTR);

        assertNotNull(dao);
        assertTrue(dao.isEmpty());
    }

    @SuppressWarnings("unchecked")
    @Test
    public void getDaosMitConfItemsCoreException() throws CoreException {
        IConfigurationElement[] confItems = new IConfigurationElement[1];
        confItems[0] = element;

        when(element.createExecutableExtension(anyString())).thenThrow(CoreException.class);

        Map<String, IDatabaseConnection> dao = DatabaseExtensionSupport.getDao(confItems, EXTENSION_ATTR);

        assertNotNull(dao);
        assertTrue(dao.isEmpty());
    }

    @Test
    public void getDaosMitConfItems() throws CoreException {
        IConfigurationElement[] confItems = new IConfigurationElement[1];
        confItems[0] = element;

        IDatabaseConnection connection = mock(IDatabaseConnection.class);
        when(connection.getName()).thenReturn(H2_DATABASE);

        when(element.createExecutableExtension(EXTENSION_ATTR)).thenReturn(connection);

        Map<String, IDatabaseConnection> dao = DatabaseExtensionSupport.getDao(confItems, EXTENSION_ATTR);
        IDatabaseConnection result = dao.get(H2_DATABASE);

        assertNotNull(result);
    }
}
