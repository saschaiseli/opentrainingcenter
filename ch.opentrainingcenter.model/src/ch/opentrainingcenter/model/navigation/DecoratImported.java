package ch.opentrainingcenter.model.navigation;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import ch.opentrainingcenter.transfer.ITraining;

public class DecoratImported {
    public static final Collection<INavigationItem> decorate(final Collection<ITraining> list) {
        final List<INavigationItem> decorated = new ArrayList<INavigationItem>();
        for (final ITraining imported : list) {
            decorated.add(new ConcreteImported(imported));
        }
        return decorated;
    }
}
