package ch.opentrainingcenter.db.postgres;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

import ch.opentrainingcenter.db.internal.AthleteDao;
import ch.opentrainingcenter.db.internal.RouteDao;
import ch.opentrainingcenter.transfer.CommonTransferFactory;
import ch.opentrainingcenter.transfer.IAthlete;
import ch.opentrainingcenter.transfer.IRoute;

@SuppressWarnings("nls")
public class RouteDaoTest extends PostgresDatabaseTestBase {

    private RouteDao routeDao;
    private IAthlete athlete, athlete2;

    @Before
    public void setUp() {
        routeDao = new RouteDao(dao);
        final AthleteDao athleteDao = new AthleteDao(dao);

        athlete = CommonTransferFactory.createAthlete("junit", 220);
        athlete2 = CommonTransferFactory.createAthlete("junit2", 220);

        athleteDao.save(athlete);
        athleteDao.save(athlete2);
    }

    @Test
    public void testSaveRoute() {
        final String beschreibung = "testet ob route gespeichert wird";
        final String name = "testSaveRoute";
        final IRoute route = CommonTransferFactory.createRoute(name, beschreibung, athlete);
        final int id1 = routeDao.saveOrUpdate(route);
        assertTrue(0 <= id1);
        final int id2 = routeDao.saveOrUpdate(CommonTransferFactory.createRoute(name + 1, beschreibung + 1, athlete));
        assertTrue(id2 > id1);
        final List<IRoute> routen = routeDao.getRoute(athlete);
        assertNotNull(routen);
        assertEquals(name, routen.get(0).getName());
        assertEquals(beschreibung, routen.get(0).getBeschreibung());
    }

    @Test
    public void testSaveRouteMehrereAthleten() {
        final String beschreibung = "testet ob route gespeichert wird";
        final String name = "testSaveRoute";
        final IRoute route = CommonTransferFactory.createRoute(name, beschreibung, athlete);
        final int id1 = routeDao.saveOrUpdate(route);
        assertTrue(0 <= id1);
        final int id2 = routeDao.saveOrUpdate(CommonTransferFactory.createRoute(name + 1, beschreibung + 1, athlete2));
        assertTrue(id2 > id1);
        final List<IRoute> routen = routeDao.getRoute(athlete);
        assertNotNull(routen);
        assertEquals(1, routen.size());
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
