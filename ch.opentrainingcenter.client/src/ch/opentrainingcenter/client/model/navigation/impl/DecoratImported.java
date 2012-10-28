package ch.opentrainingcenter.client.model.navigation.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import ch.opentrainingcenter.client.model.navigation.INavigationItem;
import ch.opentrainingcenter.transfer.IImported;

public class DecoratImported {
    public static final Collection<INavigationItem> decorate(final Collection<IImported> list) {
        final List<INavigationItem> decorated = new ArrayList<INavigationItem>();
        for (final IImported imported : list) {
            decorated.add(new ConcreteImported(imported));
        }
        return decorated;
    }
}
