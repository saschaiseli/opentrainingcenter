package ch.opentrainingcenter.db.h2;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

public class Dao {
    // private static final ThreadLocal session = new ThreadLocal();
    private Session session;
    private final SessionFactory sessionFactory;

    public Dao() {
        org.hibernate.cfg.Configuration c = new org.hibernate.cfg.Configuration();
        sessionFactory = c.configure().buildSessionFactory();
        session = sessionFactory.openSession();
    }

    public Session getSession() {
        if (session == null) {
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
        } catch (HibernateException e) {
            System.out.println(e.getMessage());
        }
        try {
            getSession().close();
        } catch (HibernateException e) {
            System.out.println(e.getMessage());
        }
    }

    public void close() {
        getSession().close();
    }
}
