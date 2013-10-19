package ch.opentrainingcenter.core.cache;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import ch.opentrainingcenter.transfer.CommonTransferFactory;
import ch.opentrainingcenter.transfer.IAthlete;
import ch.opentrainingcenter.transfer.IRoute;
import ch.opentrainingcenter.transfer.ITraining;
import ch.opentrainingcenter.transfer.IWeather;

@SuppressWarnings("nls")
public class TrainingCacheTest {

    private IAthlete athlete;

    @Before
    public void before() {
        athlete = CommonTransferFactory.createAthlete("name", 42);
    }

    @Test
    public void testInstance() {
        final TrainingCache cache = (TrainingCache) TrainingCache.getInstance();
        assertNotNull(cache);
    }

    @Test
    public void testGetKey() {
        final TrainingCache cache = (TrainingCache) TrainingCache.getInstance();
        final ITraining training = Mockito.mock(ITraining.class);
        Mockito.when(training.getDatum()).thenReturn(42L);
        final Long key = cache.getKey(training);
        assertTrue("Key muss das Datum sein.", 42 == key.longValue());
    }

    @Test
    public void testUpdateExtension() {
        final TrainingCache cache = (TrainingCache) TrainingCache.getInstance();
        cache.resetCache();

        final ITraining training = CommonTransferFactory.createTraining(1, 2, 3, 4, 5, 6, "blabla", null, null);
        training.setDatum(42);
        cache.add(training);

        final IWeather weather = CommonTransferFactory.createWeather(1);
        final IRoute route = CommonTransferFactory.createDefaultRoute(athlete);
        route.setName("name");
        route.setBeschreibung("beschreibung");

        cache.update(42L, "note", weather, route);

        final ITraining copy = cache.get(42L);
        assertEquals("note", copy.getNote());
        assertEquals(weather, copy.getWeather());
        assertEquals(route, copy.getRoute());
    }

    @Test
    public void testUpdateExtensionNull() {
        final TrainingCache cache = (TrainingCache) TrainingCache.getInstance();
        cache.resetCache();

        final ITraining training = Mockito.mock(ITraining.class);
        Mockito.when(training.getDatum()).thenReturn(42L);
        Mockito.when(training.getNote()).thenReturn(null);
        Mockito.when(training.getWeather()).thenReturn(null);
        Mockito.when(training.getRoute()).thenReturn(null);

        cache.add(training);

        final IWeather weather = CommonTransferFactory.createWeather(1);
        final IRoute route = CommonTransferFactory.createDefaultRoute(athlete);
        route.setName("name");
        route.setBeschreibung("beschreibung");

        cache.update(1L, "note", weather, route);

        final ITraining copy = cache.get(42L);
        assertNull(copy.getNote());
        assertNull(copy.getWeather());
        assertNull(copy.getRoute());
    }

    @Test
    public void testToString() {
        final TrainingCache cache = (TrainingCache) TrainingCache.getInstance();
        cache.resetCache();

        final ITraining training = Mockito.mock(ITraining.class);
        Mockito.when(training.getDatum()).thenReturn(42L);

        cache.add(training);

        assertEquals("Cache: Anzahl Elemente: 1", cache.toString());
    }

    @Test
    public void testReset() {
        final TrainingCache cache = (TrainingCache) TrainingCache.getInstance();
        cache.resetCache();

        final ITraining training = Mockito.mock(ITraining.class);
        Mockito.when(training.getDatum()).thenReturn(42L);

        cache.add(training);

        assertEquals("Ein element muss drin sein", 1, cache.size());
    }
}
