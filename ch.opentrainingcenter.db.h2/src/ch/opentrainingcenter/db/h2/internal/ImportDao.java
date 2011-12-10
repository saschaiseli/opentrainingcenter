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
    public Map<Integer, String> getImportedRecords(final IAthlete athlete) {
        if (athlete == null) {
            return null;
        }
        final Query query = dao.getSession().createQuery("from Imported where id_fk_athlete=:idAthlete");//$NON-NLS-1$
        query.setParameter("idAthlete", athlete.getId());//$NON-NLS-1$
        final List<IImported> all = query.list();
        if (all == null) {
            return null;
        }
        final Map<Integer, String> keyFileName = new HashMap<Integer, String>();
        for (final IImported rec : all) {
            keyFileName.put(rec.getId(), rec.getComments());
        }
        return keyFileName;
    }

    public int importRecord(final int athleteId, final String name) {
        final int id = searchRecord(name);
        if (id > 0) {
            return id;
        }
        final IImported record = CommonTransferFactory.createIImported();
        final IAthlete athlete = DatabaseAccessFactory.getDatabaseAccess().getAthlete(athleteId);
        athlete.addImported(record);
        record.setAthlete(athlete);
        record.setComments(name);
        record.setImportedDate(new Date());
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
    private int searchRecord(final String name) {
        final Session session = dao.getSession();
        dao.begin();
        final Query query = session.createQuery("from Imported where COMMENTS=:name");//$NON-NLS-1$
        query.setParameter("name", name);//$NON-NLS-1$
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

    public void removeImportedRecord(final Integer id) {
        final Session session = dao.getSession();
        dao.begin();
        final Query query = session.createQuery("delete Imported where id=:id");//$NON-NLS-1$
        query.setParameter("id", id);//$NON-NLS-1$
        query.executeUpdate();
        dao.commit();
        session.flush();
        dao.getSession().flush();
    }
}
