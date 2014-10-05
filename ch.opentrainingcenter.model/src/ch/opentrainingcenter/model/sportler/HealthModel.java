package ch.opentrainingcenter.model.sportler;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.Date;

import org.apache.log4j.Logger;

public class HealthModel implements PropertyChangeListener {

    private static final Logger LOG = Logger.getLogger(HealthModel.class);

    private Double weight;
    private Integer ruhePuls;
    private Date dateOfMeasure;

    private final PropertyChangeSupport propertyChangeSupport;

    /**
     * fuer tests
     */
    public HealthModel(final PropertyChangeSupport propertyChangeSupport) {
        this.propertyChangeSupport = propertyChangeSupport;
    }

    public HealthModel(final Double gewicht, final Integer ruhePuls, final Date date) {
        this.weight = gewicht;
        this.ruhePuls = ruhePuls;
        this.dateOfMeasure = date;
        propertyChangeSupport = new PropertyChangeSupport(this);
    }

    public Integer getRuhePuls() {
        return ruhePuls;
    }

    public void setRuhePuls(final Integer ruhePuls) {
        LOG.info("Puls eingegeben: " + ruhePuls); //$NON-NLS-1$
        propertyChangeSupport.firePropertyChange("ruhePuls", this.ruhePuls, ruhePuls); //$NON-NLS-1$
        this.ruhePuls = ruhePuls;
    }

    public Date getDateOfMeasure() {
        return dateOfMeasure;
    }

    public void setDateOfMeasure(final Date dateOfMeasure) {
        LOG.info("Neues datum beim Model gesetzt"); //$NON-NLS-1$
        this.dateOfMeasure = dateOfMeasure;
    }

    public Double getWeight() {
        return weight;
    }

    public void setWeight(final Double weight) {
        LOG.info("Gewicht eingegeben: " + weight); //$NON-NLS-1$
        propertyChangeSupport.firePropertyChange("weight", this.weight, weight); //$NON-NLS-1$
        this.weight = weight;
    }

    @Override
    public void propertyChange(final PropertyChangeEvent evt) {
        propertyChangeSupport.firePropertyChange("health", null, this); //$NON-NLS-1$
    }

    public void addPropertyChangeListener(final PropertyChangeListener pcl) {
    }
}
