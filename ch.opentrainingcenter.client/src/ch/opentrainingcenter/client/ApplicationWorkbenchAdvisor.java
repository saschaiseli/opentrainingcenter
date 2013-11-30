package ch.opentrainingcenter.client;

import org.apache.log4j.Logger;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.application.IWorkbenchWindowConfigurer;
import org.eclipse.ui.application.WorkbenchAdvisor;
import org.eclipse.ui.application.WorkbenchWindowAdvisor;

import ch.opentrainingcenter.client.perspectives.EinstellungenPerspective;
import ch.opentrainingcenter.client.perspectives.MainPerspective;
import ch.opentrainingcenter.client.views.ApplicationContext;
import ch.opentrainingcenter.core.PreferenceConstants;
import ch.opentrainingcenter.core.db.DBSTATE;
import ch.opentrainingcenter.core.db.IDatabaseAccess;
import ch.opentrainingcenter.core.db.IDatabaseConnection;
import ch.opentrainingcenter.core.service.IDatabaseService;
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

        final IPreferenceStore store = Activator.getDefault().getPreferenceStore();
        final String dbName = store.getString(PreferenceConstants.DB);
        final String url = store.getString(PreferenceConstants.DB_URL);
        final String user = store.getString(PreferenceConstants.DB_USER);
        final String pw = store.getString(PreferenceConstants.DB_PASS);

        final String urlAdmin = store.getString(PreferenceConstants.DB_ADMIN_URL);
        final String userAdmin = store.getString(PreferenceConstants.DB_ADMIN_USER);
        final String pwAdmin = store.getString(PreferenceConstants.DB_ADMIN_PASS);

        final IDatabaseService service = (IDatabaseService) PlatformUI.getWorkbench().getService(IDatabaseService.class);
        service.init(dbName, url, user, pw, urlAdmin, userAdmin, pwAdmin);
        final IDatabaseAccess databaseAccess = service.getDatabaseAccess();
        final IDatabaseConnection databaseConnection = service.getDatabaseConnection();
        final DBSTATE dbState = databaseConnection.getDatabaseState();
        ApplicationContext.getApplicationContext().setDbState(dbState);
        final String athleteId = Activator.getDefault().getPreferenceStore().getString(PreferenceConstants.ATHLETE_ID);
        if (isDbOkAndAthleteSelected(dbState, athleteId)) {
            final IAthlete athlete = databaseAccess.getAthlete(Integer.parseInt(athleteId));
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
        MessageDialog.openError(Display.getCurrent().getActiveShell(), Messages.ApplicationWorkbenchAdvisor_0, Messages.ApplicationWorkbenchAdvisor_1);
    }

    private boolean isDbOkAndAthleteSelected(final DBSTATE dbState, final String athleteId) {
        return DBSTATE.OK.equals(dbState) && athleteId != null && athleteId.length() > 0;
    }
}
