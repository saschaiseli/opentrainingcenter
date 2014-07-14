package ch.opentrainingcenter.core.service.internal;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import ch.opentrainingcenter.core.db.DatabaseConnectionConfiguration;
import ch.opentrainingcenter.core.db.IDatabaseConnection;

@SuppressWarnings("nls")
public class DatabaseServiceTest {

    private DatabaseService service;
    private final String dbName = "dbName";

    @Before
    public void setUp() {
        service = null;

    }

    @Test
    public void testGetInstance() {
        service = DatabaseService.getInstance();
        assertNotNull(service);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInitOhneAdminDBExisitertNicht() {
        service = DatabaseService.getInstance();
        service.init(dbName, "url", "user", "pw", null, null, null);
    }

    @Test(expected = IllegalArgumentException.class)
    @Ignore
    public void testGetDatabaseAccessNochNichtInitialisert() {
        service = DatabaseService.getInstance();
        service.getDatabaseAccess();
    }

    @Test
    public void testInit() {
        service = DatabaseService.getInstance();
        final IDatabaseConnection databaseConnection = mock(IDatabaseConnection.class);
        when(databaseConnection.getDriver()).thenReturn("junit_driver");
        when(databaseConnection.getDialect()).thenReturn("junit_dialect");

        service.getAvailableConnections().put(dbName, databaseConnection);

        service.init(dbName, "url", "sa", "", null, null, null);

        verify(databaseConnection).setConfig(any(DatabaseConnectionConfiguration.class));
        verify(databaseConnection).init();
    }

    @Test
    public void testInitAdmin() {
        service = DatabaseService.getInstance();
        final IDatabaseConnection databaseConnection = mock(IDatabaseConnection.class);
        when(databaseConnection.getDriver()).thenReturn("junit_driver");
        when(databaseConnection.getDialect()).thenReturn("junit_dialect");
        service.getAvailableConnections().put(dbName, databaseConnection);

        service.init(dbName, "url", "sa", "", "urlAdmin", "adminUser", "");

        verify(databaseConnection).setConfig(any(DatabaseConnectionConfiguration.class));
        verify(databaseConnection).init();
    }
}
