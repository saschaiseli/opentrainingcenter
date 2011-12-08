package ch.iseli.sportanalyzer.client;

import org.apache.log4j.Logger;
import org.eclipse.ui.application.IWorkbenchWindowConfigurer;
import org.eclipse.ui.application.WorkbenchAdvisor;
import org.eclipse.ui.application.WorkbenchWindowAdvisor;

import ch.iseli.sportanalyzer.client.views.ahtlete.AthletePerspective;
import ch.iseli.sportanalyzer.client.views.navigation.NavigationView;
import ch.iseli.sportanalyzer.db.DatabaseAccessFactory;
import ch.opentrainingcenter.transfer.IAthlete;

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
        try {
            DatabaseAccessFactory.getDatabaseAccess().getAthlete(1);
        } catch (final Exception e) {
            final Throwable cause = e.getCause();
            final String message = cause.getMessage();
            if (message.contains("Locked by another process")) {
                logger.error("Database Locked by another process");
            }
        }
        final String athleteId = Activator.getDefault().getPreferenceStore().getString(PreferenceConstants.ATHLETE_ID);
        if (athleteId != null && athleteId.length() > 0) {
            final IAthlete athlete = DatabaseAccessFactory.getDatabaseAccess().getAthlete(Integer.parseInt(athleteId));
            if (athlete == null) {
                logger.info("Athlete nicht gefunden.");
                return AthletePerspective.ID;
            } else {
                logger.info("athlete gefunden");
                return Perspective.ID;
            }
        } else {
            logger.info("athlete auch nicht in den preferences gesetzt");
            return AthletePerspective.ID;
        }
    }
}
