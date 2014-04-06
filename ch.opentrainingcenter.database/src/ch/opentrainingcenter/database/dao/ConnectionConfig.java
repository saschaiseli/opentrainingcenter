package ch.opentrainingcenter.database.dao;

import org.apache.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.MappingException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import ch.opentrainingcenter.core.assertions.Assertions;
import ch.opentrainingcenter.core.db.DatabaseConnectionConfiguration;
import ch.opentrainingcenter.database.USAGE;

@SuppressWarnings("nls")
public class ConnectionConfig implements IConnectionConfig {

    private static final Logger LOG = Logger.getLogger(ConnectionConfig.class);
    private Session session;
    private SessionFactory sessionFactory;
    private final USAGE usage;
    private final DatabaseConnectionConfiguration config;

    public ConnectionConfig(final USAGE usage, final DatabaseConnectionConfiguration config) {
        this(usage, config, new org.hibernate.cfg.Configuration());
    }

    public ConnectionConfig(final USAGE usage, final DatabaseConnectionConfiguration config, final org.hibernate.cfg.Configuration configuration) {
        Assertions.notNull(config, "Datenbankkonfiguration darf nicht null sein");
        this.usage = usage;
        this.config = config;
        configuration.setProperties(config.getProperties());
        configuration.setProperty("current_session_context_class", "thread");
        configuration.setProperty("cache.provider_class", "org.hibernate.cache.NoCacheProvider");
        configuration.setProperty("hibernate.show_sql", String.valueOf(usage.isShowSql()));
        configuration.setProperty("hibernate.format_sql", String.valueOf(usage.isFormatSql()));
        configuration.setProperty("hibernate.connection.pool_size", String.valueOf(10));
        try {
            configuration.addResource("Athlete.hbm.xml", this.getClass().getClassLoader());
            configuration.addResource("Health.hbm.xml", this.getClass().getClassLoader());
            configuration.addResource("Weather.hbm.xml", this.getClass().getClassLoader());
            configuration.addResource("Training.hbm.xml", this.getClass().getClassLoader());
            configuration.addResource("Trainingtype.hbm.xml", this.getClass().getClassLoader());
            configuration.addResource("Tracktrainingproperty.hbm.xml", this.getClass().getClassLoader());
            configuration.addResource("Streckenpunkte.hbm.xml", this.getClass().getClassLoader());
            configuration.addResource("Planungwoche.hbm.xml", this.getClass().getClassLoader());
            configuration.addResource("Route.hbm.xml", this.getClass().getClassLoader());
            configuration.addResource("LapInfo.hbm.xml", this.getClass().getClassLoader());
            LOG.info("Hibernate Config: show_sql=" + configuration.getProperty("hibernate.show_sql"));
            LOG.info("Hibernate Config: format_sql=" + configuration.getProperty("hibernate.format_sql"));
            LOG.info("Hibernate Config: pool_size=" + configuration.getProperty("hibernate.pool_size"));
            sessionFactory = configuration.buildSessionFactory();
        } catch (final MappingException e) {
            LOG.error(e);
        }
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
            LOG.error(e);
        }
        try {
            getSession().close();
        } catch (final HibernateException e) {
            LOG.error(e);
        }
    }

    @Override
    public DatabaseConnectionConfiguration getConfig() {
        return config;
    }
}
