package ch.opentrainingcenter.client.cache;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import ch.opentrainingcenter.transfer.CommonTransferFactory;
import ch.opentrainingcenter.transfer.IAthlete;

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
        cache.add(athlete);

        assertEquals("Athlete gefunden...", athlete, cache.get(name));
    }
}
