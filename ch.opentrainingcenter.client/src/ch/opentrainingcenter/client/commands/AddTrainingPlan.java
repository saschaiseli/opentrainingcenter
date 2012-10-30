package ch.opentrainingcenter.client.commands;

import java.util.List;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.ui.handlers.HandlerUtil;

import ch.opentrainingcenter.client.action.job.LoadJahresplanung;

public class AddTrainingPlan extends AbstractHandler {

    public static final String ID = "ch.opentrainingcenter.client.commands.AddTrainingPlan"; //$NON-NLS-1$

    @Override
    public void dispose() {

    }

    @Override
    public Object execute(final ExecutionEvent event) throws ExecutionException {
        final ISelection selection = HandlerUtil.getCurrentSelection(event);
        final List<?> records = ((StructuredSelection) selection).toList();
        final Integer jahr = (Integer) records.get(0);
        final LoadJahresplanung job = new LoadJahresplanung("Lade Planung für das Jahr ", jahr, null, null);
        job.schedule();
        return null;
    }

}
