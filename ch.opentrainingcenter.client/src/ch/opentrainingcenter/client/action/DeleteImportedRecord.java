package ch.opentrainingcenter.client.action;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.ui.ISelectionListener;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.actions.ActionFactory.IWorkbenchAction;

import ch.opentrainingcenter.client.Messages;
import ch.opentrainingcenter.client.cache.TrainingCenterDataCache;
import ch.opentrainingcenter.db.DatabaseAccessFactory;
import ch.opentrainingcenter.transfer.IImported;

public class DeleteImportedRecord extends Action implements ISelectionListener, IWorkbenchAction {

    public static final String ID = "ch.opentrainingcenter.client.action.DeleteImportedRecord"; //$NON-NLS-1$

    private static final Logger LOGGER = Logger.getLogger(DeleteImportedRecord.class.getName());

    private final TrainingCenterDataCache cache = TrainingCenterDataCache.getInstance();

    public DeleteImportedRecord() {
	setId(ID);
	setText(Messages.DeleteImportedRecord_0);
    }

    @Override
    public void dispose() {
    }

    @Override
    public void run() {
	final List<?> selection = cache.getSelection();
	final List<Date> deletedIds = new ArrayList<Date>();
	for (final Object obj : selection) {
	    final IImported record = (IImported) obj;
	    final int dbId = record.getId();
	    LOGGER.info("Lösche den Lauf mit der ID " + record.getActivityId() + " und der DB Id: " + dbId); //$NON-NLS-1$ //$NON-NLS-2$
	    DatabaseAccessFactory.getDatabaseAccess().removeImportedRecord(record.getActivityId());
	    deletedIds.add(record.getActivityId());
	}
	cache.remove(deletedIds);
    }

    @Override
    public void selectionChanged(final IWorkbenchPart part, final ISelection selection) {
    }
}
