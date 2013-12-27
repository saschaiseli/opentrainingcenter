package ch.opentrainingcenter.client.commands;

import java.util.List;

import org.apache.log4j.Logger;
import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.handlers.HandlerUtil;

import ch.opentrainingcenter.core.service.IDatabaseService;
import ch.opentrainingcenter.transfer.ITraining;

/**
 * Handler um selektierte Trainings zu löschen.
 */
public class DeleteImportedRecord extends AbstractHandler {

    private static final Logger LOGGER = Logger.getLogger(DeleteImportedRecord.class.getName());

    public static final String ID = "ch.opentrainingcenter.client.commands.DeleteImportedRecord"; //$NON-NLS-1$

    private final IDatabaseService service;

    public DeleteImportedRecord() {
        service = (IDatabaseService) PlatformUI.getWorkbench().getService(IDatabaseService.class);
    }

    @Override
    public Object execute(final ExecutionEvent event) throws ExecutionException {
        final ISelection selection = HandlerUtil.getCurrentSelection(event);
        final List<?> records = ((StructuredSelection) selection).toList();

        for (final Object obj : records) {
            final ITraining record = (ITraining) obj;
            final int dbId = record.getId();
            LOGGER.info("Lösche den Lauf mit der ID " + record.getDatum() + " und der DB Id: " + dbId); //$NON-NLS-1$ //$NON-NLS-2$
            service.getDatabaseAccess().removeTrainingByDate(record.getDatum());
        }
        return null;
    }
}
