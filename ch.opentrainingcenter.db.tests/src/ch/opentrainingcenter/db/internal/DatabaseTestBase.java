package ch.opentrainingcenter.db.internal;

import org.hibernate.Query;
import org.hibernate.Session;
import org.junit.AfterClass;
import org.junit.BeforeClass;

import ch.opentrainingcenter.core.db.DatabaseConnectionConfiguration;
import ch.opentrainingcenter.core.db.DbConnection;
import ch.opentrainingcenter.core.db.SqlException;
import ch.opentrainingcenter.db.DatabaseAccess;
import ch.opentrainingcenter.db.USAGE;

/**
 * Basis für Datebank Tests. Die Tests werden gegen eine h2 db gemacht.
 * 
 * @author sascha
 * 
 */
@SuppressWarnings("nls")
public class DatabaseTestBase {

    private static final String USER = "sa";
    private static final String DRIVER = "org.h2.Driver";
    private static final String URL = "jdbc:h2:file:~/.otc_junit/";
    private static final String DIALECT = "org.hibernate.dialect.H2Dialect";
    protected static IDao dao = null;

    @BeforeClass
    public static void createDb() throws SqlException {
        dao = new Dao(USAGE.TEST, new DatabaseConnectionConfiguration(new DbConnection(DRIVER, URL + USAGE.TEST.getDbName(), USER, ""), DIALECT));
        final DatabaseAccess access = new DatabaseAccess(dao);
        access.createDatabase();
    }

    @AfterClass
    public static void afterClass() {
        truncateDb();
    }

    protected static void truncateDb() {
        try {
            final Session session = dao.getSession();
            dao.begin();

            final Query query = session.createSQLQuery("drop all objects;");
            query.executeUpdate();

            dao.commit();
            session.flush();
        } catch (final Exception e) {
            dao.rollback();
        }
    }
}
