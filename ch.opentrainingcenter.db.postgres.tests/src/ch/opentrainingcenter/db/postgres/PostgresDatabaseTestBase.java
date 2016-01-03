package ch.opentrainingcenter.db.postgres;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.BeforeClass;

import ch.opentrainingcenter.core.db.DatabaseConnectionConfiguration;
import ch.opentrainingcenter.core.db.DbConnection;
import ch.opentrainingcenter.core.db.SqlException;
import ch.opentrainingcenter.database.USAGE;
import ch.opentrainingcenter.database.dao.CommonDao;
import ch.opentrainingcenter.database.dao.ConnectionConfig;

@SuppressWarnings("nls")
public class PostgresDatabaseTestBase {
    private static final Logger LOGGER = Logger.getLogger(PostgresDatabaseTestBase.class);
    private static final String USER = "otc_user";
    private static final String USER_ADMIN = "postgres";
    private static final String PASS = "otc_user";
    private static final String PASS_ADMIN = "postgres";
    private static final String DRIVER = "org.postgresql.Driver";
    private static final String URL = "jdbc:postgresql://localhost/otc_junit";
    private static final String URL_ADMIN = "jdbc:postgresql://localhost/postgres";

    private static final String DIALECT = "org.hibernate.dialect.PostgreSQLDialect";
    protected static ConnectionConfig connectionConfig = null;

    protected static DatabaseAccessPostgres dataConnection;
    protected static CommonDao dataAccess;

    @BeforeClass
    public static void createDb() throws SqlException {
        final DbConnection appConnection = new DbConnection(DRIVER, DIALECT, URL, USER, PASS);
        final DbConnection adminConnection = new DbConnection(DRIVER, DIALECT, URL_ADMIN, USER_ADMIN, PASS_ADMIN);
        final DatabaseConnectionConfiguration config = new DatabaseConnectionConfiguration(appConnection, adminConnection);
        connectionConfig = ConnectionConfig.getInstance(USAGE.TEST, config);
        dataConnection = new DatabaseAccessPostgres(connectionConfig);
        dataConnection.setConfig(config);
        dataConnection.createDatabase();

        dataAccess = new CommonDao(connectionConfig);
    }

    @After
    public void tearDown() throws SqlException {
        final Session session = connectionConfig.getSession();
        connectionConfig.begin();
        session.createSQLQuery("delete from ROUTE").executeUpdate();
        session.createSQLQuery("delete from TRACKTRAININGPROPERTY").executeUpdate();
        session.createSQLQuery("delete from TRAINING").executeUpdate();
        session.createSQLQuery("delete from STRECKENPUNKTE").executeUpdate();
        session.createSQLQuery("delete from HEALTH").executeUpdate();
        session.createSQLQuery("delete from PLANUNGWOCHE").executeUpdate();
        session.createSQLQuery("delete from ATHLETE WHERE id>0").executeUpdate();
        connectionConfig.commit();
        session.flush();
    }

    @AfterClass
    public static void afterClass() {
        truncateDb();
    }

    private static void truncateDb() {

        Connection conn = null;
        Statement stmt = null;
        try {
            Class.forName("org.postgresql.Driver");
            conn = DriverManager.getConnection(URL_ADMIN, USER_ADMIN, PASS_ADMIN);
            stmt = conn.createStatement();
            stmt.execute("drop schema public cascade;");
            stmt.execute("CREATE SCHEMA PUBLIC;");
            stmt.execute("ALTER SCHEMA PUBLIC OWNER TO " + USER);
        } catch (final SQLException se) {
            LOGGER.error(se);
        } catch (final Exception e) {
            LOGGER.error(e);
        } finally {
            try {
                if (stmt != null) {
                    stmt.close();
                }
            } catch (final SQLException se2) {
                LOGGER.error(se2);
            }
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (final SQLException se) {
                LOGGER.error(se);
            }
        }
    }
}
