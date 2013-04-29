package ch.opentrainingcenter.db.postgres;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import org.junit.AfterClass;
import org.junit.BeforeClass;

import ch.opentrainingcenter.core.db.DatabaseConnectionConfiguration;
import ch.opentrainingcenter.db.USAGE;

public class PostgresDatabaseTestBase {

    private static final String USER = "otc_user";
    private static final String DRIVER = "org.postgresql.Driver";
    private static final String URL = "jdbc:postgresql://localhost/otc_junit";
    private static final String DIALECT = "org.hibernate.dialect.PostgreSQLDialect";
    protected static PostgresDao dao = null;

    @BeforeClass
    public static void createDb() {
        final DatabaseConnectionConfiguration config = new DatabaseConnectionConfiguration(DRIVER, URL, USER, "otc_user", DIALECT);
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
            conn = DriverManager.getConnection("jdbc:postgresql://localhost/otc_junit", "postgres", "blabla");
            stmt = conn.createStatement();
            stmt.execute("drop schema public cascade;");
            stmt.execute("CREATE SCHEMA PUBLIC;");
            stmt.execute("ALTER SCHEMA PUBLIC OWNER TO otc_user");
        } catch (final SQLException se) {
            se.printStackTrace();
        } catch (final Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (stmt != null)
                    stmt.close();
            } catch (final SQLException se2) {
            }
            try {
                if (conn != null)
                    conn.close();
            } catch (final SQLException se) {
                se.printStackTrace();
            }
        }
    }
}
