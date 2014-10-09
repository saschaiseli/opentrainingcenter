package ch.opentrainingcenter.core.cache;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;

import ch.opentrainingcenter.core.cache.AthleteCache;
import ch.opentrainingcenter.transfer.IAthlete;
import ch.opentrainingcenter.transfer.factory.CommonTransferFactory;

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
        final IAthlete athlete = createAthlete(name, 200);

        final List<IAthlete> models = new ArrayList<>();
        models.add(athlete);

        cache.addAll(models);

        assertEquals("Athlete gefunden...", athlete, cache.get(name));
    }

    private static IAthlete createAthlete(final String name, final Integer maxHeartBeat) {
        return CommonTransferFactory.createAthlete(name, DateTime.now().toDate(), maxHeartBeat);
    }
}
