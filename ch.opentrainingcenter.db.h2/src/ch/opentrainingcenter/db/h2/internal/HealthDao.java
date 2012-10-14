package ch.opentrainingcenter.db.h2.internal;

import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

import ch.opentrainingcenter.transfer.IAthlete;
import ch.opentrainingcenter.transfer.IHealth;

public class HealthDao {
    private static final Logger LOG = Logger.getLogger(HealthDao.class);

    private final Dao dao;

    public HealthDao(final Dao dao) {
        this.dao = dao;
    }

    public void saveOrUpdate(final IHealth health) {
        LOG.info("save daily health: " + health); //$NON-NLS-1$

        IHealth exists = getHealth(health.getAthlete(), health.getDateofmeasure());
        if (exists != null) {
            exists.setCardio(health.getCardio());
            exists.setWeight(health.getWeight());
        } else {
            exists = health;
        }
        final Session session = dao.getSession();
        dao.begin();
        session.saveOrUpdate(exists);
        dao.commit();
        session.flush();
    }

    /**
     * gibt gesundheitszustand zurück. wenn nichts gefunden wird. wird null
     * zurückgegeben.
     */
    public IHealth getHealth(final IAthlete athlete, final Date date) {
        LOG.info("load health from: " + athlete); //$NON-NLS-1$
        final Session session = dao.getSession();
        dao.begin();

        final Criteria criteria = session.createCriteria(IHealth.class);
        criteria.add(Restrictions.eq("athlete", athlete));
        criteria.add(Restrictions.eq("dateofmeasure", date));
        IHealth health = null;
        @SuppressWarnings("unchecked")
        final List<IHealth> healthRecords = criteria.list();
        if (notEmpty(healthRecords)) {
            health = healthRecords.get(0);
        }
        dao.commit();
        session.flush();
        return health;
    }

    private boolean notEmpty(final List<?> records) {
        return records != null && !records.isEmpty();
    }
}
