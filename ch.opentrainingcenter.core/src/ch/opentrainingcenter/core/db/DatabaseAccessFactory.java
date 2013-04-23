package ch.opentrainingcenter.core.db;

import org.apache.log4j.Logger;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.Platform;

public final class DatabaseAccessFactory {

    public static final Logger LOGGER = Logger.getLogger(DatabaseAccessFactory.class);

    private static final String DEVELOPING_FLAG = "developing"; //$NON-NLS-1$

    private static boolean DEVELOPING = false;

    private static DatabaseAccessFactory instance = null;

    private final IDatabaseAccess databaseAccess;

    public static IDatabaseAccess getDatabaseAccess() {
        if (instance == null) {
            instance = new DatabaseAccessFactory();
            final String[] commandLineArgs = Platform.getCommandLineArgs();
            for (final String cmdArg : commandLineArgs) {
                if (cmdArg.contains(DEVELOPING_FLAG)) {
                    DEVELOPING = true;
                }
            }
            if (DEVELOPING) {
                instance.databaseAccess.setDeveloping(true);
            }
            instance.databaseAccess.init();
        }
        return instance.databaseAccess;
    }

    private DatabaseAccessFactory() {
        // test hier ob es datenbank gibt
        final IConfigurationElement[] daos = Platform.getExtensionRegistry().getConfigurationElementsFor("ch.opentrainingdatabase.db"); //$NON-NLS-1$
        databaseAccess = (IDatabaseAccess) getDao(daos, IDatabaseAccess.EXTENSION_POINT_NAME);
    }

    Object getDao(final IConfigurationElement[] confItems, final String extensionAttr) {
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

    static DatabaseAccessFactory getInstance() {
        return instance;
    }
}
