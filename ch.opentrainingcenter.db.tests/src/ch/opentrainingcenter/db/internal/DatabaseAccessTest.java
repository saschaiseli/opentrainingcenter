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

import ch.opentrainingcenter.db.DatabaseAccess;
import ch.opentrainingcenter.db.internal.AthleteDao;
import ch.opentrainingcenter.db.internal.RouteDao;
import ch.opentrainingcenter.db.internal.TrainingTypeDao;
import ch.opentrainingcenter.db.internal.WeatherDao;
import ch.opentrainingcenter.transfer.ActivityExtension;
import ch.opentrainingcenter.transfer.CommonTransferFactory;
import ch.opentrainingcenter.transfer.IAthlete;
import ch.opentrainingcenter.transfer.IRoute;
import ch.opentrainingcenter.transfer.IStreckenPunkt;
import ch.opentrainingcenter.transfer.ITrackPointProperty;
import ch.opentrainingcenter.transfer.ITraining;
import ch.opentrainingcenter.transfer.ITrainingType;
import ch.opentrainingcenter.transfer.IWeather;

@SuppressWarnings("nls")
public class DatabaseAccessTest extends DatabaseTestBase {

    private DatabaseAccess access;
    private long now;
    private IWeather weatherA;
    private IWeather weatherB;
    private AthleteDao athleteDao;
    private RouteDao routeDao;
    private TrainingTypeDao trainingTypeDao;

    @Override
    @Before
    public void setUp() {
        super.setUp();
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
        final ActivityExtension activityExtension = new ActivityExtension("note", weatherA, null);
        final ITraining training = CommonTransferFactory.createTraining(now, 1, 2, 3, 4, 5, activityExtension);
        final int id = access.saveTraining(training);
        assertTrue("Id ist sicherlich grösser als 0", 0 <= id);
    }

    @Test
    public void testTraining_2() {
        final IAthlete athlete = CommonTransferFactory.createAthlete("testTraining_2", 22, 222);
        final IRoute route = CommonTransferFactory.createRoute("name", "beschreibung", athlete);
        athleteDao.save(athlete);
        routeDao.saveOrUpdate(route);

        final ActivityExtension activityExtension = new ActivityExtension("note", weatherA, route);
        final ITraining training = CommonTransferFactory.createTraining(now, 1, 2, 3, 4, 5, activityExtension);
        training.setAthlete(athlete);
        training.setRoute(route);

        access.saveTraining(training);

        final ITraining result = access.getImportedRecord(now);

        assertNotNull(result);

        assertEquals("note", activityExtension.getNote());
        assertEquals(weatherA, activityExtension.getWeather());
        assertEquals(route, activityExtension.getRoute());
    }

    @Test
    public void testTraining_3() {
        final IAthlete athlete = CommonTransferFactory.createAthlete("testTraining_3", 22, 222);
        final IRoute route = CommonTransferFactory.createRoute("testTraining_3_route", "beschreibung", athlete);
        athleteDao.save(athlete);
        routeDao.saveOrUpdate(route);

        final ActivityExtension activityExtension = new ActivityExtension("note", weatherA, null);
        final ITraining training = CommonTransferFactory.createTraining(now, 1, 2, 3, 4, 5, activityExtension);
        training.setAthlete(athlete);
        training.setRoute(route);

        final List<ITrackPointProperty> trackPoints = new ArrayList<ITrackPointProperty>();
        final IStreckenPunkt streckenPunkt = CommonTransferFactory.createStreckenPunkt(1.1, 2.2, 3.3);
        final ITrackPointProperty property = CommonTransferFactory.createTrackPointProperty(2.0, 100, 550, 2000, 1, streckenPunkt);

        trackPoints.add(property);
        training.setTrackPoints(trackPoints);

        access.saveTraining(training);

        final ITraining result = access.getImportedRecord(now);

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
        assertEquals(1.1, punkt.getDistance(), 0.00001);
        assertEquals(1.1, punkt.getLongitude(), 2.2);
        assertEquals(1.1, punkt.getLatitude(), 3.3);
    }

    @Test
    public void testTraining_4_update_note_weather_route() {
        final IAthlete athlete = CommonTransferFactory.createAthlete("testTraining_3", 22, 222);
        athleteDao.save(athlete);

        final IRoute routeA = CommonTransferFactory.createRoute("testTraining_4_route", "beschreibungA", athlete);
        final IRoute routeB = CommonTransferFactory.createRoute("testTraining_44_route", "beschreibungB", athlete);
        routeDao.saveOrUpdate(routeA);
        routeDao.saveOrUpdate(routeB);

        final ActivityExtension activityExtension = new ActivityExtension("note1", weatherA, null);
        final ITraining training = CommonTransferFactory.createTraining(now, 1, 2, 3, 4, 5, activityExtension);
        training.setAthlete(athlete);
        training.setRoute(routeA);

        access.saveTraining(training);

        training.setNote("note2");
        training.setWeather(weatherB);
        training.setRoute(routeB);

        access.saveTraining(training);

        final ITraining result = access.getImportedRecord(now);

        assertEquals("note2", result.getNote());
        assertEquals(weatherB, result.getWeather());
        assertEquals(routeB, result.getRoute());
        assertEquals("beschreibungB", routeB.getBeschreibung());
    }

    @Test
    public void testTraining_5_getAllImported() {
        final IAthlete athleteA = CommonTransferFactory.createAthlete("testTraining_5_A", 22, 222);
        athleteDao.save(athleteA);

        final IAthlete athleteB = CommonTransferFactory.createAthlete("testTraining_5_B", 42, 242);
        athleteDao.save(athleteB);

        final ActivityExtension activityExtension = new ActivityExtension("note1", weatherA, null);
        final ITraining trainingA = CommonTransferFactory.createTraining(now, 1, 2, 3, 4, 5, activityExtension);
        trainingA.setAthlete(athleteA);

        final ITraining trainingB = CommonTransferFactory.createTraining(now + 1, 1, 2, 3, 4, 5, activityExtension);
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
        final IAthlete athleteA = CommonTransferFactory.createAthlete("testTraining_6", 22, 222);
        athleteDao.save(athleteA);

        final ActivityExtension activityExtension = new ActivityExtension("note1", weatherA, null);
        final ITraining trainingA = CommonTransferFactory.createTraining(now - 20, 1, 2, 3, 4, 5, activityExtension);
        final ITraining trainingB = CommonTransferFactory.createTraining(now + 1000, 1, 2, 3, 4, 5, activityExtension);

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
        final IAthlete athleteA = CommonTransferFactory.createAthlete("testTraining_7", 22, 222);
        athleteDao.save(athleteA);

        final ActivityExtension activityExtension = new ActivityExtension("note1", weatherA, null);
        final ITraining trainingA = CommonTransferFactory.createTraining(now - 20, 1, 2, 3, 4, 5, activityExtension);
        final ITraining trainingB = CommonTransferFactory.createTraining(now + 1000, 1, 2, 3, 4, 5, activityExtension);
        final ITraining trainingC = CommonTransferFactory.createTraining(now + 1001, 1, 2, 3, 4, 5, activityExtension);
        final ITraining trainingD = CommonTransferFactory.createTraining(now + 1002, 1, 2, 3, 4, 5, activityExtension);
        final ITraining trainingE = CommonTransferFactory.createTraining(now + 1003, 1, 2, 3, 4, 5, activityExtension);

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
        final IAthlete athleteA = CommonTransferFactory.createAthlete("testTraining_7", 22, 222);
        athleteDao.save(athleteA);

        final ActivityExtension activityExtension = new ActivityExtension("note1", weatherA, null);
        final ITraining trainingA = CommonTransferFactory.createTraining(now - 22, 1, 2, 3, 4, 5, activityExtension);
        final ITraining trainingB = CommonTransferFactory.createTraining(now + 1200, 1, 2, 3, 4, 5, activityExtension);
        final ITraining trainingC = CommonTransferFactory.createTraining(now + 1201, 1, 2, 3, 4, 5, activityExtension);
        final ITraining trainingD = CommonTransferFactory.createTraining(now + 1202, 1, 2, 3, 4, 5, activityExtension);
        final ITraining trainingE = CommonTransferFactory.createTraining(now + 2000, 1, 2, 3, 4, 5, activityExtension);

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
        final IAthlete athleteA = CommonTransferFactory.createAthlete("testTraining_9", 22, 222);
        athleteDao.save(athleteA);
        final ITraining newest = access.getNewestRun(athleteA);
        assertNull(newest);
    }

    @Test
    public void testTraining_10_remove() {
        final IAthlete athleteA = CommonTransferFactory.createAthlete("testTraining_9", 22, 222);
        athleteDao.save(athleteA);

        final ActivityExtension activityExtension = new ActivityExtension("note1", weatherA, null);
        final ITraining trainingA = CommonTransferFactory.createTraining(now, 1, 2, 3, 4, 5, activityExtension);

        trainingA.setAthlete(athleteA);

        access.saveTraining(trainingA);

        ITraining record = access.getImportedRecord(now);
        assertNotNull(record);
        access.removeImportedRecord(now);
        record = access.getImportedRecord(now);
        assertNull(record);
    }

    @Test
    public void testTraining_11_updateRecord() {
        final IAthlete athleteA = CommonTransferFactory.createAthlete("testTraining_9", 22, 222);
        athleteDao.save(athleteA);
        final List<ITrainingType> types = trainingTypeDao.getTrainingType();
        final ActivityExtension activityExtension = new ActivityExtension("note1", weatherA, null);
        final ITraining trainingA = CommonTransferFactory.createTraining(now, 1, 2, 3, 4, 5, activityExtension);

        trainingA.setAthlete(athleteA);
        trainingA.setTrainingType(types.get(0));

        access.saveTraining(trainingA);

        ITraining result = access.getImportedRecord(now);

        assertEquals(0, result.getTrainingType().getId());

        trainingA.setTrainingType(types.get(1));

        access.saveTraining(trainingA);

        result = access.getImportedRecord(now);

        assertEquals(1, result.getTrainingType().getId());

        access.updateRecord(trainingA, 2);

        result = access.getImportedRecord(now);

        assertEquals(2, result.getTrainingType().getId());
    }

    @Test
    public void testTraining_12_updateRoute() {
        final IAthlete athlete = CommonTransferFactory.createAthlete("testTraining_2", 22, 222);
        final IRoute routeA = CommonTransferFactory.createRoute("nameA", "beschreibungA", athlete);
        final IRoute routeB = CommonTransferFactory.createRoute("nameB", "beschreibungB", athlete);
        athleteDao.save(athlete);
        final int idA = routeDao.saveOrUpdate(routeA);
        final int idB = routeDao.saveOrUpdate(routeB);

        final ActivityExtension activityExtension = new ActivityExtension("note", weatherA, routeA);
        final ITraining training = CommonTransferFactory.createTraining(now, 1, 2, 3, 4, 5, activityExtension);
        training.setAthlete(athlete);

        access.saveTraining(training);

        ITraining result = access.getImportedRecord(now);

        assertNotNull(result);

        assertEquals("note", activityExtension.getNote());
        assertEquals(weatherA, activityExtension.getWeather());
        assertEquals(routeA, activityExtension.getRoute());

        training.setRoute(routeB);
        access.updateRecordRoute(training, idB);
        result = access.getImportedRecord(now);
        assertEquals(routeB, result.getRoute());

        access.updateRecordRoute(training, idA);
        result = access.getImportedRecord(now);
        assertEquals(routeA, result.getRoute());
    }

    @Test
    public void testTraining_13() {
        final IAthlete athlete = CommonTransferFactory.createAthlete("testTraining_2", 22, 222);
        final IRoute route = CommonTransferFactory.createRoute("name", "beschreibung", athlete);
        athleteDao.save(athlete);
        routeDao.saveOrUpdate(route);

        final ActivityExtension activityExtension = new ActivityExtension("note", weatherA, route);
        final ITraining training = CommonTransferFactory.createTraining(now, 1, 2, 3, 4, 5, activityExtension);
        training.setAthlete(athlete);
        training.setRoute(route);
        training.setDateOfImport(new Date(now));
        training.setFileName("22342342skflsdjfs.gpx");
        access.saveTraining(training);

        final ITraining result = access.getImportedRecord(now);

        assertEquals(new Date(now), result.getDateOfImport());
        assertEquals(training.getFileName(), result.getFileName());
    }
}
