package ch.opentrainingcenter.core.db;

import org.junit.Test;
import org.mockito.Mockito;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

@SuppressWarnings("nls")
public class DatabaseHelperTest {
    private IDatabaseAccess databaseAccess;

    @Test
    public void testDbNichtDa() {
        databaseAccess = Mockito.mock(IDatabaseAccess.class);
        final Throwable cause = new Throwable("Table \"ATHLETE\" not found; SQL statement:");
        final RuntimeException exception = new RuntimeException(cause);

        Mockito.when(databaseAccess.getAthlete(1)).thenThrow(exception);
        assertFalse("Datenbank reklamiert, dass sie Tabelle nicht gefunden hat", DatabaseHelper.isDatabaseExisting(databaseAccess));
    }

    @Test
    public void testDbNichtDaAndereException() {
        databaseAccess = Mockito.mock(IDatabaseAccess.class);
        final Throwable cause = new Throwable("Blabla");
        final RuntimeException exception = new RuntimeException(cause);

        Mockito.when(databaseAccess.getAthlete(1)).thenThrow(exception);
        assertTrue("Keine SQL Exception, also ist die Datenbank da", DatabaseHelper.isDatabaseExisting(databaseAccess));
    }

    @Test
    public void testDbException() {
        databaseAccess = Mockito.mock(IDatabaseAccess.class);
        final Throwable cause = new Throwable();
        final RuntimeException exception = new RuntimeException(cause);

        Mockito.when(databaseAccess.getAthlete(1)).thenThrow(exception);
        assertTrue("Keine SQL Exception, also ist die Datenbank da", DatabaseHelper.isDatabaseExisting(databaseAccess));
    }

    @Test
    public void testDbDa() {
        databaseAccess = Mockito.mock(IDatabaseAccess.class);
        Mockito.when(databaseAccess.getAthlete(1)).thenReturn(null);
        assertTrue("Keine Exception, also ist die Datenbank da", DatabaseHelper.isDatabaseExisting(databaseAccess));
    }

    @Test
    public void testDbLocked() {
        databaseAccess = Mockito.mock(IDatabaseAccess.class);
        final Throwable cause = new Throwable("Locked by another process");
        final RuntimeException exception = new RuntimeException(cause);

        Mockito.when(databaseAccess.getAthlete(1)).thenThrow(exception);
        assertTrue("Datenbank ist gelockt", DatabaseHelper.isDatabaseLocked(databaseAccess));
    }

    @Test
    public void testDbLockedAndereException() {
        databaseAccess = Mockito.mock(IDatabaseAccess.class);
        final Throwable cause = new Throwable("Blabla");
        final RuntimeException exception = new RuntimeException(cause);

        Mockito.when(databaseAccess.getAthlete(1)).thenThrow(exception);
        assertFalse("Keine Lock Exception, also ist die Datenbank auch nicht gelockt", DatabaseHelper.isDatabaseLocked(databaseAccess));
    }

    @Test
    public void testNichtGelockt() {
        databaseAccess = Mockito.mock(IDatabaseAccess.class);
        final Throwable cause = new Throwable();
        final RuntimeException exception = new RuntimeException(cause);

        Mockito.when(databaseAccess.getAthlete(1)).thenThrow(exception);
        assertFalse("Keine Lock Exception,  also ist die Datenbank auch nicht gelockt", DatabaseHelper.isDatabaseLocked(databaseAccess));
    }

    @Test
    public void testNichtGelockt2() {
        databaseAccess = Mockito.mock(IDatabaseAccess.class);
        Mockito.when(databaseAccess.getAthlete(1)).thenReturn(null);
        assertFalse("Keine Lock Exception,  also ist die Datenbank auch nicht gelockt", DatabaseHelper.isDatabaseLocked(databaseAccess));
    }
}
