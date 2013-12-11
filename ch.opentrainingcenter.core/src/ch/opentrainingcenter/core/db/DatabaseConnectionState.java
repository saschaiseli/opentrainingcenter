package ch.opentrainingcenter.core.db;

/**
 * Immutable!! Gibt Auskunft über den Zustand einer Datenbankverbindung und
 * mappt allefalls eine Fehlermeldung.
 * 
 */
public class DatabaseConnectionState {
    private final String message;
    private final DBSTATE state;

    /**
     * Erstellt einen Connection State vom Typ {@link DBSTATE#OK} ohne Message.
     */
    private DatabaseConnectionState() {
        this.message = null;
        this.state = DBSTATE.OK;
    }

    private DatabaseConnectionState(final String message, final DBSTATE state) {
        this.message = message;
        this.state = state;
    }

    /**
     * @return Erstellt einen Connection State vom Typ {@link DBSTATE#OK} ohne
     *         Message.
     */
    public static DatabaseConnectionState createNewOkState() {
        return new DatabaseConnectionState();
    }

    /**
     * @param message
     * @return Erstellt einen Connection State vom Typ {@link DBSTATE#OK} mit
     *         der Message.
     */
    public static DatabaseConnectionState createProblemState(final String message) {
        return new DatabaseConnectionState(message, DBSTATE.PROBLEM);
    }

    /**
     * @param message
     * @param state
     * @return Erstellt einen ConnectionState mit state und Message.
     */
    public static DatabaseConnectionState createState(final String message, final DBSTATE state) {
        return new DatabaseConnectionState(message, state);
    }

    public String getMessage() {
        return message;
    }

    public DBSTATE getState() {
        return state;
    }

    @SuppressWarnings("nls")
    @Override
    public String toString() {
        return "DatabaseConnectionState [message=" + message + ", state=" + state + "]";
    }
}
