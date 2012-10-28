package ch.opentrainingcenter.client.model.navigation.impl;

import java.util.Date;

import ch.opentrainingcenter.transfer.IAthlete;
import ch.opentrainingcenter.transfer.IImported;
import ch.opentrainingcenter.transfer.ITraining;
import ch.opentrainingcenter.transfer.ITrainingType;

public abstract class ImportedDecorator implements IImported {

    private final IImported imported;

    public ImportedDecorator(final IImported imported) {
        this.imported = imported;
    }

    @Override
    public int getId() {
        return getImported().getId();
    }

    @Override
    public void setId(final int id) {
        getImported().setId(id);
    }

    @Override
    public ITrainingType getTrainingType() {
        return getImported().getTrainingType();
    }

    @Override
    public void setTrainingType(final ITrainingType type) {
        getImported().setTrainingType(type);
    }

    @Override
    public IAthlete getAthlete() {
        return getImported().getAthlete();
    }

    @Override
    public void setAthlete(final IAthlete athlete) {
        getImported().setAthlete(athlete);
    }

    @Override
    public Date getImportedDate() {
        return getImported().getImportedDate();
    }

    @Override
    public void setImportedDate(final Date importedDate) {
        getImported().setImportedDate(importedDate);
    }

    @Override
    public String getComments() {
        return getImported().getComments();
    }

    @Override
    public void setComments(final String comments) {
        getImported().setComments(comments);
    }

    @Override
    public void setActivityId(final Date time) {
        getImported().setActivityId(time);
    }

    @Override
    public Date getActivityId() {
        return getImported().getActivityId();
    }

    @Override
    public void setTraining(final ITraining overview) {
        getImported().setTraining(overview);
    }

    @Override
    public ITraining getTraining() {
        return getImported().getTraining();
    }

    public IImported getImported() {
        return imported;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((imported == null) ? 0 : imported.hashCode());
        return result;
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final ImportedDecorator other = (ImportedDecorator) obj;
        if (imported == null) {
            if (other.imported != null) {
                return false;
            }
        } else if (!imported.equals(other.imported)) {
            return false;
        }
        return true;
    }

}
