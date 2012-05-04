package ch.opentrainingcenter.client.action;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

import ch.opentrainingcenter.client.cache.MockDataAccess;
import ch.opentrainingcenter.client.model.RunType;

public class RunTypeActionContainerTest {
    private MockDataAccess databaseAccess;
    private MockCache cache;
    private RunTypeActionContainer container;

    @Before
    public void before() {
        databaseAccess = new MockDataAccess();
        cache = new MockCache();
        container = new RunTypeActionContainer(databaseAccess, cache);
    }

    @Test
    public void test() {
        final List<ChangeRunType> actions = container.getActions();
        assertNotNull(actions);
    }

    @Test
    public void testList() {
        final List<ChangeRunType> actions = container.getActions();
        assertEquals("6 Läufe sind als Action vorhanden", 6, actions.size());
    }

    @Test
    public void testEnable() {
        // execute
        container.update(RunType.INT_INTERVALL.getIndex());
        // assert
        final List<ChangeRunType> actions = container.getActions();
        for (final ChangeRunType changeRunType : actions) {
            final boolean enabled = changeRunType.isEnabled();
            if (RunType.INT_INTERVALL.equals(changeRunType.getType())) {
                assertFalse(enabled);
            } else {
                assertTrue(enabled);
            }
        }
    }
}
