package ch.iseli.sportanalyzer.client.model;

import ch.iseli.sportanalyzer.client.cache.TrainingCenterRecord;

public class TrainingOverviewFactory {

    private TrainingOverviewFactory() {
    }

    public static ITrainingOverview creatTrainingOverview(final TrainingCenterRecord record) {
        return new TrainingOverview(record);
    }
}
