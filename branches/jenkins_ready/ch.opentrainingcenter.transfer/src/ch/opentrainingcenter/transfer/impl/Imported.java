package ch.opentrainingcenter.transfer.impl;

// default package
// Generated 21.10.2011 12:19:41 by Hibernate Tools 3.4.0.CR1

import java.util.Date;

import ch.opentrainingcenter.transfer.IAthlete;
import ch.opentrainingcenter.transfer.IImported;
import ch.opentrainingcenter.transfer.ITraining;
import ch.opentrainingcenter.transfer.ITrainingType;
import ch.opentrainingcenter.transfer.IWeather;

/**
 * Imported generated by hbm2java
 */
public class Imported implements java.io.Serializable, IImported {

    private static final long serialVersionUID = 1L;
    private int id;
    private IWeather weather;
    private IAthlete athlete;
    private Date importedDate;
    private String comments;
    private Date activityId;
    private ITraining overview;
    private ITrainingType type;

    public Imported() {
    }

    /*
     * (non-Javadoc)
     * 
     * @see ch.opentrainingcenter.transfer.internal.IImported#getId()
     */
    @Override
    public int getId() {
        return this.id;
    }

    /*
     * (non-Javadoc)
     * 
     * @see ch.opentrainingcenter.transfer.internal.IImported#setId(int)
     */
    @Override
    public void setId(final int id) {
        this.id = id;
    }

    /*
     * (non-Javadoc)
     * 
     * @see ch.opentrainingcenter.transfer.internal.IImported#getWeather()
     */
    @Override
    public IWeather getWeather() {
        return this.weather;
    }

    /*
     * (non-Javadoc)
     * 
     * @see ch.opentrainingcenter.transfer.internal.IImported#setWeather(ch.opentrainingcenter.transfer.internal.Weather)
     */
    @Override
    public void setWeather(final IWeather weather) {
        this.weather = weather;
    }

    /*
     * (non-Javadoc)
     * 
     * @see ch.opentrainingcenter.transfer.internal.IImported#getAthlete()
     */
    @Override
    public IAthlete getAthlete() {
        return this.athlete;
    }

    /*
     * (non-Javadoc)
     * 
     * @see ch.opentrainingcenter.transfer.internal.IImported#setAthlete(ch.opentrainingcenter.transfer.IAthlete)
     */
    @Override
    public void setAthlete(final IAthlete athlete) {
        this.athlete = athlete;
    }

    /*
     * (non-Javadoc)
     * 
     * @see ch.opentrainingcenter.transfer.internal.IImported#getImportedDate()
     */
    @Override
    public Date getImportedDate() {
        return this.importedDate;
    }

    /*
     * (non-Javadoc)
     * 
     * @see ch.opentrainingcenter.transfer.internal.IImported#setImportedDate(java.util.Date)
     */
    @Override
    public void setImportedDate(final Date importedDate) {
        this.importedDate = importedDate;
    }

    /*
     * (non-Javadoc)
     * 
     * @see ch.opentrainingcenter.transfer.internal.IImported#getComments()
     */
    @Override
    public String getComments() {
        return this.comments;
    }

    /*
     * (non-Javadoc)
     * 
     * @see ch.opentrainingcenter.transfer.internal.IImported#setComments(java.lang.String)
     */
    @Override
    public void setComments(final String comments) {
        this.comments = comments;
    }

    @Override
    public void setActivityId(final Date time) {
        this.activityId = time;
    }

    @Override
    public Date getActivityId() {
        return activityId;
    }

    @Override
    public void setTraining(final ITraining overview) {
        this.overview = overview;
    }

    @Override
    public ITraining getTraining() {
        return overview;
    }

    @Override
    public ITrainingType getTrainingType() {
        return type;
    }

    @Override
    public void setTrainingType(final ITrainingType type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "Imported [id=" + id + ", weather=" + weather + ", athlete=" + athlete + ", importedDate=" + importedDate + ", comments=" + comments //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$
                + ", activityId=" + activityId + ", overview=" + overview + "]"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
    }

}