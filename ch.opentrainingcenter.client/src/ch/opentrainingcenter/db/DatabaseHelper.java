package ch.opentrainingcenter.db;

import org.apache.log4j.Logger;

public class DatabaseHelper {
    public static final Logger LOGGER = Logger.getLogger(DatabaseHelper.class);

    public static boolean isDatabaseExisting() {
	try {
	    DatabaseAccessFactory.getDatabaseAccess().getAthlete(1);
	} catch (final Exception e) {
	    final Throwable cause = e.getCause();
	    final String message = cause.getMessage();
	    if (message != null && message.contains("Table \"ATHLETE\" not found; SQL statement:")) { //$NON-NLS-1$
		LOGGER.error("Database existiert noch nicht"); //$NON-NLS-1$
		return false;
	    }
	}
	return true;
    }

    public static boolean isDatabaseLocked() {
	try {
	    DatabaseAccessFactory.getDatabaseAccess().getAthlete(1);
	} catch (final Exception e) {
	    final Throwable cause = e.getCause();
	    final String message = cause.getMessage();
	    if (message != null && message.contains("Locked by another process")) { //$NON-NLS-1$
		LOGGER.error("Database Locked by another process"); //$NON-NLS-1$
		System.exit(0);
		return true;
	    }
	}
	return false;
    }

    private DatabaseHelper() {

    }
}
