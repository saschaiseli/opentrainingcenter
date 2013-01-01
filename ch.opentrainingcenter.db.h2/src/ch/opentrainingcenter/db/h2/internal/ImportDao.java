package ch.opentrainingcenter.db.h2.internal;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import ch.opentrainingcenter.core.db.DatabaseAccessFactory;
import ch.opentrainingcenter.transfer.CommonTransferFactory;
import ch.opentrainingcenter.transfer.IAthlete;
import ch.opentrainingcenter.transfer.IImported;
import ch.opentrainingcenter.transfer.IRoute;
import ch.opentrainingcenter.transfer.ITraining;
import ch.opentrainingcenter.transfer.ITrainingType;

@SuppressWarnings("nls")
public class ImportDao {

    private final Dao dao;

    public ImportDao(final Dao dao) {
        this.dao = dao;
    }

    @SuppressWarnings("unchecked")
    public Map<Date, String> getImportedRecords(final IAthlete athlete) {
        if (athlete == null) {
            return null;
        }
        final Query query = dao.getSession().createQuery("from Imported where id_fk_athlete=:idAthlete");//$NON-NLS-1$
        query.setParameter("idAthlete", athlete.getId());//$NON-NLS-1$
        final List<IImported> all = query.list();
        if (all == null) {
            return null;
        }
        final Map<Date, String> keyFileName = new HashMap<Date, String>();
        for (final IImported rec : all) {
            keyFileName.put(rec.getActivityId(), rec.getComments());
        }
        return keyFileName;
    }

    public int importRecord(final int athleteId, final String fileName, final Date activityId, final ITraining overview, final int type, final int routeId) {
        final int id = searchRecord(activityId);
        if (id > 0) {
            return -1;
        }
        final IImported record = CommonTransferFactory.createIImported();
        final IAthlete athlete = DatabaseAccessFactory.getDatabaseAccess().getAthlete(athleteId);
        athlete.addImported(record);
        record.setAthlete(athlete);
        record.setComments(fileName);
        record.setImportedDate(new Date());
        record.setActivityId(activityId);
        record.setTraining(overview);
        record.setTrainingType(getTrainingType(type));
        record.setRoute(getRoute(routeId));
        final Session session = dao.getSession();
        final Transaction tx = session.beginTransaction();
        session.saveOrUpdate(record);
        tx.commit();
        session.flush();
        return record.getId();
    }

    public void updateRecord(final IImported record, final int index) {
        record.setTrainingType(getTrainingType(index));
        final Session session = dao.getSession();
        final Transaction tx = session.beginTransaction();
        session.update(record);
        tx.commit();
        session.flush();
    }

    public void updateRecord(final IImported record) {
        final Session session = dao.getSession();
        final Transaction tx = session.beginTransaction();
        session.update(record);
        tx.commit();
        session.flush();
    }

    @SuppressWarnings("unchecked")
    private ITrainingType getTrainingType(final int id) {
        final Query query = dao.getSession().createQuery("from TrainingType where id=:idType");//$NON-NLS-1$
        query.setParameter("idType", id);//$NON-NLS-1$
        final List<ITrainingType> list = query.list();
        return list.get(0);
    }

    @SuppressWarnings("unchecked")
    private IRoute getRoute(final int routeId) {
        final Query query = dao.getSession().createQuery("from Route where id=:idType");//$NON-NLS-1$
        query.setParameter("idType", routeId);//$NON-NLS-1$
        final List<IRoute> list = query.list();
        return list.get(0);
    }

    /**
     * sucht ob es bereits einen importierten datensatz mit diesem namen gibt.
     * Damit soll geschaut werden, ob der Record bereits einmal importiert
     * wurde.
     */
    private int searchRecord(final Date activityId) {
        final Session session = dao.getSession();
        dao.begin();
        final Query query = session.createQuery("from Imported where activityId=:id");//$NON-NLS-1$
        query.setParameter("id", activityId); //$NON-NLS-1$
        @SuppressWarnings("unchecked")
        final List<IImported> all = query.list();
        dao.commit();
        session.flush();
        if (all != null && !all.isEmpty()) {
            return all.get(0).getId();
        } else {
            // record noch nicht vorhanden!!
            return -1;
        }
    }

    public void removeImportedRecord(final Date activityId) {
        final Session session = dao.getSession();
        dao.begin();
        final Query query = session.createQuery("delete Imported where activityId=:id");//$NON-NLS-1$
        query.setParameter("id", activityId);//$NON-NLS-1$
        query.executeUpdate();
        dao.commit();
        session.flush();
    }

    @SuppressWarnings("unchecked")
    public IImported getImportedRecord(final Date key) {
        final Session session = dao.getSession();
        dao.begin();
        final Query query = dao.getSession().createQuery("from Imported where ACTIVITY_ID=:key");//$NON-NLS-1$
        query.setParameter("key", key);//$NON-NLS-1$
        final List<IImported> all = query.list();
        dao.commit();
        session.flush();
        if (all.isEmpty()) {
            return null;
        } else {
            return all.get(0);
        }
    }

    @SuppressWarnings("unchecked")
    public List<IImported> getAllImported(final IAthlete athlete) {
        final Session session = dao.getSession();
        dao.begin();

        final Criteria criteria = session.createCriteria(IImported.class);//

        criteria.add(Restrictions.eq("athlete", athlete));
        criteria.addOrder(Order.desc("activityId"));

        final List<IImported> list = criteria.list();

        dao.commit();
        session.flush();

        return list;
    }

    @SuppressWarnings("unchecked")
    public List<IImported> getAllImported(final IAthlete athlete, final int limit) {
        final Session session = dao.getSession();
        dao.begin();

        final Criteria criteria = session.createCriteria(IImported.class);

        criteria.add(Restrictions.eq("athlete", athlete));
        criteria.addOrder(Order.desc("activityId"));
        criteria.setMaxResults(limit);

        final List<IImported> list = criteria.list();

        dao.commit();
        session.flush();

        return list;
    }

    public IImported getNewestRun(final IAthlete athlete) {
        final List<IImported> list = getAllImported(athlete);
        IImported result;
        if (!list.isEmpty()) {
            result = list.get(0);
        } else {
            result = null;
        }
        return result;
    }
}
