package ch.opentrainingcenter.db.postgres;

import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import ch.opentrainingcenter.db.internal.WeatherDao;
import ch.opentrainingcenter.transfer.IWeather;

@SuppressWarnings("nls")
@Ignore
public class WeatherDaoTest extends PostgresDatabaseTestBase {

    private WeatherDao weatherDao;

    @Before
    public void setUp() {
        weatherDao = new WeatherDao(dao);
    }

    @Test
    public void testGetWeather() {
        final List<IWeather> result = weatherDao.getAllWeather();
        assertTrue("Einige Wetter sind vorhanden", 3 <= result.size());
    }

}
