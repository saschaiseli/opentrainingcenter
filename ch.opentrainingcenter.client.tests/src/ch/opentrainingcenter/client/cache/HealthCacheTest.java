package ch.opentrainingcenter.client.cache;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import ch.opentrainingcenter.model.navigation.ConcreteHealth;
import ch.opentrainingcenter.transfer.IAthlete;
import ch.opentrainingcenter.transfer.factory.CommonTransferFactory;

public class HealthCacheTest {

    private HealthCache cache;

    @Before
    public void setUp() {
        cache = HealthCache.getInstance();
        cache.resetCache();
    }

    @Test
    public void testGetKey() {
        final ConcreteHealth value = mock(ConcreteHealth.class);

        cache.getKey(value);

        verify(value).getId();
    }

    @SuppressWarnings("nls")
    @Test
    public void testToStringLeererCache() {
        final List<ConcreteHealth> values = new ArrayList<>();
        cache.addAll(values);

        final String result = cache.toString();

        assertEquals("", result);
    }

    @SuppressWarnings("nls")
    @Test
    public void testToString() {
        final List<ConcreteHealth> values = new ArrayList<>();
        final IAthlete athlete = mock(IAthlete.class);
        final Date dateofmeasure = new Date(100_000_000);
        final ConcreteHealth health = new ConcreteHealth(CommonTransferFactory.createHealth(athlete, 22d, 66, dateofmeasure), "");
        values.add(health);
        cache.addAll(values);

        final String result = cache.toString();

        assertNotNull(result);
        assertTrue(result.length() > 0);
    }

}
