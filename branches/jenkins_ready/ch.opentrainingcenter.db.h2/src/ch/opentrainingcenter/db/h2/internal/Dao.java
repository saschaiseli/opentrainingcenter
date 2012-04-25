package ch.opentrainingcenter.db.h2.internal;

import org.eclipse.core.runtime.Platform;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

public class Dao {
    private static final String DEVELOPING_FLAG = "developing"; //$NON-NLS-1$
    private static boolean DEVELOPING = false;
    private Session session;
    private SessionFactory sessionFactory;

    static {
        final String[] commandLineArgs = Platform.getCommandLineArgs();
        for (final String cmdArg : commandLineArgs) {
            if (cmdArg.contains(DEVELOPING_FLAG)) {
                DEVELOPING = true;
            }
        }
    }

    public Dao() {

    }

    public Session getSession() {
        if (session == null) {
            final org.hibernate.cfg.Configuration c = new org.hibernate.cfg.Configuration();
            if (DEVELOPING) {
                sessionFactory = c.configure("hibernate_dev.cfg.xml").buildSessionFactory(); //$NON-NLS-1$
            } else {
                sessionFactory = c.configure("hibernate.cfg.xml").buildSessionFactory(); //$NON-NLS-1$
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
