package ch.opentrainingcenter.transfer;

import java.util.Set;

import ch.opentrainingcenter.transfer.impl.Health;
import ch.opentrainingcenter.transfer.impl.Imported;

public interface IAthlete {

    public abstract int getId();

    public abstract void setId(int id);

    public abstract String getName();

    public abstract void setName(String name);

    public abstract Set<Health> getHealths();

    public abstract void setHealths(Set<Health> healths);

    public abstract Set<Imported> getImporteds();

    public abstract void setImporteds(Set<Imported> importeds);

    Integer getAge();

    void setAge(Integer age);

    Integer getMaxHeartRate();

    void setMaxHeartRate(Integer maxHeartRate);

}