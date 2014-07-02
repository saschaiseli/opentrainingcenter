package ch.opentrainingcenter.database.dao;

import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.LogicalExpression;
import org.hibernate.criterion.Restrictions;
import org.hibernate.criterion.SimpleExpression;

import ch.opentrainingcenter.transfer.IAthlete;
import ch.opentrainingcenter.transfer.IPlanungWoche;

@SuppressWarnings("nls")
public class PlanungDao {
    private static final String ATHLETE = "athlete";

    private static final Logger LOG = Logger.getLogger(PlanungDao.class);

    private final IConnectionConfig dao;

    public PlanungDao(final IConnectionConfig dao) {
        this.dao = dao;
    }

    public List<IPlanungWoche> getPlanungsWoche(final IAthlete athlete, final int jahr, final int kwStart) {

        LOG.info("load IPlanungWoche from: " + athlete);
        final Session session = dao.getSession();
        dao.begin();
        final Criteria criteria = session.createCriteria(IPlanungWoche.class);
        criteria.add(Restrictions.eq(ATHLETE, athlete));
        final LogicalExpression selbesJahr = Restrictions.and(Restrictions.ge("kw", kwStart), Restrictions.eq("jahr", jahr));
        final SimpleExpression groesseresJahr = Restrictions.gt("jahr", jahr);
        final Criterion rest1 = Restrictions.or(selbesJahr, groesseresJahr);
        criteria.add(rest1);
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
                existing.setLangerLauf(planung.getLangerLauf());
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
        LOG.info("load IPlanungWoche from: " + athlete);
        final Session session = dao.getSession();
        dao.begin();
        final Criteria criteria = session.createCriteria(IPlanungWoche.class);
        criteria.add(Restrictions.eq(ATHLETE, athlete));
        criteria.add(Restrictions.eq("jahr", jahr));
        criteria.add(Restrictions.eq("kw", kw));
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
        LOG.info("load IPlanungWoche from: " + athlete);
        final Session session = dao.getSession();
        dao.begin();
        final Criteria criteria = session.createCriteria(IPlanungWoche.class);
        criteria.add(Restrictions.eq(ATHLETE, athlete));
        @SuppressWarnings("unchecked")
        final List<IPlanungWoche> records = criteria.list();
        dao.commit();
        session.flush();
        return records;
    }
}
