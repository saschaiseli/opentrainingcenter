package ch.opentrainingcenter.db.h2.internal;

import java.io.Serializable;

import org.apache.log4j.Logger;
import org.hibernate.Session;

import ch.opentrainingcenter.transfer.IHealth;

public class HealthDao {
    private static final Logger LOG = Logger.getLogger(HealthDao.class);

    private final Dao dao;

    public HealthDao(final Dao dao) {
        this.dao = dao;
    }

    public int saveHealth(final IHealth health) {
        LOG.info("save daily health: " + health); //$NON-NLS-1$
        final Session session = dao.getSession();
        dao.begin();
        final Serializable save = session.save(health);
        dao.commit();
        session.flush();
        return (Integer) save;
    }
}
