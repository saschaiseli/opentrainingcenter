package ch.opentrainingcenter.client.commands;

import org.apache.log4j.Logger;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;

import ch.opentrainingcenter.client.Activator;
import ch.opentrainingcenter.client.views.routen.RoutenView;

public class ShowTracks extends OtcAbstractHandler {

    private static final Logger LOGGER = Logger.getLogger(ShowTracks.class);
    public static final String ID = "ch.opentrainingcenter.client.commands.ShowTracks"; //$NON-NLS-1$

    public ShowTracks() {
        super(Activator.getDefault().getPreferenceStore());
    }

    @Override
    public Object execute(final ExecutionEvent event) throws ExecutionException {
        Display.getDefault().asyncExec(new Runnable() {

            @Override
            public void run() {
                try {
                    final IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
                    page.showView(RoutenView.ID);
                } catch (final PartInitException e) {
                    LOGGER.error(e);
                }
            }
        });
        return null;
    }

}
