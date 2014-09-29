package ch.opentrainingcenter.model.schuh;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

import ch.opentrainingcenter.core.assertions.Assertions;
import ch.opentrainingcenter.transfer.IAthlete;

public class SchuhModel implements PropertyChangeListener {

    private final PropertyChangeSupport propertyChangeSupport = new PropertyChangeSupport(this);
    private final IAthlete athlete;
    private int id;
    private String schuhName;

    public SchuhModel(final IAthlete athlete) {
        Assertions.notNull(athlete);
        this.athlete = athlete;
    }

    public int getId() {
        return id;
    }

    public void setId(final int id) {
        this.id = id;
    }

    public IAthlete getAthlete() {
        return athlete;
    }

    public String getSchuhName() {
        return schuhName;
    }

    public void setSchuhName(final String schuhName) {
        propertyChangeSupport.firePropertyChange("schuhName", this.schuhName, this.schuhName = schuhName); //$NON-NLS-1$
    }

    public PropertyChangeSupport getPropertyChangeSupport() {
        return propertyChangeSupport;
    }

    public void addPropertyChangeListener(final String propertyName, final PropertyChangeListener listener) {
        propertyChangeSupport.addPropertyChangeListener(propertyName, listener);
    }

    public void removePropertyChangeListener(final PropertyChangeListener listener) {
        propertyChangeSupport.removePropertyChangeListener(listener);
    }

    @Override
    public void propertyChange(final PropertyChangeEvent evt) {
        propertyChangeSupport.firePropertyChange("schuhname", null, this); //$NON-NLS-1$
    }

}
