package ch.opentrainingcenter.db.internal;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;

import ch.opentrainingcenter.database.dao.AthleteDao;
import ch.opentrainingcenter.database.dao.CommonDao;
import ch.opentrainingcenter.database.dao.RouteDao;
import ch.opentrainingcenter.database.dao.WeatherDao;
import ch.opentrainingcenter.transfer.HeartRate;
import ch.opentrainingcenter.transfer.IAthlete;
import ch.opentrainingcenter.transfer.IRoute;
import ch.opentrainingcenter.transfer.ITraining;
import ch.opentrainingcenter.transfer.IWeather;
import ch.opentrainingcenter.transfer.RunData;
import ch.opentrainingcenter.transfer.TrainingType;
import ch.opentrainingcenter.transfer.factory.CommonTransferFactory;

@SuppressWarnings("nls")
public class RouteDaoTest extends DatabaseTestBase {

    private RouteDao routeDao;
    private IAthlete athlete;
    private long now;
    private IWeather weatherA;
    private ITraining training;
    private String name;
    private String beschreibung;
    private CommonDao access;

    @Before
    public void setUp() {
        name = "testSaveRoute1";
        beschreibung = "testet ob route gespeichert wird";

        routeDao = new RouteDao(connectionConfig);

        final AthleteDao athleteDao = new AthleteDao(connectionConfig);
        athlete = CommonTransferFactory.createAthlete("junit", DateTime.now().toDate(), 220);
        athleteDao.save(athlete);

        now = DateTime.now().getMillis();

        final WeatherDao weatherDao = new WeatherDao(connectionConfig);
        weatherA = weatherDao.getAllWeather().get(0);

        final RunData runData = new RunData(now, 1, 2, 5);
        final HeartRate heart = new HeartRate(3, 4);
        training = CommonTransferFactory.createTraining(runData, heart, 5, "note", weatherA, null);
        training.setAthlete(athlete);
        training.setTrainingType(TrainingType.NONE);
        access = new CommonDao(connectionConfig);

        access.saveOrUpdate(training);

        connectionConfig.getSession().close();
    }

    @Test
    public void testSaveRoute() {

        final IRoute route = CommonTransferFactory.createRoute(name, beschreibung, training);
        final int id = routeDao.saveOrUpdate(route);
        assertTrue(0 <= id);
        final List<IRoute> routen = routeDao.getRoute(athlete);
        assertNotNull(routen);
        assertEquals(name, routen.get(0).getName());
        assertEquals(beschreibung, routen.get(0).getBeschreibung());
    }

    @Test
    public void testSaveRouteMitReferenzStrecke() {
        final String name = "testSaveRoute1";
        final String beschreibung = "testet ob route gespeichert wird";

        final IRoute exists = routeDao.getRoute(name, athlete);
        assertNull(exists);

        final RunData runData = new RunData(now + 100, 1, 2, 5);
        final HeartRate heart = new HeartRate(3, 4);
        final ITraining trainingB = CommonTransferFactory.createTraining(runData, heart, 5, "noteb", weatherA, null);
        trainingB.setAthlete(athlete);
        access.saveOrUpdate(trainingB);

        final IRoute route = CommonTransferFactory.createRoute(name, beschreibung, training);
        final int id = routeDao.saveOrUpdate(route);
        assertTrue(0 <= id);
        final List<IRoute> routen = routeDao.getRoute(athlete);
        assertNotNull(routen);
        assertEquals(name, routen.get(0).getName());
        assertEquals(beschreibung, routen.get(0).getBeschreibung());
    }

    @Test
    public void testGetRouteById() {
        final String name = "testSaveRoute1";
        final String beschreibung = "testet ob route gespeichert wird";

        final IRoute route = CommonTransferFactory.createRoute(name, beschreibung, training);
        final int id = routeDao.saveOrUpdate(route);

        final IRoute result = routeDao.getById(id);
        assertNotNull(result);
        assertEquals(route.getId(), result.getId());
    }

    @Test
    public void testUpdateRoute() {
        final String name = "testSaveRoute1";
        final String beschreibung = "testet ob route gespeichert wird";

        final IRoute route = CommonTransferFactory.createRoute(name, beschreibung, training);
        routeDao.saveOrUpdate(route);

        route.setBeschreibung("updated");
        routeDao.saveOrUpdate(route);

        final IRoute result = routeDao.getRoute(name, athlete);
        assertNotNull(result);
        assertEquals("updated", result.getBeschreibung());
    }

    @Test
    public void testGetRoutePositiv() {
        final String beschreibung = "testet ob route gespeichert wird";
        final String name = "id2";
        final IRoute route = CommonTransferFactory.createRoute(name, beschreibung, training);
        routeDao.saveOrUpdate(route);

        final IRoute result = routeDao.getRoute(name, athlete);
        assertNotNull(result);
        assertEquals(name, result.getName());
        assertEquals(beschreibung, result.getBeschreibung());
    }

    @Test
    public void testGetRouteNegativ() {
        final String beschreibung = "testet ob route gespeichert wird";
        final String name = "id3";
        final IRoute route = CommonTransferFactory.createRoute(name, beschreibung, training);
        routeDao.saveOrUpdate(route);

        final IRoute result = routeDao.getRoute("id4", athlete);
        assertNull(result);
    }
}
