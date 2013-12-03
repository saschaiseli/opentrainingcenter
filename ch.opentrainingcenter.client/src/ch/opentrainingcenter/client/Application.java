package ch.opentrainingcenter.client;

import org.apache.log4j.Logger;
import org.eclipse.equinox.app.IApplication;
import org.eclipse.equinox.app.IApplicationContext;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.PlatformUI;

import ch.opentrainingcenter.i18n.Messages;

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
        final Display display = PlatformUI.createDisplay();
        return PlatformUI.createAndRunWorkbench(display, new ApplicationWorkbenchAdvisor());
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
