package ch.opentrainingcenter.model.strecke;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

import ch.opentrainingcenter.core.assertions.Assertions;
import ch.opentrainingcenter.transfer.IAthlete;

public class StreckeModel implements PropertyChangeListener {

    private final PropertyChangeSupport propertyChangeSupport = new PropertyChangeSupport(this);
    private String name;
    private String beschreibung;
    private int id;
    private IAthlete athlete;

    public StreckeModel(final IAthlete athlete) {
        Assertions.notNull(athlete);
        this.athlete = athlete;
    }

    public StreckeModel(final int id, final IAthlete athlete, final String name, final String beschreibung) {
        Assertions.notNull(athlete);
        Assertions.notNull(name);
        this.id = id;
        this.athlete = athlete;
        this.name = name;
        this.beschreibung = beschreibung;
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

    public void setAthlete(final IAthlete athlete) {
        this.athlete = athlete;
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        propertyChangeSupport.firePropertyChange("name", this.name, this.name = name); //$NON-NLS-1$
    }

    public String getBeschreibung() {
        return beschreibung;
    }

    public void setBeschreibung(final String beschreibung) {
        propertyChangeSupport.firePropertyChange("beschreibung", this.beschreibung, this.beschreibung = beschreibung); //$NON-NLS-1$
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
        propertyChangeSupport.firePropertyChange("name", null, this); //$NON-NLS-1$
    }
}
