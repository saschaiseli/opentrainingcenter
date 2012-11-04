package ch.opentrainingcenter.db.h2.internal;

import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

import ch.opentrainingcenter.transfer.IAthlete;
import ch.opentrainingcenter.transfer.IPlanungWoche;

public class PlanungDao {
    private static final Logger LOG = Logger.getLogger(PlanungDao.class);

    private final Dao dao;

    public PlanungDao(final Dao dao) {
        this.dao = dao;
    }

    public List<IPlanungWoche> getPlanungsWoche(final IAthlete athlete, final int jahr, final int kwStart, final int anzahl) {

        LOG.info("load IPlanungWoche from: " + athlete); //$NON-NLS-1$
        final Session session = dao.getSession();
        dao.begin();
        final Criteria criteria = session.createCriteria(IPlanungWoche.class);
        criteria.add(Restrictions.eq("athlete", athlete)); //$NON-NLS-1$
        criteria.add(Restrictions.eq("jahr", jahr)); //$NON-NLS-1$
        criteria.add(Restrictions.ge("kw", kwStart)); //$NON-NLS-1$
        criteria.add(Restrictions.lt("kw", kwStart + anzahl)); //$NON-NLS-1$
        criteria.add(Restrictions.eq("jahr", jahr)); //$NON-NLS-1$
        @SuppressWarnings("unchecked")
        final List<IPlanungWoche> result = criteria.list();
        dao.commit();
        session.flush();
        return result;
    }

    public void saveOrUpdate(final List<IPlanungWoche> planungen) {

        for (final IPlanungWoche planung : planungen) {
            final IPlanungWoche existing = getPlanung(planung.getAthlete(), planung.getJahr(), planung.getKw());
            if (existing == null) {
                save(planung);

            } else {
                existing.setKmProWoche(planung.getKmProWoche());
                existing.setInterval(planung.isInterval());
                save(existing);
            }
        }
    }

    private void save(final IPlanungWoche planung) {
        final Session session = dao.getSession();
        dao.begin();
        session.saveOrUpdate(planung);
        dao.commit();
        session.flush();
    }

    private IPlanungWoche getPlanung(final IAthlete athlete, final int jahr, final int kw) {
        LOG.info("load IPlanungWoche from: " + athlete); //$NON-NLS-1$
        final Session session = dao.getSession();
        dao.begin();
        final Criteria criteria = session.createCriteria(IPlanungWoche.class);
        criteria.add(Restrictions.eq("athlete", athlete)); //$NON-NLS-1$
        criteria.add(Restrictions.eq("jahr", jahr)); //$NON-NLS-1$
        criteria.add(Restrictions.eq("kw", kw)); //$NON-NLS-1$
        IPlanungWoche result = null;
        @SuppressWarnings("unchecked")
        final List<IPlanungWoche> records = criteria.list();
        if (!records.isEmpty()) {
            result = records.get(0);
        }
        dao.commit();
        session.flush();
        return result;
    }

    public List<IPlanungWoche> getPlanungsWoche(final IAthlete athlete) {
        LOG.info("load IPlanungWoche from: " + athlete); //$NON-NLS-1$
        final Session session = dao.getSession();
        dao.begin();
        final Criteria criteria = session.createCriteria(IPlanungWoche.class);
        criteria.add(Restrictions.eq("athlete", athlete)); //$NON-NLS-1$
        @SuppressWarnings("unchecked")
        final List<IPlanungWoche> records = criteria.list();
        dao.commit();
        session.flush();
        return records;
    }
}
