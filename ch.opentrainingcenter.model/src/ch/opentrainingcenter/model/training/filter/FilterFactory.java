package ch.opentrainingcenter.model.training.filter;

import java.util.Date;

import ch.opentrainingcenter.model.training.filter.internal.FilterTrainingByDate;
import ch.opentrainingcenter.model.training.filter.internal.FilterTrainingBySport;
import ch.opentrainingcenter.transfer.ITraining;
import ch.opentrainingcenter.transfer.Sport;

public final class FilterFactory {
    private FilterFactory() {

    }

    public static Filter<ITraining> createFilterBySport(final Sport sport) {
        return new FilterTrainingBySport(sport);
    }

    public static Filter<ITraining> createFilterByDate(final Date start, final Date end) {
        return new FilterTrainingByDate(start, end);
    }
}
