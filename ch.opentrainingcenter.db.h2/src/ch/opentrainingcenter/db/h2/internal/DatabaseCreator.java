package ch.opentrainingcenter.db.h2.internal;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;

public class DatabaseCreator extends Dao {

    public DatabaseCreator() {

    }

    public void createDatabase() {
        try {
            final Session session = getSession();
            begin();
            final List<String> queries = getQueries();
            for (final String sql : queries) {
                final Query query = session.createSQLQuery(sql);
                query.executeUpdate();
            }
            commit();
            session.flush();
        } catch (final Exception e) {
            rollback();
        }
    }

    private List<String> getQueries() {
        final List<String> queries = new ArrayList<String>();
        queries.add("CREATE TABLE PUBLIC.ATHLETE (ID INTEGER NOT NULL,NAME VARCHAR(255), AGE INTEGER NOT NULL, MAXHEARTRATE INTEGER, PRIMARY KEY (ID))");//$NON-NLS-1$
        queries.add("CREATE TABLE PUBLIC.HEALTH ( ID INTEGER NOT NULL, WEIGHT INTEGER, CARDIO INTEGER, ID_FK_ATHLETE INTEGER,DATEOFMEASURE DATE, PRIMARY KEY (ID))");//$NON-NLS-1$
        queries.add("CREATE TABLE PUBLIC.IMPORTED (ID INTEGER NOT NULL, ID_FK_ATHLETE INTEGER,IMPORTED_DATE TIMESTAMP, COMMENTS VARCHAR(2147483647),ID_FK_WEATHER INTEGER, PRIMARY KEY (ID))");//$NON-NLS-1$
        queries.add("CREATE TABLE PUBLIC.WEATHER (ID INTEGER NOT NULL, WETTER VARCHAR(2147483647), PRIMARY KEY (ID))");//$NON-NLS-1$
        queries.add("ALTER TABLE PUBLIC.HEALTH ADD FOREIGN KEY (ID_FK_ATHLETE) REFERENCES OTC.PUBLIC.ATHLETE (ID)");//$NON-NLS-1$
        queries.add("ALTER TABLE PUBLIC.IMPORTED ADD FOREIGN KEY (ID_FK_ATHLETE) REFERENCES OTC.PUBLIC.ATHLETE (ID)");//$NON-NLS-1$
        queries.add("ALTER TABLE PUBLIC.IMPORTED ADD FOREIGN KEY (ID_FK_WEATHER) REFERENCES OTC.PUBLIC.WEATHER (ID)");//$NON-NLS-1$
        // queries.add("CREATE INDEX PUBLIC.CONSTRAINT_157_INDEX_A ON PUBLIC.IMPORTED (ID_FK_ATHLETE)");
        // queries.add("CREATE INDEX PUBLIC.CONSTRAINT_7E_INDEX_A ON PUBLIC.HEALTH (ID_FK_ATHLETE)");
        // queries.add("CREATE INDEX PUBLIC.CONSTRAINT_INDEX_1 ON PUBLIC.IMPORTED (ID_FK_WEATHER)");
        // queries.add("CREATE UNIQUE INDEX PUBLIC.PRIMARY_KEY_1 ON PUBLIC.ATHLETE (ID)");
        // queries.add("CREATE UNIQUE INDEX PUBLIC.PRIMARY_KEY_7 ON PUBLIC.WEATHER (ID)");
        // queries.add("CREATE UNIQUE INDEX PUBLIC.PRIMARY_KEY_A ON PUBLIC.IMPORTED (ID)");
        // queries.add("CREATE UNIQUE INDEX PUBLIC.PRIMARY_KEY_A42 ON PUBLIC.HEALTH (ID)");
        queries.add("create sequence IMPORT_ID_SEQUENCE;");//$NON-NLS-1$
        queries.add("create sequence ATHLETE_ID_SEQUENCE;");//$NON-NLS-1$
        return queries;
    }

}
