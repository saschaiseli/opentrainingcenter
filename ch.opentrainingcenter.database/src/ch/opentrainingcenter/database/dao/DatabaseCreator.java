package ch.opentrainingcenter.database.dao;

import java.io.File;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;

@SuppressWarnings("nls")
public class DatabaseCreator {

    private static final Logger LOG = Logger.getLogger(DatabaseCreator.class);

    private final IConnectionConfig connectionConfig;

    public DatabaseCreator(final IConnectionConfig connectionConfig) {
        this.connectionConfig = connectionConfig;
    }

    public void createDatabase(final List<String> sqlQueries) {
        try {
            final Session session = connectionConfig.getSession();
            connectionConfig.begin();
            for (final String sql : sqlQueries) {
                LOG.info(String.format("Execute: '%s'", sql));
                final Query query = session.createSQLQuery(sql);
                query.executeUpdate();
            }
            connectionConfig.commit();
            session.flush();
        } catch (final HibernateException e) {
            LOG.warn(e);
            connectionConfig.rollback();
        }
    }

    public File backUpDatabase(final String path) {
        try {
            final Session session = connectionConfig.getSession();
            connectionConfig.begin();
            final Query query = session.createSQLQuery(String.format("BACKUP TO '%s%smyBackup.zip'", path, File.separator));
            query.executeUpdate();
            connectionConfig.commit();
            session.flush();
        } catch (final HibernateException e) {
            LOG.warn(e);
            connectionConfig.rollback();
        }
        return new File(path, "myBackup.zip");
    }
}
