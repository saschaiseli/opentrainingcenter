package ch.opentrainingcenter.client.commands;

import java.util.List;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.ui.handlers.HandlerUtil;

import ch.opentrainingcenter.client.action.job.ShowJahresplanung;

public class ShowTrainingPlan extends AbstractHandler {

    public static final String ID = "ch.opentrainingcenter.client.commands.ShowTrainingPlan"; //$NON-NLS-1$

    @Override
    public void dispose() {

    }

    @Override
    public Object execute(final ExecutionEvent event) throws ExecutionException {
        final ISelection selection = HandlerUtil.getCurrentSelection(event);
        final List<?> records = ((StructuredSelection) selection).toList();
        final Integer jahr = (Integer) records.get(0);
        final ShowJahresplanung job = new ShowJahresplanung("Lade Planung", jahr);
        job.schedule();
        return null;
    }
}
