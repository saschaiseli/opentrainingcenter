package ch.opentrainingcenter.client.action;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.ui.handlers.HandlerUtil;

public class AddEditHandler extends AbstractHandler {

    public static final String ID = "ch.opentrainingcenter.client.action.AddEditHandler"; //$NON-NLS-1$

    @Override
    public Object execute(final ExecutionEvent event) throws ExecutionException {
        final ISelection selection = HandlerUtil.getCurrentSelection(event);
        return null;
    }

}
