package ch.iseli.sportanalyzer.client.model.sportler;

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

    public void setName(String name) {
        propertyChangeSupport.firePropertyChange("name", this.name, this.name = name);
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        propertyChangeSupport.firePropertyChange("gender", this.gender, this.gender = gender);
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
        propertyChangeSupport.firePropertyChange("age", this.age, this.age = age);
    }

    public Integer getMaxHeartBeat() {
        return maxHeartBeat;
    }

    public void setMaxHeartBeat(Integer maxHeartBeat) {
        propertyChangeSupport.firePropertyChange("maxHeartBeat", this.maxHeartBeat, this.maxHeartBeat = maxHeartBeat);
    }

    public PropertyChangeSupport getPropertyChangeSupport() {
        return propertyChangeSupport;
    }

    public void addPropertyChangeListener(String propertyName, PropertyChangeListener listener) {
        propertyChangeSupport.addPropertyChangeListener(propertyName, listener);
    }

    public void removePropertyChangeListener(PropertyChangeListener listener) {
        propertyChangeSupport.removePropertyChangeListener(listener);
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        propertyChangeSupport.firePropertyChange("sportler", null, this);
    }

    @Override
    public String toString() {
        return "Sportler [name=" + name + ", gender=" + gender + ", age=" + age + ", maxHeartBeat=" + maxHeartBeat + "]";
    }

}
