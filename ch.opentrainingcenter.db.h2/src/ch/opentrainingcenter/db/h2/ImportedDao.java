package ch.opentrainingcenter.db.h2;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.core.runtime.CoreException;
import org.hibernate.Query;
import org.hibernate.Session;

import ch.iseli.sportanalyzer.db.IImportedDao;
import ch.opentrainingcenter.transfer.IImported;
import ch.opentrainingcenter.transfer.impl.Athlete;
import ch.opentrainingcenter.transfer.impl.Imported;

public class ImportedDao extends Dao implements IImportedDao {

    @Override
    public Object create() throws CoreException {
        return new ImportedDao();
    }

    @SuppressWarnings("unchecked")
    @Override
    public Map<Integer, String> getImportedRecords(Athlete athlete) {
        Query query = getSession().createQuery("from Imported where id_fk_athlete=:idAthlete");
        query.setParameter("idAthlete", athlete.getId());
        List<Imported> all = query.list();
        if (all == null) {
            return null;
        }
        Map<Integer, String> keyFileName = new HashMap<Integer, String>();
        for (Imported rec : all) {
            keyFileName.put(rec.getId(), rec.getComments());
        }
        return keyFileName;
    }

    @Override
    public int importRecord(Athlete athlete, String name) {
        IImported record = new Imported();
        record.setAthlete(athlete);
        record.setComments(name);
        record.setImportedDate(new Date());
        Session session = getSession();
        begin();
        Serializable save = session.save(record);
        commit();
        session.flush();
        return (Integer) save;
    }

    @Override
    public Athlete getAthlete(int id) {
        Query query = getSession().createQuery("from Athlete where id=:idAthlete");
        query.setParameter("idAthlete", id);
        if (query.list() != null && query.list().size() == 1) {
            return (Athlete) query.list().get(0);
        }
        return null;
    }

}
