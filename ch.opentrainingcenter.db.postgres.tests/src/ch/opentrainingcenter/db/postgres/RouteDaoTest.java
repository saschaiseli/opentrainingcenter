package ch.opentrainingcenter.db.postgres;

import java.util.List;

import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;

import ch.opentrainingcenter.core.db.IDatabaseAccess;
import ch.opentrainingcenter.database.dao.AthleteDao;
import ch.opentrainingcenter.database.dao.RouteDao;
import ch.opentrainingcenter.database.dao.WeatherDao;
import ch.opentrainingcenter.db.DatabaseAccess;
import ch.opentrainingcenter.transfer.IAthlete;
import ch.opentrainingcenter.transfer.IRoute;
import ch.opentrainingcenter.transfer.ITraining;
import ch.opentrainingcenter.transfer.IWeather;
import ch.opentrainingcenter.transfer.factory.CommonTransferFactory;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

@SuppressWarnings("nls")
public class RouteDaoTest extends PostgresDatabaseTestBase {

    private RouteDao routeDao;
    private IAthlete athlete;
    private long now;
    private IWeather weatherA;
    private ITraining training;
    private DatabaseAccess access;
    private IDatabaseAccess dataAccess;

    @Before
    public void setUp() {
        routeDao = new RouteDao(connectionConfig);

        final AthleteDao athleteDao = new AthleteDao(connectionConfig);
        athlete = CommonTransferFactory.createAthlete("junit", 220);
        athleteDao.save(athlete);

        now = DateTime.now().getMillis();

        final WeatherDao weatherDao = new WeatherDao(connectionConfig);
        weatherA = weatherDao.getAllWeather().get(0);

        training = CommonTransferFactory.createTraining(now, 1, 2, 3, 4, 5, "note", weatherA, null);
        training.setAthlete(athlete);

        access = new DatabaseAccess(connectionConfig);
        dataAccess = access.getDataAccess();
        dataAccess.saveOrUpdate(training);

        connectionConfig.getSession().close();
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