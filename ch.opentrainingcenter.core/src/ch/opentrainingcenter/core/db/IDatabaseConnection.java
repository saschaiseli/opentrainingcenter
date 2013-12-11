package ch.opentrainingcenter.core.db;

import java.io.File;

import org.eclipse.core.runtime.IExecutableExtensionFactory;

/**
 * Datenbank Verbindungsdetails / Configuration und Administration
 */
public interface IDatabaseConnection extends IExecutableExtensionFactory {

    String EXTENSION_POINT_NAME = "classImportedDao"; //$NON-NLS-1$

    /**
     * @return {@link DbConnection} um Applikation mit der Datenbank zu
     *         connecten.
     */
    DbConnection getDbConnection();

    /**
     * @return admin {@link DbConnection} oder null, wenn keine benötigt wird.
     */
    DbConnection getAdminConnection();

    /**
     * @return true, wenn zum erstellen der datenbank eine admin connection
     *         gebraucht wird.
     */
    boolean isUsingAdminDbConnection();

    /**
     * Validiert die Datenbankverbindung mit den angegebenen Parameter und gibt
     * entsprechend {@link DBSTATE} Auskunft darüber. Wird verwendet um neue
     * Connectionkonfigurationen zu testen.
     */
    DatabaseConnectionState validateConnection(final String url, final String user, final String pass, final boolean admin);

    /**
     * @return DBSTATE um den momentanen Zustand der Datenbankverbindung zu
     *         prüfen.
     */
    DatabaseConnectionState getDatabaseState();

    /**
     * Setzt das developing flag. So kann in der entwicklung zum beispiel eine
     * andere DB verwendet werden.
     */
    void setDeveloping(boolean developing);

    /**
     * Wenn die db nicht vorhanden ist, wird die ganze datenbank mit sql queries
     * erstellt.
     */
    void createDatabase() throws SqlException;

    /**
     * Setzt die Datenbankkonfiguration (driver, url, user, password,
     * dialect,..) Darf NICHT null sein
     */
    void setConfig(DatabaseConnectionConfiguration config);

    /**
     * Initialisiert den Database Access. Zu diesem Zeitpunkt muss die
     * Konfiguraiton definiert sein. Diese Initialisierung muss in Tests nicht
     * gemacht werden. Dafür kann der Konstruktor verwendet werden.
     */
    void init();

    /**
     * @return den namen der datenbank. Ist ein Freitext, einfach um die DB
     *         wiederzuerkennen.
     */
    String getName();

    /**
     * @return den Namen des jdbc treibers. Zum Beispiel
     *         oracle.jdbc.OracleDriver.
     */
    String getDriver();

    /**
     * @return den HibernateDialect. Zum Beispiel
     *         org.hibernate.dialect.OracleDialect.
     */
    String getDialect();

    /**
     * @return ein File mit dem Backup der kompletten Datenbank.
     */
    File backUpDatabase(final String path);

    /**
     * @return {@link IDatabaseAccess}.
     */
    IDatabaseAccess getDataAccess();
}
