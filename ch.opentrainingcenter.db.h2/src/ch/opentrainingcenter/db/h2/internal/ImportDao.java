package ch.opentrainingcenter.db.h2.internal;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.Query;
import org.hibernate.Session;

import ch.opentrainingcenter.transfer.CommonTransferFactory;
import ch.opentrainingcenter.transfer.IAthlete;
import ch.opentrainingcenter.transfer.IImported;

public class ImportDao extends Dao {

    public ImportDao() {
    }

    @SuppressWarnings("unchecked")
    public Map<Integer, String> getImportedRecords(final IAthlete athlete) {
        if (athlete == null) {
            return null;
        }
        final Query query = getSession().createQuery("from Imported where id_fk_athlete=:idAthlete");
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

    public int importRecord(final IAthlete athlete, final String name) {
        final IImported record = CommonTransferFactory.createIImported();
        record.setAthlete(athlete);
        record.setComments(name);
        record.setImportedDate(new Date());
        final Session session = getSession();
        begin();
        final Serializable save = session.save(record);
        commit();
        session.flush();
        return (Integer) save;
    }

    public void removeImportedRecord(final Integer id) {
        final Session session = getSession();
        begin();
        final Query query = session.createQuery("delete Imported where id=:id");
        query.setParameter("id", id);
        query.executeUpdate();
        commit();
        session.flush();
        getSession().flush();
    }
}
