package ch.opentrainingcenter.db.internal;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.util.Date;

import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;

import ch.opentrainingcenter.database.dao.AthleteDao;
import ch.opentrainingcenter.database.dao.HealthDao;
import ch.opentrainingcenter.transfer.IAthlete;
import ch.opentrainingcenter.transfer.IHealth;
import ch.opentrainingcenter.transfer.factory.CommonTransferFactory;

@SuppressWarnings("nls")
public class AthleteDaoTest extends DatabaseTestBase {

    private AthleteDao athleteDao;
    private Date now;
    private HealthDao healthDao;

    @Before
    public void setUp() {
        athleteDao = new AthleteDao(connectionConfig);
        healthDao = new HealthDao(connectionConfig);
        now = DateTime.now().toDate();
    }

    @Test
    public void testSave() {

        final IAthlete athlete = createAthlete("junit", 300);
        final int id = athleteDao.save(athlete);

        final IAthlete result = athleteDao.getAthlete(id);
        assertEquals(athlete.getId(), result.getId());
    }

    @Test
    public void testUpdatePuls() {

        final IAthlete athlete = createAthlete("junit", 300);

        athleteDao.save(athlete);

        athlete.setMaxHeartRate(42);

        athleteDao.save(athlete);

        assertEquals(42, athleteDao.getAthlete("junit").getMaxHeartRate().intValue());
    }

    @Test
    public void testGetMitHealth() {
        final IAthlete athlete = createAthlete("junit", 300);
        final IHealth h = CommonTransferFactory.createHealth(athlete, 12.0, 13, now);

        athleteDao.save(athlete);
        healthDao.saveOrUpdate(h);

        final IHealth result = healthDao.getHealth(athlete, now);
        assertEquals(h, result);
        assertEquals(athlete, result.getAthlete());
    }

    @Test
    public void testGetByNameNotFound() {
        final IAthlete athlete = athleteDao.getAthlete("1q1q1q1q1q1q1");
        assertNull("Athlete nicht gefunden", athlete);
    }

    private static IAthlete createAthlete(final String name, final Integer maxHeartBeat) {
        return CommonTransferFactory.createAthlete(name, DateTime.now().toDate(), maxHeartBeat);
    }
}
