package ch.opentrainingcenter.client.cache;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.mockito.Mockito;

import ch.opentrainingcenter.model.strecke.StreckeModel;
import ch.opentrainingcenter.transfer.IAthlete;
import static org.junit.Assert.assertEquals;

@SuppressWarnings("nls")
public class StreckeCacheTest {
    @Test
    public void testNichtsGefunden() {
        final StreckeCache cache = StreckeCache.getInstance();
        final IAthlete athlete = Mockito.mock(IAthlete.class);
        final StreckeModel model = new StreckeModel(42, athlete, "abc", "Beschreibung", 0);

        final List<StreckeModel> models = new ArrayList<>();
        models.add(model);

        cache.addAll(models);

        assertEquals("Wenn nichts gefunden null zurück", null, cache.get("efg"));
    }

    @Test
    public void testGefunden() {
        final StreckeCache cache = StreckeCache.getInstance();
        final IAthlete athlete = Mockito.mock(IAthlete.class);
        final StreckeModel model = new StreckeModel(42, athlete, "abc", "Beschreibung", 0);
        final List<StreckeModel> models = new ArrayList<>();
        models.add(model);

        cache.addAll(models);

        assertEquals("Strecke gefunden...", model, cache.get("abc"));
    }
}
