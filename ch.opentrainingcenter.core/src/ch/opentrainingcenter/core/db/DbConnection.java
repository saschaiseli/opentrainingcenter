package ch.opentrainingcenter.core.db;

public class DbConnection {
    private final String driver;
    private final String url;
    private final String username;
    private final String password;

    public DbConnection(final String driver, final String url, final String username, final String password) {
        this.driver = driver;
        this.url = url;
        this.username = username;
        this.password = password;
    }

}
