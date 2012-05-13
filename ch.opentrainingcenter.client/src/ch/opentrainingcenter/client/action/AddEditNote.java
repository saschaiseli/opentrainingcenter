package ch.opentrainingcenter.client.action;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.ui.ISelectionListener;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.actions.ActionFactory.IWorkbenchAction;

import ch.opentrainingcenter.client.cache.Cache;
import ch.opentrainingcenter.db.IDatabaseAccess;

public class AddEditNote extends Action implements ISelectionListener, IWorkbenchAction {

    public static final String ID = "ch.opentrainingcenter.client.action.AddEditNote"; //$NON-NLS-1$

    private final Cache cache;
    private final IDatabaseAccess databaseAccess;

    public AddEditNote(final Cache cache, final IDatabaseAccess databaseAccess) {
        this.cache = cache;
        this.databaseAccess = databaseAccess;
        setId(ID);
        setText("Notiz zum Lauf hinzufügen oder Editieren");
    }

    @Override
    public void run() {
        System.out.println("run");
    }

    @Override
    public void dispose() {
    }

    @Override
    public void selectionChanged(final IWorkbenchPart part, final ISelection selection) {
    }

}
