package ch.opentrainingcenter.core.db;

public enum DBSTATE {

    /**
     * Alles OK
     */
    OK,
    /**
     * Probleme mit der Configuration
     */
    CONFIG_PROBLEM, //
    /**
     * Allgemeines Problem. Weitere Infos müssen in der
     * {@link DatabaseConnectionState} stehen
     */
    PROBLEM, //

    /**
     * Datenbank muss/kann erstellt werden.
     */
    CREATE_DB, //
    /**
     * Datenbank ist gelockt. (vor allem bei filebasierten Datenbanken wie H2
     * möglich.)
     */
    LOCKED;

}