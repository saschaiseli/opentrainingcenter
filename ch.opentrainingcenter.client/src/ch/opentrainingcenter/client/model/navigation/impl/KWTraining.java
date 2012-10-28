package ch.opentrainingcenter.client.model.navigation.impl;

import java.util.Collection;
import java.util.Date;
import java.util.Map;
import java.util.TreeMap;

import ch.opentrainingcenter.client.model.KalenderWoche;
import ch.opentrainingcenter.client.model.navigation.IKalenderWocheNavigationModel;
import ch.opentrainingcenter.client.model.navigation.INavigationItem;
import ch.opentrainingcenter.client.model.navigation.INavigationParent;

public class KWTraining implements IKalenderWocheNavigationModel {

    Map<KalenderWoche, INavigationParent> map = new TreeMap<KalenderWoche, INavigationParent>();

    @Override
    public Collection<INavigationParent> getParents() {
        return map.values();
    }

    @Override
    public void addItems(final Collection<? extends INavigationItem> items) {
        for (final INavigationItem item : items) {
            addItem(item);
        }
    }

    @Override
    public void addItem(final INavigationItem item) {
        final Date date = item.getDate();
        final KalenderWoche kw = new KalenderWoche(date);
        if (!map.containsKey(kw)) {
            map.put(kw, new NavigationParent());
        }
        final INavigationParent parent = map.get(kw);
        parent.add(item);
    }

    @Override
    public void removeItem(final INavigationItem item) {
        final Date date = item.getDate();
        final KalenderWoche kw = new KalenderWoche(date);
        if (map.containsKey(kw)) {
            final INavigationParent parent = map.get(kw);
            parent.remove(item);
            if (parent.getChilds().size() == 0) {
                map.remove(kw);
            }
        }
    }

    @Override
    public void reset() {
        map.clear();
    }

}
