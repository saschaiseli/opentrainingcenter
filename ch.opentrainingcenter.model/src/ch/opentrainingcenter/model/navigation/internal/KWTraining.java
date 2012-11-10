package ch.opentrainingcenter.model.navigation.internal;

import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Map;
import java.util.TreeMap;

import ch.opentrainingcenter.model.navigation.IKalenderWocheNavigationModel;
import ch.opentrainingcenter.model.navigation.INavigationItem;
import ch.opentrainingcenter.model.navigation.INavigationParent;
import ch.opentrainingcenter.model.navigation.KalenderWoche;

public class KWTraining implements IKalenderWocheNavigationModel {

    Map<Integer, Map<Integer, INavigationParent>> map = new TreeMap<Integer, Map<Integer, INavigationParent>>(new Comparator<Integer>() {

        @Override
        public int compare(final Integer o1, final Integer o2) {
            return o2.compareTo(o1);
        }
    });

    @Override
    public Collection<Integer> getParents() {
        return map.keySet();
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

        if (!map.containsKey(kw.getJahr())) {
            map.put(kw.getJahr(), new TreeMap<Integer, INavigationParent>(new Comparator<Integer>() {

                @Override
                public int compare(final Integer o1, final Integer o2) {
                    return o2.compareTo(o1);
                }
            }));
        }
        final Map<Integer, INavigationParent> jahr = map.get(kw.getJahr());
        if (!jahr.containsKey(kw.getKw())) {
            jahr.put(kw.getKw(), new NavigationParent());
        }
        jahr.get(kw.getKw()).add(item);
    }

    @Override
    public void removeItem(final INavigationItem item) {
        final Date date = item.getDate();
        final KalenderWoche kw = new KalenderWoche(date);

        if (map.containsKey(kw.getJahr())) {
            final Map<Integer, INavigationParent> jahr = map.get(kw.getJahr());
            if (jahr.containsKey(kw.getKw())) {
                final INavigationParent week = jahr.get(kw.getKw());
                week.remove(item);
                if (week.getChilds().isEmpty()) {
                    jahr.remove(kw.getKw());
                }
                if (jahr.values().isEmpty()) {
                    map.remove(kw.getJahr());
                }
            }
        }
    }

    @Override
    public void reset() {
        map.clear();
    }

    @Override
    public Collection<INavigationParent> getWeeks(final Integer jahr) {
        final Map<Integer, INavigationParent> weeks = map.get(jahr);
        if (weeks != null) {
            return weeks.values();
        } else {
            return Collections.emptyList();
        }
    }
}
