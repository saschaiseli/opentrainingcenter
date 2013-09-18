package ch.opentrainingcenter.db.internal;

import java.io.File;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Query;
import org.hibernate.Session;

@SuppressWarnings("nls")
public class DatabaseCreator {

    private static final Logger LOG = Logger.getLogger(DatabaseCreator.class);

    private final IDao dao;

    public DatabaseCreator(final IDao dao) {
        this.dao = dao;
    }

    public void createDatabase(final List<String> sqlQueries) {
        try {
            final Session session = dao.getSession();
            dao.begin();
            for (final String sql : sqlQueries) {
                LOG.info("Execute: " + sql);
                final Query query = session.createSQLQuery(sql);
                query.executeUpdate();
            }
            dao.commit();
            session.flush();
        } catch (final Exception e) {
            LOG.warn(e);
            dao.rollback();
        }
    }

    public File backUpDatabase(final String path) {
        try {
            final Session session = dao.getSession();
            dao.begin();
            final Query query = session.createSQLQuery("BACKUP TO '" + path + File.separator + "myBackup.zip'");
            query.executeUpdate();
            dao.commit();
            session.flush();
        } catch (final Exception e) {
            LOG.warn(e);
            dao.rollback();
        }
        return new File(path, "myBackup.zip");
    }
}
