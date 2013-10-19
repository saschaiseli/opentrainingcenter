package ch.opentrainingcenter.client.cache;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.mockito.Mockito;

import ch.opentrainingcenter.model.strecke.StreckeModel;
import ch.opentrainingcenter.transfer.IAthlete;

@SuppressWarnings("nls")
public class StreckeCacheTest {
    @Test
    public void testNichtsGefunden() {
        final StreckeCache cache = StreckeCache.getInstance();
        final IAthlete athlete = Mockito.mock(IAthlete.class);
        final StreckeModel model = new StreckeModel(42, athlete, "abc", "Beschreibung", 0);
        cache.add(model);

        assertEquals("Wenn nichts gefunden null zurück", null, cache.get("efg"));
    }

    @Test
    public void testGefunden() {
        final StreckeCache cache = StreckeCache.getInstance();
        final IAthlete athlete = Mockito.mock(IAthlete.class);
        final StreckeModel model = new StreckeModel(42, athlete, "abc", "Beschreibung", 0);
        cache.add(model);

        assertEquals("Strecke gefunden...", model, cache.get("abc"));
    }
}
