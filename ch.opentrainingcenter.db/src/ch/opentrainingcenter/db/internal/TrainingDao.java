package ch.opentrainingcenter.db.internal;

import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.FetchMode;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.joda.time.DateTime;

import ch.opentrainingcenter.transfer.IAthlete;
import ch.opentrainingcenter.transfer.IRoute;
import ch.opentrainingcenter.transfer.ITraining;
import ch.opentrainingcenter.transfer.ITrainingType;

@SuppressWarnings("nls")
public class TrainingDao {

    private static final Logger LOG = Logger.getLogger(TrainingDao.class);

    private final IDao dao;

    public TrainingDao(final IDao dao) {
        this.dao = dao;
    }

    public List<ITraining> getAllImported(final IAthlete athlete) {
        final Session session = dao.getSession();
        dao.begin();

        final Criteria criteria = session.createCriteria(ITraining.class);

        criteria.setFetchMode("athlete", FetchMode.JOIN);
        criteria.setFetchMode("trainingType", FetchMode.JOIN);
        criteria.setFetchMode("weather", FetchMode.JOIN);
        criteria.setFetchMode("route", FetchMode.JOIN);
        criteria.setFetchMode("trackPoints", FetchMode.SELECT);

        criteria.add(Restrictions.eq("athlete", athlete));
        criteria.addOrder(Order.desc("datum"));

        @SuppressWarnings("unchecked")
        final List<ITraining> list = criteria.list();

        dao.commit();
        session.flush();

        return list;
    }

    public List<ITraining> getAllImported(final IAthlete athlete, final int limit) {
        final Session session = dao.getSession();
        final long start = DateTime.now().getMillis();
        dao.begin();
        final Criteria criteria = session.createCriteria(ITraining.class);

        criteria.add(Restrictions.eq("athlete", athlete));
        criteria.addOrder(Order.desc("datum"));
        criteria.setMaxResults(limit);
        criteria.setFetchMode("athlete", FetchMode.JOIN);
        criteria.setFetchMode("trainingType", FetchMode.JOIN);
        criteria.setFetchMode("weather", FetchMode.JOIN);
        criteria.setFetchMode("route", FetchMode.JOIN);
        criteria.setFetchMode("trackPoints", FetchMode.SELECT);

        @SuppressWarnings("unchecked")
        final List<ITraining> list = criteria.list();

        dao.commit();
        session.flush();
        final long time = DateTime.now().getMillis() - start;
        LOG.info("getAllImported(final IAthlete athlete, final int limit): Time[ms]: " + time);
        return list;
    }

    public ITraining getImportedRecord(final long dateInMilliseconds) {
        final Session session = dao.getSession();
        dao.begin();

        final Criteria criteria = session.createCriteria(ITraining.class);
        criteria.add(Restrictions.eq("datum", dateInMilliseconds)); //$NON-NLS-1$

        @SuppressWarnings("unchecked")
        final List<ITraining> all = criteria.list();
        dao.commit();
        session.flush();
        if (all.isEmpty()) {
            return null;
        } else {
            return all.get(0);
        }
    }

    public ITraining getNewestRun(final IAthlete athlete) {
        final List<ITraining> list = getAllImported(athlete);
        ITraining result;
        if (!list.isEmpty()) {
            result = list.get(0);
        } else {
            result = null;
        }
        return result;
    }

    public void removeImportedRecord(final Long datum) {
        final Session session = dao.getSession();
        dao.begin();
        final Query query = session.createQuery("delete Training where DATUM=:datum");
        query.setParameter("datum", datum);
        query.executeUpdate();
        dao.commit();
        session.flush();
    }

    public void updateRecord(final ITraining record, final int index) {
        record.setTrainingType(getTrainingType(index));
        final Session session = dao.getSession();
        final Transaction tx = session.beginTransaction();
        session.update(record);
        tx.commit();
        session.flush();
    }

    private ITrainingType getTrainingType(final int index) {
        LOG.info("load ITrainingType");
        final Session session = dao.getSession();
        dao.begin();
        final Criteria criteria = session.createCriteria(ITrainingType.class);
        criteria.add(Restrictions.eq("id", index));
        ITrainingType type = null;
        @SuppressWarnings("unchecked")
        final List<ITrainingType> records = criteria.list();
        if (notEmpty(records)) {
            type = records.get(0);
        }
        dao.commit();
        session.flush();
        return type;
    }

    private boolean notEmpty(final List<?> records) {
        return records != null && !records.isEmpty();
    }

    public void updateRecordRoute(final ITraining record, final int idRoute) {
        record.setRoute(getRoute(idRoute));
        final Session session = dao.getSession();
        final Transaction tx = session.beginTransaction();
        session.update(record);
        tx.commit();
        session.flush();
    }

    @SuppressWarnings("unchecked")
    private IRoute getRoute(final int routeId) {
        final Query query = dao.getSession().createQuery("from Route where id=:idType");
        query.setParameter("idType", routeId);
        final List<IRoute> list = query.list();
        return list.get(0);
    }

    public int saveOrUpdate(final ITraining training) {
        ITraining exists = getImportedRecord(training.getDatum());
        if (exists != null) {
            LOG.info("Training bereits einmal importiert: --> updaten");
        } else {
            LOG.info("Neues Training abspeichern: " + training);
            exists = training;
        }
        final ITrainingType tt = exists.getTrainingType();
        if (tt != null) {
            exists.setTrainingType(getTrainingType(tt.getId()));
        }
        final Session session = dao.getSession();
        dao.begin();
        session.saveOrUpdate(exists);
        session.flush();
        return getImportedRecord(training.getDatum()).getId();
    }
}
