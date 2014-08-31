package ch.opentrainingcenter.core.db.criteria;

import ch.opentrainingcenter.core.db.ISearchCriteria;
import ch.opentrainingcenter.transfer.IRoute;
import ch.opentrainingcenter.transfer.ITraining;

public class StreckeCriteria implements ISearchCriteria {

    private final int referenzTrainingId;

    public StreckeCriteria(final int referenzTrainingId) {
        this.referenzTrainingId = referenzTrainingId;
    }

    @Override
    public boolean matches(final ITraining training) {
        if (referenzTrainingId <= 0) {
            return true;
        }
        final IRoute route = training.getRoute();
        return route != null && referenzTrainingId == route.getId();
    }

}
