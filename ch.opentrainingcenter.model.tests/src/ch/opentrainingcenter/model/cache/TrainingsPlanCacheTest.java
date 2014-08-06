package ch.opentrainingcenter.model.cache;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import ch.opentrainingcenter.model.ModelFactory;
import ch.opentrainingcenter.model.planing.IPlanungModel;
import ch.opentrainingcenter.model.planing.KwJahrKey;
import ch.opentrainingcenter.transfer.IAthlete;

public class TrainingsPlanCacheTest {

    private TrainingsPlanCache cache;

    @Before
    public void setUp() {
        cache = TrainingsPlanCache.getInstance();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetKeyNull() {
        cache.getKey(null);
    }

    @Test
    public void testGetKey() {
        final IAthlete athlete = null;
        final IPlanungModel value = ModelFactory.createPlanungModel(athlete, 2010, 51, 22);

        final KwJahrKey result = cache.getKey(value);

        assertEquals(new KwJahrKey(2010, 51), result);
    }
}
