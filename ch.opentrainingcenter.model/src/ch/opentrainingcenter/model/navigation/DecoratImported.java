package ch.opentrainingcenter.model.navigation;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import ch.opentrainingcenter.transfer.IHealth;
import ch.opentrainingcenter.transfer.ITraining;

public final class DecoratImported {

    private DecoratImported() {

    }

    public static final Collection<INavigationItem> decorate(final Collection<ITraining> list) {
        final List<INavigationItem> decorated = new ArrayList<INavigationItem>();
        for (final ITraining imported : list) {
            decorated.add(new ConcreteImported(imported));
        }
        return decorated;
    }

    public static final Collection<INavigationItem> decorateHealth(final Collection<IHealth> list) {
        final List<INavigationItem> decorated = new ArrayList<INavigationItem>();
        for (final IHealth imported : list) {
            decorated.add(new ConcreteHealth(imported, "icons/cardiology_32_32.png")); //$NON-NLS-1$
        }
        return decorated;
    }
}
