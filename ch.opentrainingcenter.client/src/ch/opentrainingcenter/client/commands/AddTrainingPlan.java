package ch.opentrainingcenter.client.commands;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.joda.time.DateTime;

import ch.opentrainingcenter.client.action.job.LoadJahresplanung;
import ch.opentrainingcenter.i18n.Messages;

public class AddTrainingPlan extends AbstractHandler {

    public static final String ID = "ch.opentrainingcenter.client.commands.AddTrainingPlan"; //$NON-NLS-1$

    @Override
    public void dispose() {

    }

    @Override
    public Object execute(final ExecutionEvent event) throws ExecutionException {
        final DateTime now = new DateTime();
        final int year = now.getYear();
        final LoadJahresplanung job = new LoadJahresplanung(Messages.AddTrainingPlan_0, year);
        job.schedule();
        return null;
    }

}
