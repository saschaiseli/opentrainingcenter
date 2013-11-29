//package ch.opentrainingcenter.core.db;
//
//import java.util.Collections;
//import java.util.HashMap;
//import java.util.Map;
//
//import org.apache.log4j.Logger;
//import org.eclipse.core.runtime.CoreException;
//import org.eclipse.core.runtime.IConfigurationElement;
//import org.eclipse.core.runtime.Platform;
//
//import ch.opentrainingcenter.core.assertions.Assertions;
//
///**
// * @deprecated use eclipse service.
// */
//@Deprecated
//public final class DatabaseAccessFactory {
//
//    public static final Logger LOGGER = Logger.getLogger(DatabaseAccessFactory.class);
//
//    private static final String DEVELOPING_FLAG = "developing"; //$NON-NLS-1$
//
//    private static boolean developing = false;
//
//    private static DatabaseAccessFactory instance = null;
//
//    private static final Map<String, IDatabaseConnection> DB_ACCESS;
//
//    private static IDatabaseConnection databaseAccess;
//
//    static {
//        final IConfigurationElement[] daos = Platform.getExtensionRegistry().getConfigurationElementsFor("ch.opentrainingdatabase.db"); //$NON-NLS-1$
//        DB_ACCESS = getDao(daos, IDatabaseConnection.EXTENSION_POINT_NAME);
//    }
//
//    private DatabaseAccessFactory() {
//        final String[] commandLineArgs = Platform.getCommandLineArgs();
//        for (final String cmdArg : commandLineArgs) {
//            if (cmdArg.contains(DEVELOPING_FLAG)) {
//                developing = true;
//            }
//        }
//    }
//
//    /**
//     * Initialisiert die Datenbank Factory. Diese Methode muss vor dem
//     * getDatabaseAccess aufgerufen werden.
//     * 
//     * @param dbName
//     *            Name der Datenbank
//     * @param url
//     *            URL zu der Datenbank
//     * @param user
//     *            Usernamen
//     * @param pw
//     *            Passwort
//     */
//    public static void init(final String dbName, final String url, final String user, final String pw) {
//        init(dbName, url, user, pw, null, null, null);
//    }
//
//    @SuppressWarnings("nls")
//    public static void init(final String dbName, final String url, final String user, final String pw, final String urlAdmin, final String userAdmin,
//            final String pwAdmin) {
//        if (instance == null) {
//            instance = new DatabaseAccessFactory();
//
//            databaseAccess = getDbaccesses().get(dbName);
//            if (databaseAccess != null) {
//                final DbConnection dbConnection = getConnection(url, user, pw, developing);
//                DbConnection admin = null;
//                if (databaseAccess.isUsingAdminDbConnection()) {
//                    Assertions.notNull(urlAdmin, "Administrierbare Datenbank. URL darf nicht null sein");
//                    Assertions.notNull(userAdmin, "Administrierbare Datenbank. userAdmin darf nicht null sein");
//                    Assertions.notNull(pwAdmin, "Administrierbare Datenbank. pwAdmin darf nicht null sein");
//                    admin = getConnection(urlAdmin, userAdmin, pwAdmin, false);
//                }
//                databaseAccess.setDeveloping(developing);
//                final DatabaseConnectionConfiguration config = new DatabaseConnectionConfiguration(dbConnection, admin);
//                databaseAccess.setConfiguration(config);
//                databaseAccess.init();
//            } else {
//                throw new IllegalArgumentException("Datenbank mit dem Namen: '" + dbName + "' nicht gefunden"); //$NON-NLS-1$ //$NON-NLS-2$
//            }
//
//        }
//    }
//
//    private static DbConnection getConnection(final String url, final String user, final String pw, final boolean dev) {
//        final DbConnection conn = new DbConnection(databaseAccess.getDriver(), databaseAccess.getDialect());
//        if (dev) {
//            conn.setUrl(url + "_dev"); //$NON-NLS-1$
//        } else {
//            conn.setUrl(url);
//        }
//        conn.setUsername(user);
//        conn.setPassword(pw);
//        return conn;
//    }
//
//    /**
//     * @return Database Access. Zuerst muss die Methode init aufgerufen werden,
//     *         sonst gibt es eine {@link IllegalArgumentException}.
//     */
//    public static IDatabaseConnection getDatabaseAccess() {
//        Assertions.notNull(instance, "Datenbank muss bereits initialisiert sein"); //$NON-NLS-1$
//        return databaseAccess;
//    }
//
//    public static Map<String, IDatabaseConnection> getDbaccesses() {
//        return DB_ACCESS;
//    }
//
//    private static Map<String, IDatabaseConnection> getDao(final IConfigurationElement[] confItems, final String extensionAttr) {
//        LOGGER.info("Anzahl Configuration Elements: " + confItems.length); //$NON-NLS-1$
//        final Map<String, IDatabaseConnection> result = new HashMap<>();
//        for (final IConfigurationElement element : confItems) {
//            try {
//                LOGGER.info("Element: " + element.getName()); //$NON-NLS-1$
//                LOGGER.info("Namespaceidentifier: " + element.getNamespaceIdentifier()); //$NON-NLS-1$
//                final IDatabaseConnection db = (IDatabaseConnection) element.createExecutableExtension(extensionAttr);
//                result.put(db.getName(), db);
//                LOGGER.info("Extension gefunden."); //$NON-NLS-1$
//            } catch (final CoreException e) {
//                LOGGER.error("Extension nicht gefunden: ", e); //$NON-NLS-1$
//            }
//        }
//        return Collections.unmodifiableMap(result);
//    }
//
//    /**
//     * Für Testzwecke
//     */
//    static void reset() {
//        instance = null;
//    }
//
// }
