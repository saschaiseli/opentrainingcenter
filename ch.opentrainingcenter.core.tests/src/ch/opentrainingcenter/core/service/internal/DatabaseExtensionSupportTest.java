package ch.opentrainingcenter.core.service.internal;

import java.util.Map;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.junit.Before;
import org.junit.Test;

import ch.opentrainingcenter.core.db.IDatabaseConnection;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.anyString;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SuppressWarnings("nls")
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
        final IConfigurationElement[] confItems = new IConfigurationElement[0];

        final Map<String, IDatabaseConnection> dao = DatabaseExtensionSupport.getDao(confItems, EXTENSION_ATTR);

        assertNotNull(dao);
        assertTrue(dao.isEmpty());
    }

    @SuppressWarnings("unchecked")
    @Test
    public void getDaosMitConfItemsCoreException() throws CoreException {
        final IConfigurationElement[] confItems = new IConfigurationElement[1];
        confItems[0] = element;

        when(element.createExecutableExtension(anyString())).thenThrow(CoreException.class);

        final Map<String, IDatabaseConnection> dao = DatabaseExtensionSupport.getDao(confItems, EXTENSION_ATTR);

        assertNotNull(dao);
        assertTrue(dao.isEmpty());
    }

    @Test
    public void getDaosMitConfItems() throws CoreException {
        final IConfigurationElement[] confItems = new IConfigurationElement[1];
        confItems[0] = element;

        final IDatabaseConnection connection = mock(IDatabaseConnection.class);
        when(connection.getName()).thenReturn(H2_DATABASE);

        when(element.createExecutableExtension(EXTENSION_ATTR)).thenReturn(connection);

        final Map<String, IDatabaseConnection> dao = DatabaseExtensionSupport.getDao(confItems, EXTENSION_ATTR);
        final IDatabaseConnection result = dao.get(H2_DATABASE);

        assertNotNull(result);
    }
}
