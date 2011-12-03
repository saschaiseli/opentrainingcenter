package ch.iseli.sportanalyzer.db;

import org.apache.log4j.Logger;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.Platform;

public class DatabaseAccessFactory {

    public static final Logger log = Logger.getLogger(DatabaseAccessFactory.class);

    private static DatabaseAccessFactory INSTANCE = null;
    private final IDatabaseAccess dao;

    private DatabaseAccessFactory() {
        // test hier ob es datenbank gibt
        final IConfigurationElement[] daos = Platform.getExtensionRegistry().getConfigurationElementsFor("ch.opentrainingdatabase.db");
        dao = (IDatabaseAccess) getDao(daos, IDatabaseAccess.EXTENSION_POINT_NAME);
    }

    public static IDatabaseAccess getDatabaseAccess() {
        if (INSTANCE == null) {
            INSTANCE = new DatabaseAccessFactory();
        }
        return INSTANCE.dao;
    }

    private Object getDao(final IConfigurationElement[] confItems, final String extensionAttr) {
        log.info("Anzahl Configuration Elements: " + confItems.length);
        for (final IConfigurationElement element : confItems) {
            try {
                final Object createExecutableExtension = element.createExecutableExtension(extensionAttr);
                log.info("Extension gefunden.");
                return createExecutableExtension;
            } catch (final CoreException e) {
                log.error("Extension nicht gefunden: " + e.getMessage());
            }
        }
        return null;
    }
}
