package ch.opentrainingcenter.client.cache;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import ch.opentrainingcenter.transfer.CommonTransferFactory;
import ch.opentrainingcenter.transfer.IAthlete;

@SuppressWarnings("nls")
public class AthleteCacheTest {
    @Test
    public void testNichtsGefunden() {
        final AthleteCache cache = AthleteCache.getInstance();

        assertEquals("Wenn nichts gefunden null zurück", null, cache.get("junit"));
    }

    @Test
    public void testGefunden() {
        final AthleteCache cache = AthleteCache.getInstance();
        final String name = "junit";
        final IAthlete athlete = CommonTransferFactory.createAthlete(name, 200);
        cache.add(athlete);

        assertEquals("Athlete gefunden...", athlete, cache.get(name));
    }
}
