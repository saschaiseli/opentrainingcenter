package ch.iseli.sportanalyzer.client;

import org.apache.log4j.Logger;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.ui.IMemento;
import org.eclipse.ui.application.IWorkbenchConfigurer;
import org.eclipse.ui.application.IWorkbenchWindowConfigurer;
import org.eclipse.ui.application.WorkbenchAdvisor;
import org.eclipse.ui.application.WorkbenchWindowAdvisor;

import ch.iseli.sportanalyzer.client.cache.TrainingCenterDataCache;
import ch.iseli.sportanalyzer.client.cache.TrainingCenterRecord;
import ch.iseli.sportanalyzer.client.perspectives.AthletePerspective;
import ch.iseli.sportanalyzer.client.perspectives.PerspectiveNavigation;
import ch.iseli.sportanalyzer.client.views.navigation.NavigationView;
import ch.iseli.sportanalyzer.db.DatabaseAccessFactory;
import ch.opentrainingcenter.transfer.IAthlete;

/**
 * This workbench advisor creates the window advisor, and specifies the perspective id for the initial window.
 */
public class ApplicationWorkbenchAdvisor extends WorkbenchAdvisor {

    private static final String MEMENTO_CHILD = "selection"; //$NON-NLS-1$
    private static final String MEMENTO_KEY = "recordId"; //$NON-NLS-1$
    private static final Logger logger = Logger.getLogger(NavigationView.class);

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
                return PerspectiveNavigation.ID;
            }
        } else {
            logger.info(Messages.ApplicationWorkbenchAdvisor_AthleteNotInPreferences);
            return AthletePerspective.ID;
        }
    }

    @Override
    public void initialize(final IWorkbenchConfigurer configurer) {
        super.initialize(configurer);
        configurer.setSaveAndRestore(true);
    }

    @Override
    public IStatus saveState(final IMemento memento) {
        final TrainingCenterRecord selected = TrainingCenterDataCache.getInstance().getSelected();
        final IMemento child = memento.createChild(MEMENTO_CHILD);
        if (selected != null) {
            child.putInteger(MEMENTO_KEY, selected.getId());
        }
        return super.saveState(memento);
    }

    @Override
    public IStatus restoreState(final IMemento memento) {
        if (existsMementoChild(memento)) {
            final TrainingCenterDataCache cache = TrainingCenterDataCache.getInstance();
            final Integer id = memento.getChild(MEMENTO_CHILD).getInteger(MEMENTO_KEY);
            if (id != null) {
                cache.setSelectedRun(id);
            }
            logger.debug("Der zuletzt selektierte Lauf hatte die ID: '" + id + "'"); //$NON-NLS-1$ //$NON-NLS-2$
        }
        return super.restoreState(memento);
    }

    private boolean existsMementoChild(final IMemento memento) {
        return memento != null && memento.getChild(MEMENTO_CHILD) != null;
    }
}
