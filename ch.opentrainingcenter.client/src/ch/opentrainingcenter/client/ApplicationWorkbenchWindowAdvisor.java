package ch.opentrainingcenter.client;

import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.preference.PreferenceManager;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.application.ActionBarAdvisor;
import org.eclipse.ui.application.IActionBarConfigurer;
import org.eclipse.ui.application.IWorkbenchWindowConfigurer;
import org.eclipse.ui.application.WorkbenchWindowAdvisor;

import ch.opentrainingcenter.client.perspectives.MainPerspective;
import ch.opentrainingcenter.db.DatabaseAccessFactory;
import ch.opentrainingcenter.db.IDatabaseAccess;

public class ApplicationWorkbenchWindowAdvisor extends WorkbenchWindowAdvisor {

    private final IPreferenceStore store;
    private final IDatabaseAccess databaseAccess;

    public ApplicationWorkbenchWindowAdvisor(final IWorkbenchWindowConfigurer configurer) {
        this(configurer, Activator.getDefault().getPreferenceStore(), DatabaseAccessFactory.getDatabaseAccess());
    }

    /**
     * Konstruktor für Tests
     */
    public ApplicationWorkbenchWindowAdvisor(final IWorkbenchWindowConfigurer conf, final IPreferenceStore store, final IDatabaseAccess db) {
        super(conf);
        this.store = store;
        databaseAccess = db;
    }

    @Override
    public ActionBarAdvisor createActionBarAdvisor(final IActionBarConfigurer configurer) {
        return new ApplicationActionBarAdvisor(configurer);
    }

    @Override
    public void preWindowOpen() {
        final IWorkbenchWindowConfigurer configurer = getWindowConfigurer();
        configurer.setShowPerspectiveBar(false);
        configurer.setShowCoolBar(true);
        configurer.setShowStatusLine(true);
        configurer.setShowProgressIndicator(true);
        configurer.setShowStatusLine(true);
        final String id = store.getString(PreferenceConstants.ATHLETE_ID);
        if (id != null && id.length() > 0) {
            configurer.setTitle(Application.WINDOW_TITLE + " / " + databaseAccess.getAthlete(Integer.parseInt(id)).getName()); //$NON-NLS-1$
        } else {
            configurer.setTitle(Application.WINDOW_TITLE);
        }
        final PreferenceManager pm = PlatformUI.getWorkbench().getPreferenceManager();
        pm.remove("org.eclipse.ui.preferencePages.Workbench"); //$NON-NLS-1$

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
