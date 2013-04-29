package ch.opentrainingcenter.core.db;

import java.util.Properties;

/**
 * Class for Connection configuration. One for Administration of Database and
 * one for the OpenTrainingCenter
 * 
 * <pre>
 * <property name="hibernate.connection.driver_class">org.h2.Driver</property>
 * <property name="hibernate.connection.url">jdbc:h2:file:~/.otc_dev/otc</property>
 * <property name="hibernate.connection.username">sa</property>
 * <property name="hibernate.dialect">org.hibernate.dialect.H2Dialect</property>
 * </pre>
 */
public class DatabaseConnectionConfiguration {

    public enum DB_MODE {
        /**
         * Um Datenbank einzurichten.
         */
        ADMIN,
        /**
         * Um Datenbank in der Applikation zu verwenden.
         */
        APPLICATION
    }

    private final String dialect;
    private final DbConnection adminConnection;
    private final DbConnection dbConnection;

    /**
     * @param dbConnection
     *            Database Connection for the Application
     * @param dialect
     *            HibernateDialect für die Connection
     */
    public DatabaseConnectionConfiguration(final DbConnection dbConnection, final String dialect) {
        this(dbConnection, dialect, null);
    }

    /**
     * @param dbConnection
     *            Database Connection for the Application
     * @param dialect
     *            HibernateDialect
     * @param adminConnection
     *            Admin Datenbank Connection um User, Schema und DB zu
     *            erstellen. Kann null sein. In diesem Fall werden zum
     *            Einrichten der Datenbank die ersten Parameter verwendet.
     */
    public DatabaseConnectionConfiguration(final DbConnection dbConnection, final String dialect, final DbConnection adminConnection) {
        this.adminConnection = adminConnection;
        this.dbConnection = dbConnection;
        this.dialect = dialect;
    }

    /**
     * Properties für
     * 
     * <pre>
     * <property name="hibernate.connection.driver_class">org.h2.Driver</property>
     * <property name="hibernate.connection.url">jdbc:h2:file:~/.otc_dev/otc</property>
     * <property name="hibernate.connection.username">sa</property>
     * <property name="hibernate.dialect">org.hibernate.dialect.H2Dialect</property>
     * </pre>
     */
    public Properties getProperties() {
        final Properties props = new Properties();
        props.put("hibernate.connection.driver_class", dbConnection.getDriver());
        props.put("hibernate.connection.url", dbConnection.getUrl());
        props.put("hibernate.connection.username", dbConnection.getUsername());
        props.put("hibernate.connection.password", dbConnection.getPassword());
        props.put("hibernate.dialect", dialect);
        return props;
    }

    public String getUrl(final DB_MODE mode) {
        if (adminConnection == null) {
            return dbConnection.getUrl();
        } else if (DB_MODE.ADMIN.equals(mode)) {
            return adminConnection.getUrl();
        } else {
            return dbConnection.getUrl();
        }
    }

    public String getDriver(final DB_MODE mode) {
        if (adminConnection == null) {
            return dbConnection.getDriver();
        } else if (DB_MODE.ADMIN.equals(mode)) {
            return adminConnection.getDriver();
        } else {
            return dbConnection.getDriver();
        }
    }

    public String getUsername(final DB_MODE mode) {
        if (adminConnection == null) {
            return dbConnection.getUsername();
        } else if (DB_MODE.ADMIN.equals(mode)) {
            return adminConnection.getUsername();
        } else {
            return dbConnection.getUsername();
        }
    }

    public String getPassword(final DB_MODE mode) {
        if (adminConnection == null) {
            return dbConnection.getPassword();
        } else if (DB_MODE.ADMIN.equals(mode)) {
            return adminConnection.getPassword();
        } else {
            return dbConnection.getPassword();
        }
    }

    public String getDialect() {
        return dialect;
    }

    public String getDatabaseName(final DB_MODE mode) {
        if (adminConnection == null) {
            return dbConnection.getDatabaseName();
        } else if (DB_MODE.ADMIN.equals(mode)) {
            return adminConnection.getDatabaseName();
        } else {
            return dbConnection.getDatabaseName();
        }
    }
}
