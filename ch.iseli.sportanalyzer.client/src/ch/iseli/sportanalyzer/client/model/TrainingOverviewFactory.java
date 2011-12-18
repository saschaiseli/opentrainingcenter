package ch.iseli.sportanalyzer.client.model;

import ch.iseli.sportanalyzer.tcx.ActivityT;

public class TrainingOverviewFactory {

    private TrainingOverviewFactory() {
    }

    public static ITrainingOverview creatTrainingOverview(final ActivityT activity) {
        return new TrainingOverview(activity);
    }
}
