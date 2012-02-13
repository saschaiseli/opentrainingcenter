package ch.opentrainingcenter.client.action;

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
        // TODO Auto-generated method stub

    }

    @Override
    public void selectionChanged(final IWorkbenchPart part, final ISelection selection) {

    }

    @Override
    public void run() {
        final List<?> selection = cache.getSelection();
        for (final Object obj : selection) {
            final IImported record = (IImported) obj;
            DatabaseAccessFactory.getDatabaseAccess().updateRecord(record, type.getIndex());
        }
        cache.update();
    }
}
