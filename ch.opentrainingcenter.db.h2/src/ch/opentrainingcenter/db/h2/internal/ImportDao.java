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
        final Query query = dao.getSession().createQuery("from Imported where id_fk_athlete=:idAthlete");
        query.setParameter("idAthlete", athlete.getId());
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

    public void removeImportedRecord(final Integer id) {
        final Session session = dao.getSession();
        dao.begin();
        final Query query = session.createQuery("delete Imported where id=:id");
        query.setParameter("id", id);
        query.executeUpdate();
        dao.commit();
        session.flush();
        dao.getSession().flush();
    }
}
