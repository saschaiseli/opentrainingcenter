package ch.iseli.sportanalyzer.client.action;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.ui.ISelectionListener;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.actions.ActionFactory.IWorkbenchAction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ch.iseli.sportanalyzer.client.cache.TrainingCenterDataCache;

public class DeleteImportedRecord extends Action implements ISelectionListener, IWorkbenchAction {

    public static final String ID = "ch.iseli.sportanalyzer.client.action.DeleteImportedRecord";

    private static final Logger log = LoggerFactory.getLogger(ImportGpsFilesAction.class.getName());

    private final TrainingCenterDataCache cache = TrainingCenterDataCache.getInstance();

    public DeleteImportedRecord() {
        setId(ID);
        setText("Delete den Lauf");

    }

    @Override
    public void dispose() {
        // TODO Auto-generated method stub

    }

    @Override
    public void selectionChanged(IWorkbenchPart part, ISelection selection) {
        // TODO Auto-generated method stub

    }

    @Override
    public void run() {
        log.info("delete {}", cache.getSelected().getId());
        System.out.println("mach den scheiss..." + cache.getSelected().getId());
    }

}
