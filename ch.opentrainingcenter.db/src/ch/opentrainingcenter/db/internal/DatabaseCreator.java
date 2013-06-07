package ch.opentrainingcenter.db.internal;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;

public class DatabaseCreator {

    private final IDao dao;

    public DatabaseCreator(final IDao dao) {
        this.dao = dao;
    }

    public void createDatabase(final List<String> sqlQueries) {
        try {
            final Session session = dao.getSession();
            dao.begin();
            for (final String sql : sqlQueries) {
                System.out.println(sql);
                final Query query = session.createSQLQuery(sql);
                query.executeUpdate();
            }
            dao.commit();
            session.flush();
        } catch (final Exception e) {
            System.err.println(e);
            dao.rollback();
        }
    }
}
