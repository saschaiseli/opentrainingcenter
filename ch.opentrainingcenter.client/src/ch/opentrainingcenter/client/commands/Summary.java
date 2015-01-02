package ch.opentrainingcenter.client.commands;

import java.util.List;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.handlers.HandlerUtil;

import ch.opentrainingcenter.client.Activator;
import ch.opentrainingcenter.client.action.job.NavigationSelectionSupport;
import ch.opentrainingcenter.client.action.job.SummaryJob;
import ch.opentrainingcenter.client.views.ApplicationContext;
import ch.opentrainingcenter.client.views.summary.SummaryView;
import ch.opentrainingcenter.core.db.IDatabaseAccess;
import ch.opentrainingcenter.core.service.IDatabaseService;
import ch.opentrainingcenter.i18n.Messages;
import ch.opentrainingcenter.transfer.ITraining;

public class Summary extends OtcAbstractHandler {

    private final IDatabaseService service;
    private final ApplicationContext context;

    public Summary() {
        this(Activator.getDefault().getPreferenceStore());
    }

    public Summary(final IPreferenceStore store) {
        super(store);
        service = (IDatabaseService) PlatformUI.getWorkbench().getService(IDatabaseService.class);
        context = ApplicationContext.getApplicationContext();
    }

    @Override
    public Object execute(final ExecutionEvent event) throws ExecutionException {
        final ISelection selection = HandlerUtil.getCurrentSelection(event);
        if (selection == null || selection.isEmpty()) {
            return null;
        }
        final IDatabaseAccess databaseAccess = service.getDatabaseAccess();
        final List<?> records = ((StructuredSelection) selection).toList();
        final NavigationSelectionSupport<ITraining> selectionSupport = new NavigationSelectionSupport<>(databaseAccess, context.getAthlete(), ITraining.class);
        final List<ITraining> selectedTrainings = selectionSupport.getSelectedTrainings(records);
        try {
            final IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
            final String hash = String.valueOf(Math.random());
            final SummaryView view = (SummaryView) page.showView(SummaryView.ID, hash, IWorkbenchPage.VIEW_ACTIVATE);

            final SummaryJob job = new SummaryJob(Messages.Summary_0, selectedTrainings, view);
            job.schedule();
        } catch (final PartInitException e) {
            e.printStackTrace();
        }
        return null;
    }

}
