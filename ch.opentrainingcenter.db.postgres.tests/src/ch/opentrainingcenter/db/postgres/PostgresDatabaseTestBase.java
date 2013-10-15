package ch.opentrainingcenter.db.postgres;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import org.apache.log4j.Logger;
import org.junit.AfterClass;
import org.junit.BeforeClass;

import ch.opentrainingcenter.core.db.DatabaseConnectionConfiguration;
import ch.opentrainingcenter.core.db.DbConnection;
import ch.opentrainingcenter.core.db.SqlException;
import ch.opentrainingcenter.db.USAGE;

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
    protected static PostgresDao dao = null;

    @BeforeClass
    public static void createDb() throws SqlException {
        final DbConnection appConnection = new DbConnection(DRIVER, DIALECT, URL, USER, PASS);
        final DbConnection adminConnection = new DbConnection(DRIVER, DIALECT, URL_ADMIN, USER_ADMIN, PASS_ADMIN);
        final DatabaseConnectionConfiguration config = new DatabaseConnectionConfiguration(appConnection, adminConnection);
        dao = new PostgresDao(USAGE.TEST, config);
        final DatabaseAccessPostgres access = new DatabaseAccessPostgres(dao);
        access.setConfiguration(config);
        access.createDatabase();
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
            conn = DriverManager.getConnection(URL, USER_ADMIN, PASS_ADMIN);
            stmt = conn.createStatement();
            stmt.execute("drop schema public cascade;");
            stmt.execute("CREATE SCHEMA PUBLIC;");
            stmt.execute("ALTER SCHEMA PUBLIC OWNER TO otc_user");
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
