package ch.opentrainingcenter.client.commands.runtype;

import java.util.List;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.ui.PlatformUI;

import ch.opentrainingcenter.client.views.ApplicationContext;
import ch.opentrainingcenter.core.cache.Cache;
import ch.opentrainingcenter.core.cache.TrainingCache;
import ch.opentrainingcenter.core.helper.RunType;
import ch.opentrainingcenter.core.service.IDatabaseService;
import ch.opentrainingcenter.model.navigation.ConcreteImported;
import ch.opentrainingcenter.transfer.ITraining;

/**
 * Lauf Typ.
 */
public abstract class ChangeRunType extends AbstractHandler {

    private final Cache cache;
    private final IDatabaseService service;

    public ChangeRunType() {
        this((IDatabaseService) PlatformUI.getWorkbench().getService(IDatabaseService.class), TrainingCache.getInstance());
    }

    /**
     * Konstruktor fuer Tests
     */
    public ChangeRunType(final IDatabaseService service, final Cache cache) {
        this.service = service;
        this.cache = cache;
    }

    @Override
    public Object execute(final ExecutionEvent event) throws ExecutionException {

        final List<?> selection = ApplicationContext.getApplicationContext().getSelection();
        if (selection == null || selection.isEmpty()) {
            return null;
        }
        for (final Object obj : selection) {
            final ITraining record = ((ConcreteImported) obj).getImported();
            service.getDatabaseAccess().updateRecord(record, getType().getIndex());
        }
        cache.notifyListeners();
        return null;
    }

    public abstract RunType getType();
}
