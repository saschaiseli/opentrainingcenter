package ch.opentrainingcenter.db.oracle;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import org.apache.log4j.Logger;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.osgi.util.NLS;

import ch.opentrainingcenter.core.db.DatabaseConnectionState;
import ch.opentrainingcenter.core.db.DbConnection;
import ch.opentrainingcenter.core.db.SqlException;
import ch.opentrainingcenter.database.AbstractDatabaseAccess;
import ch.opentrainingcenter.database.dao.CommonDao;
import ch.opentrainingcenter.database.dao.IConnectionConfig;
import ch.opentrainingcenter.i18n.Messages;

public class OracleDatabaseAccess extends AbstractDatabaseAccess {

    private static final Logger LOG = Logger.getLogger(OracleDatabaseAccess.class);
    private static final String DRIVER = "oracle.jdbc.OracleDriver"; //$NON-NLS-1$
    private static final String DIALECT = "org.hibernate.dialect.OracleDialect"; //$NON-NLS-1$

    /**
     * Mit diesem Konstruktur wird mit der eclipse platform der vm args
     * parameters ausgelesen und ausgewertet.
     */
    public OracleDatabaseAccess() {
        super();
    }

    /**
     * Konstruktor für Tests
     */
    public OracleDatabaseAccess(final IConnectionConfig connectionConfig) {
        super();
        createDaos(connectionConfig);
    }

    @Override
    public DbConnection getAdminConnection() {
        return null;
    }

    @Override
    public boolean isUsingAdminDbConnection() {
        return false;
    }

    @Override
    public DatabaseConnectionState validateConnection(final String url, final String user, final String pass, final boolean admin) {
        final String connectionUrl = url + ";user=" + user + ";password=***"; //$NON-NLS-1$ //$NON-NLS-2$
        DatabaseConnectionState result = DatabaseConnectionState.createNewOkState();
        try {
            Class.forName(getConfig().getDbConnection().getDriver());
            final Connection con = DriverManager.getConnection(url, user, pass);
            final Statement statement = con.createStatement();
            if (!admin) {
                // execute a select statement
                statement.execute("Select * from ATHLETE"); //$NON-NLS-1$
            }
            LOG.info(String.format("Connection to database %s successfully", connectionUrl)); //$NON-NLS-1$
        } catch (final SQLException sqlEx) {
            final String sqlState = sqlEx.getLocalizedMessage();
            if (sqlState.startsWith("ORA-28000")) { //$NON-NLS-1$
                result = DatabaseConnectionState.createProblemState(Messages.OracleDatabaseAccess_0);
            } else if (sqlState.startsWith("I/O Exception")) { //$NON-NLS-1$
                result = DatabaseConnectionState.createProblemState(Messages.OracleDatabaseAccess_1);
            } else if (sqlState.startsWith("ORA-01017")) { //$NON-NLS-1$
                result = DatabaseConnectionState.createProblemState(Messages.OracleDatabaseAccess_5);
            } else {
                result = DatabaseConnectionState.createProblemState(NLS.bind(Messages.OracleDatabaseAccess_2, sqlEx.getMessage()));
            }
        } catch (final ClassNotFoundException cnfe) {

        }
        LOG.info(result.getMessage());
        return result;
    }

    @Override
    public DatabaseConnectionState getDatabaseState() {
        final CommonDao cd = getCommonDao();
        DatabaseConnectionState result = DatabaseConnectionState.createNewOkState();
        try {
            cd.getAthlete(0);
        } catch (final Exception e) {
            final Throwable cause = e.getCause();
            if (cause != null) {
                final String localizedMessage = cause.getLocalizedMessage();
                if ("ORA-01017".startsWith(localizedMessage)) { //$NON-NLS-1$
                    result = DatabaseConnectionState.createProblemState(Messages.OracleDatabaseAccess_3);
                }
            }
            result = DatabaseConnectionState.createProblemState(NLS.bind(Messages.OracleDatabaseAccess_4, e.getMessage()));
        }
        LOG.info(result.getMessage());
        return result;
    }

    @Override
    public void createDatabase() throws SqlException {

    }

    @Override
    public String getName() {
        return "Oracle Database"; //$NON-NLS-1$
    }

    @Override
    public String getDriver() {
        return DRIVER;
    }

    @Override
    public String getDialect() {
        return DIALECT;
    }

    @Override
    public File backUpDatabase(final String path) {
        LOG.error("Noch nicht möglich..."); //$NON-NLS-1$
        return null;
    }

    @Override
    public Object create() throws CoreException {
        return new OracleDatabaseAccess();
    }

}
