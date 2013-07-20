package ch.opentrainingcenter.client;

import org.apache.log4j.Logger;
import org.eclipse.equinox.app.IApplication;
import org.eclipse.equinox.app.IApplicationContext;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.PlatformUI;

import ch.opentrainingcenter.client.views.ApplicationContext;
import ch.opentrainingcenter.core.PreferenceConstants;
import ch.opentrainingcenter.core.db.DBSTATE;
import ch.opentrainingcenter.core.db.DatabaseAccessFactory;
import ch.opentrainingcenter.core.db.IDatabaseAccess;
import ch.opentrainingcenter.core.db.SqlException;
import ch.opentrainingcenter.i18n.Messages;
import ch.opentrainingcenter.transfer.IAthlete;

/**
 * This class controls all aspects of the application's execution
 */
public class Application implements IApplication {

    public static final Logger LOGGER = Logger.getLogger(Application.class);

    public static final String ID = "ch.opentrainingcenter.client"; //$NON-NLS-1$

    public static final String DATABASE_EXTENSION_POINT = "ch.opentrainingdatabase.db"; //$NON-NLS-1$

    public static final String WINDOW_TITLE = Messages.ApplicationWindowTitle;

    @Override
    public Object start(final IApplicationContext context) {
        IDatabaseAccess databaseAccess;
        final Display display = PlatformUI.createDisplay();
        final IPreferenceStore store = Activator.getDefault().getPreferenceStore();
        final String dbUrl = store.getString(PreferenceConstants.DB_URL);
        if (dbUrl == null || dbUrl.length() == 0) {
            LOGGER.info("Datenbank ist noch nicht konfiguriert"); //$NON-NLS-1$
            // welcome page anzeigen
            return PlatformUI.createAndRunWorkbench(display, new ApplicationWorkbenchAdvisor());
        } else {
            final String url = store.getString(PreferenceConstants.DB_URL);
            final String user = store.getString(PreferenceConstants.DB_USER);
            final String pw = store.getString(PreferenceConstants.DB_PASS);
            final String dbName = store.getString(PreferenceConstants.DB);
            // db initialisieren
            DatabaseAccessFactory.init(dbName, url, user, pw);
            databaseAccess = DatabaseAccessFactory.getDatabaseAccess();
        }
        final DBSTATE dbState = databaseAccess.getDatabaseState();
        ApplicationContext.getApplicationContext().setDbState(dbState);
        try {
            if (DBSTATE.LOCKED.equals(dbState)) {
                LOGGER.error("DB gelockt. stoppe die Applikation"); //$NON-NLS-1$
                final MessageDialog messageDialog = new MessageDialog(display.getActiveShell(), Messages.Application0, null, Messages.Application1,
                        MessageDialog.ERROR, new String[] { Messages.Application2 }, 0);
                if (messageDialog.open() == 1) {
                    return IApplication.EXIT_OK;
                }
            }
            if (DBSTATE.CONFIG_PROBLEM.equals(dbState)) {
                ApplicationContext.getApplicationContext().setDbState(dbState);
                return PlatformUI.createAndRunWorkbench(display, new ApplicationWorkbenchAdvisor());
            } else {
                final boolean isExisting = databaseAccess.isDatabaseExisting();
                if (!isExisting) {
                    try {
                        databaseAccess.createDatabase();
                    } catch (final SqlException sqlException) {
                        MessageDialog.openError(display.getActiveShell(), Messages.Application_0, Messages.Application_1);
                    }
                }
            }

            addAthleteToContext(databaseAccess);

            final int returnCode = PlatformUI.createAndRunWorkbench(display, new ApplicationWorkbenchAdvisor());
            if (returnCode == PlatformUI.RETURN_RESTART) {
                return IApplication.EXIT_RESTART;
            }
            return IApplication.EXIT_OK;
        } finally {
            display.dispose();
        }
    }

    private void addAthleteToContext(final IDatabaseAccess databaseAccess) {
        final String athleteId = Activator.getDefault().getPreferenceStore().getString(PreferenceConstants.ATHLETE_ID);
        final ApplicationContext appContext = ApplicationContext.getApplicationContext();
        if (athleteId != null && athleteId.length() > 0) {
            final IAthlete athlete = databaseAccess.getAthlete(Integer.parseInt(athleteId));
            if (athlete != null) {
                appContext.setAthlete(athlete);
            }
        }
    }

    @Override
    public void stop() {
        if (!PlatformUI.isWorkbenchRunning()) {
            return;
        }
        final IWorkbench workbench = PlatformUI.getWorkbench();
        final Display display = workbench.getDisplay();
        display.syncExec(new Runnable() {
            @Override
            public void run() {
                if (!display.isDisposed()) {
                    workbench.close();
                }
            }
        });
    }
}
