package ch.opentrainingcenter.db.h2.internal;

import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

import ch.opentrainingcenter.transfer.IWeather;

@SuppressWarnings("nls")
public class WeatherDaoTest extends DatabaseTestBase {

    private WeatherDao weatherDao;

    @Override
    @Before
    public void setUp() {
        super.setUp();
        weatherDao = new WeatherDao(dao);
    }

    @Test
    public void testGetWeather() {
        final List<IWeather> result = weatherDao.getAllWeather();
        assertTrue("Einige Wetter sind vorhanden", 3 <= result.size());
    }

}
