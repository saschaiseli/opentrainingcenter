package ch.opentrainingcenter.client;

import org.apache.log4j.Logger;
import org.eclipse.ui.application.IWorkbenchWindowConfigurer;
import org.eclipse.ui.application.WorkbenchAdvisor;
import org.eclipse.ui.application.WorkbenchWindowAdvisor;

import ch.opentrainingcenter.client.perspectives.AthletePerspective;
import ch.opentrainingcenter.client.perspectives.MainPerspective;
import ch.opentrainingcenter.client.views.ApplicationContext;
import ch.opentrainingcenter.core.db.DatabaseAccessFactory;
import ch.opentrainingcenter.transfer.IAthlete;

/**
 * This workbench advisor creates the window advisor, and specifies the
 * perspective id for the initial window.
 */
public class ApplicationWorkbenchAdvisor extends WorkbenchAdvisor {

    private static final Logger LOGGER = Logger.getLogger(ApplicationWorkbenchAdvisor.class);

    @Override
    public WorkbenchWindowAdvisor createWorkbenchWindowAdvisor(final IWorkbenchWindowConfigurer configurer) {
        return new ApplicationWorkbenchWindowAdvisor(configurer);
    }

    @Override
    public String getInitialWindowPerspectiveId() {
        final String athleteId = Activator.getDefault().getPreferenceStore().getString(PreferenceConstants.ATHLETE_ID);
        if (athleteId != null && athleteId.length() > 0) {
            final IAthlete athlete = DatabaseAccessFactory.getDatabaseAccess().getAthlete(Integer.parseInt(athleteId));
            if (athlete == null) {
                LOGGER.info(Messages.ApplicationWorkbenchAdvisorAthleteNotFound);
                return AthletePerspective.ID;
            } else {
                LOGGER.info(Messages.ApplicationWorkbenchAdvisorAthleteFound);

                init(athlete);

                return MainPerspective.ID;
            }
        } else {
            LOGGER.info(Messages.ApplicationWorkbenchAdvisorAthleteNotInPreferences);
            return AthletePerspective.ID;
        }
    }

    private void init(final IAthlete athlete) {
        ApplicationContext.getApplicationContext().setAthlete(athlete);
        // do some cache initial load
    }
}
