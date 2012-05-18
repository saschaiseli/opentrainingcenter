package ch.opentrainingcenter.transfer;

import java.util.Date;

/**
 * Transfer Object.
 * 
 */
public interface IImported {

    int getId();

    void setId(int id);

    ITrainingType getTrainingType();

    void setTrainingType(ITrainingType type);

    IAthlete getAthlete();

    void setAthlete(IAthlete athlete);

    Date getImportedDate();

    void setImportedDate(Date importedDate);

    String getComments();

    void setComments(String comments);

    /**
     * @param time
     *            die startzeit der aktivität. dies ist gleichzeitig auch die id
     *            des records.
     */
    void setActivityId(Date time);

    /**
     * @return das startdatum des Laufes. Dies ist auch gleich die Id.
     */
    Date getActivityId();

    void setTraining(ITraining overview);

    ITraining getTraining();

}