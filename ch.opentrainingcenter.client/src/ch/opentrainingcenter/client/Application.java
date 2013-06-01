package ch.opentrainingcenter.client;

import org.apache.log4j.Logger;
import org.eclipse.equinox.app.IApplication;
import org.eclipse.equinox.app.IApplicationContext;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.PlatformUI;

import ch.opentrainingcenter.client.views.ApplicationContext;
import ch.opentrainingcenter.core.PreferenceConstants;
import ch.opentrainingcenter.core.db.DatabaseAccessFactory;
import ch.opentrainingcenter.core.db.DatabaseHelper;
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

    private final IDatabaseAccess databaseAccess = DatabaseAccessFactory.getDatabaseAccess();

    @Override
    public Object start(final IApplicationContext context) {
        final Display display = PlatformUI.createDisplay();
        final boolean isLocked = DatabaseHelper.isDatabaseLocked(databaseAccess);

        try {
            if (isLocked) {
                LOGGER.error("DB gelockt. stoppe die Applikation"); //$NON-NLS-1$
                System.exit(0);
                final MessageDialog messageDialog = new MessageDialog(display.getActiveShell(), Messages.Application0, null, Messages.Application1,
                        MessageDialog.ERROR, new String[] { Messages.Application2 }, 0);
                if (messageDialog.open() == 1) {
                    return IApplication.EXIT_OK;
                }
            } else {
                final boolean isExisting = DatabaseHelper.isDatabaseExisting(databaseAccess);
                if (!isExisting) {
                    try {
                        databaseAccess.createDatabase();
                    } catch (final SqlException sqlException) {
                        MessageDialog.openError(display.getActiveShell(), "DB Fehler", "Datenbank konnte nicht erstellt werden");
                    }
                }
            }
            final String athleteId = Activator.getDefault().getPreferenceStore().getString(PreferenceConstants.ATHLETE_ID);
            final ApplicationContext appContext = ApplicationContext.getApplicationContext();
            if (athleteId != null && athleteId.length() > 0) {
                final IAthlete athlete = databaseAccess.getAthlete(Integer.parseInt(athleteId));
                if (athlete != null) {
                    appContext.setAthlete(athlete);
                }
            }
            final int returnCode = PlatformUI.createAndRunWorkbench(display, new ApplicationWorkbenchAdvisor());
            if (returnCode == PlatformUI.RETURN_RESTART) {
                return IApplication.EXIT_RESTART;
            }
            return IApplication.EXIT_OK;
        } finally {
            display.dispose();
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
