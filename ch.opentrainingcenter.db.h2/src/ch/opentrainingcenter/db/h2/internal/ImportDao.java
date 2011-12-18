package ch.opentrainingcenter.db.h2.internal;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import ch.iseli.sportanalyzer.db.DatabaseAccessFactory;
import ch.opentrainingcenter.transfer.CommonTransferFactory;
import ch.opentrainingcenter.transfer.IAthlete;
import ch.opentrainingcenter.transfer.IImported;

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

    public int importRecord(final int athleteId, final String fileName, final Date activityId) {
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
        final Session session = dao.getSession();
        final Transaction tx = session.beginTransaction();
        session.saveOrUpdate(record);
        tx.commit();
        session.flush();
        return record.getId();
    }

    /**
     * sucht ob es bereits einen importierten datensatz mit diesem namen gibt. Damit soll geschaut werden, ob der Record bereits einmal importiert wurde.
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
        dao.getSession().flush();
    }

}
