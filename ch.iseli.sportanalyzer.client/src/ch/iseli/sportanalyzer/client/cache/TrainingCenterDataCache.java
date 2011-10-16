package ch.iseli.sportanalyzer.client.cache;

import java.util.ArrayList;
import java.util.List;

import ch.iseli.sportanalyzer.tcx.TrainingCenterDatabaseT;

public class TrainingCenterDataCache {

	private static TrainingCenterDataCache INSTANCE = null;

	private final List<TrainingCenterDatabaseT> list = new ArrayList<TrainingCenterDatabaseT>();

	private static TrainingCenterDatabaseT selected;

	private TrainingCenterDataCache(List<TrainingCenterDatabaseT> all) {
		list.addAll(all);
	}

	public static TrainingCenterDataCache initCache(
			List<TrainingCenterDatabaseT> all) {
		if (INSTANCE == null) {
			INSTANCE = new TrainingCenterDataCache(all);
		}
		return INSTANCE;
	}

	public static void setSelectedRun(TrainingCenterDatabaseT selected) {
		TrainingCenterDataCache.selected = selected;
	}

	public static TrainingCenterDatabaseT getSelected() {
		return selected;
	}
}
