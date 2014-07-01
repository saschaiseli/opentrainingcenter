package ch.opentrainingcenter.client.action;

import org.apache.log4j.Logger;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.ui.ISelectionListener;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.actions.ActionFactory.IWorkbenchAction;

/**
 * Action um OTC neu zu starten.
 * 
 */
public class RestartOtc extends Action implements ISelectionListener, IWorkbenchAction {

    public static final String ID = "ch.opentrainingcenter.client.RestartOtc"; //$NON-NLS-1$

    private static final Logger LOGGER = Logger.getLogger(RestartOtc.class.getName());

    private final IWorkbenchWindow window;

    public RestartOtc(final IWorkbenchWindow window, final String text) {
        this.window = window;
        setId(ID);
        setText(text);
    }

    @Override
    public void run() {
        LOGGER.info("OTC wird restarted"); //$NON-NLS-1$
        window.getWorkbench().restart();
    }

    @Override
    public void dispose() {
        // do nothing
    }

    @Override
    public void selectionChanged(final IWorkbenchPart part, final ISelection selection) {
        // do nothing
    }
}
