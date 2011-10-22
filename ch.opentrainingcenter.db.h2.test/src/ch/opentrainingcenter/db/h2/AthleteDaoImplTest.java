package ch.opentrainingcenter.db.h2;

import static org.junit.Assert.assertNotNull;

import org.junit.Test;

import ch.iseli.sportanalyzer.db.IAthleteDao;
import ch.opentrainingcenter.transfer.IAthlete;

public class AthleteDaoImplTest {
    private IAthleteDao athleteDaoImpl;

    @Test
    public void testDao() {
        athleteDaoImpl = new AthleteDaoImpl();
        IAthlete athleteByName = athleteDaoImpl.getAthleteByName("sascha");
        assertNotNull(athleteByName);
    }
}
