package ch.opentrainingcenter.db;

import org.apache.log4j.Logger;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.Platform;

public final class DatabaseAccessFactory {

    public static final Logger LOGGER = Logger.getLogger(DatabaseAccessFactory.class);

    private static DatabaseAccessFactory instance = null;

    public static IDatabaseAccess getDatabaseAccess() {
        if (instance == null) {
            instance = new DatabaseAccessFactory();
        }
        return instance.dao;
    }

    private final IDatabaseAccess dao;

    private DatabaseAccessFactory() {
        // test hier ob es datenbank gibt
        final IConfigurationElement[] daos = Platform.getExtensionRegistry().getConfigurationElementsFor("ch.opentrainingdatabase.db"); //$NON-NLS-1$
        dao = (IDatabaseAccess) getDao(daos, IDatabaseAccess.EXTENSION_POINT_NAME);
    }

    private Object getDao(final IConfigurationElement[] confItems, final String extensionAttr) {
        LOGGER.info("Anzahl Configuration Elements: " + confItems.length); //$NON-NLS-1$
        for (final IConfigurationElement element : confItems) {
            try {
                LOGGER.info("Element: " + element.getName());//$NON-NLS-1$
                LOGGER.info("Namespaceidentifier: " + element.getNamespaceIdentifier()); //$NON-NLS-1$
                final Object createExecutableExtension = element.createExecutableExtension(extensionAttr);
                LOGGER.info("Extension gefunden."); //$NON-NLS-1$
                return createExecutableExtension;
            } catch (final CoreException e) {
                LOGGER.error("Extension nicht gefunden: ", e); //$NON-NLS-1$
            }
        }
        return null;
    }
}
