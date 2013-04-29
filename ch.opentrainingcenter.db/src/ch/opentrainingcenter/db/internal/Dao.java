package ch.opentrainingcenter.db.internal;

import org.hibernate.HibernateException;
import org.hibernate.MappingException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import ch.opentrainingcenter.core.db.DatabaseConnectionConfiguration;
import ch.opentrainingcenter.db.USAGE;

public class Dao implements IDao {

    private Session session;
    private SessionFactory sessionFactory;
    private final USAGE usage;
    private final DatabaseConnectionConfiguration config;

    public Dao(final USAGE usage, final DatabaseConnectionConfiguration config) {
        this.usage = usage;
        this.config = config;
    }

    /*
     * (non-Javadoc)
     * 
     * @see ch.opentrainingcenter.db.internal.IDao#begin()
     */
    @Override
    public Transaction begin() {
        return getSession().beginTransaction();
    }

    /*
     * (non-Javadoc)
     * 
     * @see ch.opentrainingcenter.db.internal.IDao#close()
     */
    @Override
    public void close() {
        getSession().close();
    }

    /*
     * (non-Javadoc)
     * 
     * @see ch.opentrainingcenter.db.internal.IDao#commit()
     */
    @Override
    public void commit() {
        getSession().getTransaction().commit();
    }

    /*
     * (non-Javadoc)
     * 
     * @see ch.opentrainingcenter.db.internal.IDao#getSession()
     */
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

    /*
     * (non-Javadoc)
     * 
     * @see ch.opentrainingcenter.db.internal.IDao#getUsage()
     */
    @Override
    public USAGE getUsage() {
        return usage;
    }

    /*
     * (non-Javadoc)
     * 
     * @see ch.opentrainingcenter.db.internal.IDao#rollback()
     */
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
