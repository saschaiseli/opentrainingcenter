package ch.iseli.sportanalyzer.client.action;

import org.apache.log4j.Logger;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.ui.ISelectionListener;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.actions.ActionFactory.IWorkbenchAction;

public class RestartOtc extends Action implements ISelectionListener, IWorkbenchAction {

    public static final String ID = "ch.iseli.sportanalyzer.client.RestartOtc";

    private static final Logger logger = Logger.getLogger(RestartOtc.class.getName());

    private final IWorkbenchWindow window;

    public RestartOtc(final IWorkbenchWindow window, final String text) {
        this.window = window;
        setId(ID);
        setText(text);
    }

    @Override
    public void dispose() {
        // TODO Auto-generated method stub

    }

    @Override
    public void selectionChanged(final IWorkbenchPart part, final ISelection selection) {
        // TODO Auto-generated method stub

    }

    @Override
    public void run() {
        final IWorkbenchWindow window = PlatformUI.getWorkbench().getActiveWorkbenchWindow();
        window.getWorkbench().restart();
    }
}
