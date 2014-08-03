package ch.opentrainingcenter.client;

import java.lang.reflect.InvocationTargetException;

import org.apache.log4j.Logger;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.action.IStatusLineManager;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.preference.PreferenceManager;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.application.ActionBarAdvisor;
import org.eclipse.ui.application.IActionBarConfigurer;
import org.eclipse.ui.application.IWorkbenchWindowConfigurer;
import org.eclipse.ui.application.WorkbenchWindowAdvisor;

import ch.opentrainingcenter.client.perspectives.MainPerspective;
import ch.opentrainingcenter.client.splash.InitialLoadRunnable;
import ch.opentrainingcenter.client.splash.OtcSplashHandler;
import ch.opentrainingcenter.client.views.ApplicationContext;
import ch.opentrainingcenter.client.views.statusline.DatabaseContributeItem;
import ch.opentrainingcenter.client.views.statusline.VersionContributeItem;
import ch.opentrainingcenter.core.PreferenceConstants;
import ch.opentrainingcenter.core.db.DBSTATE;
import ch.opentrainingcenter.core.db.IDatabaseAccess;
import ch.opentrainingcenter.core.db.IDatabaseConnection;
import ch.opentrainingcenter.core.service.IDatabaseService;
import ch.opentrainingcenter.transfer.IAthlete;

public class ApplicationWorkbenchWindowAdvisor extends WorkbenchWindowAdvisor {

    private static final Logger LOG = Logger.getLogger(ApplicationWorkbenchWindowAdvisor.class);
    private final IPreferenceStore store;
    private final ApplicationContext context = ApplicationContext.getApplicationContext();
    private IWorkbenchWindowConfigurer configurer;
    private final IDatabaseAccess databaseAccess;
    private final IDatabaseConnection databaseConnection;

    public ApplicationWorkbenchWindowAdvisor(final IWorkbenchWindowConfigurer configurer) {

        this(configurer, Activator.getDefault().getPreferenceStore(), null);
    }

    /**
     * Konstruktor für Tests
     */
    public ApplicationWorkbenchWindowAdvisor(final IWorkbenchWindowConfigurer conf, final IPreferenceStore store, final IDatabaseService dbservice) {
        super(conf);
        this.store = store;
        IDatabaseService service = dbservice;
        if (service == null) {
            service = (IDatabaseService) PlatformUI.getWorkbench().getService(IDatabaseService.class);
        }
        databaseAccess = service.getDatabaseAccess();
        databaseConnection = service.getDatabaseConnection();
    }

    @Override
    public ActionBarAdvisor createActionBarAdvisor(final IActionBarConfigurer configurer) {
        return new ApplicationActionBarAdvisor(configurer);
    }

    @Override
    public void preWindowOpen() {
        configurer = getWindowConfigurer();
        configurer.setShowPerspectiveBar(false);
        configurer.setShowCoolBar(true);
        configurer.setShowStatusLine(true);
        configurer.setShowProgressIndicator(true);
        configurer.setShowStatusLine(true);

        final PreferenceManager pm = PlatformUI.getWorkbench().getPreferenceManager();
        pm.remove("org.eclipse.ui.preferencePages.Workbench"); //$NON-NLS-1$

        final OtcSplashHandler splashHandler = Activator.getSplashHandler();

        final String id = store.getString(PreferenceConstants.ATHLETE_ID);
        final DBSTATE dbState = ApplicationContext.getApplicationContext().getDbState().getState();
        if (DBSTATE.OK.equals(dbState)) {
            if (id != null && id.length() > 0) {
                final IAthlete athlete = databaseAccess.getAthlete(Integer.parseInt(id));
                configurer.setTitle(Application.WINDOW_TITLE + " / " + athlete.getName()); //$NON-NLS-1$
                context.setAthlete(athlete);
            } else {
                configurer.setTitle(Application.WINDOW_TITLE);
            }

            final IProgressMonitor monitor = splashHandler.getBundleProgressMonitor();
            try {
                new InitialLoadRunnable().run(monitor);
            } catch (final InvocationTargetException e) {
                LOG.error("Fehler im InitialLoad", e); //$NON-NLS-1$
            } catch (final InterruptedException e) {
                LOG.error("Fehler im InitialLoad", e); //$NON-NLS-1$
            }
        } else {
            configurer.setTitle(Application.WINDOW_TITLE);
        }

    }

    @Override
    public void postWindowOpen() {
        super.postWindowOpen();
        configurer.getWindow().getShell().setMaximized(true);
        final IStatusLineManager manager = getWindowConfigurer().getActionBarConfigurer().getStatusLineManager();
        manager.add(new DatabaseContributeItem(databaseConnection.getName(), databaseConnection.getDbConnection().getUrl()));
        manager.add(new VersionContributeItem());
        manager.update(true);
    }

    @Override
    public boolean isDurableFolder(final String perspectiveId, final String folderId) {
        if (MainPerspective.ID.equals(perspectiveId) && MainPerspective.RIGHT_PART.equals(folderId)) {
            return true;
        }
        return super.isDurableFolder(perspectiveId, folderId);
    }

    @Override
    public void createWindowContents(final Shell shell) {
        super.createWindowContents(shell);
        shell.setMaximized(true);
    }
}
