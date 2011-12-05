package ch.opentrainingcenter.transfer;

import java.util.Set;

import ch.opentrainingcenter.transfer.impl.Health;

public interface IAthlete {

    int getId();

    void setId(int id);

    String getName();

    void setName(String name);

    Set<Health> getHealths();

    void setHealths(Set<Health> healths);

    Set<IImported> getImporteds();

    void setImporteds(Set<IImported> importeds);

    void addImported(IImported record);

    Integer getAge();

    void setAge(Integer age);

    Integer getMaxHeartRate();

    void setMaxHeartRate(Integer maxHeartRate);

}