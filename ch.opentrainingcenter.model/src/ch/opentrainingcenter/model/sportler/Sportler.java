package ch.opentrainingcenter.model.sportler;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.Date;

public class Sportler implements PropertyChangeListener {

    private String name;
    private Date birthday;
    private Integer maxHeartBeat;

    private final PropertyChangeSupport propertyChangeSupport = new PropertyChangeSupport(this);

    public Sportler() {

    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        propertyChangeSupport.firePropertyChange("name", this.name, this.name = name); //$NON-NLS-1$
    }

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(final Date birthday) {
        propertyChangeSupport.firePropertyChange("birthday", this.birthday, this.birthday = birthday); //$NON-NLS-1$
    }

    public Integer getMaxHeartBeat() {
        return maxHeartBeat;
    }

    public void setMaxHeartBeat(final Integer maxHeartBeat) {
        propertyChangeSupport.firePropertyChange("maxHeartBeat", this.maxHeartBeat, this.maxHeartBeat = maxHeartBeat); //$NON-NLS-1$
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

    @Override
    public String toString() {
        return "Sportler [name=" + name + ", birthday=" + birthday + ", maxHeartBeat=" + maxHeartBeat + "]"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
    }

}
