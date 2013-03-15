package ch.opentrainingcenter.transfer.impl;

import java.util.HashSet;
import java.util.Set;

import ch.opentrainingcenter.transfer.IAthlete;
import ch.opentrainingcenter.transfer.IImported;

/**
 * Athlete generated by hbm2java
 */
public class Athlete implements java.io.Serializable, IAthlete {

    private static final long serialVersionUID = 1L;
    private int id;
    private String name;
    private Integer age;
    private Integer maxHeartRate;

    private Set<Health> healths = new HashSet<Health>(0);
    private Set<IImported> importeds = new HashSet<IImported>(0);

    /**
     * nur für hibernate
     */
    public Athlete() {
        this(null, null, null, null, null);
    }

    public Athlete(final String name) {
        this(name, null, null, new HashSet<Health>(0), new HashSet<IImported>(0));
    }

    public Athlete(final String name, final Integer age, final Integer maxHeartRate) {
        this(name, age, maxHeartRate, new HashSet<Health>(0), new HashSet<IImported>(0));
    }

    public Athlete(final String name, final Integer age, final Integer maxHeartRate, final Set<Health> healths, final Set<IImported> importeds) {
        this.name = name;
        this.age = age;
        this.maxHeartRate = maxHeartRate;
        this.healths = healths;
        this.importeds = importeds;
    }

    /*
     * (non-Javadoc)
     * 
     * @see ch.opentrainingcenter.transfer.internal.IAthlete#getId()
     */
    @Override
    public int getId() {
        return this.id;
    }

    /*
     * (non-Javadoc)
     * 
     * @see ch.opentrainingcenter.transfer.internal.IAthlete#setId(int)
     */
    @Override
    public void setId(final int id) {
        this.id = id;
    }

    /*
     * (non-Javadoc)
     * 
     * @see ch.opentrainingcenter.transfer.internal.IAthlete#getName()
     */
    @Override
    public String getName() {
        return this.name;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * ch.opentrainingcenter.transfer.internal.IAthlete#setName(java.lang.String
     * )
     */
    @Override
    public void setName(final String name) {
        this.name = name;
    }

    @Override
    public Integer getAge() {
        return age;
    }

    @Override
    public void setAge(final Integer age) {
        this.age = age;
    }

    @Override
    public Integer getMaxHeartRate() {
        return maxHeartRate;
    }

    @Override
    public void setMaxHeartRate(final Integer maxHeartRate) {
        this.maxHeartRate = maxHeartRate;
    }

    /*
     * (non-Javadoc)
     * 
     * @see ch.opentrainingcenter.transfer.internal.IAthlete#getHealths()
     */
    @Override
    public Set<Health> getHealths() {
        return this.healths;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * ch.opentrainingcenter.transfer.internal.IAthlete#setHealths(java.util
     * .Set)
     */
    @Override
    public void setHealths(final Set<Health> healths) {
        this.healths = healths;
    }

    /*
     * (non-Javadoc)
     * 
     * @see ch.opentrainingcenter.transfer.internal.IAthlete#getImporteds()
     */
    @Override
    public Set<IImported> getImporteds() {
        return this.importeds;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * ch.opentrainingcenter.transfer.internal.IAthlete#setImporteds(java.util
     * .Set)
     */
    @Override
    public void setImporteds(final Set<IImported> importeds) {
        this.importeds = importeds;
    }

    @Override
    public String toString() {
        return "Athlete [id=" + id + ", name=" + name + ", importeds=" + importeds.size() + "]";//$NON-NLS-1$//$NON-NLS-2$//$NON-NLS-3$//$NON-NLS-4$
    }

    @Override
    public void addImported(final IImported record) {
        this.importeds.add(record);
    }

}
