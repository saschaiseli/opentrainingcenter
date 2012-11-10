package ch.opentrainingcenter.model.navigation;

import java.util.Date;

import ch.opentrainingcenter.transfer.IAthlete;
import ch.opentrainingcenter.transfer.IHealth;

public abstract class HealthDecorator implements IHealth {

    private final IHealth health;

    public HealthDecorator(final IHealth health) {
        this.health = health;
    }

    @Override
    public int getId() {
        return health.getId();
    }

    @Override
    public void setId(final int id) {
        health.setId(id);
    }

    @Override
    public IAthlete getAthlete() {
        return health.getAthlete();
    }

    @Override
    public void setAthlete(final IAthlete athlete) {
        health.setAthlete(athlete);
    }

    @Override
    public Double getWeight() {
        return health.getWeight();
    }

    @Override
    public void setWeight(final Double weight) {
        health.setWeight(weight);
    }

    @Override
    public Integer getCardio() {
        return health.getCardio();
    }

    @Override
    public void setCardio(final Integer cardio) {
        health.setCardio(cardio);
    }

    @Override
    public Date getDateofmeasure() {
        return health.getDateofmeasure();
    }

    @Override
    public void setDateofmeasure(final Date dateofmeasure) {
        health.setDateofmeasure(dateofmeasure);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((health == null) ? 0 : health.hashCode());
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
        final HealthDecorator other = (HealthDecorator) obj;
        if (health == null) {
            if (other.health != null) {
                return false;
            }
        } else if (!health.equals(other.health)) {
            return false;
        }
        return true;
    }

}
