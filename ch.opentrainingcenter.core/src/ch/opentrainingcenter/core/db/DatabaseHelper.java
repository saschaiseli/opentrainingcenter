package ch.opentrainingcenter.core.db;

import org.apache.log4j.Logger;

public class DatabaseHelper {
    private static final Logger LOGGER = Logger.getLogger(DatabaseHelper.class);

    public enum DBSTATE {
        OK, CONFIG_PROBLEM, PROBLEM, LOCKED;
    }

    private DatabaseHelper() {

    }

    public static boolean isDatabaseExisting(final IDatabaseAccess databaseAccess) {
        try {
            databaseAccess.getAthlete(1);
        } catch (final Exception e) {
            final Throwable cause = e.getCause();
            final String message = cause != null ? cause.getMessage() : e.getMessage();
            if (message != null && message.contains("Table \"ATHLETE\" not found; SQL statement:")) { //$NON-NLS-1$
                LOGGER.error("Database existiert noch nicht"); //$NON-NLS-1$
                return false;
            } else {
                LOGGER.error("Fehler mit der Datenbank: " + message);
            }
        }
        return true;
    }

    /**
     * @param databaseAccess
     * @return
     */
    public static DBSTATE getDatabaseState(final IDatabaseAccess databaseAccess) {
        try {
            databaseAccess.getAthlete(1);
        } catch (final Exception e) {
            final Throwable cause = e.getCause();
            final String message = cause != null ? cause.getMessage() : e.getMessage();
            if (message != null && message.contains("Locked by another process")) { //$NON-NLS-1$
                LOGGER.error("Database Locked by another process"); //$NON-NLS-1$
                return DBSTATE.LOCKED;
            } else if (message != null && message.contains("Wrong user name or password")) { //$NON-NLS-1$
                LOGGER.error("Wrong user name or password"); //$NON-NLS-1$
                return DBSTATE.CONFIG_PROBLEM;
            } else {
                LOGGER.error("Fehler mit der Datenbank: " + message); //$NON-NLS-1$
                return DBSTATE.PROBLEM;
            }
        }
        return DBSTATE.OK;
    }
}
