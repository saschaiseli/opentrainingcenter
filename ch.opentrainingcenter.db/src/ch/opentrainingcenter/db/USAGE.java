package ch.opentrainingcenter.db;

public enum USAGE {
    /**
     * Use a production db configuration
     */
    PRODUCTION("otc"),
    /**
     * use the otc_dev database
     */
    DEVELOPING("otc_dev"),
    /**
     * the content of the database will be deleted after each test.
     */
    TEST("otc_junit");

    private final String dbName;

    private USAGE(final String name) {
        dbName = name;
    }

    public String getDbName() {
        return dbName;
    }
}
