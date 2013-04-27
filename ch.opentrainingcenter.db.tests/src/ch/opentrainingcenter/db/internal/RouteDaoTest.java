package ch.opentrainingcenter.db.internal;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

import ch.opentrainingcenter.transfer.CommonTransferFactory;
import ch.opentrainingcenter.transfer.IAthlete;
import ch.opentrainingcenter.transfer.IRoute;

@SuppressWarnings("nls")
public class RouteDaoTest extends DatabaseTestBase {

    private RouteDao routeDao;
    private IAthlete athlete;

    @Before
    public void setUp() {
        routeDao = new RouteDao(dao);
        athlete = CommonTransferFactory.createAthlete("junit", 22, 220);

        final AthleteDao athleteDao = new AthleteDao(dao);
        athleteDao.save(athlete);
    }

    @Test
    public void testSaveRoute() {
        final String beschreibung = "testet ob route gespeichert wird";
        final String name = "testSaveRoute";
        final IRoute route = CommonTransferFactory.createRoute(name, beschreibung, athlete);
        final int id = routeDao.saveOrUpdate(route);
        assertTrue(0 <= id);
        final List<IRoute> routen = routeDao.getRoute(athlete);
        assertNotNull(routen);
        assertEquals(name, routen.get(0).getName());
        assertEquals(beschreibung, routen.get(0).getBeschreibung());
    }

    @Test
    public void testUpdateRoute() {
        final String beschreibung = "testet ob route gespeichert wird";
        final String name = "testSaveRoute";
        IRoute route = CommonTransferFactory.createRoute(name, beschreibung, athlete);
        routeDao.saveOrUpdate(route);

        route = CommonTransferFactory.createRoute(name, "updated", athlete);
        routeDao.saveOrUpdate(route);

        final IRoute result = routeDao.getRoute(name, athlete);
        assertNotNull(result);
        assertEquals("updated", result.getBeschreibung());
    }

    @Test
    public void testGetRoutePositiv() {
        final String beschreibung = "testet ob route gespeichert wird";
        final String name = "id2";
        final IRoute route = CommonTransferFactory.createRoute(name, beschreibung, athlete);
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
        final IRoute route = CommonTransferFactory.createRoute(name, beschreibung, athlete);
        routeDao.saveOrUpdate(route);

        final IRoute result = routeDao.getRoute("id4", athlete);
        assertNull(result);
    }
}
