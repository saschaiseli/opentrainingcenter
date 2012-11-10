package ch.opentrainingcenter.model.planing.internal;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

import ch.opentrainingcenter.model.planing.IPlanungModel;
import ch.opentrainingcenter.transfer.IAthlete;

public class PlanungModel implements PropertyChangeListener, IPlanungModel {

    private int id;
    private IAthlete athlete;
    private int kw;
    private int jahr;
    private int kmProWoche;
    private boolean interval;
    private final PropertyChangeSupport propertyChangeSupport = new PropertyChangeSupport(this);
    private int langerLauf;

    public PlanungModel(final IAthlete athlete, final int jahr, final int kw, final int kmProWoche) {
        this(athlete, jahr, kw, kmProWoche, false, 0);
    }

    public PlanungModel(final IAthlete athlete, final int jahr, final int kw, final int kmProWoche, final boolean interval, final int langerLauf) {
        this.athlete = athlete;
        this.kw = kw;
        this.jahr = jahr;
        this.kmProWoche = kmProWoche;
        this.interval = interval;
        this.langerLauf = langerLauf;
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public void setId(final int id) {
        this.id = id;
    }

    @Override
    public IAthlete getAthlete() {
        return athlete;
    }

    @Override
    public void setAthlete(final IAthlete athlete) {
        this.athlete = athlete;
    }

    @Override
    public int getKw() {
        return kw;
    }

    @Override
    public void setKw(final int kw) {
        this.kw = kw;
    }

    @Override
    public int getJahr() {
        return jahr;
    }

    @Override
    public void setJahr(final int jahr) {
        this.jahr = jahr;
    }

    @Override
    public int getKmProWoche() {
        return kmProWoche;
    }

    @Override
    public void setKmProWoche(final int kmProWoche) {
        this.kmProWoche = kmProWoche;
        propertyChangeSupport.firePropertyChange("kmProWoche", this.kmProWoche, this.kmProWoche = kmProWoche); //$NON-NLS-1$
    }

    @Override
    public boolean isInterval() {
        return interval;
    }

    @Override
    public void setLangerLauf(final int langerLauf) {
        this.langerLauf = langerLauf;
        propertyChangeSupport.firePropertyChange("langerLauf", this.langerLauf, this.langerLauf = langerLauf); //$NON-NLS-1$
    }

    @Override
    public int getLangerLauf() {
        return langerLauf;
    }

    @Override
    public void setInterval(final boolean interval) {
        this.interval = interval;
        propertyChangeSupport.firePropertyChange("interval", this.interval, this.interval = interval); //$NON-NLS-1$
    }

    @Override
    public String toString() {
        return "PlanungWoche [id=" + id + ", athlete=" + athlete + ", kw=" + kw + ", jahr=" + jahr + ", kmProWoche=" + kmProWoche //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$ 
                + ", interval=" + interval + "]"; //$NON-NLS-1$ //$NON-NLS-2$
    }

    @Override
    public PropertyChangeSupport getPropertyChangeSupport() {
        return propertyChangeSupport;
    }

    @Override
    public void addPropertyChangeListener(final String propertyName, final PropertyChangeListener listener) {
        propertyChangeSupport.addPropertyChangeListener(propertyName, listener);
    }

    @Override
    public void removePropertyChangeListener(final PropertyChangeListener listener) {
        propertyChangeSupport.removePropertyChangeListener(listener);
    }

    @Override
    public void propertyChange(final PropertyChangeEvent evt) {
        propertyChangeSupport.firePropertyChange("planungmodel", null, this); //$NON-NLS-1$
    }

}
