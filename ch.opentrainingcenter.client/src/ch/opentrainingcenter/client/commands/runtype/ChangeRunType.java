package ch.opentrainingcenter.client.commands.runtype;

import java.util.List;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;

import ch.opentrainingcenter.client.cache.Cache;
import ch.opentrainingcenter.client.cache.impl.TrainingCenterDataCache;
import ch.opentrainingcenter.client.model.RunType;
import ch.opentrainingcenter.client.model.navigation.impl.ConcreteImported;
import ch.opentrainingcenter.client.views.ApplicationContext;
import ch.opentrainingcenter.db.DatabaseAccessFactory;
import ch.opentrainingcenter.db.IDatabaseAccess;

public abstract class ChangeRunType extends AbstractHandler {

    private final Cache cache = TrainingCenterDataCache.getInstance();
    private final IDatabaseAccess db = DatabaseAccessFactory.getDatabaseAccess();

    @Override
    public Object execute(final ExecutionEvent event) throws ExecutionException {

        final List<?> selection = ApplicationContext.getApplicationContext().getSelection();
        if (selection == null || selection.isEmpty()) {
            return null;
        }
        for (final Object obj : selection) {
            final ConcreteImported record = (ConcreteImported) obj;
            db.updateRecord(record.getImported(), getType().getIndex());
        }
        cache.notifyListeners();
        return null;
    }

    public abstract RunType getType();
}
