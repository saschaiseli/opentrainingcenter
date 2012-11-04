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
        criteria.add(Restrictions.sizeGe("kw", kwStart)); //$NON-NLS-1$
        criteria.add(Restrictions.sizeLt("kw", kwStart + anzahl)); //$NON-NLS-1$
        criteria.add(Restrictions.eq("jahr", jahr)); //$NON-NLS-1$
        @SuppressWarnings("unchecked")
        final List<IPlanungWoche> result = criteria.list();
        dao.commit();
        session.flush();
        return result;
    }
}
