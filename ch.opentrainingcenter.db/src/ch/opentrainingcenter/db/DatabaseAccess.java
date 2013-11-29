package ch.opentrainingcenter.db;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;

import org.apache.log4j.Logger;
import org.eclipse.core.runtime.CoreException;

import ch.opentrainingcenter.core.db.DBSTATE;
import ch.opentrainingcenter.core.db.DbConnection;
import ch.opentrainingcenter.core.db.SqlException;
import ch.opentrainingcenter.database.AbstractDatabaseAccess;
import ch.opentrainingcenter.database.USAGE;
import ch.opentrainingcenter.database.dao.ConnectionConfig;
import ch.opentrainingcenter.database.dao.DbScriptReader;
import ch.opentrainingcenter.database.dao.IConnectionConfig;

@SuppressWarnings("nls")
public class DatabaseAccess extends AbstractDatabaseAccess {

    private static final Logger LOG = Logger.getLogger(DatabaseAccess.class);
    private static final String DRIVER = "org.h2.Driver";
    private static final String DIALOECT = "org.hibernate.dialect.H2Dialect";

    private IConnectionConfig connectionConfig;

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
        this.connectionConfig = connectionConfig;
        createDaos(connectionConfig);
    }

    @Override
    public void init() {
        if (developing) {
            this.connectionConfig = new ConnectionConfig(USAGE.DEVELOPING, config);
        } else {
            this.connectionConfig = new ConnectionConfig(USAGE.PRODUCTION, config);
        }
        createDaos(connectionConfig);
    }

    @Override
    public String getName() {
        return "H2 Database";
    }

    @Override
    public Object create() throws CoreException {
        return new DatabaseAccess();
    }

    @Override
    public DBSTATE validateConnection(final String url, final String user, final String pass, final boolean admin) {
        return getDatabaseState();
    }

    @Override
    public DBSTATE getDatabaseState() {
        try {
            commonDao.getAthlete(1);
        } catch (final Exception e) {
            final Throwable cause = e.getCause();
            final String message = cause != null ? cause.getMessage() : e.getMessage();
            if (message != null && message.contains("Locked by another process")) { //$NON-NLS-1$
                LOG.error("Database Locked by another process"); //$NON-NLS-1$
                return DBSTATE.LOCKED;
            } else if (message != null && message.contains("Wrong user name or password")) { //$NON-NLS-1$
                LOG.error("Wrong user name or password"); //$NON-NLS-1$
                return DBSTATE.CONFIG_PROBLEM;
            } else {
                LOG.error(String.format("Fehler mit der Datenbank: %s", message), e); //$NON-NLS-1$
                return DBSTATE.PROBLEM;
            }
        }
        return DBSTATE.OK;
    }

    @Override
    public boolean isUsingAdminDbConnection() {
        return false;
    }

    @Override
    public void createDatabase() throws SqlException {
        try {
            final InputStream in = DatabaseAccess.class.getClassLoader().getResourceAsStream("otc.sql"); //$NON-NLS-1$
            databaseCreator.createDatabase(DbScriptReader.readDbScript(in));
        } catch (final FileNotFoundException fnne) {
            throw new SqlException(fnne);
        }
    }

    @Override
    public File backUpDatabase(final String path) {
        return databaseCreator.backUpDatabase(path);
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
