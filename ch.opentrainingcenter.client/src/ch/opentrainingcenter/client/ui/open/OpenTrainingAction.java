package ch.opentrainingcenter.client.ui.open;

import java.util.Arrays;
import java.util.List;

import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.widgets.Display;

import ch.opentrainingcenter.transfer.ITraining;

/**
 * Öffnet aus einer Table Selektion das {@link ITraining}.
 */
public class OpenTrainingAction {

    public void open(final TableViewer viewer) {
        final StructuredSelection selection = (StructuredSelection) viewer.getSelection();
        final Object[] array = selection.toArray();
        final List<Object> records = Arrays.asList(array);
        for (final Object record : records) {
            final ITraining training = (ITraining) record;
            Display.getDefault().asyncExec(new OpenTrainingRunnable(training));
        }
    }
}
