package ch.opentrainingcenter.core.db;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.Platform;

import ch.opentrainingcenter.core.assertions.Assertions;

public final class DatabaseAccessFactory {

    public static final Logger LOGGER = Logger.getLogger(DatabaseAccessFactory.class);

    private static final String DEVELOPING_FLAG = "developing"; //$NON-NLS-1$

    private static boolean DEVELOPING = false;

    private static DatabaseAccessFactory INSTANCE = null;

    private static final Map<String, IDatabaseAccess> dbAccesses;

    private static IDatabaseAccess databaseAccess;

    static {
        final IConfigurationElement[] daos = Platform.getExtensionRegistry().getConfigurationElementsFor("ch.opentrainingdatabase.db"); //$NON-NLS-1$
        dbAccesses = getDao(daos, IDatabaseAccess.EXTENSION_POINT_NAME);
    }

    private DatabaseAccessFactory() {
        final String[] commandLineArgs = Platform.getCommandLineArgs();
        for (final String cmdArg : commandLineArgs) {
            if (cmdArg.contains(DEVELOPING_FLAG)) {
                DEVELOPING = true;
            }
        }

    }

    /**
     * Initialisiert die Datenbank Factory. Diese Methode muss vor dem
     * getDatabaseAccess aufgerufen werden.
     * 
     * @param dbName
     *            Name der Datenbank
     * @param url
     *            URL zu der Datenbank
     * @param user
     *            Usernamen
     * @param pw
     *            Passwort
     */
    public static void init(final String dbName, final String url, final String user, final String pw) {
        if (INSTANCE == null) {
            INSTANCE = new DatabaseAccessFactory();

            databaseAccess = getDbaccesses().get(dbName);
            if (databaseAccess != null) {
                final DbConnection dbConnection = databaseAccess.getDbConnection();
                if (DEVELOPING) {
                    dbConnection.setUrl(url + "_dev"); //$NON-NLS-1$
                } else {
                    dbConnection.setUrl(url);
                }
                dbConnection.setUsername(user);
                dbConnection.setPassword(pw);

                databaseAccess.setDeveloping(DEVELOPING);
                final DatabaseConnectionConfiguration config = new DatabaseConnectionConfiguration(dbConnection);
                databaseAccess.setConfiguration(config);
                databaseAccess.init();
            } else {
                throw new IllegalArgumentException("Datenbank mit dem Namen: '" + dbName + "' nicht gefunden"); //$NON-NLS-1$ //$NON-NLS-2$
            }

        }
    }

    /**
     * @return Database Access. Zuerst muss die Methode init aufgerufen werden,
     *         sonst gibt es eine {@link IllegalArgumentException}.
     */
    public static IDatabaseAccess getDatabaseAccess() {
        Assertions.notNull(INSTANCE, "Datenbank muss bereits initialisiert sein"); //$NON-NLS-1$
        return databaseAccess;
    }

    public static Map<String, IDatabaseAccess> getDbaccesses() {
        return dbAccesses;
    }

    static Map<String, IDatabaseAccess> getDao(final IConfigurationElement[] confItems, final String extensionAttr) {
        LOGGER.info("Anzahl Configuration Elements: " + confItems.length); //$NON-NLS-1$
        final Map<String, IDatabaseAccess> result = new HashMap<>();
        for (final IConfigurationElement element : confItems) {
            try {
                LOGGER.info("Element: " + element.getName());//$NON-NLS-1$
                LOGGER.info("Namespaceidentifier: " + element.getNamespaceIdentifier()); //$NON-NLS-1$
                final IDatabaseAccess db = (IDatabaseAccess) element.createExecutableExtension(extensionAttr);
                result.put(db.getName(), db);
                LOGGER.info("Extension gefunden."); //$NON-NLS-1$
            } catch (final CoreException e) {
                LOGGER.error("Extension nicht gefunden: ", e); //$NON-NLS-1$
            }
        }
        return Collections.unmodifiableMap(result);
    }

    /**
     * Für Testzwecke
     */
    static DatabaseAccessFactory getInstance() {
        return INSTANCE;
    }

    /**
     * Für Testzwecke
     */
    static void reset() {
        INSTANCE = null;
    }

}
