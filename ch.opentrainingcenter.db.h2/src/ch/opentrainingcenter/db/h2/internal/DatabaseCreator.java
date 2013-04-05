package ch.opentrainingcenter.db.h2.internal;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.hibernate.Query;
import org.hibernate.Session;

public class DatabaseCreator {

    private final Dao dao;

    public DatabaseCreator(final Dao dao) {
        this.dao = dao;
    }

    public void createDatabase() {
        try {
            final Session session = dao.getSession();
            dao.begin();

            final InputStream sqlStream = this.getClass().getClassLoader().getResourceAsStream("otc.sql"); //$NON-NLS-1$
            final BufferedReader bufRead = new BufferedReader(new InputStreamReader(sqlStream));
            final StringBuilder builder = new StringBuilder();
            int nextchar;
            while ((nextchar = bufRead.read()) != -1) {
                builder.append((char) nextchar);
            }
            final Query query = session.createSQLQuery(builder.toString());
            query.executeUpdate();
            dao.commit();
            session.flush();
        } catch (final Exception e) {
            dao.rollback();
        }
    }
    //
    // @SuppressWarnings("nls")
    // private List<String> getQueries() {
    // final List<String> queries = new ArrayList<String>();
    // queries.add("CREATE TABLE PUBLIC.ATHLETE (ID INTEGER NOT NULL,NAME VARCHAR(255), AGE INTEGER NOT NULL, MAXHEARTRATE INTEGER, PRIMARY KEY (ID))");
    // queries.add("CREATE TABLE PUBLIC.HEALTH ( ID INTEGER NOT NULL, WEIGHT DOUBLE, CARDIO INTEGER, ID_FK_ATHLETE INTEGER,DATEOFMEASURE DATE, PRIMARY KEY (ID))");
    // queries.add("CREATE TABLE PUBLIC.WEATHER (ID INTEGER NOT NULL, WETTER VARCHAR(2147483647), IMAGEICON VARCHAR(2147483647), PRIMARY KEY (ID))");
    // queries.add("CREATE TABLE PUBLIC.TRAININGTYPE (ID INTEGER NOT NULL, TITLE VARCHAR(2147483647), DESCRIPTION VARCHAR(2147483647), IMAGEICON VARCHAR(2147483647), PRIMARY KEY (ID))");
    // queries.add("CREATE TABLE PUBLIC.PLANUNGWOCHE ( ID INTEGER NOT NULL, KW INTEGER, JAHR INTEGER,KMPROWOCHE INTEGER, INTERVAL BOOLEAN,LANGERLAUF INTEGER, ID_FK_ATHLETE INTEGER, PRIMARY KEY (ID))");
    // queries.add("CREATE TABLE PUBLIC.ROUTE ( ID INTEGER NOT NULL, ID_FK_ATHLETE INTEGER, NAME VARCHAR(255), BESCHREIBUNG VARCHAR(2147483647), PRIMARY KEY (ID))");
    // queries.add("CREATE TABLE PUBLIC.STRECKENPUNKTE(ID INTEGER NOT NULL, ID_FK_TRACKPOINTPROPERTY INTEGER, DISTANCE DOUBLE, LONGITUDE DOUBLE, LATITUDE DOUBLE,PRIMARY KEY (ID))");
    //
    // queries.add("CREATE TABLE PUBLIC.TRACKTRAININGPROPERTY(ID INTEGER NOT NULL, DISTANCE DOUBLE, HEARTBEAT INTEGER, ALTITUDE INTEGER,TIME LONG, ID_FK_TRAINING INTEGER, PRIMARY KEY (ID))");
    //
    // queries.add("CREATE TABLE PUBLIC.TRAINING (ID INTEGER NOT NULL, DATUM LONG, DAUER DOUBLE, LAENGEINMETER DOUBLE, AVERAGEHEARTBEAT INTEGER, MAXHEARTBEAT INTEGER, MAXSPEED DOUBLE, NOTE VARCHAR(2147483647), ID_FK_WEATHER INTEGER, ID_FK_TRAININGTYPE INTEGER, ID_FK_ROUTE INTEGER,ID_FK_ATHLETE INTEGER,ID_FK_TRAININGPROPERTY INTEGER, PRIMARY KEY (ID))");
    //
    // queries.add("ALTER TABLE PUBLIC.HEALTH ADD FOREIGN KEY (ID_FK_ATHLETE) REFERENCES OTC.PUBLIC.ATHLETE (ID)");
    //
    // queries.add("ALTER TABLE PUBLIC.STRECKENPUNKTE ADD FOREIGN KEY (ID_FK_TRACKPOINTPROPERTY) REFERENCES OTC.PUBLIC.TRACKTRAININGPROPERTY (ID)");
    //
    // queries.add("ALTER TABLE PUBLIC.TRAINING ADD FOREIGN KEY (ID_FK_ROUTE) REFERENCES OTC.PUBLIC.WEATHER (ID)");
    // queries.add("ALTER TABLE PUBLIC.TRAINING ADD FOREIGN KEY (ID_FK_WEATHER) REFERENCES OTC.PUBLIC.WEATHER (ID)");
    // queries.add("ALTER TABLE PUBLIC.TRAINING ADD FOREIGN KEY (ID_FK_ATHLETE) REFERENCES OTC.PUBLIC.ATHLETE (ID)");
    // queries.add("ALTER TABLE PUBLIC.TRAINING ADD FOREIGN KEY (ID_FK_TRAININGTYPE) REFERENCES OTC.PUBLIC.TRAININGTYPE (ID)");
    // queries.add("ALTER TABLE PUBLIC.TRAINING ADD FOREIGN KEY (ID_FK_TRAININGPROPERTY) REFERENCES OTC.PUBLIC.TRACKTRAININGPROPERTY (ID)");
    //
    // queries.add("ALTER TABLE PUBLIC.PLANUNGWOCHE ADD FOREIGN KEY (ID_FK_ATHLETE) REFERENCES OTC.PUBLIC.ATHLETE (ID)");
    // queries.add("ALTER TABLE PUBLIC.ROUTE ADD FOREIGN KEY (ID_FK_ATHLETE) REFERENCES OTC.PUBLIC.ATHLETE (ID)");
    //
    // queries.add("CREATE sequence TRACKPOINTPROPERTY_ID_SEQUENCE;");
    // queries.add("CREATE sequence ATHLETE_ID_SEQUENCE;");
    // queries.add("CREATE sequence TRAINING_ID_SEQUENCE;");
    // queries.add("CREATE sequence TRAININGTYPE_ID_SEQUENCE;");
    // queries.add("CREATE sequence HEALTH_ID_SEQUENCE;");
    // queries.add("CREATE sequence PLANUNGWOCHE_ID_SEQUENCE;");
    // queries.add("CREATE sequence ROUTE_ID_SEQUENCE;");
    // queries.add("CREATE sequence STRECKENPUNKTE_ID_SEQUENCE;");
    //
    // queries.add("INSERT INTO PUBLIC.ATHLETE (ID,NAME,AGE,MAXHEARTRATE) values(0,'Sascha',37, 200);");
    // queries.add("INSERT INTO PUBLIC.ROUTE (ID,NAME,BESCHREIBUNG) values(0,'Unbekannt',0);");
    //
    // queries.add("INSERT INTO PUBLIC.TRAININGTYPE (ID,TITLE,DESCRIPTION, IMAGEICON) values(0,'NONE','Unbekannter Typ','icons/man_u_32_32.png');");
    // queries.add("INSERT INTO PUBLIC.TRAININGTYPE (ID,TITLE,DESCRIPTION, IMAGEICON) values(1,'EXT_INTERVALL','Gleich wie intensives Intervalltraining. Der Unterschied liegt in der Länge der Intervalle und der damit verbundenen geringeren Laufgeschwindigkeit. Beispiel: 5 Minuten schnell, 2 Minuten Trabpause, 5 Minuten schnell etc. Extensive Intervalle werden bereits schon in der Aufbauetappe des Jahresplanes integriert.','icons/man_ei_32_32.png');");
    // queries.add("INSERT INTO PUBLIC.TRAININGTYPE (ID,TITLE,DESCRIPTION, IMAGEICON) values(2,'INT_INTERVALL','Intervalltrainings werden in Serien gelaufen. Zum Beispiel: 6 x 200 m schnell, jeweils 2 Minuten Trabpause. Intervall bedeutet Pause. Der Kreislauf wird belastet, anschliessend erhält er Zeit, sich zum Teil wieder zu erholen. Dann folgt der nächste Intervall. Intensive Intervalltrainings werden meist in der Wettkampfvorbereitung durchgeführt.','icons/man_ii_32_32.png');");
    // queries.add("INSERT INTO PUBLIC.TRAININGTYPE (ID,TITLE,DESCRIPTION, IMAGEICON) values(3,'LONG_JOG','Langer Dauerlauf 70-75% maximalen Herzfrequenz.','icons/man_lj_32_32.png');");
    // queries.add("INSERT INTO PUBLIC.TRAININGTYPE (ID,TITLE,DESCRIPTION, IMAGEICON) values(4,'POWER_LONG_JOG','letztes drittel 80-85% der maximalen Herzfrequenz, die ersten zwei drittel wie {@link RunType#LONG_JOG}','icons/man_plj_32_32.png');");
    // queries.add("INSERT INTO PUBLIC.TRAININGTYPE (ID,TITLE,DESCRIPTION, IMAGEICON) values(5,'TEMPO_JOG','UNBEKANNT','icons/man_tj_32_32.png');");
    //
    // queries.add("INSERT INTO PUBLIC.WEATHER (ID,WETTER, IMAGEICON) values(0,'SONNE','icons/man_u_32_32.png');");
    // queries.add("INSERT INTO PUBLIC.WEATHER (ID,WETTER, IMAGEICON) values(1,'LEICHT_BEWOELCKT','icons/man_u_32_32.png');");
    // queries.add("INSERT INTO PUBLIC.WEATHER (ID,WETTER, IMAGEICON) values(2,'BEWOELCKT','icons/man_u_32_32.png');");
    // queries.add("INSERT INTO PUBLIC.WEATHER (ID,WETTER, IMAGEICON) values(3,'STARK_BEWOELCKT','icons/man_u_32_32.png');");
    // queries.add("INSERT INTO PUBLIC.WEATHER (ID,WETTER, IMAGEICON) values(4,'LEICHTER_REGEN','icons/man_u_32_32.png');");
    // queries.add("INSERT INTO PUBLIC.WEATHER (ID,WETTER, IMAGEICON) values(5,'REGEN','icons/man_u_32_32.png');");
    // queries.add("INSERT INTO PUBLIC.WEATHER (ID,WETTER, IMAGEICON) values(6,'STARKER_REGEN','icons/man_u_32_32.png');");
    // queries.add("INSERT INTO PUBLIC.WEATHER (ID,WETTER, IMAGEICON) values(7,'LEICHTER_SCHNEE','icons/man_u_32_32.png');");
    // queries.add("INSERT INTO PUBLIC.WEATHER (ID,WETTER, IMAGEICON) values(8,'SCHNEE','icons/man_u_32_32.png');");
    // queries.add("INSERT INTO PUBLIC.WEATHER (ID,WETTER, IMAGEICON) values(9,'UNBEKANNT','icons/man_u_32_32.png');");
    // return queries;
    // }

}
