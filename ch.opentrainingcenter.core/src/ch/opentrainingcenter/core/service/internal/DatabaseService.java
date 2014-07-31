package ch.opentrainingcenter.core.service.internal;

import java.util.Map;

import org.apache.log4j.Logger;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.Platform;

import ch.opentrainingcenter.core.assertions.Assertions;
import ch.opentrainingcenter.core.db.DatabaseConnectionConfiguration;
import ch.opentrainingcenter.core.db.DbConnection;
import ch.opentrainingcenter.core.db.IDatabaseAccess;
import ch.opentrainingcenter.core.db.IDatabaseConnection;
import ch.opentrainingcenter.core.service.IDatabaseService;

public class DatabaseService implements IDatabaseService {

    private static final Logger LOGGER = Logger.getLogger(DatabaseService.class);

    private static final String DEVELOPING_FLAG = "developing"; //$NON-NLS-1$

    private static final DatabaseService INSTANCE = new DatabaseService();

    private static final Map<String, IDatabaseConnection> DB_ACCESS;

    private boolean developing;

    private IDatabaseConnection connection;

    static {
        final IConfigurationElement[] daos = Platform.getExtensionRegistry().getConfigurationElementsFor("ch.opentrainingdatabase.db"); //$NON-NLS-1$
        DB_ACCESS = DatabaseExtensionSupport.getDao(daos, IDatabaseConnection.EXTENSION_POINT_NAME);
        System.out.println("***************** DB_ACCESS initialisiert");
    }

    private DatabaseService() {
        final String[] commandLineArgs = Platform.getCommandLineArgs();
        for (final String cmdArg : commandLineArgs) {
            if (cmdArg.contains(DEVELOPING_FLAG)) {
                developing = true;
            }
        }
    }

    public static DatabaseService getInstance() {
        return INSTANCE;
    }

    @Override
    public void init(final String dbName, final String url, final String user, final String pw, final String urlAdmin, final String userAdmin,
            final String pwAdmin) {
        connection = DB_ACCESS.get(dbName);
        Assertions.notNull(connection, "Ausgewählte Datenbank darf nicht null sein"); //$NON-NLS-1$
        connection.setDeveloping(developing);
        final DbConnection dbConnection = getConnection(url, user, pw, developing);
        final DbConnection dbAdminConnection = getAdminConnection(urlAdmin, userAdmin, pwAdmin);
        final DatabaseConnectionConfiguration config = new DatabaseConnectionConfiguration(dbConnection, dbAdminConnection);
        connection.setConfig(config);
        connection.init();
        LOGGER.info("Datenbankverbindung ist initialisiert..."); //$NON-NLS-1$
    }

    private DbConnection getConnection(final String url, final String user, final String pw, final boolean dev) {
        final DbConnection conn = new DbConnection(connection.getDriver(), connection.getDialect());
        if (dev) {
            conn.setUrl(url + "_dev"); //$NON-NLS-1$
        } else {
            conn.setUrl(url);
        }
        conn.setUsername(user);
        conn.setPassword(pw);
        return conn;
    }

    private DbConnection getAdminConnection(final String urlAdmin, final String userAdmin, final String pwAdmin) {
        if (urlAdmin != null && userAdmin != null && pwAdmin != null) {
            final DbConnection dbConnection = new DbConnection(connection.getDriver(), connection.getDialect());
            dbConnection.setUrl(urlAdmin);
            dbConnection.setUsername(userAdmin);
            dbConnection.setPassword(pwAdmin);
            return dbConnection;
        }
        return null;
    }

    @Override
    public IDatabaseAccess getDatabaseAccess() {
        Assertions.notNull(connection, "Datenbank noch nicht initialisiert!"); //$NON-NLS-1$
        return connection.getDataAccess();
    }

    @Override
    public IDatabaseConnection getDatabaseConnection() {
        return connection;
    }

    @Override
    public Map<String, IDatabaseConnection> getAvailableConnections() {
        return DB_ACCESS;
    }
}
