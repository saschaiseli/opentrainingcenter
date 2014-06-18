package ch.opentrainingcenter.db.internal;

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

import ch.opentrainingcenter.core.cache.TrainingCache;
import ch.opentrainingcenter.database.dao.AthleteDao;
import ch.opentrainingcenter.database.dao.CommonDao;
import ch.opentrainingcenter.database.dao.RouteDao;
import ch.opentrainingcenter.database.dao.TrainingTypeDao;
import ch.opentrainingcenter.database.dao.WeatherDao;
import ch.opentrainingcenter.transfer.IAthlete;
import ch.opentrainingcenter.transfer.IRoute;
import ch.opentrainingcenter.transfer.IStreckenPunkt;
import ch.opentrainingcenter.transfer.ITrackPointProperty;
import ch.opentrainingcenter.transfer.ITraining;
import ch.opentrainingcenter.transfer.ITrainingType;
import ch.opentrainingcenter.transfer.IWeather;
import ch.opentrainingcenter.transfer.Sport;
import ch.opentrainingcenter.transfer.factory.CommonTransferFactory;

@SuppressWarnings("nls")
public class DatabaseAccessTest extends DatabaseTestBase {

    private CommonDao access;
    private long now;
    private IWeather weatherA;
    private IWeather weatherB;
    private AthleteDao athleteDao;
    private RouteDao routeDao;
    private TrainingTypeDao trainingTypeDao;

    @Before
    public void setUp() {
        TrainingCache.getInstance().resetCache();
        access = new CommonDao(connectionConfig);
        final WeatherDao weatherDao = new WeatherDao(connectionConfig);
        weatherA = weatherDao.getAllWeather().get(0);
        weatherB = weatherDao.getAllWeather().get(1);
        athleteDao = new AthleteDao(connectionConfig);
        routeDao = new RouteDao(connectionConfig);
        trainingTypeDao = new TrainingTypeDao(connectionConfig);
        now = DateTime.now().getMillis();
    }

    @Test
    public void testTraining_1() {
        final ITraining training = CommonTransferFactory.createTraining(now, 1, 2, 3, 4, 5, "note", weatherA, null);
        final int id = access.saveOrUpdate(training);
        assertTrue("Id ist sicherlich grösser als 0", 0 <= id);
    }

    @Test
    public void testTraining_2() {
        final IAthlete athlete = CommonTransferFactory.createAthlete("testTraining_2", 222);
        athleteDao.save(athlete);

        final ITraining training = CommonTransferFactory.createTraining(now, 1, 2, 3, 4, 5, "note", weatherA, null);
        training.setAthlete(athlete);
        // training.setRoute(route);

        dataAccess.saveOrUpdate(training);

        final IRoute route = CommonTransferFactory.createRoute("name", "beschreibung", training);
        routeDao.saveOrUpdate(route);

        training.setRoute(route);
        training.setSport(Sport.BIKING);

        dataAccess.saveOrUpdate(training);

        final ITraining result = access.getTrainingById(now);

        assertNotNull(result);

        assertEquals("note", result.getNote());
        assertEquals(weatherA, result.getWeather());
        assertEquals(route, result.getRoute());
        assertEquals(Sport.BIKING, result.getSport());
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

        dataAccess.saveOrUpdate(training);

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

        dataAccess.saveOrUpdate(training);

        final IRoute routeA = CommonTransferFactory.createRoute("testTraining_4_route", "beschreibungA", training);
        routeDao.saveOrUpdate(routeA);

        final IRoute routeB = CommonTransferFactory.createRoute("testTraining_44_route", "beschreibungB", training);
        routeDao.saveOrUpdate(routeB);

        training.setNote("note2");
        training.setWeather(weatherB);
        training.setRoute(routeB);

        dataAccess.saveOrUpdate(training);

        final ITraining result = dataAccess.getTrainingById(now);

        assertEquals("note2", result.getNote());
        assertEquals(weatherB, result.getWeather());
        assertEquals(routeB, result.getRoute());
        assertEquals("beschreibungB", routeB.getBeschreibung());
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

        dataAccess.saveOrUpdate(trainingA);
        dataAccess.saveOrUpdate(trainingB);

        final List<ITraining> allFromAthleteA = dataAccess.getAllTrainings(athleteA);
        final List<ITraining> allFromAthleteB = dataAccess.getAllTrainings(athleteB);

        assertEquals(1, allFromAthleteA.size());
        assertEquals(1, allFromAthleteB.size());
    }

    @Test
    public void testTraining_5_getAllMitRoute() {
        final IAthlete athleteA = CommonTransferFactory.createAthlete("testTraining_5_A", 222);
        athleteDao.save(athleteA);

        final ITraining referenzTraining = CommonTransferFactory.createTraining(now, 1, 2, 3, 4, 5, "note1", weatherA, null);
        referenzTraining.setAthlete(athleteA);

        final ITraining training = CommonTransferFactory.createTraining(now + 1, 1, 2, 3, 4, 5, "note1", weatherA, null);
        training.setAthlete(athleteA);

        dataAccess.saveOrUpdate(referenzTraining);
        dataAccess.saveOrUpdate(training);

        final IRoute routeA = CommonTransferFactory.createRoute("name", "beschreibung", referenzTraining);
        routeDao.saveOrUpdate(routeA);

        referenzTraining.setRoute(routeA);
        training.setRoute(routeA);

        dataAccess.saveOrUpdate(referenzTraining);
        dataAccess.saveOrUpdate(training);

        List<ITraining> result = dataAccess.getAllTrainingByRoute(athleteA, routeA);

        assertEquals("Zwei Trainings müssen gefunden werden", 2, result.size());

        training.setRoute(null);
        dataAccess.saveOrUpdate(training);
        result = dataAccess.getAllTrainingByRoute(athleteA, routeA);
        assertEquals("Nur noch ein Training muss gefunden werden", 1, result.size());
    }

    @Test
    public void testTraining_6_getAllImported_Sort() {
        final IAthlete athleteA = CommonTransferFactory.createAthlete("testTraining_6", 222);
        athleteDao.save(athleteA);

        final ITraining trainingA = CommonTransferFactory.createTraining(now - 20, 1, 2, 3, 4, 5, "note1", weatherA, null);
        final ITraining trainingB = CommonTransferFactory.createTraining(now + 1000, 1, 2, 3, 4, 5, "note1", weatherA, null);

        trainingA.setAthlete(athleteA);
        trainingB.setAthlete(athleteA);

        dataAccess.saveOrUpdate(trainingA);
        dataAccess.saveOrUpdate(trainingB);

        final List<ITraining> allFromAthleteA = dataAccess.getAllTrainings(athleteA);

        assertEquals(2, allFromAthleteA.size());

        final ITraining first = allFromAthleteA.get(0);
        final ITraining second = allFromAthleteA.get(1);

        assertTrue(first.getDatum() > second.getDatum());
    }

    // @Test
    // public void testTraining_7_getAllImported_Sort_And_Limit() {
    // final IAthlete athleteA =
    // CommonTransferFactory.createAthlete("testTraining_7", 222);
    // athleteDao.save(athleteA);
    //
    // final ITraining trainingA = CommonTransferFactory.createTraining(now -
    // 20, 1, 2, 3, 4, 5, "note1", weatherA, null);
    // final ITraining trainingB = CommonTransferFactory.createTraining(now +
    // 1000, 1, 2, 3, 4, 5, "note1", weatherA, null);
    // final ITraining trainingC = CommonTransferFactory.createTraining(now +
    // 1001, 1, 2, 3, 4, 5, "note1", weatherA, null);
    // final ITraining trainingD = CommonTransferFactory.createTraining(now +
    // 1002, 1, 2, 3, 4, 5, "note1", weatherA, null);
    // final ITraining trainingE = CommonTransferFactory.createTraining(now +
    // 1003, 1, 2, 3, 4, 5, "note1", weatherA, null);
    //
    // trainingA.setAthlete(athleteA);
    // trainingB.setAthlete(athleteA);
    // trainingC.setAthlete(athleteA);
    // trainingD.setAthlete(athleteA);
    // trainingE.setAthlete(athleteA);
    //
    // dataAccess.saveOrUpdate(trainingA);
    // dataAccess.saveOrUpdate(trainingB);
    // dataAccess.saveOrUpdate(trainingC);
    // dataAccess.saveOrUpdate(trainingD);
    // dataAccess.saveOrUpdate(trainingE);
    //
    // final List<ITraining> allFromAthleteA =
    // dataAccess.getAllTrainings(athleteA);
    //
    // assertEquals(5, allFromAthleteA.size());
    //
    // final List<ITraining> limited = dataAccess.getAllTrainings(athleteA, 2);
    //
    // assertEquals(2, limited.size());
    // }

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

        dataAccess.saveOrUpdate(trainingA);
        dataAccess.saveOrUpdate(trainingB);
        dataAccess.saveOrUpdate(trainingC);
        dataAccess.saveOrUpdate(trainingD);
        dataAccess.saveOrUpdate(trainingE);

        final ITraining newest = dataAccess.getNewestTraining(athleteA);

        assertEquals(now + 2000, newest.getDatum());
    }

    @Test
    public void testTraining_9_getNewestEmpty() {
        final IAthlete athleteA = CommonTransferFactory.createAthlete("testTraining_9", 222);
        athleteDao.save(athleteA);
        final ITraining newest = dataAccess.getNewestTraining(athleteA);
        assertNull(newest);
    }

    @Test
    public void testTraining_10_remove() {
        final IAthlete athleteA = CommonTransferFactory.createAthlete("testTraining_9", 222);
        athleteDao.save(athleteA);

        final ITraining trainingA = CommonTransferFactory.createTraining(now, 1, 2, 3, 4, 5, "note1", weatherA, null);

        trainingA.setAthlete(athleteA);

        dataAccess.saveOrUpdate(trainingA);

        ITraining record = dataAccess.getTrainingById(now);
        assertNotNull(record);
        dataAccess.removeTrainingByDate(now);
        record = dataAccess.getTrainingById(now);
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

        dataAccess.saveOrUpdate(trainingA);

        ITraining result = dataAccess.getTrainingById(now);

        assertEquals(0, result.getTrainingType().getId());

        trainingA.setTrainingType(types.get(1));

        dataAccess.saveOrUpdate(trainingA);

        result = dataAccess.getTrainingById(now);

        assertEquals(1, result.getTrainingType().getId());

        dataAccess.updateTrainingType(trainingA, 2);

        result = dataAccess.getTrainingById(now);

        assertEquals(2, result.getTrainingType().getId());
    }

    @Test
    public void testTraining_12_updateRoute() {
        final IAthlete athlete = CommonTransferFactory.createAthlete("testTraining_12", 222);
        athleteDao.save(athlete);

        final ITraining training = CommonTransferFactory.createTraining(now, 1, 2, 3, 4, 5, "note", weatherA, null);
        training.setAthlete(athlete);

        dataAccess.saveOrUpdate(training);

        final IRoute routeA = CommonTransferFactory.createRoute("nameA", "beschreibungA", training);
        final IRoute routeB = CommonTransferFactory.createRoute("nameB", "beschreibungB", training);

        final int idA = routeDao.saveOrUpdate(routeA);
        final int idB = routeDao.saveOrUpdate(routeB);

        ITraining result = dataAccess.getTrainingById(now);

        assertNotNull(result);

        training.setRoute(routeB);

        dataAccess.updateTrainingRoute(training, idB);

        result = dataAccess.getTrainingById(now);
        assertEquals(routeB, result.getRoute());

        dataAccess.updateTrainingRoute(training, idA);
        result = dataAccess.getTrainingById(now);
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

        dataAccess.saveOrUpdate(training);

        final IRoute route = CommonTransferFactory.createRoute("name", "beschreibung", training);
        routeDao.saveOrUpdate(route);

        final ITraining result = dataAccess.getTrainingById(now);

        assertEquals(new Date(now), result.getDateOfImport());
        assertEquals(training.getFileName(), result.getFileName());
    }
}
