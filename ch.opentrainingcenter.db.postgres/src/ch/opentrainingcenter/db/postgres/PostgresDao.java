package ch.opentrainingcenter.db.postgres;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Path;
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

    private File getFile(final String hibernateMappingFile) throws IOException {
        final URL url = FileLocator.find(dbBundle, new Path("hibernate/" + hibernateMappingFile), null); //$NON-NLS-1$
        final URL fileURL = FileLocator.toFileURL(url);
        return new File(fileURL.getPath());
    }

    @Override
    public Session getSession() {
        if (session == null) {
            final org.hibernate.cfg.Configuration configuration = new org.hibernate.cfg.Configuration();
            configuration.setProperties(config.getProperties());
            switch (usage) {
            case PRODUCTION:
                sessionFactory = configuration.configure("hibernate.cfg.xml").buildSessionFactory(); //$NON-NLS-1$
                break;
            case DEVELOPING:
                sessionFactory = configuration.configure("hibernate_dev.cfg.xml").buildSessionFactory(); //$NON-NLS-1$
                break;
            default:
                configuration.configure("hibernate_junit.cfg.xml"); //$NON-NLS-1$
            }
            try {
                // configuration.addClass(ch.opentrainingcenter.transfer.impl.Athlete.class);
                // configuration.addClass(ch.opentrainingcenter.transfer.impl.Health.class);
                // configuration.addClass(ch.opentrainingcenter.transfer.impl.Weather.class);
                // configuration.addClass(ch.opentrainingcenter.transfer.impl.Training.class);
                // configuration.addClass(ch.opentrainingcenter.transfer.impl.Trainingtype.class);
                // configuration.addClass(ch.opentrainingcenter.transfer.impl.Tracktrainingproperty.class);
                // configuration.addClass(ch.opentrainingcenter.transfer.impl.Streckenpunkte.class);
                // configuration.addClass(ch.opentrainingcenter.transfer.impl.Planungwoche.class);
                // configuration.addClass(ch.opentrainingcenter.transfer.impl.Route.class);
                configuration.addResource("Athlete.hbm.xml");//$NON-NLS-1$
                configuration.addResource("Health.hbm.xml"); //$NON-NLS-1$
                configuration.addResource("Weather.hbm.xml"); //$NON-NLS-1$
                configuration.addResource("Training.hbm.xml"); //$NON-NLS-1$
                configuration.addResource("Trainingtype.hbm.xml"); //$NON-NLS-1$
                configuration.addResource("Tracktrainingproperty.hbm.xml"); //$NON-NLS-1$
                configuration.addResource("Streckenpunkte.hbm.xml"); //$NON-NLS-1$
                configuration.addResource("Planungwoche.hbm.xml"); //$NON-NLS-1$
                configuration.addResource("Route.hbm.xml"); //$NON-NLS-1$

                try {
                    final Class<?> driverClass = Class.forName("org.postgresql.Driver");
                    final org.postgresql.Driver dr = (org.postgresql.Driver) driverClass.newInstance();
                    final Driver driver = (Driver) driverClass.newInstance();
                    DriverManager.registerDriver(dr);
                } catch (final ClassNotFoundException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (final InstantiationException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (final IllegalAccessException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (final SQLException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } finally {
                }
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
}
