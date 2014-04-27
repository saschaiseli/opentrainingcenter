package ch.opentrainingcenter.client.cache;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import ch.opentrainingcenter.transfer.IAthlete;
import ch.opentrainingcenter.transfer.factory.CommonTransferFactory;
import static org.junit.Assert.assertEquals;

@SuppressWarnings("nls")
public class AthleteCacheTest {

    private final AthleteCache cache = AthleteCache.getInstance();

    @Before
    public void setUp() {
        cache.resetCache();
    }

    @Test
    public void testNichtsGefunden() {

        assertEquals("Wenn nichts gefunden null zurück", null, cache.get("junit"));
    }

    @Test
    public void testGefunden() {
        final String name = "junit";
        final IAthlete athlete = CommonTransferFactory.createAthlete(name, 200);

        final List<IAthlete> models = new ArrayList<>();
        models.add(athlete);

        cache.addAll(models);

        assertEquals("Athlete gefunden...", athlete, cache.get(name));
    }
}
