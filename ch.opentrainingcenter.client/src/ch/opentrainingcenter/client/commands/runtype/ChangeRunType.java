package ch.opentrainingcenter.client.commands.runtype;

import java.util.List;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;

import ch.opentrainingcenter.client.views.ApplicationContext;
import ch.opentrainingcenter.core.cache.Cache;
import ch.opentrainingcenter.core.cache.TrainingCenterDataCache;
import ch.opentrainingcenter.core.db.DatabaseAccessFactory;
import ch.opentrainingcenter.core.db.IDatabaseAccess;
import ch.opentrainingcenter.core.helper.RunType;
import ch.opentrainingcenter.model.navigation.ConcreteImported;

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
            db.updateRecord(record, getType().getIndex());
        }
        cache.notifyListeners();
        return null;
    }

    public abstract RunType getType();
}