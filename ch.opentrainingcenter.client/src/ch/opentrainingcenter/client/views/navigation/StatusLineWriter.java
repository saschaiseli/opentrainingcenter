package ch.opentrainingcenter.client.views.navigation;

import java.util.List;

import org.eclipse.jface.action.IStatusLineManager;

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
            str.append("Ruhepuls/Gewicht vom ").append(TimeHelper.convertDateToString(health.getDate(), false));
            str.append(" --> ");
            str.append(health.getCardio()).append("bpm / ").append(health.getWeight()).append("kg");
        } else if (health.getCardio() != null) {
            str.append("Ruhepuls vom ").append(TimeHelper.convertDateToString(health.getDate(), false));
            str.append(" --> ").append(health.getCardio()).append("bpm");
        } else if (health.getWeight() != null) {
            str.append("Gewicht vom ").append(TimeHelper.convertDateToString(health.getDate(), false));
            str.append(" --> ").append(health.getWeight()).append("kg");
        } else {
            throw new IllegalArgumentException("Falsche Datenstruktur. Health ohne cardio und gewicht ist nicht möglich");
        }
        writeToStatusLine(str.toString());
    }

    private void writeToStatusLine(final NavigationParent parent) {
        final List<INavigationItem> childs = parent.getChilds();
        final StringBuffer str = new StringBuffer("Kalenderwoche ");
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
                throw new IllegalArgumentException("Typ: " + child + " nicht bekannt");
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
        return "Anzahl Läufe: " + size;
    }

    private Object getHealthText(final int size) {
        return "Anzahl Messdaten: " + size;
    }

    private void writeToStatusLine(final IImported record) {
        final StringBuffer str = new StringBuffer();
        final ITraining training = record.getTraining();
        str.append("Lauf vom ").append(TimeHelper.convertDateToString(record.getActivityId(), false));
        str.append(" --> Distanz: ").append(DistanceHelper.roundDistanceFromMeterToKmMitEinheit(training.getLaengeInMeter()));
        str.append(" Dauer: ").append(TimeHelper.convertSecondsToHumanReadableZeit(training.getDauerInSekunden()));
        writeToStatusLine(str.toString());
    }

    private void writeToStatusLine(final String message) {
        manager.setMessage(message);
    }
}
