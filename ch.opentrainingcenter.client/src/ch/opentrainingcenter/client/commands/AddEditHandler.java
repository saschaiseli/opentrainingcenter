package ch.opentrainingcenter.client.commands;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.dialogs.InputDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.ui.handlers.HandlerUtil;

import ch.opentrainingcenter.core.cache.TrainingCenterDataCache;
import ch.opentrainingcenter.core.db.DatabaseAccessFactory;
import ch.opentrainingcenter.i18n.Messages;
import ch.opentrainingcenter.transfer.ActivityExtension;
import ch.opentrainingcenter.transfer.IImported;
import ch.opentrainingcenter.transfer.ITraining;

public class AddEditHandler extends AbstractHandler {

    public static final String ID = "ch.opentrainingcenter.client.action.AddEditHandler"; //$NON-NLS-1$

    @Override
    public Object execute(final ExecutionEvent event) throws ExecutionException {
        final ISelection selection = HandlerUtil.getCurrentSelection(event);
        final StructuredSelection sel = (StructuredSelection) selection;

        final IImported record = (IImported) sel.getFirstElement();
        final ITraining training = record.getTraining();
        final String initialValue = training.getNote();
        final InputDialog dialog = new InputDialog(null, Messages.AddEditHandler_0, Messages.AddEditHandler_1, initialValue, null);
        if (dialog.open() == InputDialog.OK) {
            training.setNote(dialog.getValue());
            updateNote(record);
        }
        return null;
    }

    private void updateNote(final IImported record) {
        DatabaseAccessFactory.getDatabaseAccess().updateRecord(record);
        final ITraining training = record.getTraining();
        final ActivityExtension extension = new ActivityExtension(training.getNote(), training.getWeather());
        TrainingCenterDataCache.getInstance().updateExtension(record.getActivityId(), extension);
    }
}
