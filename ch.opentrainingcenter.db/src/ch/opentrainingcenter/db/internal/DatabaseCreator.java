package ch.opentrainingcenter.db.internal;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.hibernate.Query;
import org.hibernate.Session;

public class DatabaseCreator {

    private final Dao dao;

    public DatabaseCreator(final Dao dao) {
        this.dao = dao;
    }

    public void createDatabase() {
        try {
            final Session session = dao.getSession();
            dao.begin();

            final InputStream sqlStream = this.getClass().getClassLoader().getResourceAsStream("otc.sql"); //$NON-NLS-1$
            final BufferedReader bufRead = new BufferedReader(new InputStreamReader(sqlStream));
            final StringBuilder builder = new StringBuilder();
            int nextchar;
            while ((nextchar = bufRead.read()) != -1) {
                builder.append((char) nextchar);
            }
            final Query query = session.createSQLQuery(builder.toString());
            query.executeUpdate();
            dao.commit();
            session.flush();
        } catch (final Exception e) {
            dao.rollback();
        }
    }
}
