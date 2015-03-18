package ch.opentrainingcenter.client.views.best;

import org.apache.log4j.Logger;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.part.ViewPart;

import ch.opentrainingcenter.client.Activator;
import ch.opentrainingcenter.core.PreferenceConstants;
import ch.opentrainingcenter.core.db.IDatabaseAccess;
import ch.opentrainingcenter.core.service.IDatabaseService;
import ch.opentrainingcenter.transfer.IAthlete;
import ch.opentrainingcenter.transfer.Sport;

public class BestRunsView extends ViewPart {

    public static final String ID = "ch.opentrainingcenter.client.best.bestof"; //$NON-NLS-1$

    private static final Logger LOGGER = Logger.getLogger(BestRunsView.class);

    private IAthlete athlete;

    private FormToolkit toolkit;

    private final IDatabaseAccess databaseAccess;

    public BestRunsView() {
        LOGGER.debug("<< --- DynamicChartViewPart"); //$NON-NLS-1$
        final IDatabaseService service = (IDatabaseService) PlatformUI.getWorkbench().getService(IDatabaseService.class);
        databaseAccess = service.getDatabaseAccess();
        LOGGER.debug("DynamicChartViewPart --- >>"); //$NON-NLS-1$
    }

    @Override
    public void createPartControl(final Composite parent) {
        LOGGER.debug("create BestRunsView view"); //$NON-NLS-1$
        final String athleteId = Activator.getDefault().getPreferenceStore().getString(PreferenceConstants.ATHLETE_ID);
        if (athleteId != null && athleteId.length() > 0) {
            athlete = databaseAccess.getAthlete(Integer.parseInt(athleteId));
        } else {
            LOGGER.error("Kein Athlete am Start..."); //$NON-NLS-1$
            return;
        }
        toolkit = new FormToolkit(parent.getDisplay());

        if (hasRunData(Sport.RUNNING)) {
            final BestRunsComposite runningComposite = new BestRunsComposite(toolkit, Sport.RUNNING, databaseAccess, athlete);
            runningComposite.createPartControl(parent);
        }

        if (hasRunData(Sport.BIKING)) {
            final BestRunsComposite runningComposite = new BestRunsComposite(toolkit, Sport.BIKING, databaseAccess, athlete);
            runningComposite.createPartControl(parent);
        }
        LOGGER.debug("create BestRunsView view finished"); //$NON-NLS-1$
    }

    private boolean hasRunData(final Sport running) {
        return true;
    }

    //
    @Override
    public void setFocus() {
    }
}
