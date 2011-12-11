package ch.iseli.sportanalyzer.client;

import org.apache.log4j.Logger;
import org.eclipse.equinox.app.IApplication;
import org.eclipse.equinox.app.IApplicationContext;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.PlatformUI;

import ch.iseli.sportanalyzer.db.DatabaseAccessFactory;

/**
 * This class controls all aspects of the application's execution
 */
public class Application implements IApplication {

    public static final Logger logger = Logger.getLogger(Application.class);

    public static final String ID = "ch.iseli.sportanalyzer.client"; //$NON-NLS-1$

    public static final String DATABASE_EXTENSION_POINT = "ch.opentrainingdatabase.db"; //$NON-NLS-1$

    public static final String IMPORT_EXTENSION_POINT = "ch.iseli.sportanalyzer.myimporter";//$NON-NLS-1$

    public static final String WINDOW_TITLE = Messages.Application_WindowTitle;

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.equinox.app.IApplication#start(org.eclipse.equinox.app. IApplicationContext)
     */
    @Override
    public Object start(final IApplicationContext context) {
        final Display display = PlatformUI.createDisplay();
        final boolean isLocked = isDatabaseLocked(display);

        try {
            if (isLocked) {
                return IApplication.EXIT_OK;
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

    private boolean isDatabaseLocked(final Display display) {
        try {
            DatabaseAccessFactory.getDatabaseAccess().getAthlete(1);
        } catch (final Exception e) {
            final Throwable cause = e.getCause();
            final String message = cause.getMessage();
            if (message.contains("Locked by another process")) { //$NON-NLS-1$
                logger.error("Database Locked by another process"); //$NON-NLS-1$
                final MessageDialog messageDialog = new MessageDialog(display.getActiveShell(), Messages.Application_0, null, Messages.Application_1,
                        MessageDialog.ERROR, new String[] { Messages.Application_2 }, 0);
                if (messageDialog.open() == 1) {
                    return true;
                }
            }
        }
        return false;
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
