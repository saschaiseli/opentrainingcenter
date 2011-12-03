package ch.iseli.sportanalyzer.client;

import org.apache.log4j.Logger;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.Platform;
import org.eclipse.equinox.app.IApplication;
import org.eclipse.equinox.app.IApplicationContext;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.PlatformUI;

import ch.iseli.sportanalyzer.client.helper.DaoHelper;
import ch.iseli.sportanalyzer.db.IImportedDao;

/**
 * This class controls all aspects of the application's execution
 */
public class Application implements IApplication {

    public static final Logger log = Logger.getLogger(Application.class);

    public static final String ID = "ch.iseli.sportanalyzer.client";

    public static final String CH_OPENTRAININGDATABASE_DB = "ch.opentrainingdatabase.db";

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.equinox.app.IApplication#start(org.eclipse.equinox.app. IApplicationContext)
     */
    @Override
    public Object start(IApplicationContext context) {
        Display display = PlatformUI.createDisplay();

        // test hier ob es datenbank gibt
        IConfigurationElement[] daos = Platform.getExtensionRegistry().getConfigurationElementsFor("ch.opentrainingdatabase.db");
        IImportedDao dao = (IImportedDao) DaoHelper.getDao(daos, IImportedDao.EXTENSION_POINT_NAME);
        try {
            dao.getAthlete(1);
        } catch (Exception e) {
            // db erstellen
            Throwable cause = e.getCause();
            String message = cause.getMessage();
            if (message.contains("Table \"ATHLETE\" not found;")) {
                log.info("Datenbank existiert noch nicht. Es wird eine neue Datenbank erstellt");
                dao.createDatabase();
            }
        }
        try {
            int returnCode = PlatformUI.createAndRunWorkbench(display, new ApplicationWorkbenchAdvisor());
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
