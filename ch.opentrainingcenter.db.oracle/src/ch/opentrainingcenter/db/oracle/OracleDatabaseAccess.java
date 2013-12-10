package ch.opentrainingcenter.db.oracle;

import java.io.File;

import org.apache.log4j.Logger;
import org.eclipse.core.runtime.CoreException;

import ch.opentrainingcenter.core.db.DBSTATE;
import ch.opentrainingcenter.core.db.DbConnection;
import ch.opentrainingcenter.core.db.SqlException;
import ch.opentrainingcenter.database.AbstractDatabaseAccess;
import ch.opentrainingcenter.database.dao.IConnectionConfig;

@SuppressWarnings("nls")
public class OracleDatabaseAccess extends AbstractDatabaseAccess {

    private static final Logger LOG = Logger.getLogger(OracleDatabaseAccess.class);
    private static final String DRIVER = "oracle.jdbc.OracleDriver";
    private static final String DIALECT = "org.hibernate.dialect.OracleDialect";

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
    public DBSTATE validateConnection(final String url, final String user, final String pass, final boolean admin) {
        return getDatabaseState();
    }

    @Override
    public DBSTATE getDatabaseState() {
        return DBSTATE.OK;
    }

    @Override
    public void createDatabase() throws SqlException {

    }

    @Override
    public String getName() {
        return "Oracle Database";
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
        LOG.error("Noch nicht möglich...");
        return null;
    }

    @Override
    public Object create() throws CoreException {
        return new OracleDatabaseAccess();
    }

}
