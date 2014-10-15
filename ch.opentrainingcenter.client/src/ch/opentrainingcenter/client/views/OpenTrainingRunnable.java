package ch.opentrainingcenter.client.views;

import org.apache.log4j.Logger;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;

import ch.opentrainingcenter.client.views.overview.SingleActivityViewPart;
import ch.opentrainingcenter.core.assertions.Assertions;
import ch.opentrainingcenter.transfer.ITraining;

public class OpenTrainingRunnable implements Runnable {

    private static final Logger LOGGER = Logger.getLogger(OpenTrainingRunnable.class);
    private final ITraining training;

    public OpenTrainingRunnable(final ITraining training) {
        Assertions.notNull(training, "Um das Training zu öffnen, darf dieses nicht NULL sein"); //$NON-NLS-1$
        this.training = training;
    }

    @Override
    public void run() {
        final String hash = String.valueOf(training.getDatum());
        try {
            ApplicationContext.getApplicationContext().setSelectedId(training.getDatum());
            PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().showView(SingleActivityViewPart.ID, hash, 1);
        } catch (final PartInitException e) {
            LOGGER.error(e);
        }
    }

}
