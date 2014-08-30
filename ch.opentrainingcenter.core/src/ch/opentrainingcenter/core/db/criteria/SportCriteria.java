package ch.opentrainingcenter.core.db.criteria;

import java.util.Set;

import ch.opentrainingcenter.core.assertions.Assertions;
import ch.opentrainingcenter.core.db.ISearchCriteria;
import ch.opentrainingcenter.transfer.ITraining;
import ch.opentrainingcenter.transfer.Sport;

public class SportCriteria implements ISearchCriteria {

    private final Set<Sport> sports;

    public SportCriteria(final Set<Sport> sports) {
        Assertions.notNull(sports);
        this.sports = sports;
    }

    @Override
    public boolean matches(final ITraining training) {
        return sports.contains(training.getSport());
    }

}
