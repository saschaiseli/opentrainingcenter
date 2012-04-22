package ch.opentrainingcenter.db;

import org.apache.log4j.Logger;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.Platform;

public class DatabaseAccessFactory {

    public static final Logger logger = Logger.getLogger(DatabaseAccessFactory.class);

    private static DatabaseAccessFactory INSTANCE = null;
    private final IDatabaseAccess dao;

    private DatabaseAccessFactory() {
        // test hier ob es datenbank gibt
        final IConfigurationElement[] daos = Platform.getExtensionRegistry().getConfigurationElementsFor("ch.opentrainingdatabase.db"); //$NON-NLS-1$
        dao = (IDatabaseAccess) getDao(daos, IDatabaseAccess.EXTENSION_POINT_NAME);
    }

    public static IDatabaseAccess getDatabaseAccess() {
        if (INSTANCE == null) {
            INSTANCE = new DatabaseAccessFactory();
        }
        return INSTANCE.dao;
    }

    private Object getDao(final IConfigurationElement[] confItems, final String extensionAttr) {
        logger.info("Anzahl Configuration Elements: " + confItems.length); //$NON-NLS-1$
        for (final IConfigurationElement element : confItems) {
            try {
                final Object createExecutableExtension = element.createExecutableExtension(extensionAttr);
                logger.info("Extension gefunden."); //$NON-NLS-1$
                return createExecutableExtension;
            } catch (final CoreException e) {
                logger.error("Extension nicht gefunden: " + e.getMessage()); //$NON-NLS-1$
            }
        }
        return null;
    }
}
