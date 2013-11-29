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
import org.postgresql.util.PSQLException;

import ch.opentrainingcenter.core.db.DBSTATE;
import ch.opentrainingcenter.core.db.DatabaseConnectionConfiguration.DB_MODE;
import ch.opentrainingcenter.core.db.DbConnection;
import ch.opentrainingcenter.core.db.SqlException;
import ch.opentrainingcenter.database.AbstractDatabaseAccess;
import ch.opentrainingcenter.database.USAGE;
import ch.opentrainingcenter.database.dao.ConnectionConfig;
import ch.opentrainingcenter.database.dao.DbScriptReader;
import ch.opentrainingcenter.database.dao.IConnectionConfig;

@SuppressWarnings("nls")
public class DatabaseAccessPostgres extends AbstractDatabaseAccess {
    private static final Logger LOG = Logger.getLogger(DatabaseAccessPostgres.class);
    private final static String DRIVER = "org.postgresql.Driver";
    private final static String DIALECT = "org.hibernate.dialect.PostgreSQLDialect";

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
    public void init() {
        if (developing) {
            this.connectionConfig = new ConnectionConfig(USAGE.DEVELOPING, config);
        } else {
            this.connectionConfig = new ConnectionConfig(USAGE.PRODUCTION, config);
        }
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
            databaseCreator.createDatabase(DbScriptReader.readDbScript(in));
        } catch (final FileNotFoundException fnne) {
            throw new SqlException(fnne);
        }
    }

    @Override
    public File backUpDatabase(final String path) {
        return new File(path, "pleaseBackUpYourself.sql");
    }

    private void createDB() {
        Connection conn = null;
        Statement stmt = null;
        ResultSet db = null;
        ResultSet user = null;
        try {
            Class.forName(config.getDriver(DB_MODE.ADMIN));
            conn = DriverManager.getConnection(config.getUrl(DB_MODE.ADMIN), config.getUsername(DB_MODE.ADMIN), config.getPassword(DB_MODE.ADMIN));
            stmt = conn.createStatement();
            user = stmt.executeQuery("SELECT COUNT(*) FROM pg_user WHERE usename='" + config.getUsername(DB_MODE.APPLICATION) + "'");
            user.next();
            final int count = user.getInt("count");
            if (count == 0) {
                stmt.execute("CREATE USER " + config.getUsername(DB_MODE.APPLICATION) + " WITH PASSWORD '" + config.getPassword(DB_MODE.APPLICATION) + "'");
            }
            db = stmt.executeQuery("SELECT count(*) from pg_database where datname='" + config.getDatabaseName(DB_MODE.APPLICATION) + "'");
            db.next();
            final int countDb = db.getInt("count");
            if (countDb == 0) {
                stmt.execute("CREATE DATABASE " + connectionConfig.getUsage().getDbName());
            }
            stmt.execute(String.format("ALTER DATABASE %s OWNER TO %s", connectionConfig.getUsage().getDbName(), config.getUsername(DB_MODE.APPLICATION)));
            stmt.execute("ALTER SCHEMA PUBLIC OWNER TO " + config.getUsername(DB_MODE.APPLICATION));
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
        return "Postgres Database";
    }

    @Override
    public DBSTATE validateConnection(final String url, final String user, final String pass, final boolean admin) {
        final String connectionUrl = url + ";user=" + user + ";password=***";
        DBSTATE result = DBSTATE.OK;
        try {
            Class.forName(config.getDbConnection().getDriver());
            final Connection con = DriverManager.getConnection(url, user, pass);
            final Statement statement = con.createStatement();
            if (!admin) {
                // execute a select statement
                statement.execute("Select * from ATHLETE");
            }
            LOG.info(String.format("Connection to database %s successfully", connectionUrl));
        } catch (final PSQLException plsqlEx) {
            final String sqlState = plsqlEx.getSQLState();
            if (sqlState.equals("08001")) {
                LOG.info("DBSTATE.PROBLEM: " + plsqlEx);
                result = DBSTATE.PROBLEM;
            } else if (sqlState.equals("3D000") || sqlState.equals("28P01")) {
                LOG.info(String.format("DBSTATE.CREATE_DB: %s", plsqlEx));
                result = DBSTATE.CREATE_DB;
            } else {
                LOG.error(String.format("Unbehandelter Datenbankfehler: %s", plsqlEx));
                result = DBSTATE.PROBLEM;
            }
        } catch (final ClassNotFoundException | SQLException e) {
            LOG.error(String.format("Connection to database %s failed", connectionUrl));
        }
        return result;
    }

    @Override
    public DBSTATE getDatabaseState() {
        DBSTATE result = DBSTATE.OK;
        final DbConnection adminConn = config.getAdminConnection();
        final String adminUrl = adminConn.getUrl();
        final String adminUser = adminConn.getUsername();
        final String adminPw = adminConn.getPassword();
        final DBSTATE adminStatus = validateConnection(adminUrl, adminUser, adminPw, true);
        if (DBSTATE.OK.equals(adminStatus)) {
            LOG.info("Admin Connection funktioniert, nun testen ob die user db mit dem user erstellt werden kann");
            try {
                Class.forName(config.getDbConnection().getDriver());
                final Connection con = DriverManager.getConnection(adminUrl, adminUser, adminPw);
                final Statement statement = con.createStatement();
                final String url = config.getDbConnection().getUrl();
                final String dbName = url.substring(url.lastIndexOf('/') + 1, url.length());

                final ResultSet rs = statement
                        .executeQuery("SELECT pg_database.datname,pg_user.usename FROM pg_database, pg_user WHERE pg_database.datdba = pg_user.usesysid UNION SELECT pg_database.datname, NULL FROM pg_database WHERE pg_database.datdba NOT IN (SELECT usesysid FROM pg_user)");

                boolean existsDb = false;
                while (rs.next()) {
                    final String datname = rs.getString("datname");
                    final String usename = rs.getString("usename");
                    if (datname.equals(dbName)) {
                        if (!usename.equals(config.getDbConnection().getUsername())) {
                            LOG.info(String.format("Datenbank %s existiert bereits unter dem User %s. Kann nicht neu erstellt werden", datname, usename));
                            result = DBSTATE.PROBLEM;
                        }
                        existsDb = true;
                        break;
                    }
                }
                if (!existsDb) {
                    LOG.info(String.format("Die Datenbank %s existiert noch nicht und kann auch erstellt werden.", dbName));
                    result = DBSTATE.CREATE_DB;
                }
            } catch (final ClassNotFoundException | SQLException ex) {
                result = DBSTATE.PROBLEM;
            }
        } else {
            LOG.error("DB Admin User / URL stimmt nicht");
            result = DBSTATE.PROBLEM;
        }
        return result;
    }

    @Override
    public DbConnection getAdminConnection() {
        return config.getAdminConnection();
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
