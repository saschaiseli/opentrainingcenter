package ch.opentrainingcenter.client.model.planing.impl;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

import org.apache.log4j.Logger;

import ch.opentrainingcenter.transfer.IAthlete;

public class PlanungModel implements PropertyChangeListener {

    private static final Logger LOG = Logger.getLogger(PlanungModel.class);

    private int id;
    private IAthlete athlete;
    private int kw;
    private int jahr;
    private int kmProWoche;
    private boolean interval;
    private final PropertyChangeSupport propertyChangeSupport = new PropertyChangeSupport(this);

    public PlanungModel(final IAthlete athlete, final int jahr, final int kw, final int kmProWoche) {
        this(athlete, jahr, kw, kmProWoche, false);
    }

    public PlanungModel(final IAthlete athlete, final int jahr, final int kw, final int kmProWoche, final boolean interval) {
        this.athlete = athlete;
        this.kw = kw;
        this.jahr = jahr;
        this.kmProWoche = kmProWoche;
        this.interval = interval;
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

    public int getKw() {
        return kw;
    }

    public void setKw(final int kw) {
        this.kw = kw;
    }

    public int getJahr() {
        return jahr;
    }

    public void setJahr(final int jahr) {
        this.jahr = jahr;
    }

    public int getKmProWoche() {
        return kmProWoche;
    }

    public void setKmProWoche(final int kmProWoche) {
        this.kmProWoche = kmProWoche;
        propertyChangeSupport.firePropertyChange("kmProWoche", this.kmProWoche, this.kmProWoche = kmProWoche); //$NON-NLS-1$
    }

    public boolean isInterval() {
        return interval;
    }

    public void setInterval(final boolean interval) {
        this.interval = interval;
        propertyChangeSupport.firePropertyChange("interval", this.interval, this.interval = interval); //$NON-NLS-1$
    }

    @Override
    public String toString() {
        return "PlanungWoche [id=" + id + ", athlete=" + athlete + ", kw=" + kw + ", jahr=" + jahr + ", kmProWoche=" + kmProWoche //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$ 
                + ", interval=" + interval + "]"; //$NON-NLS-1$ //$NON-NLS-2$
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
        propertyChangeSupport.firePropertyChange("planungmodel", null, this); //$NON-NLS-1$
    }
}
