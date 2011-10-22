package ch.opentrainingcenter.db.h2;

import java.util.logging.Logger;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

public class Dao {
    @SuppressWarnings("unused")
    private static final Logger log = Logger.getAnonymousLogger();
    @SuppressWarnings("unchecked")
    private static final ThreadLocal session = new ThreadLocal();
    private static final SessionFactory sessionFactory = new org.hibernate.cfg.Configuration().configure().buildSessionFactory();

    protected Dao() {
    }

    @SuppressWarnings("unchecked")
    public static Session getSession() {
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
    public static void close() {
        getSession().close();
        Dao.session.set(null);
    }
}
