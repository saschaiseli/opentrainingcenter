package ch.iseli.sportanalyzer.client;

import org.apache.log4j.Logger;
import org.eclipse.equinox.app.IApplication;
import org.eclipse.equinox.app.IApplicationContext;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.PlatformUI;

import ch.iseli.sportanalyzer.db.DatabaseAccessFactory;

/**
 * This class controls all aspects of the application's execution
 */
public class Application implements IApplication {

    public static final Logger log = Logger.getLogger(Application.class);

    public static final String ID = "ch.iseli.sportanalyzer.client";

    public static final String CH_OPENTRAININGDATABASE_DB = "ch.opentrainingdatabase.db";

    public static final String WINDOW_TITLE = "Open Training Center";

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.equinox.app.IApplication#start(org.eclipse.equinox.app. IApplicationContext)
     */
    @Override
    public Object start(final IApplicationContext context) {
        final Display display = PlatformUI.createDisplay();

        try {
            DatabaseAccessFactory.getDatabaseAccess().getAthlete(1);
        } catch (final Exception e) {
            // db erstellen
            final Throwable cause = e.getCause();
            final String message = cause.getMessage();
            if (message.contains("Table \"ATHLETE\" not found;")) {
                log.info("Datenbank existiert noch nicht. Es wird eine neue Datenbank erstellt");
                DatabaseAccessFactory.getDatabaseAccess().createDatabase();
            }
        }
        try {
            final int returnCode = PlatformUI.createAndRunWorkbench(display, new ApplicationWorkbenchAdvisor());
            if (returnCode == PlatformUI.RETURN_RESTART) {
                return IApplication.EXIT_RESTART;
            }
            return IApplication.EXIT_OK;
        } finally {
            display.dispose();
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.equinox.app.IApplication#stop()
     */
    @Override
    public void stop() {
        if (!PlatformUI.isWorkbenchRunning())
            return;
        final IWorkbench workbench = PlatformUI.getWorkbench();
        final Display display = workbench.getDisplay();
        display.syncExec(new Runnable() {
            @Override
            public void run() {
                if (!display.isDisposed())
                    workbench.close();
            }
        });
    }
}
