package ch.opentrainingcenter.db.internal;

import java.io.File;
import java.io.IOException;
import java.net.URL;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.hibernate.HibernateException;
import org.hibernate.MappingException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.osgi.framework.Bundle;

import ch.opentrainingcenter.db.DbPluginActivator;

public class Dao {

    public enum USAGE {
        /**
         * Use a production db configuration
         */
        PRODUCTION,
        /**
         * use the otc_dev database
         */
        DEVELOPING,
        /**
         * the content of the database will be deleted after each test.
         */
        TEST
    }

    private Session session;
    private SessionFactory sessionFactory;
    private final USAGE usage;
    private final Bundle dbBundle;

    public Dao(final USAGE usage) {
        this.usage = usage;
        dbBundle = Platform.getBundle(DbPluginActivator.PLUGIN_ID);
    }

    public Session getSession() {
        if (session == null) {
            final org.hibernate.cfg.Configuration configuration = new org.hibernate.cfg.Configuration();
            switch (usage) {
            case PRODUCTION:
                sessionFactory = configuration.configure("hibernate.cfg.xml").buildSessionFactory(); //$NON-NLS-1$
                break;
            case DEVELOPING:
                sessionFactory = configuration.configure("hibernate_dev.cfg.xml").buildSessionFactory(); //$NON-NLS-1$
                break;
            default:
                sessionFactory = configuration.configure("hibernate_junit.cfg.xml").buildSessionFactory(); //$NON-NLS-1$
            }
            session = sessionFactory.openSession();

        }
        return session;
    }

    public Transaction begin() {
        return getSession().beginTransaction();
    }

    public void commit() {
        getSession().getTransaction().commit();
    }

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

    public void close() {
        getSession().close();
    }
}
