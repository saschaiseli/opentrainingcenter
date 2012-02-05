package ch.opentrainingcenter.client;

import org.apache.log4j.Logger;
import org.eclipse.ui.application.IWorkbenchWindowConfigurer;
import org.eclipse.ui.application.WorkbenchAdvisor;
import org.eclipse.ui.application.WorkbenchWindowAdvisor;

import ch.opentrainingcenter.client.cache.TrainingCenterDataCache;
import ch.opentrainingcenter.client.perspectives.AthletePerspective;
import ch.opentrainingcenter.client.perspectives.PerspectiveNavigation;
import ch.opentrainingcenter.db.DatabaseAccessFactory;
import ch.opentrainingcenter.transfer.IAthlete;

/**
 * This workbench advisor creates the window advisor, and specifies the perspective id for the initial window.
 */
public class ApplicationWorkbenchAdvisor extends WorkbenchAdvisor {

    private static final Logger logger = Logger.getLogger(ApplicationWorkbenchAdvisor.class);

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
                logger.info(Messages.ApplicationWorkbenchAdvisor_AthleteNotFound);
                return AthletePerspective.ID;
            } else {
                logger.info(Messages.ApplicationWorkbenchAdvisor_AthleteFound);

                initCache(athlete);

                return PerspectiveNavigation.ID;
            }
        } else {
            logger.info(Messages.ApplicationWorkbenchAdvisor_AthleteNotInPreferences);
            return AthletePerspective.ID;
        }
    }

    private void initCache(final IAthlete athlete) {
        final TrainingCenterDataCache cache = TrainingCenterDataCache.getInstance();
        cache.setSelectedProfile(athlete);
        cache.addAllImported(DatabaseAccessFactory.getDatabaseAccess().getAllImported(athlete));
        cache.cacheLoaded();
    }
}
