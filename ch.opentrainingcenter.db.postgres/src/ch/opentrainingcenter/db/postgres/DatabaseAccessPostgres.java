package ch.opentrainingcenter.db.postgres;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.apache.log4j.Logger;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.osgi.util.NLS;
import org.postgresql.util.PSQLException;

import ch.opentrainingcenter.core.db.DBSTATE;
import ch.opentrainingcenter.core.db.DatabaseConnectionConfiguration;
import ch.opentrainingcenter.core.db.DatabaseConnectionConfiguration.DB_MODE;
import ch.opentrainingcenter.core.db.DatabaseConnectionState;
import ch.opentrainingcenter.core.db.DbConnection;
import ch.opentrainingcenter.core.db.SqlException;
import ch.opentrainingcenter.database.AbstractDatabaseAccess;
import ch.opentrainingcenter.database.dao.DbScriptReader;
import ch.opentrainingcenter.database.dao.IConnectionConfig;
import ch.opentrainingcenter.i18n.Messages;

public class DatabaseAccessPostgres extends AbstractDatabaseAccess {
    private static final String COUNT = "count";
    private static final Logger LOG = Logger.getLogger(DatabaseAccessPostgres.class);
    private static final String DRIVER = "org.postgresql.Driver"; //$NON-NLS-1$
    private static final String DIALECT = "org.hibernate.dialect.PostgreSQLDialect"; //$NON-NLS-1$

    private IConnectionConfig connectionConfig;

    /**
     * Mit diesem Konstruktur wird mit der eclipse platform der vm args
     * parameters ausgelesen und ausgewertet.
     */
    public DatabaseAccessPostgres() {
    }

    /**
     * Konstruktor für Tests
     */
    public DatabaseAccessPostgres(final IConnectionConfig connectionConfig) {
        super();
        this.connectionConfig = connectionConfig;
        createDaos(connectionConfig);
    }

    @Override
    public Object create() throws CoreException {
        return new DatabaseAccessPostgres();
    }

    @Override
    public boolean isUsingAdminDbConnection() {
        return true;
    }

    @Override
    public void createDatabase() throws SqlException {
        createDB();
        connectionConfig.begin();
        connectionConfig.getSession();
        try {
            final InputStream in = DatabaseAccessPostgres.class.getClassLoader().getResourceAsStream("otc_postgres.sql"); //$NON-NLS-1$
            getDatabaseCreator().createDatabase(DbScriptReader.readDbScript(in));
        } catch (final FileNotFoundException fnne) {
            throw new SqlException(fnne);
        }
    }

    @Override
    public File backUpDatabase(final String path) {
        return new File(path, "pleaseBackUpYourself.sql"); //$NON-NLS-1$
    }

    private void createDB() {
        Connection conn = null;
        Statement stmt = null;
        ResultSet db = null;
        ResultSet user = null;
        try {
            final DatabaseConnectionConfiguration cfg = getConfig();
            Class.forName(cfg.getDriver(DB_MODE.ADMIN));
            conn = DriverManager.getConnection(cfg.getUrl(DB_MODE.ADMIN), getConfig().getUsername(DB_MODE.ADMIN), cfg.getPassword(DB_MODE.ADMIN));
            stmt = conn.createStatement();
            user = stmt.executeQuery("SELECT COUNT(*) FROM pg_user WHERE usename='" + cfg.getUsername(DB_MODE.APPLICATION) + "'"); //$NON-NLS-1$ //$NON-NLS-2$
            user.next();
            final int count = user.getInt(COUNT);
            if (count == 0) {
                stmt.execute("CREATE USER " + cfg.getUsername(DB_MODE.APPLICATION) + " WITH PASSWORD '" + cfg.getPassword(DB_MODE.APPLICATION) + "'"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
            }
            db = stmt.executeQuery("SELECT count(*) from pg_database where datname='" + cfg.getDatabaseName(DB_MODE.APPLICATION) + "'"); //$NON-NLS-1$ //$NON-NLS-2$
            db.next();
            final int countDb = db.getInt(COUNT);
            if (countDb == 0) {
                stmt.execute("CREATE DATABASE " + connectionConfig.getUsage().getDbName()); //$NON-NLS-1$
            }
            stmt.execute(String.format("ALTER DATABASE %s OWNER TO %s", connectionConfig.getUsage().getDbName(), cfg.getUsername(DB_MODE.APPLICATION))); //$NON-NLS-1$
            stmt.execute("ALTER SCHEMA PUBLIC OWNER TO " + cfg.getUsername(DB_MODE.APPLICATION)); //$NON-NLS-1$
        } catch (final SQLException se) {
            LOG.error(se);
        } catch (final Exception e) {
            LOG.error(e);
        } finally {
            close(stmt);
            close(db);
            close(user);
            close(conn);
        }
    }

    private void close(final Connection conn) {
        try {
            if (conn != null) {
                conn.close();
            }
        } catch (final SQLException se) {
            LOG.error(se);
        }
    }

    private void close(final ResultSet db) {
        try {
            if (db != null) {
                db.close();
            }
        } catch (final SQLException se) {
            LOG.error(se);
        }
    }

    private void close(final Statement stmt) {
        try {
            if (stmt != null) {
                stmt.close();
            }
        } catch (final SQLException se) {
            LOG.error(se);
        }
    }

    @Override
    public String getName() {
        return "Postgres Database"; //$NON-NLS-1$
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
        } catch (final PSQLException plsqlEx) {
            final String sqlState = plsqlEx.getSQLState();
            if (sqlState.equals("08001")) { //$NON-NLS-1$
                LOG.info(String.format("DBSTATE.PROBLEM: %s", plsqlEx)); //$NON-NLS-1$
                result = DatabaseConnectionState.createProblemState(Messages.DatabaseAccessPostgres_12);
            } else if (sqlState.equals("3D000") || sqlState.equals("28P01")) { //$NON-NLS-1$//$NON-NLS-2$
                LOG.info(String.format("DBSTATE.CREATE_DB: %s", plsqlEx)); //$NON-NLS-1$
                result = DatabaseConnectionState.createState(Messages.DatabaseAccessPostgres_14, DBSTATE.CREATE_DB);
            } else {
                LOG.error(String.format("Unbehandelter Datenbankfehler: %s", plsqlEx)); //$NON-NLS-1$
                result = DatabaseConnectionState.createProblemState(NLS.bind(Messages.DatabaseAccessPostgres_15, sqlState));
            }
        } catch (final ClassNotFoundException | SQLException e) {
            LOG.error(String.format("Connection to database %s failed", connectionUrl), e); //$NON-NLS-1$
            result = DatabaseConnectionState.createProblemState(NLS.bind(Messages.DatabaseAccessPostgres_15, e.getMessage()));
        }
        return result;
    }

    @Override
    public DatabaseConnectionState getDatabaseState() {
        DatabaseConnectionState result = DatabaseConnectionState.createNewOkState();
        final DatabaseConnectionConfiguration cfg = getConfig();
        final DbConnection adminConn = cfg.getAdminConnection();
        final String adminUrl = adminConn.getUrl();
        final String adminUser = adminConn.getUsername();
        final String adminPw = adminConn.getPassword();
        final DBSTATE adminStatus = validateConnection(adminUrl, adminUser, adminPw, true).getState();
        if (DBSTATE.OK.equals(adminStatus)) {
            LOG.info("Admin Connection funktioniert, nun testen ob die user db mit dem user erstellt werden kann"); //$NON-NLS-1$
            try {
                Class.forName(cfg.getDbConnection().getDriver());
                final Connection con = DriverManager.getConnection(adminUrl, adminUser, adminPw);
                final Statement statement = con.createStatement();
                final String url = cfg.getDbConnection().getUrl();
                final String dbName = url.substring(url.lastIndexOf('/') + 1, url.length());

                final ResultSet rs = statement
                        .executeQuery("SELECT pg_database.datname,pg_user.usename FROM pg_database, pg_user WHERE pg_database.datdba = pg_user.usesysid UNION SELECT pg_database.datname, NULL FROM pg_database WHERE pg_database.datdba NOT IN (SELECT usesysid FROM pg_user)"); //$NON-NLS-1$

                boolean existsDb = false;
                while (rs.next()) {
                    final String datname = rs.getString("datname"); //$NON-NLS-1$
                    final String usename = rs.getString("usename"); //$NON-NLS-1$
                    if (datname.equals(dbName)) {
                        if (!usename.equals(cfg.getDbConnection().getUsername())) {
                            final String msg = NLS.bind(Messages.DatabaseAccessPostgres_17, datname, usename);
                            LOG.info(msg);
                            result = DatabaseConnectionState.createProblemState(msg);
                        }
                        existsDb = true;
                        break;
                    }
                }
                if (!existsDb) {
                    final String msg = NLS.bind(Messages.DatabaseAccessPostgres_18, dbName);
                    LOG.info(msg);
                    result = DatabaseConnectionState.createState(msg, DBSTATE.CREATE_DB);
                }
            } catch (final ClassNotFoundException | SQLException ex) {
                LOG.error(ex);
                result = DatabaseConnectionState.createProblemState(ex.getMessage());
            }
        } else {
            final String msg = Messages.DatabaseAccessPostgres_19;
            LOG.error(msg);
            result = DatabaseConnectionState.createProblemState(msg);
        }
        return result;
    }

    @Override
    public DbConnection getAdminConnection() {
        return getConfig().getAdminConnection();
    }

    @Override
    public String getDriver() {
        return DRIVER;
    }

    @Override
    public String getDialect() {
        return DIALECT;
    }

}
