package ch.opentrainingcenter.client.commands;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.ui.handlers.HandlerUtil;

import ch.opentrainingcenter.client.cache.HealthCache;
import ch.opentrainingcenter.core.db.DatabaseAccessFactory;
import ch.opentrainingcenter.core.db.IDatabaseAccess;
import ch.opentrainingcenter.transfer.IHealth;

public class DeleteHealthRecord extends AbstractHandler {
    private static final Logger LOGGER = Logger.getLogger(DeleteHealthRecord.class.getName());

    public static final String ID = "ch.opentrainingcenter.client.commands.DeleteHealthRecord"; //$NON-NLS-1$

    @Override
    public Object execute(final ExecutionEvent event) throws ExecutionException {
        final IDatabaseAccess db = DatabaseAccessFactory.getDatabaseAccess();

        final ISelection selection = HandlerUtil.getCurrentSelection(event);

        final List<?> records = ((StructuredSelection) selection).toList();
        final List<Integer> ids = new ArrayList<Integer>();
        for (final Object record : records) {
            final IHealth health = (IHealth) record;
            final int id = health.getId();
            LOGGER.info("Lösche Vitaldaten mit der ID " + id); //$NON-NLS-1$ 
            db.removeHealth(id);
            ids.add(id);
        }
        HealthCache.getInstance().remove(ids);
        return null;
    }
}
