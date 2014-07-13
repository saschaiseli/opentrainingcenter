package ch.opentrainingcenter.model.navigation;

import ch.opentrainingcenter.transfer.IAthlete;
import ch.opentrainingcenter.transfer.IRoute;
import ch.opentrainingcenter.transfer.ITraining;
import ch.opentrainingcenter.transfer.TrainingType;

public abstract class ImportedDecorator implements ITraining {

    protected final ITraining training;

    public ImportedDecorator(final ITraining training) {
        this.training = training;
    }

    @Override
    public int getId() {
        return training.getId();
    }

    @Override
    public void setId(final int id) {
        training.setId(id);
    }

    @Override
    public TrainingType getTrainingType() {
        return training.getTrainingType();
    }

    @Override
    public void setTrainingType(final TrainingType type) {
        training.setTrainingType(type);
    }

    @Override
    public IAthlete getAthlete() {
        return training.getAthlete();
    }

    @Override
    public void setAthlete(final IAthlete athlete) {
        training.setAthlete(athlete);
    }

    @Override
    public String getFileName() {
        return training.getFileName();
    }

    @Override
    public void setFileName(final String fileName) {
        training.setFileName(fileName);
    }

    @Override
    public void setRoute(final IRoute route) {
        training.setRoute(route);
    }

    @Override
    public IRoute getRoute() {
        return training.getRoute();
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((training == null) ? 0 : training.hashCode());
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
        if (training == null) {
            if (other.training != null) {
                return false;
            }
        } else if (!training.equals(other.training)) {
            return false;
        }
        return true;
    }

}
