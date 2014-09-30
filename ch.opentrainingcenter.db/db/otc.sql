CREATE TABLE PUBLIC.ATHLETE 
(
	ID INTEGER NOT NULL,
	NAME VARCHAR(255), 
	BIRTHDAY DATE NOT NULL,
	MAXHEARTRATE INTEGER, 
	PRIMARY KEY (ID)
);

CREATE TABLE PUBLIC.HEALTH 
( 
	ID INTEGER NOT NULL,
	WEIGHT DOUBLE, 
	CARDIO INTEGER, 
	DATEOFMEASURE DATE, 
	ID_FK_ATHLETE INTEGER,
	PRIMARY KEY (ID)
);

CREATE TABLE PUBLIC.WEATHER 
(
	ID INTEGER NOT NULL, 
	WETTER VARCHAR(2147483647), 
	IMAGEICON VARCHAR(2147483647), 
	PRIMARY KEY (ID)
);

CREATE TABLE PUBLIC.TRAININGTYPE 
(
	ID INTEGER NOT NULL, 
	TITLE VARCHAR(2147483647), 
	DESCRIPTION VARCHAR(2147483647), 
	IMAGEICON VARCHAR(2147483647), 
	PRIMARY KEY (ID)
);

CREATE TABLE PUBLIC.PLANUNGWOCHE
( 
	ID INTEGER NOT NULL, 
	KW INTEGER, 
	JAHR INTEGER,
	KMPROWOCHE INTEGER, 
	INTERVAL BOOLEAN,
	LANGERLAUF INTEGER, 
	ID_FK_ATHLETE INTEGER, 
	PRIMARY KEY (ID)
);

CREATE TABLE PUBLIC.ROUTE 
( 
	ID INTEGER NOT NULL, 
	ID_FK_ATHLETE INTEGER, 
	ID_FK_TRAINING INTEGER,
	NAME VARCHAR(255), 
	BESCHREIBUNG VARCHAR(2147483647), 
	PRIMARY KEY (ID)
);

CREATE TABLE PUBLIC.STRECKENPUNKTE
(
	ID INTEGER NOT NULL, 
	LONGITUDE DOUBLE, 
	LATITUDE DOUBLE,
	PRIMARY KEY (ID)
); 

CREATE TABLE PUBLIC.TRACKTRAININGPROPERTY
(
	ID INTEGER NOT NULL, 
	DISTANCE DOUBLE, 
	HEARTBEAT INTEGER, 
	ALTITUDE INTEGER,
	ZEIT LONG, 
	LAP INTEGER,
	ID_FK_STRECKENPUNKT INTEGER,
	IDX INTEGER,
	ID_TRAINING INTEGER,
	PRIMARY KEY (ID)
); 

CREATE TABLE PUBLIC.LAPINFO
(
	ID INTEGER NOT NULL, 
	START INTEGER, 
	END INTEGER, 
	HEARTBEAT INTEGER, 
	PACE VARCHAR(50), 
	GESCHWINDIGKEIT VARCHAR(50),
	TIME LONG, 
	LAP INTEGER,
	IDX INTEGER,
	ID_TRAINING INTEGER,
	PRIMARY KEY (ID)
); 

CREATE TABLE PUBLIC.TRAINING 
(
	ID_TRAINING INTEGER NOT NULL, 
	DATUM LONG, 
	DAUER DOUBLE, 
	LAENGEINMETER DOUBLE, 
	AVERAGEHEARTBEAT INTEGER, 
	MAXHEARTBEAT INTEGER, 
	MAXSPEED DOUBLE, 
	NOTE VARCHAR(2147483647),
	DATEOFIMPORT TIMESTAMP,
	FILENAME VARCHAR(10000),
	UPMETER INTEGER,
	DOWNMETER INTEGER,
	SPORT INTEGER,
	TRAININGTYPE INTEGER,
	ID_FK_WEATHER INTEGER, 
	ID_FK_ROUTE INTEGER,
	ID_FK_ATHLETE INTEGER,
	ID_FK_SHOES INTEGER,
	PRIMARY KEY (ID_TRAINING)
);

CREATE TABLE PUBLIC.SHOES 
(
	ID INTEGER NOT NULL, 
	SCHUHNAME VARCHAR(2147483647), 
	IMAGEICON VARCHAR(2147483647),
	PREIS INTEGER,
	KAUFDATUM TIMESTAMP,
	ID_FK_ATHLETE INTEGER,
	PRIMARY KEY (ID)
);

-- Foreign Keys
ALTER TABLE PUBLIC.HEALTH ADD FOREIGN KEY (ID_FK_ATHLETE) REFERENCES PUBLIC.ATHLETE (ID);
ALTER TABLE PUBLIC.TRAINING ADD FOREIGN KEY (ID_FK_WEATHER) REFERENCES PUBLIC.WEATHER (ID);
ALTER TABLE PUBLIC.TRAINING ADD FOREIGN KEY (ID_FK_ATHLETE) REFERENCES PUBLIC.ATHLETE (ID);
ALTER TABLE PUBLIC.TRAINING ADD FOREIGN KEY (ID_FK_ROUTE) REFERENCES PUBLIC.ROUTE (ID) ON DELETE SET NULL;
ALTER TABLE PUBLIC.TRAINING ADD FOREIGN KEY (ID_FK_SHOES) REFERENCES PUBLIC.SHOES (ID);
ALTER TABLE PUBLIC.TRACKTRAININGPROPERTY ADD FOREIGN KEY(ID_FK_STRECKENPUNKT) REFERENCES PUBLIC.STRECKENPUNKTE(ID);
ALTER TABLE PUBLIC.PLANUNGWOCHE ADD FOREIGN KEY (ID_FK_ATHLETE) REFERENCES PUBLIC.ATHLETE (ID);
ALTER TABLE PUBLIC.ROUTE ADD FOREIGN KEY (ID_FK_ATHLETE) REFERENCES PUBLIC.ATHLETE (ID);
ALTER TABLE PUBLIC.ROUTE ADD FOREIGN KEY (ID_FK_TRAINING) REFERENCES PUBLIC.TRAINING (ID_TRAINING) ON DELETE SET NULL;
ALTER TABLE PUBLIC.SHOES ADD FOREIGN KEY (ID_FK_ATHLETE) REFERENCES PUBLIC.ATHLETE (ID);
-- alter table route add ID_FK_TRAINING int;

-- Unique 
alter table athlete add unique ( name );

CREATE sequence TRACKPOINTPROPERTY_ID_SEQUENCE;
CREATE sequence ATHLETE_ID_SEQUENCE;
CREATE sequence TRAINING_ID_SEQUENCE;
CREATE sequence HEALTH_ID_SEQUENCE; 
CREATE sequence PLANUNGWOCHE_ID_SEQUENCE; 
CREATE sequence ROUTE_ID_SEQUENCE; 
CREATE sequence STRECKENPUNKTE_ID_SEQUENCE; 
CREATE sequence MAINTENANCE_ID_SEQUENCE; 
CREATE sequence LAPINFO_ID_SEQUENCE;
CREATE sequence SHOE_ID_SEQUENCE; 

INSERT INTO PUBLIC.ATHLETE (ID,NAME,BIRTHDAY,MAXHEARTRATE) values(0,'Sascha','1974-08-29', 200);
INSERT INTO PUBLIC.ROUTE (ID,ID_FK_ATHLETE,NAME,BESCHREIBUNG) values(0,0,'Unbekannt','');

-- INSERT INTO PUBLIC.TRAININGTYPE (ID,TITLE,DESCRIPTION, IMAGEICON) values(0,'NONE','Unbekannter Typ','icons/man_u_32_32.png');
-- INSERT INTO PUBLIC.TRAININGTYPE (ID,TITLE,DESCRIPTION, IMAGEICON) values(1,'EXT_INTERVALL','Gleich wie intensives Intervalltraining. Der Unterschied liegt in der Länge der Intervalle und der damit verbundenen geringeren Laufgeschwindigkeit. Beispiel: 5 Minuten schnell, 2 Minuten Trabpause, 5 Minuten schnell etc. Extensive Intervalle werden bereits schon in der Aufbauetappe des Jahresplanes integriert.','icons/man_ei_32_32.png');
-- INSERT INTO PUBLIC.TRAININGTYPE (ID,TITLE,DESCRIPTION, IMAGEICON) values(2,'INT_INTERVALL','Intervalltrainings werden in Serien gelaufen. Zum Beispiel: 6 x 200 m schnell, jeweils 2 Minuten Trabpause. Intervall bedeutet Pause. Der Kreislauf wird belastet, anschliessend erhält er Zeit, sich zum Teil wieder zu erholen. Dann folgt der nächste Intervall. Intensive Intervalltrainings werden meist in der Wettkampfvorbereitung durchgeführt.','icons/man_ii_32_32.png');
-- INSERT INTO PUBLIC.TRAININGTYPE (ID,TITLE,DESCRIPTION, IMAGEICON) values(3,'LONG_JOG','Langer Dauerlauf 70-75% maximalen Herzfrequenz.','icons/man_lj_32_32.png');
-- INSERT INTO PUBLIC.TRAININGTYPE (ID,TITLE,DESCRIPTION, IMAGEICON) values(4,'POWER_LONG_JOG','letztes drittel 80-85% der maximalen Herzfrequenz, die ersten zwei drittel wie {@link RunType#LONG_JOG}','icons/man_plj_32_32.png');
-- INSERT INTO PUBLIC.TRAININGTYPE (ID,TITLE,DESCRIPTION, IMAGEICON) values(5,'TEMPO_JOG','UNBEKANNT','icons/man_tj_32_32.png');

INSERT INTO PUBLIC.WEATHER (ID,WETTER, IMAGEICON) values(0,'SONNE','icons/man_u_32_32.png');
INSERT INTO PUBLIC.WEATHER (ID,WETTER, IMAGEICON) values(1,'LEICHT_BEWOELCKT','icons/man_u_32_32.png');
INSERT INTO PUBLIC.WEATHER (ID,WETTER, IMAGEICON) values(2,'BEWOELCKT','icons/man_u_32_32.png');
INSERT INTO PUBLIC.WEATHER (ID,WETTER, IMAGEICON) values(3,'STARK_BEWOELCKT','icons/man_u_32_32.png');
INSERT INTO PUBLIC.WEATHER (ID,WETTER, IMAGEICON) values(4,'LEICHTER_REGEN','icons/man_u_32_32.png');
INSERT INTO PUBLIC.WEATHER (ID,WETTER, IMAGEICON) values(5,'REGEN','icons/man_u_32_32.png');
INSERT INTO PUBLIC.WEATHER (ID,WETTER, IMAGEICON) values(6,'STARKER_REGEN','icons/man_u_32_32.png');
INSERT INTO PUBLIC.WEATHER (ID,WETTER, IMAGEICON) values(7,'LEICHTER_SCHNEE','icons/man_u_32_32.png');
INSERT INTO PUBLIC.WEATHER (ID,WETTER, IMAGEICON) values(8,'SCHNEE','icons/man_u_32_32.png');
INSERT INTO PUBLIC.WEATHER (ID,WETTER, IMAGEICON) values(9,'UNBEKANNT','icons/man_u_32_32.png');