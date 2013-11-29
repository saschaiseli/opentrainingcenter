package ch.opentrainingcenter.core.service;

import ch.opentrainingcenter.core.db.IDatabaseAccess;
import ch.opentrainingcenter.core.db.IDatabaseConnection;

/**
 * Service der die Datenbank Zugriffe kapselt.
 */
public interface IDatabaseService {

    /**
     * Initialisiert die Datenbankverbindung. Diese Methode muss aufgerufen
     * werden, damit die Verbindung zu der Datenbank ordentlich eingerichtet
     * wird.
     * 
     * @param dbName
     *            Name der Datenbank (z.B. Postgres / H2)
     * @param url
     *            URL zu der Datenbank
     * @param user
     *            User mit dem die Applikation betrieben wird
     * @param pw
     *            Passwort für den User
     * @return
     */
    void init(final String dbName, final String url, final String user, final String pw);

    /**
     * @return den Access auf alle DAO's.
     */
    IDatabaseAccess getDatabaseAccess();

    /**
     * @return die DB Verbindung um Admin Task auf der DB auszuführen.
     */
    IDatabaseConnection getDatabaseConnection();
}
