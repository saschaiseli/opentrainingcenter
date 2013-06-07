package ch.opentrainingcenter.client.views.navigation;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.action.IStatusLineManager;

import ch.opentrainingcenter.core.helper.DistanceHelper;
import ch.opentrainingcenter.core.helper.TimeHelper;
import ch.opentrainingcenter.i18n.Messages;
import ch.opentrainingcenter.model.navigation.ConcreteImported;
import ch.opentrainingcenter.model.navigation.INavigationItem;
import ch.opentrainingcenter.model.navigation.INavigationParent;

class StatusLineWriter {

    private final IStatusLineManager manager;

    StatusLineWriter(final IStatusLineManager manager) {
        this.manager = manager;
    }

    /**
     * @param array
     */
    protected void writeStatusLine(final Object[] array) {
        if (isSelectionMitInhalt(array)) {
            final List<ConcreteImported> items = new ArrayList<ConcreteImported>();

            for (final Object obj : array) {
                if (obj instanceof INavigationParent) {
                    final INavigationParent navi = (INavigationParent) obj;
                    final List<INavigationItem> childs = navi.getChilds();
                    for (final INavigationItem item : childs) {
                        addLauf(items, item);
                    }
                }
                addLauf(items, obj);
            }
            if (!items.isEmpty()) {
                int count = 0;
                double distance = 0d;
                double dauer = 0d;
                for (final ConcreteImported item : items) {
                    count++;
                    distance += item.getLaengeInMeter();
                    dauer += item.getDauer();
                }
                final String msg = Messages.StatusLineWriter_19 + count + Messages.StatusLineWriter_20 + DistanceHelper.roundDistanceFromMeterToKm(distance)
                        + Messages.StatusLineWriter_21 + TimeHelper.convertSecondsToHumanReadableZeit(dauer);
                writeToStatusLine(msg);
            } else {
                writeToStatusLine(""); //$NON-NLS-1$
            }
        } else {
            writeToStatusLine(""); //$NON-NLS-1$
        }
    }

    private boolean isSelectionMitInhalt(final Object[] array) {
        return array != null && array.length > 0;
    }

    private void addLauf(final List<ConcreteImported> items, final Object obj) {
        if (obj instanceof ConcreteImported) {
            items.add((ConcreteImported) obj);
        }
    }

    private void writeToStatusLine(final String message) {
        manager.setMessage(message);
    }
}
