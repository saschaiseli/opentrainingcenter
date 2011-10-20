package ch.opentrainingcenter.tcx.data.internal;

import ch.iseli.sportanalyzer.tcx.TrainingCenterDatabaseT;
import ch.opentrainingcenter.tcx.data.Cardio;

public class CardioImpl implements Cardio {

    private final TrainingCenterDatabaseT t;

    public CardioImpl(TrainingCenterDatabaseT t) {
	this.t = t;

    }

    @Override
    public float getAverageCardio() {
	return 0;
    }

}
