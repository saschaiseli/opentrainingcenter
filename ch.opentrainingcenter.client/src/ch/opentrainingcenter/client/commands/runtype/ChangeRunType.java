package ch.opentrainingcenter.client.commands.runtype;

import java.util.List;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.ui.PlatformUI;

import ch.opentrainingcenter.client.views.ApplicationContext;
import ch.opentrainingcenter.core.service.IDatabaseService;
import ch.opentrainingcenter.model.navigation.ConcreteImported;
import ch.opentrainingcenter.transfer.ITraining;
import ch.opentrainingcenter.transfer.TrainingType;

/**
 * Lauf Typ.
 */
public abstract class ChangeRunType extends AbstractHandler {

    private final IDatabaseService service;

    public ChangeRunType() {
        this((IDatabaseService) PlatformUI.getWorkbench().getService(IDatabaseService.class));
    }

    /**
     * Konstruktor fuer Tests
     */
    public ChangeRunType(final IDatabaseService service) {
        this.service = service;
    }

    @Override
    public Object execute(final ExecutionEvent event) throws ExecutionException {

        final List<?> selection = ApplicationContext.getApplicationContext().getSelection();
        if (selection == null || selection.isEmpty()) {
            return null;
        }
        for (final Object obj : selection) {
            final ITraining record = ((ConcreteImported) obj).getTraining();
            service.getDatabaseAccess().updateTrainingType(record, getType().getIndex());
        }
        return null;
    }

    public abstract TrainingType getType();
}
