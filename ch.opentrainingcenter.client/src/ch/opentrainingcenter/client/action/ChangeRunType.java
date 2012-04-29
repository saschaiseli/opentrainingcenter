package ch.opentrainingcenter.client.action;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.ui.ISelectionListener;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.actions.ActionFactory.IWorkbenchAction;

import ch.opentrainingcenter.client.cache.TrainingCenterDataCache;
import ch.opentrainingcenter.client.model.RunType;
import ch.opentrainingcenter.db.DatabaseAccessFactory;
import ch.opentrainingcenter.transfer.IImported;

public class ChangeRunType extends Action implements ISelectionListener, IWorkbenchAction {
    public static final String ID = "ch.opentrainingcenter.client.action.ChangeRunType"; //$NON-NLS-1$
    private final RunType type;
    private final TrainingCenterDataCache cache = TrainingCenterDataCache.getInstance();

    public ChangeRunType(final RunType type) {
        this.type = type;
        setId(ID);
        setText(type.getTitle());
    }

    @Override
    public void dispose() {
    }

    @Override
    public void selectionChanged(final IWorkbenchPart part, final ISelection selection) {
    }

    @Override
    public void run() {
        final List<?> selection = cache.getSelection();
        final List<IImported> changedTypes = new ArrayList<IImported>();
        for (final Object obj : selection) {
            final IImported record = (IImported) obj;
            DatabaseAccessFactory.getDatabaseAccess().updateRecord(record, getType().getIndex());
            RunTypeActionContainer.update(record.getTrainingType().getId());
            changedTypes.add(record);
        }
        cache.changeType(changedTypes, getType());
        cache.update();
    }

    public RunType getType() {
        return type;
    }
}
