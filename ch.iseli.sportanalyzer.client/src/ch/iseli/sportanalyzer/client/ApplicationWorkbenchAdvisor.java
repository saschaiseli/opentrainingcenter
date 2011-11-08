package ch.iseli.sportanalyzer.client;

import org.eclipse.ui.application.IWorkbenchConfigurer;
import org.eclipse.ui.application.IWorkbenchWindowConfigurer;
import org.eclipse.ui.application.WorkbenchAdvisor;
import org.eclipse.ui.application.WorkbenchWindowAdvisor;

import ch.iseli.sportanalyzer.client.athlete.AthletePerspective;

/**
 * This workbench advisor creates the window advisor, and specifies the perspective id for the initial window.
 */
public class ApplicationWorkbenchAdvisor extends WorkbenchAdvisor {

    @Override
    public WorkbenchWindowAdvisor createWorkbenchWindowAdvisor(IWorkbenchWindowConfigurer configurer) {
        return new ApplicationWorkbenchWindowAdvisor(configurer);
    }

    @Override
    public String getInitialWindowPerspectiveId() {
        String athleteName = Activator.getDefault().getPreferenceStore().getString(PreferenceConstants.ATHLETE_NAME);
        if (athleteName == null || athleteName.length() <= 0) {
            return AthletePerspective.ID;
        } else {
            return Perspective.ID;
        }
    }

    @Override
    public void initialize(IWorkbenchConfigurer configurer) {
        // configurer.setSaveAndRestore(true);
    }
}
