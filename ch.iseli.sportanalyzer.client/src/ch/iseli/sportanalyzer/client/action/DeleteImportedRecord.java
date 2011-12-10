package ch.iseli.sportanalyzer.client.action;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.ui.ISelectionListener;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.actions.ActionFactory.IWorkbenchAction;

import ch.iseli.sportanalyzer.client.cache.TrainingCenterDataCache;
import ch.iseli.sportanalyzer.client.cache.TrainingCenterRecord;
import ch.iseli.sportanalyzer.db.DatabaseAccessFactory;

public class DeleteImportedRecord extends Action implements ISelectionListener, IWorkbenchAction {

    public static final String ID = "ch.iseli.sportanalyzer.client.action.DeleteImportedRecord"; //$NON-NLS-1$

    private static final Logger log = Logger.getLogger(ImportGpsFilesAction.class.getName());

    private final TrainingCenterDataCache cache = TrainingCenterDataCache.getInstance();

    public DeleteImportedRecord() {
        setId(ID);
        setText("Lauf löschen");
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
        final List<Integer> deletedIds = new ArrayList<Integer>();
        for (final Object obj : selection) {
            final TrainingCenterRecord t = (TrainingCenterRecord) obj;
            log.debug("Lösche den Lauf mit der ID " + t.getId()); //$NON-NLS-1$
            DatabaseAccessFactory.getDatabaseAccess().removeImportedRecord(t.getId());
            deletedIds.add(t.getId());
        }
        cache.remove(deletedIds);
    }
}
