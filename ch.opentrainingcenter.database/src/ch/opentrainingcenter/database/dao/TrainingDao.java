package ch.opentrainingcenter.database.dao;

import java.util.Collection;
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
import ch.opentrainingcenter.transfer.TrainingType;

@SuppressWarnings("nls")
public class TrainingDao {

    private static final String ATHLETE = "athlete";
    private static final String TRACK_POINTS = "trackPoints";
    private static final String ROUTE = "route";
    private static final String WEATHER = "weather";
    private static final String DATUM = "datum";

    private static final Logger LOG = Logger.getLogger(TrainingDao.class);

    private final IConnectionConfig dao;

    public TrainingDao(final IConnectionConfig dao) {
        this.dao = dao;
    }

    public List<ITraining> getAllTrainings(final IAthlete athlete) {
        final Session session = dao.getSession();
        dao.begin();

        final Criteria criteria = session.createCriteria(ITraining.class);

        criteria.setFetchMode(ATHLETE, FetchMode.JOIN);
        criteria.setFetchMode(WEATHER, FetchMode.JOIN);
        criteria.setFetchMode(ROUTE, FetchMode.JOIN);
        criteria.setFetchMode(TRACK_POINTS, FetchMode.SELECT);

        criteria.add(Restrictions.eq(ATHLETE, athlete));
        criteria.addOrder(Order.desc(DATUM));

        @SuppressWarnings("unchecked")
        final List<ITraining> list = criteria.list();

        dao.commit();
        session.flush();

        return list;
    }

    public List<ITraining> getTrainingsByAthleteAndDate(final IAthlete athlete, final DateTime von, final DateTime bis) {
        final Session session = dao.getSession();
        dao.begin();

        final Criteria criteria = session.createCriteria(ITraining.class);

        criteria.setFetchMode(ATHLETE, FetchMode.JOIN);
        criteria.setFetchMode(WEATHER, FetchMode.JOIN);
        criteria.setFetchMode(ROUTE, FetchMode.JOIN);
        criteria.setFetchMode(TRACK_POINTS, FetchMode.SELECT);

        criteria.add(Restrictions.eq(ATHLETE, athlete));
        criteria.add(Restrictions.ge(DATUM, von.getMillis()));
        criteria.add(Restrictions.le(DATUM, bis.getMillis()));
        criteria.addOrder(Order.desc(DATUM));

        @SuppressWarnings("unchecked")
        final List<ITraining> list = criteria.list();

        dao.commit();
        session.flush();

        return list;
    }

    public List<ITraining> getAllTrainingsByRoute(final IAthlete athlete, final IRoute route) {
        final Session session = dao.getSession();
        final long start = DateTime.now().getMillis();
        dao.begin();
        final Criteria criteria = session.createCriteria(ITraining.class);

        criteria.add(Restrictions.eq(ATHLETE, athlete));
        criteria.add(Restrictions.eq(ROUTE, route));

        @SuppressWarnings("unchecked")
        final List<ITraining> list = criteria.list();

        dao.commit();
        session.flush();
        final long time = DateTime.now().getMillis() - start;
        LOG.info("getAllFromRoute(final IAthlete athlete, final IRoute routenName): Time[ms]: " + time);
        return list;
    }

    public ITraining getTrainingByDate(final long dateInMilliseconds) {
        final Session session = dao.getSession();
        dao.begin();

        final Criteria criteria = session.createCriteria(ITraining.class);
        criteria.add(Restrictions.eq(DATUM, dateInMilliseconds));

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

    public ITraining getNewestTraining(final IAthlete athlete) {
        final List<ITraining> list = getAllTrainings(athlete);
        ITraining result;
        if (!list.isEmpty()) {
            result = list.get(0);
        } else {
            result = null;
        }
        return result;
    }

    public void removeTrainingByDate(final Long datum) {
        final Session session = dao.getSession();
        dao.begin();
        final Query query = session.createQuery("delete Training where DATUM=:datum");
        query.setParameter(DATUM, datum);
        query.executeUpdate();
        dao.commit();
        session.flush();
    }

    public void updateTrainingType(final ITraining record, final int index) {
        record.setTrainingType(TrainingType.getByIndex(index));
        final Session session = dao.getSession();
        final Transaction tx = session.beginTransaction();
        session.update(record);
        tx.commit();
        session.flush();
    }

    public void updateTrainingRoute(final ITraining record, final int idRoute) {
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
        final Session session = dao.getSession();
        dao.begin();
        session.saveOrUpdate(training);
        session.flush();
        LOG.info(String.format("Training mit ID %s in Datenbank geschrieben", training.getId()));
        return training.getId();
    }

    public Collection<? extends ITraining> getTrainingByRoute(final int id) {
        return null;
    }

}
