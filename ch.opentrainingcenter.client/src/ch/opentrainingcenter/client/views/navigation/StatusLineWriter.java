package ch.opentrainingcenter.client.views.navigation;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.action.IStatusLineManager;

import ch.opentrainingcenter.core.helper.DistanceHelper;
import ch.opentrainingcenter.core.helper.TimeHelper;
import ch.opentrainingcenter.i18n.Messages;
import ch.opentrainingcenter.model.navigation.ConcreteHealth;
import ch.opentrainingcenter.model.navigation.ConcreteImported;
import ch.opentrainingcenter.model.navigation.INavigationItem;
import ch.opentrainingcenter.model.navigation.INavigationParent;

class StatusLineWriter {

    private final IStatusLineManager manager;

    StatusLineWriter(final IStatusLineManager manager) {
        this.manager = manager;
    }

    protected void writeStatusLine(final Object[] array) {
        final StringBuilder msg = new StringBuilder();
        if (isSelectionMitInhalt(array)) {
            final List<ConcreteImported> trainings = new ArrayList<ConcreteImported>();
            final List<ConcreteHealth> healths = new ArrayList<ConcreteHealth>();
            for (final Object obj : array) {
                if (obj instanceof INavigationParent) {
                    final INavigationParent navi = (INavigationParent) obj;
                    final List<INavigationItem> childs = navi.getChilds();
                    for (final INavigationItem item : childs) {
                        addSelection(trainings, healths, item);
                    }
                }
                addSelection(trainings, healths, obj);
            }
            if (!trainings.isEmpty()) {
                int count = 0;
                double distance = 0d;
                double dauer = 0d;
                for (final ConcreteImported item : trainings) {
                    count++;
                    distance += item.getLaengeInMeter();
                    dauer += item.getDauer();
                }
                msg.append(Messages.StatusLineWriter_14).append(count).append(Messages.StatusLineWriter_20);
                msg.append(DistanceHelper.roundDistanceFromMeterToKm(distance));
                msg.append(Messages.StatusLineWriter_21).append(TimeHelper.convertSecondsToHumanReadableZeit(dauer));
            } else if (!healths.isEmpty()) {
                if (healths.size() > 1) {
                    msg.append(Messages.StatusLineWriter_LetzteVitaldaten);
                }
                final ConcreteHealth lastHealth = healths.get(0);
                msg.append(TimeHelper.convertDateToString(lastHealth.getDate())).append(' ').append(lastHealth.getTooltip());
            }
        }
        writeToStatusLine(msg.toString());
    }

    private void addSelection(final List<ConcreteImported> items, final List<ConcreteHealth> healths, final Object item) {
        if (item instanceof ConcreteImported) {
            items.add((ConcreteImported) item);
        }
        if (item instanceof ConcreteHealth) {
            final ConcreteHealth health = (ConcreteHealth) item;
            healths.add(health);
        }
    }

    private boolean isSelectionMitInhalt(final Object[] array) {
        return array != null && array.length > 0;
    }

    private void writeToStatusLine(final String message) {
        manager.setMessage(message);
    }
}
