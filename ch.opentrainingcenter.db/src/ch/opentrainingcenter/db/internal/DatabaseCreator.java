package ch.opentrainingcenter.db.internal;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.hibernate.Query;
import org.hibernate.Session;

public class DatabaseCreator {

    private final IDao dao;

    public DatabaseCreator(final IDao dao) {
        this.dao = dao;
    }

    public void createDatabase(final InputStream sqlStream) {
        try {
            final Session session = dao.getSession();
            dao.begin();
            final BufferedReader bufRead = new BufferedReader(new InputStreamReader(sqlStream));
            final StringBuilder builder = new StringBuilder();
            int nextchar;
            while ((nextchar = bufRead.read()) != -1) {
                builder.append((char) nextchar);
            }
            final String sql = builder.toString();
            final Query query = session.createSQLQuery(sql);
            query.executeUpdate();
            dao.commit();
            session.flush();
        } catch (final Exception e) {
            dao.rollback();
        }
    }
}
