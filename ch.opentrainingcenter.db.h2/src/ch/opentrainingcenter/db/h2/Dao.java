package ch.opentrainingcenter.db.h2;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

public class Dao {
    private static final ThreadLocal session = new ThreadLocal();
    private final SessionFactory sessionFactory;

    public Dao() {
        org.hibernate.cfg.Configuration c = new org.hibernate.cfg.Configuration();
        sessionFactory = c.configure().buildSessionFactory();
    }

    @SuppressWarnings("unchecked")
    public Session getSession() {
        Session session = (Session) Dao.session.get();
        if (session == null) {
            session = sessionFactory.openSession();
            Dao.session.set(session);
        }
        return session;
    }

    protected void begin() {
        getSession().beginTransaction();
    }

    protected void commit() {
        getSession().getTransaction().commit();
    }

    @SuppressWarnings("unchecked")
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
        Dao.session.set(null);
    }

    @SuppressWarnings("unchecked")
    public void close() {
        getSession().close();
        Dao.session.set(null);
    }
}
