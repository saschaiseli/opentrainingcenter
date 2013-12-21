package ch.opentrainingcenter.client.views;

import org.junit.Test;

import ch.opentrainingcenter.core.db.DatabaseConnectionState;
import ch.opentrainingcenter.transfer.IAthlete;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import static org.mockito.Mockito.mock;

@SuppressWarnings("nls")
public class ApplicationContextTest {

    @Test
    public void testGetApplicationContext() {
        final ApplicationContext context = ApplicationContext.getApplicationContext();

        assertNotNull(context);
    }

    @Test
    public void testGetSelectedId() {
        final ApplicationContext context = ApplicationContext.getApplicationContext();

        context.setSelectedId(42L);

        assertEquals(42, context.getSelectedId().longValue());
    }

    @Test
    public void testGetAthlete() {
        final ApplicationContext context = ApplicationContext.getApplicationContext();
        final IAthlete athlete = mock(IAthlete.class);
        context.setAthlete(athlete);

        assertEquals(athlete, context.getAthlete());
    }

    @Test
    public void testSetSelection() {
        final ApplicationContext context = ApplicationContext.getApplicationContext();
        context.setSelection(new Object[] { "abc" });

        assertEquals("abc", context.getSelection().get(0));
    }

    @Test
    public void testGetSelectedJahr() {
        final ApplicationContext context = ApplicationContext.getApplicationContext();
        context.setSelectedJahr(42);

        assertEquals(42, context.getSelectedJahr());
    }

    @Test
    public void testGetDbState() {
        final ApplicationContext context = ApplicationContext.getApplicationContext();
        final DatabaseConnectionState dbState = mock(DatabaseConnectionState.class);
        context.setDbState(dbState);

        assertEquals(dbState, context.getDbState());
    }
}
