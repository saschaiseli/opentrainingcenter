package ch.opentrainingcenter.db.internal;

import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

import ch.opentrainingcenter.transfer.IWeather;

@SuppressWarnings("nls")
public class WeatherDaoTest extends DatabaseTestBase {

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
