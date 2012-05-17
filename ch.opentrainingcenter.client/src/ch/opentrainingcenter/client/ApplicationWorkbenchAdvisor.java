package ch.opentrainingcenter.client;

import org.apache.log4j.Logger;
import org.eclipse.ui.application.IWorkbenchWindowConfigurer;
import org.eclipse.ui.application.WorkbenchAdvisor;
import org.eclipse.ui.application.WorkbenchWindowAdvisor;

import ch.opentrainingcenter.client.cache.Cache;
import ch.opentrainingcenter.client.cache.impl.TrainingCenterDataCache;
import ch.opentrainingcenter.client.perspectives.AthletePerspective;
import ch.opentrainingcenter.client.perspectives.PerspectiveNavigation;
import ch.opentrainingcenter.client.views.ApplicationContext;
import ch.opentrainingcenter.db.DatabaseAccessFactory;
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
                LOGGER.info(Messages.ApplicationWorkbenchAdvisor_AthleteNotFound);
                return AthletePerspective.ID;
            } else {
                LOGGER.info(Messages.ApplicationWorkbenchAdvisor_AthleteFound);

                init(athlete);

                return PerspectiveNavigation.ID;
            }
        } else {
            LOGGER.info(Messages.ApplicationWorkbenchAdvisor_AthleteNotInPreferences);
            return AthletePerspective.ID;
        }
    }

    private void init(final IAthlete athlete) {
        ApplicationContext.getApplicationContext().setAthlete(athlete);
        final Cache cache = TrainingCenterDataCache.getInstance();
        cache.addAllImported(DatabaseAccessFactory.getDatabaseAccess().getAllImported(athlete));
    }
}
