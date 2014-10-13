package ch.opentrainingcenter.client.commands;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.handlers.HandlerUtil;

import ch.opentrainingcenter.core.db.IDatabaseAccess;
import ch.opentrainingcenter.core.helper.TimeHelper;
import ch.opentrainingcenter.core.service.IDatabaseService;
import ch.opentrainingcenter.i18n.Messages;
import ch.opentrainingcenter.transfer.IRoute;
import ch.opentrainingcenter.transfer.ITraining;

/**
 * Handler um selektierte Trainings zu löschen.
 */
public class DeleteImportedRecord extends AbstractHandler {

    private static final Logger LOGGER = Logger.getLogger(DeleteImportedRecord.class.getName());

    public static final String ID = "ch.opentrainingcenter.client.commands.DeleteImportedRecord"; //$NON-NLS-1$

    private final IDatabaseService service;

    /**
     * Konstruktor fuer OSGI
     */
    public DeleteImportedRecord() {
        this((IDatabaseService) PlatformUI.getWorkbench().getService(IDatabaseService.class));
    }

    /**
     * Konstruktor fuer Tests
     */
    public DeleteImportedRecord(final IDatabaseService service) {
        this.service = service;
    }

    @Override
    public Object execute(final ExecutionEvent event) throws ExecutionException {
        final ISelection selection = HandlerUtil.getCurrentSelection(event);
        final List<?> records = ((StructuredSelection) selection).toList();
        final IDatabaseAccess databaseAccess = service.getDatabaseAccess();
        Display.getCurrent().asyncExec(new Runnable() {

            @Override
            public void run() {
                final List<ITraining> recordsNotDeleted = removeRecords(records, databaseAccess);
                if (!recordsNotDeleted.isEmpty()) {
                    final Shell shell = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell();
                    final StringBuilder str = new StringBuilder();
                    for (final ITraining training : recordsNotDeleted) {
                        str.append(Messages.DeleteImportedRecord_prefix + TimeHelper.convertDateToString(training.getDatum())).append('\n');
                    }
                    MessageDialog.openError(shell, Messages.DeleteImportedRecord_Error_Title, Messages.DeleteImportedRecord_Error_Message + str.toString());
                }
            }
        });
        return null;
    }

    List<ITraining> removeRecords(final List<?> records, final IDatabaseAccess databaseAccess) {
        final List<ITraining> result = new ArrayList<ITraining>();
        for (final Object obj : records) {
            final ITraining record = (ITraining) obj;
            final int dbId = record.getId();
            LOGGER.info("Lösche den Lauf mit der ID " + record.getDatum() + " und der DB Id: " + dbId); //$NON-NLS-1$ //$NON-NLS-2$
            final IRoute route = record.getRoute();
            if (hasReferenzTrack(route)) {
                if (isRecordReferenzTrack(record, route)) {
                    // record ist die referenz fuer die route!!!
                    LOGGER.warn(String.format("Der Record '%s' ist die Referenz für die Route '%s' und kann nicht gelöscht werden", record, route)); //$NON-NLS-1$
                    result.add(record);
                    continue;
                }
            }
            databaseAccess.removeTrainingByDate(record.getDatum());
        }
        return result;
    }

    /**
     * Alle Routen haben eine Referenz, ausser die initiale Route "Unbekannt"
     */
    private boolean hasReferenzTrack(final IRoute route) {
        return route.getReferenzTrack() != null;
    }

    private boolean isRecordReferenzTrack(final ITraining record, final IRoute route) {
        return route.getReferenzTrack().getId() == record.getId();
    }
}
