package ch.opentrainingcenter.db.internal;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;

import ch.opentrainingcenter.db.DatabaseAccess;
import ch.opentrainingcenter.transfer.ActivityExtension;
import ch.opentrainingcenter.transfer.CommonTransferFactory;
import ch.opentrainingcenter.transfer.IAthlete;
import ch.opentrainingcenter.transfer.IRoute;
import ch.opentrainingcenter.transfer.ITraining;
import ch.opentrainingcenter.transfer.IWeather;

@SuppressWarnings("nls")
public class RouteDaoTest extends DatabaseTestBase {

    private RouteDao routeDao;
    private IAthlete athlete;
    private long now;
    private IWeather weatherA;
    private ITraining training;
    private DatabaseAccess access;

    @Before
    public void setUp() {
        routeDao = new RouteDao(dao);

        final AthleteDao athleteDao = new AthleteDao(dao);
        athlete = CommonTransferFactory.createAthlete("junit", 220);
        athleteDao.save(athlete);

        now = DateTime.now().getMillis();

        final WeatherDao weatherDao = new WeatherDao(dao);
        weatherA = weatherDao.getAllWeather().get(0);

        final ActivityExtension activityExtension = new ActivityExtension("note", weatherA, null);
        training = CommonTransferFactory.createTraining(now, 1, 2, 3, 4, 5, activityExtension);
        training.setAthlete(athlete);

        access = new DatabaseAccess(dao);
        access.saveTraining(training);

        dao.getSession().close();
    }

    @Test
    public void testSaveRoute() {
        final String name = "testSaveRoute1";
        final String beschreibung = "testet ob route gespeichert wird";

        final IRoute route = CommonTransferFactory.createRoute(name, beschreibung, training);
        final int id = routeDao.saveOrUpdate(route);
        assertTrue(0 <= id);
        final List<IRoute> routen = routeDao.getRoute(athlete);
        assertNotNull(routen);
        assertEquals(name, routen.get(0).getName());
        assertEquals(beschreibung, routen.get(0).getBeschreibung());
    }

    @Test
    public void testUpdateRoute() {
        final String name = "testSaveRoute2";
        final String beschreibung = "testet ob route gespeichert wird";

        IRoute route = CommonTransferFactory.createRoute(name, beschreibung, training);
        routeDao.saveOrUpdate(route);

        route = CommonTransferFactory.createRoute(name, "updated", training);
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
