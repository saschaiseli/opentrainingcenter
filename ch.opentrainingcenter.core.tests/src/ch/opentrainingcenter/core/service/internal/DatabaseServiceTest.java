package ch.opentrainingcenter.core.service.internal;

import org.junit.Ignore;
import org.junit.Test;

import ch.opentrainingcenter.core.db.IDatabaseAccess;
import ch.opentrainingcenter.core.db.IDatabaseConnection;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

@SuppressWarnings("nls")
public class DatabaseServiceTest {

    private DatabaseService service;

    @Test
    public void testGetInstance() {
        service = DatabaseService.getInstance();
        assertNotNull(service);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInitOhneAdminDBExisitertNicht() {
        service = DatabaseService.getInstance();
        service.init("dbName", "url", "user", "pw", null, null, null);
    }

    @Test
    @Ignore
    public void testInitOhneAdminDBExisitert() {
        service = DatabaseService.getInstance();
        service.init("H2 Database", "jdbc:h2:file:~/.otc/abc", "sa", "", null, null, null);
        final IDatabaseAccess access = service.getDatabaseAccess();
        assertNotNull(access);

        final IDatabaseConnection connection = service.getDatabaseConnection();
        assertNotNull(connection);

        assertNull(connection.getAdminConnection());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetDatabaseAccessNochNichtInitialisert() {
        service = DatabaseService.getInstance();
        service.getDatabaseAccess();
    }

    @Test
    @Ignore
    public void testGetAvailableConnections() {
        service = DatabaseService.getInstance();
        assertNotNull(service.getAvailableConnections());
        assertTrue("Mindestens eine connection muss da sein", service.getAvailableConnections().size() >= 1);
    }

}
