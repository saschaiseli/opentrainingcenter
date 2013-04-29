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

    public String getDriver() {
        return driver;
    }

    public String getUrl() {
        return url;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getDatabaseName() {
        final int lastSlash = url.lastIndexOf('/');
        return url.substring(lastSlash + 1, url.length());
    }

    @Override
    public String toString() {
        return "DbConnection [driver=" + driver + ", url=" + url + ", username=" + username + ", password=" + password + "]";
    }

}
