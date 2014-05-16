package ch.opentrainingcenter.db;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;

import org.apache.log4j.Logger;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.osgi.util.NLS;

import ch.opentrainingcenter.core.db.DBSTATE;
import ch.opentrainingcenter.core.db.DatabaseConnectionState;
import ch.opentrainingcenter.core.db.DbConnection;
import ch.opentrainingcenter.core.db.SqlException;
import ch.opentrainingcenter.database.AbstractDatabaseAccess;
import ch.opentrainingcenter.database.dao.DbScriptReader;
import ch.opentrainingcenter.database.dao.IConnectionConfig;
import ch.opentrainingcenter.i18n.Messages;

public class DatabaseAccess extends AbstractDatabaseAccess {

    private static final Logger LOG = Logger.getLogger(DatabaseAccess.class);
    private static final String DRIVER = "org.h2.Driver"; //$NON-NLS-1$
    private static final String DIALOECT = "org.hibernate.dialect.H2Dialect"; //$NON-NLS-1$

    /**
     * Mit diesem Konstruktur wird mit der eclipse platform der vm args
     * parameters ausgelesen und ausgewertet.
     */
    public DatabaseAccess() {
        super();
    }

    /**
     * Konstruktor für Tests
     */
    public DatabaseAccess(final IConnectionConfig connectionConfig) {
        super();
        createDaos(connectionConfig);
    }

    @Override
    public String getName() {
        return "H2 Database"; //$NON-NLS-1$
    }

    @Override
    public Object create() throws CoreException {
        return new DatabaseAccess();
    }

    @Override
    public DatabaseConnectionState validateConnection(final String url, final String user, final String pass, final boolean admin) {
        return getDatabaseState();
    }

    @Override
    public DatabaseConnectionState getDatabaseState() {
        DatabaseConnectionState result = DatabaseConnectionState.createNewOkState();
        try {
            getCommonDao().getAthlete(1);
        } catch (final Exception e) {
            final Throwable cause = e.getCause();

            // TODO Mapping auf sql nummer
            final String message = cause != null ? cause.getMessage() : e.getMessage();
            if (message != null && message.contains("Locked by another process")) { //$NON-NLS-1$
                LOG.error("Database Locked by another process"); //$NON-NLS-1$
                result = DatabaseConnectionState.createState(Messages.DatabaseAccess_0, DBSTATE.LOCKED);
            } else if (message != null && message.contains("Wrong user name or password")) { //$NON-NLS-1$
                LOG.error("Wrong user name or password"); //$NON-NLS-1$
                result = DatabaseConnectionState.createState(Messages.DatabaseAccess_1, DBSTATE.CONFIG_PROBLEM);
            } else if (message != null && message.contains("Tabelle x existiert nicht")) {
                result = DatabaseConnectionState.createState("Datenbank existier nicht", DBSTATE.CREATE_DB);
            } else {
                LOG.error(String.format("Fehler mit der Datenbank: %s", message), e); //$NON-NLS-1$
                result = DatabaseConnectionState.createProblemState(NLS.bind(Messages.DatabaseAccess_2, message));
            }
        }
        return result;
    }

    @Override
    public boolean isUsingAdminDbConnection() {
        return false;
    }

    @Override
    public void createDatabase() throws SqlException {
        try {
            final InputStream in = DatabaseAccess.class.getClassLoader().getResourceAsStream("otc.sql"); //$NON-NLS-1$
            getDatabaseCreator().createDatabase(DbScriptReader.readDbScript(in));
        } catch (final FileNotFoundException fnne) {
            throw new SqlException(fnne);
        }
    }

    @Override
    public File backUpDatabase(final String path) {
        return getDatabaseCreator().backUpDatabase(path);
    }

    @Override
    public DbConnection getAdminConnection() {
        // da keine admin connection gebraucht wird.
        return null;
    }

    @Override
    public String getDriver() {
        return DRIVER;
    }

    @Override
    public String getDialect() {
        return DIALOECT;
    }

}
