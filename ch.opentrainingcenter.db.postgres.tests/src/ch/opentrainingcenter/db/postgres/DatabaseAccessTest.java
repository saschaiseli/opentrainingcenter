package ch.opentrainingcenter.db.postgres;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;

import ch.opentrainingcenter.db.DatabaseAccess;
import ch.opentrainingcenter.db.internal.AthleteDao;
import ch.opentrainingcenter.db.internal.RouteDao;
import ch.opentrainingcenter.db.internal.TrainingTypeDao;
import ch.opentrainingcenter.db.internal.WeatherDao;
import ch.opentrainingcenter.transfer.CommonTransferFactory;
import ch.opentrainingcenter.transfer.IAthlete;
import ch.opentrainingcenter.transfer.IRoute;
import ch.opentrainingcenter.transfer.IStreckenPunkt;
import ch.opentrainingcenter.transfer.ITrackPointProperty;
import ch.opentrainingcenter.transfer.ITraining;
import ch.opentrainingcenter.transfer.ITrainingType;
import ch.opentrainingcenter.transfer.IWeather;

@SuppressWarnings("nls")
public class DatabaseAccessTest extends PostgresDatabaseTestBase {

    private DatabaseAccess access;
    private long now;
    private IWeather weatherA;
    private IWeather weatherB;
    private AthleteDao athleteDao;
    private RouteDao routeDao;
    private TrainingTypeDao trainingTypeDao;

    @Before
    public void setUp() {
        access = new DatabaseAccess(dao);
        final WeatherDao weatherDao = new WeatherDao(dao);
        weatherA = weatherDao.getAllWeather().get(0);
        weatherB = weatherDao.getAllWeather().get(1);
        athleteDao = new AthleteDao(dao);
        routeDao = new RouteDao(dao);
        trainingTypeDao = new TrainingTypeDao(dao);
        now = DateTime.now().getMillis();
    }

    @Test
    public void testTraining_1() {
        final ITraining training = CommonTransferFactory.createTraining(now, 1, 2, 3, 4, 5, "note", weatherA, null);
        final int id = access.saveTraining(training);
        assertTrue("Id ist sicherlich grösser als 0", 0 <= id);
    }

    @Test
    public void testTraining_2() {
        final IAthlete athlete = CommonTransferFactory.createAthlete("testTraining_2", 222);
        athleteDao.save(athlete);

        final ITraining training = CommonTransferFactory.createTraining(now, 1, 2, 3, 4, 5, "note", weatherA, null);
        training.setAthlete(athlete);
        // training.setRoute(route);

        access.saveTraining(training);

        final IRoute route = CommonTransferFactory.createRoute("name", "beschreibung", training);
        routeDao.saveOrUpdate(route);

        training.setRoute(route);

        access.saveTraining(training);

        final ITraining result = access.getTrainingById(now);

        assertNotNull(result);

        assertEquals("note", result.getNote());
        assertEquals(weatherA, result.getWeather());
        assertEquals(route, result.getRoute());
    }

    @Test
    public void testTraining_3() {
        final IAthlete athlete = CommonTransferFactory.createAthlete("testTraining_3", 222);
        athleteDao.save(athlete);

        final ITraining training = CommonTransferFactory.createTraining(now, 1, 2, 3, 4, 5, "note", weatherA, null);
        training.setAthlete(athlete);

        final List<ITrackPointProperty> trackPoints = new ArrayList<ITrackPointProperty>();
        final IStreckenPunkt streckenPunkt = CommonTransferFactory.createStreckenPunkt(1.1, 2.2, 3.3);
        final ITrackPointProperty property = CommonTransferFactory.createTrackPointProperty(2.0, 100, 550, 2000, 1, streckenPunkt);

        trackPoints.add(property);
        training.setTrackPoints(trackPoints);

        access.saveTraining(training);

        final IRoute route = CommonTransferFactory.createRoute("testTraining_3_route", "beschreibung", training);
        routeDao.saveOrUpdate(route);

        final ITraining result = access.getTrainingById(now);

        assertNotNull(result);

        final List<ITrackPointProperty> list = result.getTrackPoints();
        assertNotNull(list);
        assertFalse(list.isEmpty());

        final ITrackPointProperty prop = list.get(0);

        assertNotNull(prop);
        assertEquals(2.0, prop.getDistance(), 0.0001);
        assertEquals(100, prop.getHeartBeat());
        assertEquals(550, prop.getAltitude());
        assertEquals(2000, prop.getZeit());
        assertEquals(1, prop.getLap());

        final IStreckenPunkt punkt = prop.getStreckenPunkt();
        assertNotNull(punkt);
        assertEquals(1.1, punkt.getLongitude(), 2.2);
        assertEquals(1.1, punkt.getLatitude(), 3.3);
    }

    @Test
    public void testTraining_4_update_note_weather_route() {
        final IAthlete athlete = CommonTransferFactory.createAthlete("testTraining_4", 222);
        athleteDao.save(athlete);

        final ITraining training = CommonTransferFactory.createTraining(now, 1, 2, 3, 4, 5, "note1", weatherA, null);

        training.setAthlete(athlete);

        access.saveTraining(training);

        final IRoute routeA = CommonTransferFactory.createRoute("testTraining_4_route", "beschreibungA", training);
        routeDao.saveOrUpdate(routeA);

        final IRoute routeB = CommonTransferFactory.createRoute("testTraining_44_route", "beschreibungB", training);
        routeDao.saveOrUpdate(routeB);

        training.setNote("note2");
        training.setWeather(weatherB);
        training.setRoute(routeB);

        access.saveTraining(training);

        final ITraining result = access.getTrainingById(now);

        assertEquals("note2", result.getNote());
        assertEquals(weatherB, result.getWeather());
        assertEquals(routeB, result.getRoute());
        assertEquals("beschreibungB", routeB.getBeschreibung());
    }

    @Test
    public void testTraining_5_getAllMitRoute() {
        final IAthlete athleteA = CommonTransferFactory.createAthlete("testTraining_5_A", 222);
        athleteDao.save(athleteA);

        final ITraining referenzTraining = CommonTransferFactory.createTraining(now, 1, 2, 3, 4, 5, "note1", weatherA, null);
        referenzTraining.setAthlete(athleteA);

        final ITraining training = CommonTransferFactory.createTraining(now + 1, 1, 2, 3, 4, 5, "note1", weatherA, null);
        training.setAthlete(athleteA);

        access.saveTraining(referenzTraining);
        access.saveTraining(training);

        final IRoute routeA = CommonTransferFactory.createRoute("name", "beschreibung", referenzTraining);
        routeDao.saveOrUpdate(routeA);

        referenzTraining.setRoute(routeA);
        training.setRoute(routeA);

        access.saveTraining(referenzTraining);
        access.saveTraining(training);

        List<ITraining> result = access.getAllFromRoute(athleteA, routeA);

        assertEquals("Zwei Trainings müssen gefunden werden", 2, result.size());

        training.setRoute(null);
        access.saveTraining(training);
        result = access.getAllFromRoute(athleteA, routeA);
        assertEquals("Nur noch ein Training muss gefunden werden", 1, result.size());
    }

    @Test
    public void testTraining_5_getAllImported() {
        final IAthlete athleteA = CommonTransferFactory.createAthlete("testTraining_5_A", 222);
        athleteDao.save(athleteA);

        final IAthlete athleteB = CommonTransferFactory.createAthlete("testTraining_5_B", 242);
        athleteDao.save(athleteB);

        final ITraining trainingA = CommonTransferFactory.createTraining(now, 1, 2, 3, 4, 5, "note1", weatherA, null);
        trainingA.setAthlete(athleteA);

        final ITraining trainingB = CommonTransferFactory.createTraining(now + 1, 1, 2, 3, 4, 5, "note1", weatherA, null);
        trainingB.setAthlete(athleteB);

        access.saveTraining(trainingA);
        access.saveTraining(trainingB);

        final List<ITraining> allFromAthleteA = access.getAllImported(athleteA);
        final List<ITraining> allFromAthleteB = access.getAllImported(athleteB);

        assertEquals(1, allFromAthleteA.size());
        assertEquals(1, allFromAthleteB.size());
    }

    @Test
    public void testTraining_6_getAllImported_Sort() {
        final IAthlete athleteA = CommonTransferFactory.createAthlete("testTraining_6", 222);
        athleteDao.save(athleteA);

        final ITraining trainingA = CommonTransferFactory.createTraining(now - 20, 1, 2, 3, 4, 5, "note1", weatherA, null);
        final ITraining trainingB = CommonTransferFactory.createTraining(now + 1000, 1, 2, 3, 4, 5, "note1", weatherA, null);

        trainingA.setAthlete(athleteA);
        trainingB.setAthlete(athleteA);

        access.saveTraining(trainingA);
        access.saveTraining(trainingB);

        final List<ITraining> allFromAthleteA = access.getAllImported(athleteA);

        assertEquals(2, allFromAthleteA.size());

        final ITraining first = allFromAthleteA.get(0);
        final ITraining second = allFromAthleteA.get(1);

        assertTrue(first.getDatum() > second.getDatum());
    }

    @Test
    public void testTraining_7_getAllImported_Sort_And_Limit() {
        final IAthlete athleteA = CommonTransferFactory.createAthlete("testTraining_7", 222);
        athleteDao.save(athleteA);

        final ITraining trainingA = CommonTransferFactory.createTraining(now - 20, 1, 2, 3, 4, 5, "note1", weatherA, null);
        final ITraining trainingB = CommonTransferFactory.createTraining(now + 1000, 1, 2, 3, 4, 5, "note1", weatherA, null);
        final ITraining trainingC = CommonTransferFactory.createTraining(now + 1001, 1, 2, 3, 4, 5, "note1", weatherA, null);
        final ITraining trainingD = CommonTransferFactory.createTraining(now + 1002, 1, 2, 3, 4, 5, "note1", weatherA, null);
        final ITraining trainingE = CommonTransferFactory.createTraining(now + 1003, 1, 2, 3, 4, 5, "note1", weatherA, null);

        trainingA.setAthlete(athleteA);
        trainingB.setAthlete(athleteA);
        trainingC.setAthlete(athleteA);
        trainingD.setAthlete(athleteA);
        trainingE.setAthlete(athleteA);

        access.saveTraining(trainingA);
        access.saveTraining(trainingB);
        access.saveTraining(trainingC);
        access.saveTraining(trainingD);
        access.saveTraining(trainingE);

        final List<ITraining> allFromAthleteA = access.getAllImported(athleteA);

        assertEquals(5, allFromAthleteA.size());

        final List<ITraining> limited = access.getAllImported(athleteA, 2);

        assertEquals(2, limited.size());
    }

    @Test
    public void testTraining_8_getNewest() {
        final IAthlete athleteA = CommonTransferFactory.createAthlete("testTraining_8", 222);
        athleteDao.save(athleteA);

        final ITraining trainingA = CommonTransferFactory.createTraining(now - 22, 1, 2, 3, 4, 5, "note1", weatherA, null);
        final ITraining trainingB = CommonTransferFactory.createTraining(now + 1200, 1, 2, 3, 4, 5, "note1", weatherA, null);
        final ITraining trainingC = CommonTransferFactory.createTraining(now + 1201, 1, 2, 3, 4, 5, "note1", weatherA, null);
        final ITraining trainingD = CommonTransferFactory.createTraining(now + 1202, 1, 2, 3, 4, 5, "note1", weatherA, null);
        final ITraining trainingE = CommonTransferFactory.createTraining(now + 2000, 1, 2, 3, 4, 5, "note1", weatherA, null);

        trainingA.setAthlete(athleteA);
        trainingB.setAthlete(athleteA);
        trainingC.setAthlete(athleteA);
        trainingD.setAthlete(athleteA);
        trainingE.setAthlete(athleteA);

        access.saveTraining(trainingA);
        access.saveTraining(trainingB);
        access.saveTraining(trainingC);
        access.saveTraining(trainingD);
        access.saveTraining(trainingE);

        final ITraining newest = access.getNewestRun(athleteA);

        assertEquals(now + 2000, newest.getDatum());
    }

    @Test
    public void testTraining_9_getNewestEmpty() {
        final IAthlete athleteA = CommonTransferFactory.createAthlete("testTraining_9", 222);
        athleteDao.save(athleteA);
        final ITraining newest = access.getNewestRun(athleteA);
        assertNull(newest);
    }

    @Test
    public void testTraining_10_remove() {
        final IAthlete athleteA = CommonTransferFactory.createAthlete("testTraining_9", 222);
        athleteDao.save(athleteA);

        final ITraining trainingA = CommonTransferFactory.createTraining(now, 1, 2, 3, 4, 5, "note1", weatherA, null);

        trainingA.setAthlete(athleteA);

        access.saveTraining(trainingA);

        ITraining record = access.getTrainingById(now);
        assertNotNull(record);
        access.removeImportedRecord(now);
        record = access.getTrainingById(now);
        assertNull(record);
    }

    @Test
    public void testTraining_11_updateRecord() {
        final IAthlete athleteA = CommonTransferFactory.createAthlete("testTraining_9", 222);
        athleteDao.save(athleteA);
        final List<ITrainingType> types = trainingTypeDao.getTrainingType();
        final ITraining trainingA = CommonTransferFactory.createTraining(now, 1, 2, 3, 4, 5, "note1", weatherA, null);

        trainingA.setAthlete(athleteA);
        trainingA.setTrainingType(types.get(0));

        access.saveTraining(trainingA);

        ITraining result = access.getTrainingById(now);

        assertEquals(0, result.getTrainingType().getId());

        trainingA.setTrainingType(types.get(1));

        access.saveTraining(trainingA);

        result = access.getTrainingById(now);

        assertEquals(1, result.getTrainingType().getId());

        access.updateRecord(trainingA, 2);

        result = access.getTrainingById(now);

        assertEquals(2, result.getTrainingType().getId());
    }

    @Test
    public void testTraining_12_updateRoute() {
        final IAthlete athlete = CommonTransferFactory.createAthlete("testTraining_12", 222);
        athleteDao.save(athlete);

        final ITraining training = CommonTransferFactory.createTraining(now, 1, 2, 3, 4, 5, "note", weatherA, null);
        training.setAthlete(athlete);

        access.saveTraining(training);

        final IRoute routeA = CommonTransferFactory.createRoute("nameA", "beschreibungA", training);
        final IRoute routeB = CommonTransferFactory.createRoute("nameB", "beschreibungB", training);

        final int idA = routeDao.saveOrUpdate(routeA);
        final int idB = routeDao.saveOrUpdate(routeB);

        ITraining result = access.getTrainingById(now);

        assertNotNull(result);

        training.setRoute(routeB);

        access.updateRecordRoute(training, idB);

        result = access.getTrainingById(now);
        assertEquals(routeB, result.getRoute());

        access.updateRecordRoute(training, idA);
        result = access.getTrainingById(now);
        assertEquals(routeA, result.getRoute());
    }

    @Test
    public void testTraining_13() {
        final IAthlete athlete = CommonTransferFactory.createAthlete("testTraining_13", 222);
        athleteDao.save(athlete);

        final ITraining training = CommonTransferFactory.createTraining(now, 1, 2, 3, 4, 5, "note", weatherA, null);
        training.setAthlete(athlete);
        training.setDateOfImport(new Date(now));
        training.setFileName("22342342skflsdjfs.gpx");

        access.saveTraining(training);

        final IRoute route = CommonTransferFactory.createRoute("name", "beschreibung", training);
        routeDao.saveOrUpdate(route);

        final ITraining result = access.getTrainingById(now);

        assertEquals(new Date(now), result.getDateOfImport());
        assertEquals(training.getFileName(), result.getFileName());
    }
}
