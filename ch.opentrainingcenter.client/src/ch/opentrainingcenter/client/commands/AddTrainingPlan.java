package ch.opentrainingcenter.client.commands;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.preference.IPreferenceStore;
import org.joda.time.DateTime;

import ch.opentrainingcenter.client.Activator;
import ch.opentrainingcenter.client.action.job.LoadJahresplanung;
import ch.opentrainingcenter.i18n.Messages;

/**
 * Fügt einen neuen Trainingsplan hinzu.
 */
public class AddTrainingPlan extends OtcAbstractHandler {

    public static final String ID = "ch.opentrainingcenter.client.commands.AddTrainingPlan"; //$NON-NLS-1$
    private final LoadJahresplanung job;

    public AddTrainingPlan() {
        this(Activator.getDefault().getPreferenceStore(), new LoadJahresplanung(Messages.AddTrainingPlan_0, new DateTime().getYear()));
    }

    public AddTrainingPlan(final IPreferenceStore store, final LoadJahresplanung job) {
        super(store);
        this.job = job;
    }

    @Override
    public Object execute(final ExecutionEvent event) throws ExecutionException {
        job.schedule();
        return null;
    }

}
