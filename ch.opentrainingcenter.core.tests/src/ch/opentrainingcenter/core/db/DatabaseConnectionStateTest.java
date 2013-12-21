package ch.opentrainingcenter.core.db;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@SuppressWarnings("nls")
public class DatabaseConnectionStateTest {

    @Test
    public void testCreateNewOkState() {
        final DatabaseConnectionState result = DatabaseConnectionState.createNewOkState();
        assertEquals(DBSTATE.OK, result.getState());
    }

    @Test
    public void testCreateProblemState() {
        final DatabaseConnectionState result = DatabaseConnectionState.createProblemState("Junit");
        assertEquals(DBSTATE.PROBLEM, result.getState());
        assertEquals("Junit", result.getMessage());
    }

    @Test
    public void testCreateState_ConfigProblem() {
        final DatabaseConnectionState result = DatabaseConnectionState.createState("Junit", DBSTATE.CONFIG_PROBLEM);
        assertEquals(DBSTATE.CONFIG_PROBLEM, result.getState());
        assertEquals("Junit", result.getMessage());
    }

    @Test
    public void testCreateState_CREATE_DB() {
        final DatabaseConnectionState result = DatabaseConnectionState.createState("Junit", DBSTATE.CREATE_DB);
        assertEquals(DBSTATE.CREATE_DB, result.getState());
        assertEquals("Junit", result.getMessage());
    }

    @Test
    public void testCreateState_LOCKED() {
        final DatabaseConnectionState result = DatabaseConnectionState.createState("Junit", DBSTATE.LOCKED);
        assertEquals(DBSTATE.LOCKED, result.getState());
        assertEquals("Junit", result.getMessage());
    }

    @Test
    public void testToString() {
        final DatabaseConnectionState result = DatabaseConnectionState.createState("Junit", DBSTATE.CREATE_DB);

        assertTrue(result.toString().startsWith("DatabaseConnectionState ["));
    }

}
