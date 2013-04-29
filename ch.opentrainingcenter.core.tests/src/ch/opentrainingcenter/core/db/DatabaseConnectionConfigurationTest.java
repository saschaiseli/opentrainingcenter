package ch.opentrainingcenter.core.db;

import static org.junit.Assert.assertEquals;

import java.util.Properties;

import org.junit.Test;

import ch.opentrainingcenter.core.db.DatabaseConnectionConfiguration.DB_MODE;

public class DatabaseConnectionConfigurationTest {
    private DbConnection adminConnection;
    private DbConnection dbConnection;
    private DatabaseConnectionConfiguration config;

    @Test
    public void testAdminDriverConnectionNull() {
        dbConnection = new DbConnection("driver1", "url1", "username1", "password1");
        config = new DatabaseConnectionConfiguration(dbConnection, "dialect1");
        final String result = config.getDriver(DB_MODE.ADMIN);
        assertEquals("driver1", result);
    }

    @Test
    public void testAdminUrlConnectionNull() {
        dbConnection = new DbConnection("driver1", "url1", "username1", "password1");
        config = new DatabaseConnectionConfiguration(dbConnection, "dialect1");
        final String result = config.getUrl(DB_MODE.ADMIN);
        assertEquals("url1", result);
    }

    @Test
    public void testAdminUserConnectionNull() {
        dbConnection = new DbConnection("driver1", "url1", "username1", "password1");
        config = new DatabaseConnectionConfiguration(dbConnection, "dialect1");
        final String result = config.getUsername(DB_MODE.ADMIN);
        assertEquals("username1", result);
    }

    @Test
    public void testAdminPasswordConnectionNull() {
        dbConnection = new DbConnection("driver1", "url1", "username1", "password1");
        config = new DatabaseConnectionConfiguration(dbConnection, "dialect1");
        final String result = config.getPassword(DB_MODE.ADMIN);
        assertEquals("password1", result);
    }

    @Test
    public void testAdminDriverConnectionNichtNull() {
        dbConnection = new DbConnection("driver1", "url1", "username1", "password1");
        adminConnection = new DbConnection("driver2", "url2", "username2", "password2");
        config = new DatabaseConnectionConfiguration(dbConnection, "dialect1", adminConnection);
        final String result = config.getDriver(DB_MODE.ADMIN);
        assertEquals("driver2", result);
    }

    @Test
    public void testAdminUrlConnectionNichtNull() {
        dbConnection = new DbConnection("driver1", "url1", "username1", "password1");
        adminConnection = new DbConnection("driver2", "url2", "username2", "password2");
        config = new DatabaseConnectionConfiguration(dbConnection, "dialect1", adminConnection);
        final String result = config.getUrl(DB_MODE.ADMIN);
        assertEquals("url2", result);
    }

    @Test
    public void testAdminUserConnectionNichtNull() {
        dbConnection = new DbConnection("driver1", "url1", "username1", "password1");
        adminConnection = new DbConnection("driver2", "url2", "username2", "password2");
        config = new DatabaseConnectionConfiguration(dbConnection, "dialect1", adminConnection);
        final String result = config.getUsername(DB_MODE.ADMIN);
        assertEquals("username2", result);
    }

    @Test
    public void testAdminPasswordConnectionNichtNull() {
        dbConnection = new DbConnection("driver1", "url1", "username1", "password1");
        adminConnection = new DbConnection("driver2", "url2", "username2", "password2");
        config = new DatabaseConnectionConfiguration(dbConnection, "dialect1", adminConnection);
        final String result = config.getPassword(DB_MODE.ADMIN);
        assertEquals("password2", result);
    }

    @Test
    public void testDriverConnectionNichtNull() {
        dbConnection = new DbConnection("driver1", "url1", "username1", "password1");
        adminConnection = new DbConnection("driver2", "url2", "username2", "password2");
        config = new DatabaseConnectionConfiguration(dbConnection, "dialect1", adminConnection);
        final String result = config.getDriver(DB_MODE.APPLICATION);
        assertEquals("driver1", result);
    }

    @Test
    public void testUrlConnectionNichtNull() {
        dbConnection = new DbConnection("driver1", "url1", "username1", "password1");
        adminConnection = new DbConnection("driver2", "url2", "username2", "password2");
        config = new DatabaseConnectionConfiguration(dbConnection, "dialect1", adminConnection);
        final String result = config.getUrl(DB_MODE.APPLICATION);
        assertEquals("url1", result);
    }

    @Test
    public void testUserConnectionNichtNull() {
        dbConnection = new DbConnection("driver1", "url1", "username1", "password1");
        adminConnection = new DbConnection("driver2", "url2", "username2", "password2");
        config = new DatabaseConnectionConfiguration(dbConnection, "dialect1", adminConnection);
        final String result = config.getUsername(DB_MODE.APPLICATION);
        assertEquals("username1", result);
    }

    @Test
    public void testPasswordConnectionNichtNull() {
        dbConnection = new DbConnection("driver1", "url1", "username1", "password1");
        adminConnection = new DbConnection("driver2", "url2", "username2", "password2");
        config = new DatabaseConnectionConfiguration(dbConnection, "dialect1", adminConnection);
        final String result = config.getPassword(DB_MODE.APPLICATION);
        assertEquals("password1", result);
    }

    @Test
    public void testPasswordDbNameNichtNull() {
        dbConnection = new DbConnection("driver1", "url1", "username1", "password1");
        adminConnection = new DbConnection("driver2", "url2", "username2", "password2");
        config = new DatabaseConnectionConfiguration(dbConnection, "dialect1", adminConnection);
        final String result = config.getDatabaseName(DB_MODE.APPLICATION);
        assertEquals("url1", result);
    }

    @Test
    public void testDialect() {
        dbConnection = new DbConnection("driver1", "url1", "username1", "password1");
        adminConnection = new DbConnection("driver2", "url2", "username2", "password2");
        config = new DatabaseConnectionConfiguration(dbConnection, "dialect1", adminConnection);
        final String result = config.getDialect();
        assertEquals("dialect1", result);
    }

    @Test
    public void testProperties() {
        dbConnection = new DbConnection("driver1", "url1", "username1", "password1");
        adminConnection = new DbConnection("driver2", "url2", "username2", "password2");
        config = new DatabaseConnectionConfiguration(dbConnection, "dialect1", adminConnection);
        final Properties properties = config.getProperties();

        assertEquals("driver1", properties.getProperty("hibernate.connection.driver_class"));
        assertEquals("url1", properties.getProperty("hibernate.connection.url"));
        assertEquals("username1", properties.getProperty("hibernate.connection.username"));
        assertEquals("password1", properties.getProperty("hibernate.connection.password"));
        assertEquals("dialect1", properties.getProperty("hibernate.dialect"));
    }
}
