package ch.opentrainingcenter.client.action;

import java.util.List;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.ui.ISelectionListener;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.actions.ActionFactory.IWorkbenchAction;

import ch.opentrainingcenter.client.cache.Cache;
import ch.opentrainingcenter.client.model.RunType;
import ch.opentrainingcenter.db.IDatabaseAccess;
import ch.opentrainingcenter.transfer.IImported;

public class ChangeRunType extends Action implements ISelectionListener, IWorkbenchAction {
    public static final String ID = "ch.opentrainingcenter.client.action.ChangeRunType"; //$NON-NLS-1$
    private final RunType type;
    private final Cache cache;
    private final IDatabaseAccess databaseAccess;

    ChangeRunType(final RunType type, final IDatabaseAccess databaseAccess, final Cache cache) {
        this.type = type;
        this.databaseAccess = databaseAccess;
        this.cache = cache;
        setId(ID);
        setText(type.getTitle());
    }

    @Override
    public void run() {
        final List<?> selection = cache.getSelection();
        if (selection == null) {
            return;
        }
        for (final Object obj : selection) {
            final IImported record = (IImported) obj;
            databaseAccess.updateRecord(record, type.getIndex());
        }
        cache.update();
    }

    RunType getType() {
        return type;
    }

    @Override
    public void dispose() {
    }

    @Override
    public void selectionChanged(final IWorkbenchPart part, final ISelection selection) {
    }
}
