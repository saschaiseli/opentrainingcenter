package ch.opentrainingcenter.core.db;

import java.io.File;

import org.eclipse.core.runtime.IExecutableExtensionFactory;

/**
 * Datenbank Verbindungsdetails / Configuration und Administration
 */
public interface IDatabaseConnection extends IExecutableExtensionFactory {

    String EXTENSION_POINT_NAME = "classImportedDao"; //$NON-NLS-1$

    DbConnection getDbConnection();

    DbConnection getAdminConnection();

    /**
     * @return true, wenn zum erstellen der datenbank eine admin connection
     *         gebraucht wird.
     */
    boolean isUsingAdminDbConnection();

    /**
     * Validiert die Datenbankverbindung und gibt entsprechend {@link DBSTATE}
     * auskunft darüber.
     */
    DBSTATE validateConnection(final String url, final String user, final String pass, final boolean admin);

    DBSTATE getDatabaseState();

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
    void setConfiguration(DatabaseConnectionConfiguration config);

    /**
     * Initialisiert den Database Access. Zu diesem Zeitpunkt muss die
     * Konfiguraiton definiert sein. Diese Initialisierung muss in Tests nicht
     * gemacht werden. Dafür kann der Konstruktor verwendet werden.
     */
    void init();

    /**
     * @return den namen der datenbank
     */
    String getName();

    String getDriver();

    String getDialect();

    /**
     * @return ein File mit dem Backup der kompletten Datenbank.
     */
    File backUpDatabase(final String path);

    IDatabaseAccess getDataAccess();
}
