package ch.opentrainingcenter.db.postgres;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import ch.opentrainingcenter.database.dao.AthleteDao;
import ch.opentrainingcenter.database.dao.PlanungDao;
import ch.opentrainingcenter.transfer.CommonTransferFactory;
import ch.opentrainingcenter.transfer.IAthlete;
import ch.opentrainingcenter.transfer.IPlanungWoche;

@SuppressWarnings("nls")
public class PlanungDaoTest extends PostgresDatabaseTestBase {

    private PlanungDao planungDao;
    private IAthlete athlete;
    private IAthlete athleteOther;

    @Before
    public void setUp() {
        planungDao = new PlanungDao(connectionConfig);
        athlete = CommonTransferFactory.createAthlete("junit", 220);
        athleteOther = CommonTransferFactory.createAthlete("junit2", 2203);

        final AthleteDao athleteDao = new AthleteDao(connectionConfig);
        athleteDao.save(athlete);
        athleteDao.save(athleteOther);
    }

    @Test
    public void testGetByAthletePositiv() {
        final List<IPlanungWoche> planungen = new ArrayList<IPlanungWoche>();
        planungen.add(CommonTransferFactory.createIPlanungWoche(athlete, 2013, 1, 40, true, 29));
        planungen.add(CommonTransferFactory.createIPlanungWoche(athlete, 2014, 2, 41, false, 30));
        planungen.add(CommonTransferFactory.createIPlanungWoche(athlete, 2015, 3, 42, true, 31));
        planungen.add(CommonTransferFactory.createIPlanungWoche(athlete, 2016, 4, 43, false, 32));
        planungDao.saveOrUpdate(planungen);

        final List<IPlanungWoche> result = planungDao.getPlanungsWoche(athlete);
        assertEquals(4, result.size());
        assertPlanung(result.get(0), 2013, 1, 40, true, 29);
        assertPlanung(result.get(1), 2014, 2, 41, false, 30);
        assertPlanung(result.get(2), 2015, 3, 42, true, 31);
        assertPlanung(result.get(3), 2016, 4, 43, false, 32);
    }

    @Test
    public void testUpdate() {
        List<IPlanungWoche> planungen = new ArrayList<IPlanungWoche>();
        planungen.add(CommonTransferFactory.createIPlanungWoche(athlete, 2013, 1, 40, true, 29));
        planungen.add(CommonTransferFactory.createIPlanungWoche(athlete, 2014, 2, 41, false, 30));
        planungen.add(CommonTransferFactory.createIPlanungWoche(athlete, 2015, 3, 42, true, 31));
        planungen.add(CommonTransferFactory.createIPlanungWoche(athlete, 2016, 4, 43, false, 32));
        planungDao.saveOrUpdate(planungen);

        List<IPlanungWoche> result = planungDao.getPlanungsWoche(athlete);
        assertEquals(4, result.size());
        assertPlanung(result.get(0), 2013, 1, 40, true, 29);
        assertPlanung(result.get(1), 2014, 2, 41, false, 30);
        assertPlanung(result.get(2), 2015, 3, 42, true, 31);
        assertPlanung(result.get(3), 2016, 4, 43, false, 32);

        // update the vals
        planungen = new ArrayList<IPlanungWoche>();
        planungen.add(CommonTransferFactory.createIPlanungWoche(athlete, 2013, 1, 140, false, 429));
        planungen.add(CommonTransferFactory.createIPlanungWoche(athlete, 2014, 2, 141, true, 430));
        planungen.add(CommonTransferFactory.createIPlanungWoche(athlete, 2015, 3, 142, false, 431));
        planungen.add(CommonTransferFactory.createIPlanungWoche(athlete, 2016, 4, 143, true, 432));
        planungDao.saveOrUpdate(planungen);

        result = planungDao.getPlanungsWoche(athlete);
        assertEquals(4, result.size());
        assertPlanung(result.get(0), 2013, 1, 140, false, 429);
        assertPlanung(result.get(1), 2014, 2, 141, true, 430);
        assertPlanung(result.get(2), 2015, 3, 142, false, 431);
        assertPlanung(result.get(3), 2016, 4, 143, true, 432);
    }

    @Test
    public void testGetByAthleteNegativ() {
        final List<IPlanungWoche> planungen = new ArrayList<IPlanungWoche>();
        planungen.add(CommonTransferFactory.createIPlanungWoche(athlete, 2013, 1, 40, true, 29));
        planungen.add(CommonTransferFactory.createIPlanungWoche(athlete, 2014, 2, 41, false, 30));
        planungen.add(CommonTransferFactory.createIPlanungWoche(athlete, 2015, 3, 42, true, 31));
        planungen.add(CommonTransferFactory.createIPlanungWoche(athlete, 2016, 4, 43, false, 32));

        planungDao.saveOrUpdate(planungen);

        final List<IPlanungWoche> result = planungDao.getPlanungsWoche(athleteOther);
        assertEquals(0, result.size());
    }

    @Test
    public void testGetByAthleteAndKw() {
        final List<IPlanungWoche> planungen = new ArrayList<IPlanungWoche>();
        planungen.add(CommonTransferFactory.createIPlanungWoche(athlete, 2013, 1, 40, true, 29));
        planungen.add(CommonTransferFactory.createIPlanungWoche(athlete, 2014, 2, 41, false, 30));
        planungen.add(CommonTransferFactory.createIPlanungWoche(athlete, 2015, 3, 42, true, 31));
        planungen.add(CommonTransferFactory.createIPlanungWoche(athlete, 2016, 4, 43, false, 32));

        planungDao.saveOrUpdate(planungen);

        final List<IPlanungWoche> result = planungDao.getPlanungsWoche(athlete, 2015, 3);
        assertEquals(2, result.size());

        assertPlanung(result.get(0), 2015, 3, 42, true, 31);
        assertPlanung(result.get(1), 2016, 4, 43, false, 32);
    }

    private void assertPlanung(final IPlanungWoche planung, final int jahr, final int kw, final int km, final boolean inter, final int lang) {
        assertEquals("Jahr testen: ", jahr, planung.getJahr());
        assertEquals("kw testen: ", kw, planung.getKw());
        assertEquals("km/woche testen: ", km, planung.getKmProWoche());
        assertEquals("intervall testen: ", inter, planung.isInterval());
        assertEquals("langerlauf testen: ", lang, planung.getLangerLauf());
    }
}
