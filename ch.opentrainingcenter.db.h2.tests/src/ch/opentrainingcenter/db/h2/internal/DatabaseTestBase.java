package ch.opentrainingcenter.db.h2.internal;

import org.hibernate.Query;
import org.hibernate.Session;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;

import ch.opentrainingcenter.db.h2.DatabaseAccess;
import ch.opentrainingcenter.db.h2.internal.Dao.USAGE;

@SuppressWarnings("nls")
public class DatabaseTestBase {

    protected Dao dao;

    @Before
    public void setUp() {
        dao = new Dao(USAGE.TEST);

    }

    @BeforeClass
    public static void before() {
        createDb();
    }

    @AfterClass
    public static void afterClass() {
        truncateDb();
    }

    protected static void createDb() {
        final DatabaseAccess access = new DatabaseAccess(new Dao(USAGE.TEST));
        access.createDatabase();
    }

    protected static void truncateDb() {
        final Dao dao = new Dao(USAGE.TEST);
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
