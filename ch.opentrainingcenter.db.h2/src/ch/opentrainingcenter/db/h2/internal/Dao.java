package ch.opentrainingcenter.db.h2.internal;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

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

    public Dao(final USAGE usage) {
        this.usage = usage;
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

    protected Transaction begin() {
        return getSession().beginTransaction();
    }

    protected void commit() {
        getSession().getTransaction().commit();
    }

    protected void rollback() {
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
