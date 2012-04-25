package ch.opentrainingcenter.client.model.sportler;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

public class Sportler implements PropertyChangeListener {

    private String name;
    private String gender;
    private Integer age;
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

    public String getGender() {
        return gender;
    }

    public void setGender(final String gender) {
        propertyChangeSupport.firePropertyChange("gender", this.gender, this.gender = gender); //$NON-NLS-1$
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(final Integer age) {
        propertyChangeSupport.firePropertyChange("age", this.age, this.age = age); //$NON-NLS-1$
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
        return "Sportler [name=" + name + ", gender=" + gender + ", age=" + age + ", maxHeartBeat=" + maxHeartBeat + "]"; //$NON-NLS-1$//$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$
    }

}
