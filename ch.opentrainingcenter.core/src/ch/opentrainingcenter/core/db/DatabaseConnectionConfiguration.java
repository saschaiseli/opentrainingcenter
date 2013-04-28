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
    private final String driver;
    private final String url;
    private final String username;
    private final String password;
    private final String dialect;

    public DatabaseConnectionConfiguration(final String driver, final String url, final String username, final String password, final String dialect) {
        this.driver = driver;
        this.url = url;
        this.username = username;
        this.password = password;
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
        props.put("hibernate.connection.driver_class", driver);
        props.put("hibernate.connection.url", url);
        props.put("hibernate.connection.username", username);
        props.put("hibernate.connection.password", password);
        props.put("hibernate.dialect", dialect);
        return props;
    }

    public String getUrl() {
        return url;
    }
}
