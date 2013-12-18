package ch.opentrainingcenter.client.commands;

import org.apache.log4j.Logger;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;

import ch.opentrainingcenter.client.Activator;
import ch.opentrainingcenter.client.views.chart.ChartViewPart;

public class HeartScatterHandler extends OtcAbstractHandler {

    private static final Logger LOGGER = Logger.getLogger(HeartScatterHandler.class.getName());

    public static final String ID = "ch.opentrainingcenter.client.commands.HeartScatter"; //$NON-NLS-1$

    public HeartScatterHandler() {
        this(Activator.getDefault().getPreferenceStore());
    }

    public HeartScatterHandler(final IPreferenceStore store) {
        super(store);
    }

    @Override
    public Object execute(final ExecutionEvent event) throws ExecutionException {
        LOGGER.info("execute HeartScatterHandler"); //$NON-NLS-1$

        Display.getDefault().asyncExec(new Runnable() {

            @Override
            public void run() {
                final String hash = String.valueOf(Math.random());
                try {
                    PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().showView(ChartViewPart.ID, hash, IWorkbenchPage.VIEW_ACTIVATE);
                } catch (final PartInitException e) {
                    LOGGER.error(e);
                }
            }
        });

        return null;
    }
}
