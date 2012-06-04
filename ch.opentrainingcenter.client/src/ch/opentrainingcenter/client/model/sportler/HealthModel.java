package ch.opentrainingcenter.client.model.sportler;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

import org.apache.log4j.Logger;

public class HealthModel implements PropertyChangeListener {

    private static final Logger LOG = Logger.getLogger(HealthModel.class);

    private int ruhePuls;

    private double weight;

    private final PropertyChangeSupport propertyChangeSupport = new PropertyChangeSupport(this);

    public int getRuhePuls() {
        return ruhePuls;
    }

    public void setRuhePuls(final int puls) {
        LOG.info("Puls eingegeben: " + puls); //$NON-NLS-1$
        propertyChangeSupport.firePropertyChange("ruhePuls", this.ruhePuls, this.ruhePuls = puls); //$NON-NLS-1$
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(final double gewicht) {
        LOG.info("Gewicht eingegeben: " + gewicht); //$NON-NLS-1$
        propertyChangeSupport.firePropertyChange("weight", this.weight, this.weight = gewicht); //$NON-NLS-1$
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
        propertyChangeSupport.firePropertyChange("health", null, this); //$NON-NLS-1$
    }

}
