package ch.opentrainingcenter.db.h2;

import java.io.Serializable;
import java.util.ArrayList;
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
        if (athlete == null) {
            return null;
        }
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
        Session session = getSession();
        begin();
        Query query = session.createQuery("from Athlete where id=:idAthlete");
        query.setParameter("idAthlete", id);
        Athlete athlete = null;
        if (query.list() != null && query.list().size() == 1) {
            athlete = (Athlete) query.list().get(0);
        }
        commit();
        session.flush();
        return athlete;
    }

    @Override
    public void removeImportedRecord(Integer id) {
        Session session = getSession();
        begin();
        Query query = session.createQuery("delete Imported where id=:id");
        query.setParameter("id", id);
        query.executeUpdate();
        commit();
        session.flush();
        getSession().flush();
    }

    @Override
    public void createDatabase() {
        try {
            Session session = getSession();
            begin();
            List<String> queries = getQueries();
            for (String sql : queries) {
                Query query = session.createSQLQuery(sql);
                query.executeUpdate();
            }
            commit();
            session.flush();
        } catch (Exception e) {
            rollback();
        }
    }

    private List<String> getQueries() {
        List<String> queries = new ArrayList<String>();
        queries.add("CREATE TABLE PUBLIC.ATHLETE (ID INTEGER NOT NULL,NAME VARCHAR(255),PRIMARY KEY (ID))");
        queries.add("CREATE TABLE PUBLIC.HEALTH ( ID INTEGER NOT NULL, WEIGHT INTEGER, CARDIO INTEGER, ID_FK_ATHLETE INTEGER,DATEOFMEASURE DATE, PRIMARY KEY (ID))");
        queries.add("CREATE TABLE PUBLIC.IMPORTED (ID INTEGER NOT NULL, ID_FK_ATHLETE INTEGER,IMPORTED_DATE TIMESTAMP, COMMENTS VARCHAR(2147483647),ID_FK_WEATHER INTEGER, PRIMARY KEY (ID))");
        queries.add("CREATE TABLE PUBLIC.WEATHER (ID INTEGER NOT NULL, WETTER VARCHAR(2147483647), PRIMARY KEY (ID))");
        queries.add("ALTER TABLE PUBLIC.HEALTH ADD FOREIGN KEY (ID_FK_ATHLETE) REFERENCES OTC.PUBLIC.ATHLETE (ID)");
        queries.add("ALTER TABLE PUBLIC.IMPORTED ADD FOREIGN KEY (ID_FK_ATHLETE) REFERENCES OTC.PUBLIC.ATHLETE (ID)");
        queries.add("ALTER TABLE PUBLIC.IMPORTED ADD FOREIGN KEY (ID_FK_WEATHER) REFERENCES OTC.PUBLIC.WEATHER (ID)");
        // queries.add("CREATE INDEX PUBLIC.CONSTRAINT_157_INDEX_A ON PUBLIC.IMPORTED (ID_FK_ATHLETE)");
        // queries.add("CREATE INDEX PUBLIC.CONSTRAINT_7E_INDEX_A ON PUBLIC.HEALTH (ID_FK_ATHLETE)");
        // queries.add("CREATE INDEX PUBLIC.CONSTRAINT_INDEX_1 ON PUBLIC.IMPORTED (ID_FK_WEATHER)");
        // queries.add("CREATE UNIQUE INDEX PUBLIC.PRIMARY_KEY_1 ON PUBLIC.ATHLETE (ID)");
        // queries.add("CREATE UNIQUE INDEX PUBLIC.PRIMARY_KEY_7 ON PUBLIC.WEATHER (ID)");
        // queries.add("CREATE UNIQUE INDEX PUBLIC.PRIMARY_KEY_A ON PUBLIC.IMPORTED (ID)");
        // queries.add("CREATE UNIQUE INDEX PUBLIC.PRIMARY_KEY_A42 ON PUBLIC.HEALTH (ID)");
        queries.add("create sequence IMPORT_ID_SEQUENCE;");
        return queries;
    }
}
