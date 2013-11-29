package ch.opentrainingcenter.core.db;

/**
 * Verbindungsconfig zu einer Datenbank.
 */
public class DbConnection {
    private final String driver;
    private final String dialect;

    private String url;
    private String username;
    private String password;

    public DbConnection(final String driver, final String dialect) {
        this(driver, dialect, null, null, null);
    }

    public DbConnection(final String driver, final String dialect, final String url, final String username, final String password) {
        this.driver = driver;
        this.dialect = dialect;
        this.url = url;
        this.username = username;
        this.password = password;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(final String url) {
        this.url = url;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(final String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(final String password) {
        this.password = password;
    }

    public String getDriver() {
        return driver;
    }

    public String getDialect() {
        return dialect;
    }

    /**
     * @return den namen der Datenbank aus der url extrahiert
     */
    public String getDatabaseName() {
        final int lastSlash = url.lastIndexOf('/');
        return url.substring(lastSlash + 1, url.length());
    }

    @SuppressWarnings("nls")
    @Override
    public String toString() {
        return "DbConnection [driver=" + driver + ", dialect=" + dialect + ", url=" + url + ", username=" + username + ", password=" + password + "]";
    }
}
