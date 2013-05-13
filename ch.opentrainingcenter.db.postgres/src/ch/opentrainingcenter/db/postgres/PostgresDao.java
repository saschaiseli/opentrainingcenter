package ch.opentrainingcenter.db.postgres;

import org.eclipse.core.runtime.Platform;
import org.hibernate.HibernateException;
import org.hibernate.MappingException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.osgi.framework.Bundle;

import ch.opentrainingcenter.core.assertions.Assertions;
import ch.opentrainingcenter.core.db.DatabaseConnectionConfiguration;
import ch.opentrainingcenter.db.DbPluginActivator;
import ch.opentrainingcenter.db.USAGE;
import ch.opentrainingcenter.db.internal.IDao;

public class PostgresDao implements IDao {

    private Session session;
    private SessionFactory sessionFactory;
    private final USAGE usage;
    private final DatabaseConnectionConfiguration config;
    private final Bundle dbBundle;

    public PostgresDao(final USAGE usage, final DatabaseConnectionConfiguration config) {
        Assertions.notNull(config);
        this.usage = usage;
        this.config = config;
        dbBundle = Platform.getBundle(DbPluginActivator.PLUGIN_ID);
    }

    @Override
    public Transaction begin() {
        return getSession().beginTransaction();
    }

    @Override
    public void close() {
        getSession().close();
    }

    @Override
    public void commit() {
        getSession().getTransaction().commit();
    }

    @Override
    public Session getSession() {
        if (session == null || !session.isOpen()) {
            final org.hibernate.cfg.Configuration configuration = new org.hibernate.cfg.Configuration();
            configuration.setProperties(config.getProperties());
            // switch (usage) {
            // case PRODUCTION:
            //                sessionFactory = configuration.configure("hibernate.cfg.xml").buildSessionFactory(); //$NON-NLS-1$
            // break;
            // case DEVELOPING:
            //                sessionFactory = configuration.configure("hibernate_dev.cfg.xml").buildSessionFactory(); //$NON-NLS-1$
            // break;
            // default:
            //                configuration.configure("hibernate_junit.cfg.xml"); //$NON-NLS-1$
            // }
            configuration.setProperty("current_session_context_class", "thread");
            configuration.setProperty("cache.provider_class", "org.hibernate.cache.NoCacheProvider");
            configuration.setProperty("show_sql", String.valueOf(usage.isShowSql()));
            configuration.setProperty("format_sql", String.valueOf(usage.isFormatSql()));
            try {
                configuration.addResource("Athlete.hbm.xml");//$NON-NLS-1$
                configuration.addResource("Health.hbm.xml"); //$NON-NLS-1$
                configuration.addResource("Weather.hbm.xml"); //$NON-NLS-1$
                configuration.addResource("Training.hbm.xml"); //$NON-NLS-1$
                configuration.addResource("Trainingtype.hbm.xml"); //$NON-NLS-1$
                configuration.addResource("Tracktrainingproperty.hbm.xml"); //$NON-NLS-1$
                configuration.addResource("Streckenpunkte.hbm.xml"); //$NON-NLS-1$
                configuration.addResource("Planungwoche.hbm.xml"); //$NON-NLS-1$
                configuration.addResource("Route.hbm.xml"); //$NON-NLS-1$
                sessionFactory = configuration.buildSessionFactory();
            } catch (final MappingException e) {
                e.printStackTrace();
            }
            session = sessionFactory.openSession();

        }
        return session;
    }

    @Override
    public USAGE getUsage() {
        return usage;
    }

    @Override
    public void rollback() {
        try {
            getSession().getTransaction().rollback();
        } catch (final HibernateException e) {
            System.out.println(e.getMessage());
        }
        try {
            getSession().close();
        } catch (final HibernateException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public DatabaseConnectionConfiguration getConfig() {
        return config;
    }

}
