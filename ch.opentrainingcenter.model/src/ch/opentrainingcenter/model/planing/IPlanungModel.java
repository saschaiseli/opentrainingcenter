package ch.opentrainingcenter.model.planing;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

import ch.opentrainingcenter.transfer.IAthlete;

public interface IPlanungModel {

    int getId();

    void setId(final int id);

    IAthlete getAthlete();

    void setAthlete(final IAthlete athlete);

    int getKw();

    void setKw(final int kw);

    int getJahr();

    void setJahr(final int jahr);

    int getKmProWoche();

    void setKmProWoche(final int kmProWoche);

    boolean isInterval();

    void setLangerLauf(final int langerLauf);

    int getLangerLauf();

    void setInterval(final boolean interval);

    @Override
    String toString();

    PropertyChangeSupport getPropertyChangeSupport();

    void addPropertyChangeListener(final String propertyName, final PropertyChangeListener listener);

    void removePropertyChangeListener(final PropertyChangeListener listener);
}