package ch.iseli.sportanalyzer.client;

import org.apache.log4j.Logger;
import org.eclipse.ui.application.IWorkbenchConfigurer;
import org.eclipse.ui.application.IWorkbenchWindowConfigurer;
import org.eclipse.ui.application.WorkbenchAdvisor;
import org.eclipse.ui.application.WorkbenchWindowAdvisor;

import ch.iseli.sportanalyzer.client.views.ahtlete.AthletePerspective;
import ch.iseli.sportanalyzer.client.views.navigation.NavigationView;
import ch.iseli.sportanalyzer.db.DatabaseAccessFactory;
import ch.opentrainingcenter.transfer.impl.Athlete;

/**
 * This workbench advisor creates the window advisor, and specifies the perspective id for the initial window.
 */
public class ApplicationWorkbenchAdvisor extends WorkbenchAdvisor {

    private static final Logger logger = Logger.getLogger(NavigationView.class);

    @Override
    public WorkbenchWindowAdvisor createWorkbenchWindowAdvisor(final IWorkbenchWindowConfigurer configurer) {
        return new ApplicationWorkbenchWindowAdvisor(configurer);
    }

    @Override
    public String getInitialWindowPerspectiveId() {
        final String athleteId = Activator.getDefault().getPreferenceStore().getString(PreferenceConstants.ATHLETE_NAME);
        // return Perspective.ID;
        if (athleteId != null && athleteId.length() > 0) {
            final Athlete athlete = DatabaseAccessFactory.getDatabaseAccess().getAthlete(Integer.parseInt(athleteId));
            if (athlete == null) {
                // Athlete nicht gefunden.
                return AthletePerspective.ID;
            } else {
                // athlete gefunden
                return Perspective.ID;
            }
        } else {
            // athlete auch nicht in den preferences gesetzt
            return AthletePerspective.ID;
        }
    }

    @Override
    public void initialize(final IWorkbenchConfigurer configurer) {
        // configurer.setSaveAndRestore(true);
    }
}
