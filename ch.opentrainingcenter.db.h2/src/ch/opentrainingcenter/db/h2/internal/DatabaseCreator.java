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
        queries.add("CREATE TABLE PUBLIC.IMPORTED (ID INTEGER NOT NULL, ID_FK_ATHLETE INTEGER,IMPORTED_DATE TIMESTAMP, ACTIVITY_ID TIMESTAMP,COMMENTS VARCHAR(2147483647),ID_FK_WEATHER INTEGER,ID_FK_TRAININGTYPE INTEGER,ID_FK_TRAINING INTEGER, PRIMARY KEY (ID))");//$NON-NLS-1$
        queries.add("CREATE TABLE PUBLIC.WEATHER (ID INTEGER NOT NULL, WETTER VARCHAR(2147483647), PRIMARY KEY (ID))");//$NON-NLS-1$
        queries.add("CREATE TABLE PUBLIC.TRAININGTYPE (ID INTEGER NOT NULL, TITLE VARCHAR(2147483647), DESCRIPTION VARCHAR(2147483647), PRIMARY KEY (ID))");//$NON-NLS-1$
        queries.add("CREATE TABLE PUBLIC.TRAINING (ID INTEGER NOT NULL, DATUM VARCHAR(2147483647), DAUER DOUBLE, LAENGEINMETER DOUBLE, AVERAGEHEARTBEAT INTEGER,MAXHEARTBEAT INTEGER, MAXSPEED DOUBLE, PRIMARY KEY (ID))");//$NON-NLS-1$

        queries.add("ALTER TABLE PUBLIC.HEALTH ADD FOREIGN KEY (ID_FK_ATHLETE) REFERENCES OTC.PUBLIC.ATHLETE (ID)");//$NON-NLS-1$
        queries.add("ALTER TABLE PUBLIC.IMPORTED ADD FOREIGN KEY (ID_FK_ATHLETE) REFERENCES OTC.PUBLIC.ATHLETE (ID)");//$NON-NLS-1$
        queries.add("ALTER TABLE PUBLIC.IMPORTED ADD FOREIGN KEY (ID_FK_WEATHER) REFERENCES OTC.PUBLIC.WEATHER (ID)");//$NON-NLS-1$
        queries.add("ALTER TABLE PUBLIC.IMPORTED ADD FOREIGN KEY (ID_FK_TRAININGTYPE) REFERENCES OTC.PUBLIC.TRAININGTYPE (ID)");//$NON-NLS-1$
        queries.add("CREATE sequence IMPORT_ID_SEQUENCE;");//$NON-NLS-1$
        queries.add("CREATE sequence ATHLETE_ID_SEQUENCE;");//$NON-NLS-1$
        queries.add("CREATE sequence TRAINING_ID_SEQUENCE;");//$NON-NLS-1$
        queries.add("INSERT INTO PUBLIC.ATHLETE (ID,NAME,AGE,MAXHEARTRATE) values(0,'Sascha',37, 200);");//$NON-NLS-1$

        queries.add("INSERT INTO PUBLIC.TRAININGTYPE (ID,TITLE,DESCRIPTION) values(0,'NONE','Unbekannter Typ');");//$NON-NLS-1$
        queries.add("INSERT INTO PUBLIC.TRAININGTYPE (ID,TITLE,DESCRIPTION) values(1,'EXT_INTERVALL','Gleich wie intensives Intervalltraining. Der Unterschied liegt in der Länge der Intervalle und der damit verbundenen geringeren Laufgeschwindigkeit. Beispiel: 5 Minuten schnell, 2 Minuten Trabpause, 5 Minuten schnell etc. Extensive Intervalle werden bereits schon in der Aufbauetappe des Jahresplanes integriert.');");//$NON-NLS-1$
        queries.add("INSERT INTO PUBLIC.TRAININGTYPE (ID,TITLE,DESCRIPTION) values(2,'INT__INTERVALL','Intervalltrainings werden in Serien gelaufen. Zum Beispiel: 6 x 200 m schnell, jeweils 2 Minuten Trabpause. Intervall bedeutet Pause. Der Kreislauf wird belastet, anschliessend erhält er Zeit, sich zum Teil wieder zu erholen. Dann folgt der nächste Intervall. Intensive Intervalltrainings werden meist in der Wettkampfvorbereitung durchgeführt.');");//$NON-NLS-1$
        queries.add("INSERT INTO PUBLIC.TRAININGTYPE (ID,TITLE,DESCRIPTION) values(3,'LONG_JOG','Langer Dauerlauf 70-75% maximalen Herzfrequenz.');");//$NON-NLS-1$
        queries.add("INSERT INTO PUBLIC.TRAININGTYPE (ID,TITLE,DESCRIPTION) values(4,'POWER_LONG_JOG','letztes drittel 80-85% der maximalen Herzfrequenz, die ersten zwei drittel wie {@link RunType#LONG_JOG}');");//$NON-NLS-1$
        queries.add("INSERT INTO PUBLIC.TRAININGTYPE (ID,TITLE,DESCRIPTION) values(5,'TEMPO_JOG','Schneller Lauf z.B 8km in 40min');");//$NON-NLS-1$
        return queries;
    }

}
