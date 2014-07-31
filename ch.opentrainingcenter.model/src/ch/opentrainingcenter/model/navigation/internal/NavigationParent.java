package ch.opentrainingcenter.model.navigation.internal;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.eclipse.osgi.util.NLS;

import ch.opentrainingcenter.core.helper.DistanceHelper;
import ch.opentrainingcenter.core.helper.TimeHelper;
import ch.opentrainingcenter.i18n.Messages;
import ch.opentrainingcenter.model.navigation.INavigationItem;
import ch.opentrainingcenter.model.navigation.INavigationParent;
import ch.opentrainingcenter.model.navigation.KalenderWoche;

public class NavigationParent implements INavigationParent {

    private final List<INavigationItem> items = new ArrayList<INavigationItem>();
    private KalenderWoche kw = null;

    public NavigationParent() {

    }

    @Override
    public String getName() {
        final StringBuilder result = new StringBuilder("KW"); //$NON-NLS-1$
        result.append(kw != null ? kw.getKw() : ""); //$NON-NLS-1$
        return result.toString();
    }

    @Override
    public String getTooltip() {
        if (kw != null) {
            final List<INavigationItem> childs = getChilds();
            if (childs.size() == 1) {
                return childs.get(0).getTooltip();
            } else {
                // mehr als eins
                double distance = 0;
                for (final INavigationItem child : childs) {
                    distance += child.getLaengeInMeter();
                }
                final INavigationItem first = childs.get(0);
                final String start = TimeHelper.convertDateToString(first.getDate());
                final INavigationItem last = childs.get(childs.size() - 1);
                final String end = TimeHelper.convertDateToString(last.getDate());
                final Object[] binding = new Object[] { childs.size(), start, end, DistanceHelper.roundDistanceFromMeterToKm(distance) };
                return NLS.bind(Messages.NAVIGATION_TOOLTIP_KW, binding);
            }
        }
        return null;
    }

    @Override
    public List<INavigationItem> getChilds() {
        Collections.sort(items);
        return items;
    }

    @Override
    public void add(final INavigationItem item) {
        items.add(item);
        if (kw == null) {
            kw = new KalenderWoche(item.getDate());
        }
    }

    @Override
    public void remove(final INavigationItem item) {
        items.remove(item);
    }

    @Override
    public KalenderWoche getKalenderWoche() {
        return kw;
    }

}
