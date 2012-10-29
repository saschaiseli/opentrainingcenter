package ch.opentrainingcenter.client.views.navigation;

import java.util.List;

import org.eclipse.jface.action.IStatusLineManager;

import ch.opentrainingcenter.client.Messages;
import ch.opentrainingcenter.client.helper.DistanceHelper;
import ch.opentrainingcenter.client.helper.TimeHelper;
import ch.opentrainingcenter.client.model.navigation.INavigationItem;
import ch.opentrainingcenter.client.model.navigation.impl.ConcreteHealth;
import ch.opentrainingcenter.client.model.navigation.impl.ConcreteImported;
import ch.opentrainingcenter.client.model.navigation.impl.NavigationParent;
import ch.opentrainingcenter.transfer.IImported;
import ch.opentrainingcenter.transfer.ITraining;

class StatusLineWriter {

    private final IStatusLineManager manager;

    StatusLineWriter(final IStatusLineManager manager) {
        this.manager = manager;
    }

    /**
     * Schreibt die Statuslinie auf Grund der Selektion
     */
    protected void writeStatusLine(final Object selection) {
        if (selection == null) {
            writeToStatusLine(""); //$NON-NLS-1$
        } else if (selection instanceof NavigationParent) {
            final NavigationParent parent = (NavigationParent) selection;
            if (parent.getChilds().isEmpty()) {
                throw new IllegalArgumentException("Parent hat keine Childs. Fehlerhafte Datenstruktur"); //$NON-NLS-1$
            }
            writeToStatusLine(parent);
        } else if (selection instanceof ConcreteHealth) {
            writeHealthToStatus((ConcreteHealth) selection);
        } else if (selection instanceof ConcreteImported) {
            final ConcreteImported record = (ConcreteImported) selection;
            writeToStatusLine(record);
        } else {
            throw new IllegalArgumentException("Parent hat nicht definierte Childs. Fehlerhafte Datenstruktur"); //$NON-NLS-1$
        }
    }

    private void writeHealthToStatus(final ConcreteHealth health) {
        final StringBuffer str = new StringBuffer();
        if (health.getCardio() != null && health.getWeight() != null) {
            str.append(Messages.StatusLineWriter_0).append(TimeHelper.convertDateToString(health.getDate(), false));
            str.append(Messages.StatusLineWriter_1);
            str.append(health.getCardio()).append(Messages.StatusLineWriter_2).append(health.getWeight()).append(Messages.StatusLineWriter_3);
        } else if (health.getCardio() != null) {
            str.append(Messages.StatusLineWriter_4).append(TimeHelper.convertDateToString(health.getDate(), false));
            str.append(Messages.StatusLineWriter_5).append(health.getCardio()).append(Messages.StatusLineWriter_6);
        } else if (health.getWeight() != null) {
            str.append(Messages.StatusLineWriter_7).append(TimeHelper.convertDateToString(health.getDate(), false));
            str.append(Messages.StatusLineWriter_8).append(health.getWeight()).append(Messages.StatusLineWriter_9);
        } else {
            throw new IllegalArgumentException(Messages.StatusLineWriter_10);
        }
        writeToStatusLine(str.toString());
    }

    private void writeToStatusLine(final NavigationParent parent) {
        final List<INavigationItem> childs = parent.getChilds();
        final StringBuffer str = new StringBuffer(Messages.StatusLineWriter_11);
        str.append(parent.getKalenderWoche().getKw());
        str.append(' ');
        int rec = 0;
        int health = 0;
        for (final INavigationItem child : childs) {
            if (child instanceof ConcreteImported) {
                rec++;
            } else if (child instanceof ConcreteHealth) {
                health++;
            } else {
                throw new IllegalArgumentException(Messages.StatusLineWriter_12 + child + Messages.StatusLineWriter_13);
            }
        }
        if (rec == 0) {
            // -> health nicht leer
            str.append(getHealthText(health));
        } else if (health == 0) {
            // --> records nicht leer
            str.append(getRecordText(rec));
        } else {
            // beide abgefüllt
            str.append(getRecordText(rec)).append(' ').append(getHealthText(health));
        }
        writeToStatusLine(str.toString());
    }

    private Object getRecordText(final int size) {
        return Messages.StatusLineWriter_14 + size;
    }

    private Object getHealthText(final int size) {
        return Messages.StatusLineWriter_15 + size;
    }

    private void writeToStatusLine(final IImported record) {
        final StringBuffer str = new StringBuffer();
        final ITraining training = record.getTraining();
        str.append(Messages.StatusLineWriter_16).append(TimeHelper.convertDateToString(record.getActivityId(), false));
        str.append(Messages.StatusLineWriter_17).append(DistanceHelper.roundDistanceFromMeterToKmMitEinheit(training.getLaengeInMeter()));
        str.append(Messages.StatusLineWriter_18).append(TimeHelper.convertSecondsToHumanReadableZeit(training.getDauerInSekunden()));
        writeToStatusLine(str.toString());
    }

    private void writeToStatusLine(final String message) {
        manager.setMessage(message);
    }
}
