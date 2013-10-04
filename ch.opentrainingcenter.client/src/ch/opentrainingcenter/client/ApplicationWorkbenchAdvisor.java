package ch.opentrainingcenter.client;

import org.apache.log4j.Logger;
import org.eclipse.ui.application.IWorkbenchWindowConfigurer;
import org.eclipse.ui.application.WorkbenchAdvisor;
import org.eclipse.ui.application.WorkbenchWindowAdvisor;

import ch.opentrainingcenter.client.perspectives.EinstellungenPerspective;
import ch.opentrainingcenter.client.perspectives.MainPerspective;
import ch.opentrainingcenter.client.views.ApplicationContext;
import ch.opentrainingcenter.core.PreferenceConstants;
import ch.opentrainingcenter.core.db.DBSTATE;
import ch.opentrainingcenter.core.db.DatabaseAccessFactory;
import ch.opentrainingcenter.i18n.Messages;
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
        final DBSTATE dbState = ApplicationContext.getApplicationContext().getDbState();
        final String athleteId = Activator.getDefault().getPreferenceStore().getString(PreferenceConstants.ATHLETE_ID);
        if (isDbOkAndAthleteSelected(dbState, athleteId)) {
            final IAthlete athlete = DatabaseAccessFactory.getDatabaseAccess().getAthlete(Integer.parseInt(athleteId));
            if (athlete == null) {
                LOGGER.info(Messages.ApplicationWorkbenchAdvisorAthleteNotFound);
                return EinstellungenPerspective.ID;
            } else {
                LOGGER.info(Messages.ApplicationWorkbenchAdvisorAthleteFound);
                ApplicationContext.getApplicationContext().setAthlete(athlete);
                return MainPerspective.ID;
            }
        } else {
            LOGGER.info(Messages.ApplicationWorkbenchAdvisorAthleteNotInPreferences);
            return EinstellungenPerspective.ID;
        }
    }

    @Override
    public void eventLoopException(final Throwable exception) {
        LOGGER.error("Exception in UI", exception); //$NON-NLS-1$
        super.eventLoopException(exception);
    }

    private boolean isDbOkAndAthleteSelected(final DBSTATE dbState, final String athleteId) {
        return DBSTATE.OK.equals(dbState) && athleteId != null && athleteId.length() > 0;
    }
}
