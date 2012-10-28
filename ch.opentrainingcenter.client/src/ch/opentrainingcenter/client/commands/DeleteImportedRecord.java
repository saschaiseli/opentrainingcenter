package ch.opentrainingcenter.client.commands;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.ui.handlers.HandlerUtil;

import ch.opentrainingcenter.client.cache.Cache;
import ch.opentrainingcenter.client.cache.impl.TrainingCenterDataCache;
import ch.opentrainingcenter.db.DatabaseAccessFactory;
import ch.opentrainingcenter.db.IDatabaseAccess;
import ch.opentrainingcenter.transfer.IImported;

public class DeleteImportedRecord extends AbstractHandler {

    private static final Logger LOGGER = Logger.getLogger(DeleteImportedRecord.class.getName());

    public static final String ID = "ch.opentrainingcenter.client.commands.DeleteImportedRecord"; //$NON-NLS-1$

    @Override
    public Object execute(final ExecutionEvent event) throws ExecutionException {
        final IDatabaseAccess db = DatabaseAccessFactory.getDatabaseAccess();
        final Cache cache = TrainingCenterDataCache.getInstance();

        final ISelection selection = HandlerUtil.getCurrentSelection(event);

        final List<?> records = ((StructuredSelection) selection).toList();

        final List<Date> deletedIds = new ArrayList<Date>();
        for (final Object obj : records) {
            final IImported record = (IImported) obj;
            final int dbId = record.getId();
            LOGGER.info("Lösche den Lauf mit der ID " + record.getActivityId() + " und der DB Id: " + dbId); //$NON-NLS-1$ //$NON-NLS-2$
            db.removeImportedRecord(record.getActivityId());
            deletedIds.add(record.getActivityId());
        }
        cache.remove(deletedIds);
        return null;
    }
}
