package ch.iseli.sportanalyzer.client;

import org.eclipse.ui.application.ActionBarAdvisor;
import org.eclipse.ui.application.IActionBarConfigurer;
import org.eclipse.ui.application.IWorkbenchWindowConfigurer;
import org.eclipse.ui.application.WorkbenchWindowAdvisor;

import ch.iseli.sportanalyzer.client.perspectives.PerspectiveNavigation;
import ch.iseli.sportanalyzer.db.DatabaseAccessFactory;

public class ApplicationWorkbenchWindowAdvisor extends WorkbenchWindowAdvisor {

    public ApplicationWorkbenchWindowAdvisor(final IWorkbenchWindowConfigurer configurer) {
        super(configurer);
    }

    @Override
    public ActionBarAdvisor createActionBarAdvisor(final IActionBarConfigurer configurer) {
        return new ApplicationActionBarAdvisor(configurer);
    }

    @Override
    public void preWindowOpen() {
        final IWorkbenchWindowConfigurer configurer = getWindowConfigurer();
        configurer.setShowPerspectiveBar(true);
        configurer.setShowCoolBar(true);
        configurer.setShowStatusLine(true);
        configurer.setShowProgressIndicator(true);
        configurer.setShowStatusLine(true);
        final String id = Activator.getDefault().getPreferenceStore().getString(PreferenceConstants.ATHLETE_ID);
        if (id != null && id.length() > 0) {
            configurer.setTitle(Application.WINDOW_TITLE + " / " + DatabaseAccessFactory.getDatabaseAccess().getAthlete(Integer.parseInt(id)).getName());
        } else {
            configurer.setTitle(Application.WINDOW_TITLE);
        }
    }

    @Override
    public boolean isDurableFolder(final String perspectiveId, final String folderId) {
        if (PerspectiveNavigation.ID.equals(perspectiveId) && PerspectiveNavigation.RIGHT_PART.equals(folderId)) {
            return true;
        }
        return super.isDurableFolder(perspectiveId, folderId);
    }
}
