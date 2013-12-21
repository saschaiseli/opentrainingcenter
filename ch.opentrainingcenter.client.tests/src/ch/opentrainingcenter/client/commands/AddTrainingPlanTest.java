package ch.opentrainingcenter.client.commands;

import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.preference.IPreferenceStore;
import org.junit.Test;

import ch.opentrainingcenter.client.action.job.LoadJahresplanung;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class AddTrainingPlanTest {

    @Test
    public void testExecute() throws ExecutionException {
        final IPreferenceStore store = mock(IPreferenceStore.class);
        final LoadJahresplanung job = mock(LoadJahresplanung.class);

        final AddTrainingPlan command = new AddTrainingPlan(store, job);

        command.execute(null);

        verify(job).schedule();
    }

}
